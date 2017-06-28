<#include '/begin.ftl'>
<#assign style>

</#assign>

<#assign content>
<form id="Js_form">
    <div class="form-group">
        <label class="col-sm-4"><@message>库编号</@message>：</label>
        <select name="house_no" class="form-control">
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
            <#include "/efcs_business/common/work_procedure.ftl">
        </select>
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>托盘号</@message></label>
        <input type="text" placeholder="请扫码" id="Js_palletNo" name="palletNo" class="form-control">
    </div>

    <div class="form-group">
        <label class="col-sm-4"><@message>设备号／位置</@message></label>
        <input type="text" placeholder="请扫码" id="device_no" name="device_no" class="form-control">
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>类型</@message></label>
        <select name="type" class="form-control">
            <option value="1">任务完成</option>
            <option value="0">托盘到位</option>
        </select>
    </div>
</form>
<input type="button" class="btn btn-primary" style="margin-top:10px;" id="Js_manual_btn" data-type="group" value="执行">
</#assign>

<#assign script>
<script>
    seajs.use("manualProcedure/test.js")
</script>
</#assign>

<#include '/end.ftl'>