<#include "/begin.ftl">

<#assign layout="single">

<#assign content>

<div class="Js_location_container">
    <#if wareHouseDto??>
        <div class="chart-container"
             data-hid="${wareHouseDto.id}"
             data-x="${wareHouseDto.row_x}"
             data-y="${wareHouseDto.row_max_y}"
             data-miny="${(wareHouseDto.row_min_y)!""}"
             data-z="${wareHouseDto.row_max_z}"
             data-minz="${(wareHouseDto.row_min_z)!""}"
        >
        </div>

    </#if>
</div>
</#assign>

<#assign script>
<script>
    <#if wareHouseDto?? >
        seajs.use("storeDetail/single");
    </#if>
</script>
</#assign>

<#include "/end.ftl">