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
                <@message>设备号：</@message>
            </label>
            <input type="text" class="form-control" name="equip_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>电池条码</@message>：
            </label>
            <input type="text" class="form-control" name="battery_barcode">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>拉线通道号</@message>：
            </label>
            <input type="text" class="form-control" name="line_channel_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>开始时间</@message>：
            </label>
            <input type="text" class="form-control" id="Js_start_time" name="start_time">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>结束时间</@message>：
            </label>
            <input type="text" class="form-control" id="Js_end_time" name="complete_time">
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
    seajs.use("batteryOutList/index")
</script>
</#assign>

<#include "/end.ftl">