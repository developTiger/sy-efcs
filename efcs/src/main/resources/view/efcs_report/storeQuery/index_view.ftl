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
            <label class="control-label"><@message>库位编号</@message>：</label>
            <input type="text" class="form-control" name="loc_no">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>托盘编号</@message>：</label>
            <input type="text" class="form-control" name="pallet_no">
        </div>
        <div class="form-group">
            <label class="control-label"><@message>sku类型</@message>：</label>
            <select class="form-control" name="sku_id">
                <option value="">请选择</option>
                <#list skuInfos as skuInfo>
                    <option value="${skuInfo.id}">${skuInfo.sku_name}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>sku条码</@message>：</label>
            <input type="text" class="form-control" name="sku_barcode">
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
    seajs.use("storeQuery/index")
</script>
</#assign>

<#include "/end.ftl">