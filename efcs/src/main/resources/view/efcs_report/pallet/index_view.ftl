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
            <label class="control-label"><@message>托盘条码</@message>：</label>
            <input type="text" class="form-control" name="container_no">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>托盘状态</@message>：</label>
            <select name="pallet_status" class="form-control">
                <#include "/efcs_business/common/pallet_status.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>调度状态</@message></label>
            <select class="form-control" name="dispatchStatus">
                <option value="">全部</option>
                <option value="Dispatching" selected>调度中</option>
                <option value="Finished">调度完成</option>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>当前位置</@message>：</label>
            <input type="text" class="form-control" name="current_pos">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>工序状态</@message>：</label>
            <select name="work_procedure" class="form-control">
                <#include "/efcs_business/common/work_procedure.ftl">
            </select>
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
    seajs.use("pallet/index")
</script>
</#assign>

<#include "/end.ftl">