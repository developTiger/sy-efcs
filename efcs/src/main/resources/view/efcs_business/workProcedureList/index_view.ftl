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
                <@message>工序</@message>：
            </label>
            <select class="form-control" name="work_procedure">
                <#include "/efcs_business/common/work_procedure.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>托盘号</@message>：
            </label>
            <input type="text" class="form-control" name="pallet_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>托盘状态</@message>：
            </label>
            <select class="form-control" name="pallet_status">
                <#include "/efcs_business/common/pallet_status.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"></label>
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
    seajs.use("workProcedureList/index")
</script>
</#assign>

<#include "/end.ftl">