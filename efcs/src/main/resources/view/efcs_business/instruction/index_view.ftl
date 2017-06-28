<#include "/begin.ftl">

<#assign style></#assign>

<#assign content>
<div class="page-search">
    <form class="form-inline" id="Js_search_form">
        <div class="form-group">
            <label class="control-label">
                <@message>库编号</@message>：
            </label>
            <input type="text" class="form-control" name="house_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>单据号</@message>：
            </label>
            <input type="text" class="form-control" name="form_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>指令号</@message>：
            </label>
            <input type="text" class="form-control" name="instr_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>工序类型</@message>：
            </label>
            <select class="form-control" name="work_procedure">
                <#include "/efcs_business/common/work_procedure.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>设备号</@message>：
            </label>
            <input type="text" class="form-control" name="equip_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>托盘号</@message>：
            </label>
            <input type="text" class="form-control" name="pallet_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>操作时间</@message>：
            </label>
            <input type="text" class="form-control" name="start_time" id="Js_operate_datetime" autocomplete="off">
        </div>
        
        <div class="form-group">
            <label class="control-label">
                <@message>指令状态</@message>：
            </label>
            <select class="form-control" name="userDefinedStatus">
            	<option value=""><@message>请选择</@message></option>
            	<option value="Executing"><@message>未完成</@message></option>
            	<option value="Finished"><@message>已完成</@message></option>
            </select>
        </div>
        
        <div class="form-group">
            <label class="control-label">

            </label>
            <input type="submit" class="btn btn-success" value="<@message>查询</@message>">
        </div>
    </form>
</div>
<div class="page-wrapper">
    <div class="page-content">
        <table id="Js_table"></table>
        <div id="Js_pager"></div>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("instruction/index")
</script>
</#assign>

<#include "/end.ftl">