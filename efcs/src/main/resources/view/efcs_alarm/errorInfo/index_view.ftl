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
            <label class="control-label"><@message>异常类型</@message>：</label>
            <select class="form-control" name="alarm_type">
                <#include "/efcs_alarm/common/alarm_type.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>异常级别</@message>：</label>
            <select class="form-control" name="alarm_level">
                <#include "/efcs_alarm/common/alarm_level.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>库位号／设备号</@message>：</label>
            <input type="text" class="form-control" name="location">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>查询</@message>">
        </div>
    </form>
</div>
<#--公共操作区-->
<div class="page-wrapper">
    <div class="page-content" id="Js_table_container">
        <table id="Js_table"></table>
        <div id="Js_pager"></div>
    </div>
</div>

</#assign>
<#assign script>
<script>
    seajs.use("errorInfo/index")
</script>

</#assign>

<#include "/end.ftl">