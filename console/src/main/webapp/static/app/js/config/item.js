var PageConst = {
    BaseUrl: '/config/item/',
    Query: function (params) {
        return Commons.formData("#searchForm", params);
    },
    /**
     * @return {string}
     */
    TypeFormatter: function (value) {
        switch (value) {
            case 1:
                return '<span class="label label-success">字符串</span>';
            case 2:
                return '<span class="label label-default">数值</span>';
            case 3:
                return '<span class="label label-primary">布尔</span>';
            case 4:
                return '<span class="label label-info">数组</span>';
            case 5:
                return '<span class="label label-warning">字典</span>';
            default:
                return '<span class="label label-danger">什么鬼</span>';
        }
    },
    /**
     * @return {string}
     */
    RiskLevelFormatter: function (value) {
        switch (value) {
            case 0:
            case 1:
                return '<span class="label label-success">' + value + '</span>';
            case 2:
            case 3:
                return '<span class="label label-primary">' + value + '</span>';
            case 4:
            case 5:
                return '<span class="label label-info">' + value + '</span>';
            case 6:
            case 7:
                return '<span class="label label-warning">' + value + '</span>';
            case 8:
            case 9:
                return '<span class="label label-danger">' + value + '</span>';
            default:
                return '<span class="label label-danger">什么鬼</span>';
        }
    },
    /**
     * @return {string}
     */
    ValueFormatter: function (value, row) {
        var result = '<a class="item-value-edit" title="点击编辑">';
        switch (row.type) {
            case 1:
                result = result + value;
                break;
            case 2:
                result = result + '<span class="label label-default">' + value + '</span>';
                break;
            case 3:
                result = result + '<span class="label label-primary">' + value + '</span>';
                break;
            case 4:
                try {
                    var array = JSON.parse(value);
                    if (array.length === 0) {
                        return '<span class="label label-danger"><a class="item-value-edit" title="点击编辑">空</span></a>';
                    }
                    result = result + _.map(array, function (item) {
                            return '<span class="label label-info">' + item + '</span>';
                        }).join(' ');
                } catch (e) {
                    return '<span class="label label-danger"><a title="数据类型不正确"><li class="fa fa-minus-circle"></li></a>&nbsp;&nbsp;<a class="item-value-edit" title="点击编辑">' + value + '</span></a>';
                }
                break;
            case 5:
                try {
                    var obj = JSON.parse(value);
                    if (_.isEmpty(obj)) {
                        return '<span class="label label-danger"><a class="item-value-edit" title="点击编辑">空</span></a>';
                    }
                    result = result + _.map(_.keys(obj), function (key) {
                            return '<span class="label label-warning">' + key + ' => ' + obj[key] + '</span>';
                        }).join(' ');
                } catch (e) {
                    return '<span class="label label-danger"><a title="数据类型不正确"><li class="fa fa-minus-circle"></li></a>&nbsp;&nbsp;<a class="item-value-edit" title="点击编辑">' + value + '</span></a>';
                }
                break;
            default:
                return result + '<span class="label label-danger">' + value + '</span></a>';
        }
        return result + '</a>&nbsp;&nbsp;<div class="btn-group" role="group">' +
            // '<a class="btn btn-default btn-xs item-value-sync" title="同步"><li class="fa fa-refresh"></li></a>' +
            '</div>';
    },
    /**
     * @return {string}
     */
    OperatesFormatter: function (value, row) {
        if (row.latest === 1) {
            return '<div class="btn-group" role="group">' +
                '<a class="btn btn-default btn-xs item-edit" title="编辑"><li class="fa fa-edit"></li></a>' +
                '</div>';
        }
        return '';
    },
    Operates: {
        'click .item-edit': function (e, value, row, index) {
            PageConst.Save(row, '编辑');
        },
        'click .item-value-edit': function (e, value, row, index) {
            Ajax.Get(Commons.sprintf('fetch/%s/%s?key=%s', row.project, row.env, row.key), null, function (result) {
                var renderData = {currentRow: row};
                if (result.code === 200) {
                    renderData.remoteConfigValue = result.data;
                }
                laytpl($('#saveValueModelTpl').html()).render(renderData, function (render) {
                    var index = layer.open({
                        type: 1,
                        title: Commons.sprintf('编辑配置[项目:%s,环境:%s]', row.project, row.env),
                        zIndex: 100,
                        area: ['60%', '70%'],
                        shadeClose: false,
                        content: render,
                        btn: ['保存', '取消'],
                        success: function () {
                            var valuesTable = $('#valuesTable');
                            if (row.type === 4) {
                                valuesTable.bootstrapTable({
                                    useRowAttrFunc: true,
                                    reorderableRows: true,
                                    classes: "table table-no-bordered",
                                    columns: [{
                                        field: 'checked',
                                        checkbox: true,
                                        formatter: function () {
                                            return {
                                                checked: true
                                            };
                                        }
                                    }, {
                                        field: 'value',
                                        title: '已配置的值,可拖拽排序',
                                        editable: true,
                                        editableMode: "inline"
                                    }]
                                });
                                try {
                                    valuesTable.bootstrapTable('load', _.map(JSON.parse(row.value), function (item) {
                                        return {value: item}
                                    }));
                                } catch (e) {
                                }

                                $("#addToListBtn").click(function () {
                                    var listValue = $("#listValue").val();
                                    if (!listValue) {
                                        Messager.warn("不能添加空值")
                                    } else {
                                        valuesTable.bootstrapTable('append', [{value: listValue}]);
                                    }
                                });
                            }
                            if (row.type === 5) {
                                valuesTable.bootstrapTable({
                                    useRowAttrFunc: true,
                                    classes: "table table-no-bordered",
                                    columns: [{
                                        field: 'checked',
                                        checkbox: true,
                                        formatter: function () {
                                            return {
                                                checked: true
                                            };
                                        }
                                    }, {
                                        field: 'key',
                                        title: 'Key',
                                        editable: true,
                                        editableMode: "inline"
                                    }, {
                                        field: 'value',
                                        title: 'Value',
                                        editable: true,
                                        editableMode: "inline"
                                    }]
                                });
                                try {
                                    var valueMap = JSON.parse(row.value);
                                    valuesTable.bootstrapTable('load', _.map(_.keys(valueMap), function (key) {
                                        return {key: key, value: valueMap[key]}
                                    }));
                                } catch (e) {
                                }

                                $("#addToMapBtn").click(function () {
                                    var mapKey = $("#mapKey").val();
                                    var mapValue = $("#mapValue").val();
                                    if (!mapKey || !mapValue) {
                                        Messager.warn("不能添加空值")
                                    } else {
                                        valuesTable.bootstrapTable('append', [{key: mapKey, value: mapValue}]);
                                    }
                                });
                            }
                        },
                        yes: function () {
                            var saveValueForm = Commons.formData("#saveValueForm");
                            if (row.type === 4) {
                                saveValueForm.configValue = JSON.stringify(_.map($('#valuesTable').bootstrapTable("getSelections"), function (item) {
                                    return item.value;
                                }));
                            }
                            if (row.type === 5) {
                                saveValueForm.configValue = JSON.stringify(_.object(_.map($('#valuesTable').bootstrapTable("getSelections"), function (item) {
                                    return [item.key, item.value];
                                })));
                            }
                            if (!saveValueForm.configValue) {
                                Messager.warn("配置不允许为空");
                                return;
                            }
                            if (saveValueForm.remoteConfigValue && saveValueForm.remoteConfigValue === saveValueForm.configValue) {
                                Messager.warn("配置没有任何变化");
                                return;
                            }
                            row.value = saveValueForm.configValue;
                            Ajax.Post('save', JSON.stringify(row), function (result) {
                                if (result.code === 200) {
                                    Messager.success('保存成功');
                                    $('#search').click();
                                    layer.close(index);
                                    $("#saveModal").modal("hide");
                                } else {
                                    Messager.warn(result.msg);
                                }
                            });
                        },
                        close: function () {
                            layer.close(index);
                        }
                    });
                });
            });
        },
        'click .item-value-sync': function (e, value, row, index) {
            layer.confirm('确认将配置同步至远程服务器?', {icon: 3, title: '提示'}, function (confirmIndex) {
                layer.close(confirmIndex);
            });
        }
    },
    Save: function (data, title) {
        laytpl($('#saveModelTpl').html()).render(data, function (render) {
            var index = layer.open({
                type: 1,
                title: title,
                zIndex: 100,
                area: ['80%', '80%'],
                shadeClose: false,
                content: render,
                btn: ['保存', '取消'],
                success: function () {
                    if (data || data.project) {
                        $("#viewProject").val($("#selectProject").select2("val"))
                    }
                    if (data || data.env) {
                        $("#viewEnv").val($("#selectEnv").select2("val"))
                    }
                },
                yes: function () {
                    var $saveForm = $('#saveForm');
                    $saveForm.validate({
                        errorClass: "has-error",
                        rules: {},
                        onkeyup: false
                    });
                    if ($saveForm.valid()) {
                        Ajax.Post('save', JSON.stringify(Commons.formData("#saveForm")), function (result) {
                            if (result.code === 200) {
                                Messager.success('保存成功');
                                $('#search').click();
                                layer.close(index);
                                $("#saveModal").modal("hide");
                            } else {
                                Messager.warn(result.msg);
                            }
                        });
                    }
                },
                close: function () {
                    layer.close(index);
                }
            });
        });
    }
};

$(document).ready(function () {
    var $table = $('#table'),
        $create = $('#create'),
        $search = $('#search');

    document.onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode === 13) {
            $search.click();
        }
    };

    /**
     * 初始化按钮状态
     */
    var initButtonStatus = function () {
    };

    /**
     * Check Box事件
     */
    $table.on('check.bs.table uncheck.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        initButtonStatus();
    });

    $table.on('load-error.bs.table', function (status) {
        Logger.warn(status);
        Messager.warn("加载数据失败");
    });

    $table.on('editable-save.bs.table', function (event, field, row) {

    });

    $search.click(function () {
        $table.bootstrapTable('refresh');
    });

    $create.click(function () {
        PageConst.Save({}, '新增');
    });

    $("#selectProject").on('change', function () {
        $search.click();
    });

    $("#selectEnv").on('change', function () {
        $search.click();
    });
});
