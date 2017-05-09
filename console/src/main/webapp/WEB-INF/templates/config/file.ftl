<#assign title = "Apollo">
<#import "/layout.ftl" as l>
<#macro link>
</#macro>
<#macro script>
<script src="/static/app/js/config/file.js"></script>
</#macro>
<@detail/>
<#macro detail>
    <@l.layout title="${title}" enabled=true script=script link=link>
    <section class="content-header">
        <h1>
            配置文件管理
            <small>Config File Manage</small>
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
                               data-url="/config/file/list.json"
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
                                <th data-field="desc">描述</th>
                                <th data-field="version">版本</th>
                                <th data-field="latest" data-formatter="Formatter.Boolean">最新</th>
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
                    <div class="col-md-3"><label>项目</label>
                        <input type="hidden" name="id" value="{{d.id}}">
                        <input type="hidden" name="version" value="{{d.version}}">
                        <input type="text" class="form-control" name="project" value="{{=d.project}}" id="viewProject"
                               readonly>
                    </div>
                    <div class="col-md-3"><label>环境</label>
                        <input type="text" class="form-control" name="env" value="{{=d.env}}" id="viewEnv" readonly>
                    </div>
                    <div class="col-md-4"><label>名称</label>
                        <input type="text" class="form-control" name="name"
                               placeholder="名称" value="{{= d.name}}" required>
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
                        <label>描述</label>
                        <input type="text" class="form-control" name="desc"
                               placeholder="描述" value="{{= d.desc}}">
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <label>内容</label>
                        <textarea class="form-control" rows="15" name="content" required>{{=d.content}}</textarea>
                    </div>
                </div>
                <br/>

            </div>
        </form>
    </script>
    </@l.layout>
</#macro>