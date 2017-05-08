<#assign title = "Apollo">
<#import "/layout.ftl" as l>
<#macro link>
</#macro>
<#macro script>
<script src="/static/app/js/environment/index.js"></script>
</#macro>
<@detail/>
<#macro detail>
    <@l.layout title="${title}" enabled=true script=script link=link>
    <section class="content-header">
        <h1>
            环境管理
            <small>Environment Manage</small>
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
                                    <input type="text" name="code" class="form-control" placeholder="编码">
                                </div>
                                <div class="col-md-2">
                                    <input type="text" name="name" class="form-control" placeholder="名称">
                                </div>
                                <div class="col-md-2">
                                    <select class="form-control" name="enable">
                                        <option value="">状态</option>
                                        <option value="0">不可用</option>
                                        <option value="1">可用</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <button id="reset" type="reset" class="btn btn-default">
                                        <i class="glyphicon glyphicon-refresh"></i> 重置
                                    </button>
                                    <span class="pull-right">&nbsp;</span>
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
                                <a id="edit" class="btn btn-default" disabled>
                                    <i class="glyphicon glyphicon-edit"></i> 编辑
                                </a>
                            </div>
                            <a id="remove" class="btn btn-danger" disabled>
                                <i class="glyphicon glyphicon-remove"></i> 删除
                            </a>
                        </div>
                        <table id="table"
                               data-toolbar="#toolbar"
                               data-classes="table table-no-bordered"
                               data-toggle="table"
                               data-show-toggle="true"
                               data-show-columns="true"
                               data-click-to-select="true"
                               data-height="600"
                               data-url="/environment/list.json"
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
                                <th data-field="code">编码</th>
                                <th data-field="name">名称</th>
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
                    <div class="col-md-6"><label>编码</label>
                        <input type="hidden" name="id" value="{{d.id}}">
                        <input type="text" class="form-control" name="code"
                               placeholder="编码" value="{{= d.code}}" required {{#if(d.id){ }} readonly {{# } }}>
                    </div>
                    <div class="col-md-6"><label>名称</label>
                        <input type="text" class="form-control" name="name"
                               placeholder="名称" value="{{= d.name}}" required>
                    </div>

                </div>
                <br/>
                <div class="row">
                    <div class="col-md-6">
                        <label>可用</label>
                        <select class="form-control" name="enable">
                            <option value="1" {{#if(d.enable==1){ }} selected {{# } }}>是</option>
                            <option value="0" {{#if(d.enable==0){ }} selected {{# } }}>否</option>
                        </select>
                    </div>
                </div>
                <br/>
            </div>
        </form>
    </script>
    </@l.layout>
</#macro>