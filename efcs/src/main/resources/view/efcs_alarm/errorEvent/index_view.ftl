<#include "/begin.ftl">

<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
</#assign>

<#assign content>
<#--公共操作区-->
<div class="page-wrapper">
    <div class="page-content" id="Js_table_container">
        <table id="Js_table"></table>
    </div>
</div>

</#assign>
<#assign script>
<script>
    seajs.use("errorEvent/index")
</script>

</#assign>

<#include "/end.ftl">