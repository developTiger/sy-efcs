<#include "/begin.ftl">

<#assign layout="single">

<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
</#assign>

<#assign content>
<div class="model-body">
    <form class="form-horizontal" method="post" action="../equipment/addorupdate" id="Js_equipment_form">
        <input type="hidden" name="parent_id" value="">
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>处置说明</@message>
            </label>
            <div class="col-xs-7">
                <textarea class="form-control"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>处置时间</@message>
            </label>
            <div class="col-xs-7">
                <input type="text" class="form-control" name="xpos_name">
            </div>
        </div>
    </form>
</div>

</#assign>

<#assign script>
<script>
    seajs.use("errorInfo/handle.js")
</script>
</#assign>

<#include "/end.ftl">