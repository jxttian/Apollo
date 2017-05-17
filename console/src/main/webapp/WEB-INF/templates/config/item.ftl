<#assign title = "Apollo">
<#import "/layout.ftl" as l>
<#macro link>
<link href="/static/lib/bootstrap-table/extensions/reorder-rows/bootstrap-table-reorder-rows.css">
<link href="/static/lib/bootstrap-table/extensions/editable/bootstrap-editable.css">
</#macro>
<#macro script>
<script src="/static/lib/bootstrap-table/extensions/reorder-rows/jquery.tablednd.js"></script>
<script src="/static/lib/bootstrap-table/extensions/reorder-rows/bootstrap-table-reorder-rows.js"></script>
<script src="/static/lib/bootstrap-table/extensions/editable/bootstrap-editable.js"></script>
<script src="/static/lib/bootstrap-table/extensions/editable/bootstrap-table-editable.min.js"></script>
<script src="/static/app/js/config/item.js"></script>
</#macro>
<@detail/>
<#macro detail>
    <@l.layout title="${title}" enabled=true script=script link=link>
    <section class="content-header">
        <h1>
            配置项管理
            <small>Config Item Manage</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            <li class="active">Here</li>
        </ol>
    </section>
    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">筛选</h3>
                    </div>
                    <div class="box-body">
                        <form id="searchForm" onkeydown="if(event.keyCode===13){return false;}">
                            <div class="row">
                                <div class="col-md-2">
                                    <select class="form-control select2" id="selectProject" name="project"
                                            style="width: 100%">
                                        <#if projects??><#list projects as project>
                                            <option value="${project.code}">${project.code}
                                                -${project.name}</option>
                                        </#list></#if>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <select class="form-control select2" id="selectEnv" name="env" style="width: 100%">
                                        <#if environments??><#list environments as environment>
                                            <option value="${environment.code}">${environment.code}
                                                -${environment.name}</option>
                                        </#list></#if>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <input type="text" name="name" class="form-control" placeholder="名称">
                                </div>
                                <div class="col-md-2">
                                    <input type="text" name="key" class="form-control" placeholder="键">
                                </div>
                                <div class="col-md-2">
                                    <select class="form-control" name="enable">
                                        <option value="">状态</option>
                                        <option value="0">不可用</option>
                                        <option value="1" selected>可用</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <button id="search" type="button" class="btn btn-primary">
                                        <i class="glyphicon glyphicon-search"></i> 搜索
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="box box-primary">
                    <div class="box-body">
                        <div id="toolbar">
                            <div class="btn-group" role="group">
                                <a id="create" class="btn btn-default">
                                    <i class="glyphicon glyphicon-plus"></i> 添加
                                </a>
                            </div>
                        </div>
                        <table id="table"
                               data-toolbar="#toolbar"
                               data-classes="table table-no-bordered"
                               data-toggle="table"
                               data-show-toggle="true"
                               data-show-columns="true"
                               data-click-to-select="true"
                               data-height="600"
                               data-url="/config/item/list.json"
                               data-side-pagination="server"
                               data-pagination="true"
                               data-query-params="PageConst.Query"
                               data-id-field="id"
                               data-unique-id="id"
                               data-sort-name="id"
                               data-sort-order="desc"
                               data-page-list="[10, 20, 50, 100]">
                            <thead>
                            <tr>
                                <th data-field="operate" data-formatter="PageConst.OperatesFormatter"
                                    data-events="PageConst.Operates">操作
                                </th>
                                <th data-field="project" data-visible="false">项目</th>
                                <th data-field="env" data-visible="false">环境</th>
                                <th data-field="name">名称</th>
                                <th data-field="key">键</th>
                                <th data-field="value" data-formatter="PageConst.ValueFormatter"
                                    data-events="PageConst.Operates">值
                                </th>
                                <th data-field="type" data-formatter="PageConst.TypeFormatter">类型</th>
                                <th data-field="riskLevel" data-formatter="PageConst.RiskLevelFormatter">风险等级</th>
                                <th data-field="desc">描述</th>
                                <th data-field="version" data-visible="false">版本</th>
                                <th data-field="latest" data-visible="false" data-formatter="Formatter.Boolean">最新</th>
                                <th data-field="enable" data-formatter="Formatter.Boolean">可用</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script id="saveModelTpl" type="text/html">
        <form id="saveForm">
            <div class="container-fluid">
                <br/>
                <div class="row">
                    <div class="col-md-4"><label>项目</label>
                        <input type="hidden" name="id" value="{{d.id}}">
                        <input type="hidden" name="version" value="{{d.version}}">
                        <input type="text" class="form-control" name="project" value="{{=d.project}}" id="viewProject"
                               readonly>
                    </div>
                    <div class="col-md-4"><label>环境</label>
                        <input type="text" class="form-control" name="env" value="{{=d.env}}" id="viewEnv" readonly>
                    </div>
                    <div class="col-md-4"><label>名称</label>
                        <input type="text" class="form-control" name="name"
                               placeholder="名称" value="{{= d.name}}" required>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-6"><label>键</label>
                        <input type="text" class="form-control" name="key"
                               placeholder="键" value="{{= d.key}}" required>
                    </div>
                    <div class="col-md-2">
                        <label>风险等级</label>
                        <select class="form-control" name="riskLevel">
                            <option value="0" {{#if(d.riskLevel==0){ }} selected {{# } }}>0</option>
                            <option value="1" {{#if(d.riskLevel==1){ }} selected {{# } }}>1</option>
                            <option value="2" {{#if(d.riskLevel==2){ }} selected {{# } }}>2</option>
                            <option value="3" {{#if(d.riskLevel==3){ }} selected {{# } }}>3</option>
                            <option value="4" {{#if(d.riskLevel==4){ }} selected {{# } }}>4</option>
                            <option value="5" {{#if(d.riskLevel==5){ }} selected {{# } }}>5</option>
                            <option value="6" {{#if(d.riskLevel==6){ }} selected {{# } }}>6</option>
                            <option value="7" {{#if(d.riskLevel==7){ }} selected {{# } }}>7</option>
                            <option value="8" {{#if(d.riskLevel==8){ }} selected {{# } }}>8</option>
                            <option value="9" {{#if(d.riskLevel==9){ }} selected {{# } }}>9</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label>类型</label>
                        <select class="form-control" name="type">
                            <option value="1" {{#if(d.type==1){ }} selected {{# } }}>String</option>
                            <option value="2" {{#if(d.type==2){ }} selected {{# } }}>Number</option>
                            <option value="3" {{#if(d.type==3){ }} selected {{# } }}>Boolean</option>
                            <option value="4" {{#if(d.type==4){ }} selected {{# } }}>List</option>
                            <option value="5" {{#if(d.type==5){ }} selected {{# } }}>Map</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label>可用</label>
                        <select class="form-control" name="enable">
                            <option value="1" {{#if(d.enable==1){ }} selected {{# } }}>是</option>
                            <option value="0" {{#if(d.enable==0){ }} selected {{# } }}>否</option>
                        </select>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <label>值</label>
                        <textarea class="form-control" rows="5" name="value" required>{{=d.value}}</textarea>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <label>描述</label>
                        <input type="text" class="form-control" name="desc"
                               placeholder="描述" value="{{= d.desc}}">
                    </div>
                </div>
                <br/>
            </div>
        </form>
    </script>

    <script id="saveValueModelTpl" type="text/html">
        <form id="saveValueForm">
            <div class="container-fluid">
                <br/>
                <div class="row">
                    <div class="col-md-6"><label>名称</label>
                        <input type="text" class="form-control"
                               value="{{= d.currentRow.name}}" readonly>
                    </div>
                    <div class="col-md-6"><label>键</label>
                        <input type="text" class="form-control"
                               value="{{= d.currentRow.key}}" readonly>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12"><label>远程配置内容</label>
                        <textarea class="form-control" rows="3" name="remoteConfigValue"
                                  readonly>{{=d.remoteConfigValue}}</textarea>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <label>配置</label>
                        {{#if(d.currentRow.type == 1){}}
                        <textarea class="form-control" rows="3"
                                  name="configValue">{{=d.currentRow.value}}</textarea>
                        {{#}}}
                        {{#if(d.currentRow.type == 2){}}
                        <input type="number" class="form-control"
                               name="configValue" value="{{=d.currentRow.value}}" style="width: 30%">
                        {{#}}}
                        {{#if(d.currentRow.type == 3){}}
                        <select class="form-control" name="configValue" style="width: 30%">
                            <option value="true" {{#if(d.currentRow.value==
                            'true'){}} selected {{#}}}>true</option>
                            <option value="false" {{#if(d.currentRow.value==
                            'false'){}} selected {{#}}}>false</option>
                        </select>
                        {{#}}}
                        {{#if(d.currentRow.type == 4){}}
                        <div class="row">
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="listValue" placeholder="值">
                            </div>
                            <div class="col-md-2">
                                <button type="button" id="addToListBtn" class="btn btn-primary">
                                    <i class="glyphicon glyphicon-plus"></i> 添加
                                </button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <table id="valuesTable">
                                </table>
                            </div>
                        </div>
                        {{#}}}
                        {{#if(d.currentRow.type == 5){}}
                        <div class="row">
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="mapKey" placeholder="键">
                            </div>
                            <div class="col-md-4">
                                <input type="text" class="form-control" id="mapValue" placeholder="值">
                            </div>
                            <div class="col-md-2">
                                <button type="button" id="addToMapBtn" class="btn btn-primary">
                                    <i class="glyphicon glyphicon-plus"></i> 添加
                                </button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <table id="valuesTable">
                                </table>
                            </div>
                        </div>
                        {{#}}}
                    </div>
                </div>
                <br/>
            </div>
        </form>
    </script>
    </@l.layout>
</#macro>