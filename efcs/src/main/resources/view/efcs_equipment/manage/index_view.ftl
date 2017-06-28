<#include "/begin.ftl">

<#assign style>
</#assign>

<#assign content>

<div class="page-search">
    <form class="form-inline" id="Js_search_form" method="post" action="../core/equipment/grid" >
        <div class="form-group">
            <label class="control-label">
                <@message>设备名称</@message>：
            </label>
            <input type="text" class="form-control" name="equip_name">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>设备ID</@message>：
            </label>
            <input type="text" class="form-control" name="equip_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>设备类型</@message>：
            </label>
            <select class="form-control" name="equip_type">
                <#include "/efcs_equipment/common/equip_type.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>库编号</@message>：
            </label>
            <select class="form-control" name="house_no">
                <option value="">
                    <@message>请选择</@message>
                </option>
                <#list wareHouseDtos as wareHouse>
                    <option value="${wareHouse.house_no}">${wareHouse.house_no}</option>
                </#list>
            </select>
        </div>
        <#--<div class="form-group">-->
            <#--<label class="control-label"><@message>厂家</@message>：</label>-->
            <#--<select class="form-control" name="equip_vender">-->
                <#--<option value="">请选择</option>-->
                <#--<#if equipList?? && equipList?size gt 0>-->
                    <#--<#list equipList as equip>-->
                        <#--<option value="">${equip}</option>-->
                    <#--</#list>-->
                <#--</#if>-->
            <#--</select>-->
        <#--</div>-->
        <div class="form-group">
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
    seajs.use("manage/index")
</script>

</#assign>

<#include "/end.ftl">