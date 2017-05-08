<#import "common/head.ftl" as head>
<#macro layout title enabled script link>
<!DOCTYPE html>
<html>
<head>
    <@head.head title="${title}"/>
    <@link/>
</head>
<body class="hold-transition fixed skin-green-light sidebar-mini">
<div class="wrapper">
    <#if enabled?? && enabled>
        <#nested>
    </#if>
    <#include "common/scripts.ftl">
    <@script/>
</div>
</body>
</html>
</#macro>