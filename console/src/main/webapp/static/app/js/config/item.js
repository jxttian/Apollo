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
