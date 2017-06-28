<#include '/begin.ftl'>
<#assign style>

</#assign>

<#assign content>
<form id="Js_form">
    <div class="form-group">
        <label class="col-sm-4"><@message>库编号</@message>：</label>
        <select name="wareHouse" class="form-control">
            <#list houseDto as item>
                <option value="${item.house_no}">${item.house_name}</option>
            </#list>
        </select>
    </div>
    <div class="form-group">
        <label class="col-sm-4">
            <@message>工序</@message>：
        </label>
        <select class="form-control" name="work_procedure" id="Js_work_procedure">
            <#include "/efcs_business/common/manual_work_procedure.ftl">
        </select>
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>托盘号</@message></label>
        <input type="text" placeholder="请扫码" name="palletNo" id="Js_palletNo" class="form-control">
    </div>
</form>
<a class="btn btn-primary" style="margin-top:10px;" id="Js_manual_btn" data-type="fake">补假电池</a>
</#assign>

<#assign script>
<script>
    seajs.use("manualProcedure/index.js")
</script>
</#assign>

<#include '/end.ftl'>