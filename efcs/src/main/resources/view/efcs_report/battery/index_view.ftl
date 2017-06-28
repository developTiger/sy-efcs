<#include "/begin.ftl">

<#assign style>

</#assign>

<#assign content>
<div class="page-search">
    <form class="form-inline" id="Js_search_form" method="post">
        <div class="form-group">
            <label class="control-label"><@message>库编号</@message>：</label>
            <input type="text" class="form-control" name="house_no">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>电池条码</@message>：</label>
            <input type="text" class="form-control" name="battery_barcode">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>单据号</@message>：</label>
            <input type="text" class="form-control" name="form_no">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>电池状态</@message>：</label>
            <select class="form-control" name="battery_status">
               <#include "/efcs_business/common/battery_status.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>工序状态</@message>：</label>
            <select class="form-control" name="work_procedure">
                <#include "/efcs_business/common/work_procedure.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>托盘号</@message>：</label>
            <input type="text" class="form-control" name="pallet_no">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>当前位置</@message>：</label>
            <input type="text" class="form-control" name="current_pos">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>查询</@message>">
        </div>
    </form>
</div>

<div class="page-wrapper">

    <div class="page-content" id="Js_table_container">
        <table id="Js_table"></table>
        <div id="Js_pager"></div>
    </div>
</div>

</#assign>

<#assign script>
<script>
    seajs.use("battery/index")
</script>
</#assign>

<#include "/end.ftl">