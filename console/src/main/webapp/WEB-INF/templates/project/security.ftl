<#assign title = "Apollo">
<#import "/layout.ftl" as l>
<#macro link>
</#macro>
<#macro script>
<script src="/static/app/js/project/security.js"></script>
</#macro>
<@detail/>
<#macro detail>
    <@l.layout title="${title}" enabled=true script=script link=link>
    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="box box-primary" hidden>
                    <div class="box-header">
                        <h3 class="box-title">筛选</h3>
                    </div>
                    <div class="box-body">
                        <form id="searchForm" onkeydown="if(event.keyCode===13){return false;}">
                            <div class="row">
                                <div class="col-md-2">
                                    <input type="text" name="project" class="form-control" value="${project}">
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
                       data-show-refresh="true"
                       data-show-toggle="true"
                       data-show-columns="true"
                       data-click-to-select="true"
                       data-height="600"
                       data-url="/project/security.json"
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
                        <th data-field="env">环境</th>
                        <th data-field="certification">认证信息</th>
                        <th data-field="whitelists">白名单</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </section>

    <script id="saveModelTpl" type="text/html">
        <form id="saveForm">
            <div class="container-fluid">
                <br/>
                <div class="row">
                    <div class="col-md-5"><label>环境</label>
                        <input type="hidden" name="id" value="{{d.id}}">
                        <input type="hidden" name="project" value="${project}">
                        <select class="form-control" id="selectEnv" name="env" style="width: 100%">
                            <#if environments??><#list environments as environment>
                                <option value="${environment.code}" {{#if(d.env===
                                '${environment.code}'){}} selected {{#}}}>${environment.code}
                                -${environment.name}</option>
                            </#list></#if>
                        </select>
                    </div>
                    <div class="col-md-7"><label>认证信息[<a id="buildCertification">生成</a>]</label>
                        <input type="text" class="form-control" id="certification" name="certification"
                               placeholder="认证信息" value="{{= d.certification}}" readonly required>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12"><label>白名单[多个以','分隔,'0.0.0.0'表示不限制]</label>
                        <textarea rows="3" class="form-control" name="whitelists" required>{{= d.whitelists}}{{#if(!d.id){}}0.0.0.0{{#}}}</textarea>
                    </div>
                </div>
                <br/>
            </div>
        </form>
    </script>
    </@l.layout>
</#macro>