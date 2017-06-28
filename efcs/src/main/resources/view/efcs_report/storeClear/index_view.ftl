<#include "/begin.ftl">

<#assign style>

</#assign>

<#assign content>
<div class="page-search">
    <form class="form-inline" id="Js_search_form" method="post">
        <div class="form-group">
            <label class="control-label"><@message>库编号</@message>：</label>
            <select name="wareHouse" class="form-control">
                <#list houseDto as item>
                    <option value="${item.house_no}">${item.house_name}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label"><@message>库位编号</@message>：</label>
            <select class="form-control" name="loc_no">
                <option value="1010">1010</option>
                <option value="3011">3011</option>
                <option value="3121">3121</option>
            </select>
        </div>
    </form>
</div>
<div class="page-buttons">
    <a class="btn btn-link Js_storeClear" data-type="0">移除</a>
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
    seajs.use("storeClear/index")
</script>
</#assign>

<#include "/end.ftl">