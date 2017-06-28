<#include "/begin.ftl">
<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
</#assign>

<#assign content>
<div class="page-search">
    <div class="mt10">
        <span><@message>库</@message>：</span>
        <#if wareHouseDtos?? && wareHouseDtos?size gt 0>
            <#list wareHouseDtos as wareHouseDto>
                <a class="btn btn-default Js_wareHouse_btn
                    <#if wareHouseDto_index ==0>
                        active
                    </#if>" data-id="${wareHouseDto.id}"
                >${wareHouseDto.house_name}</a>
            </#list>
        </#if>
    </div>
</div>

<div class="page-wrapper heatmap-wrapper tab-content">
    <#if wareHouseDtos?? && wareHouseDtos?size gt 0>
        <#list wareHouseDtos as wareHouseDto>
            <div class="Js_location_container tab-pane  <#if wareHouseDto_index ==0>
                        active
                    </#if>" id="${wareHouseDto.id}">
                <#list wareHouseDto.warehouseRows as row>
                    <#if rowMap[(row_index+1)?string("0")] != "HighTemplate">
                        <div class="panel panel-default Js_panel_container">
                            <div class="panel-heading clearfix">
                                <span style="float:left;">${row.row_name}</span>
                            </div>
                            <div class="chart-container"
                                 data-x="${(row.row_x)!""}"
                                 data-y="${(row.row_max_y)!""}"
                                 data-miny="${(row.row_min_y)!""}"
                                 data-z="${(row.row_max_z)!""}"
                                 data-minz="${(row.row_min_z)!""}"
                                  <#if rowMap[(row_index+1)?string("0")] == "Formation">
                                        data-id="hc"
                                  <#else >
                                    data-id="cw" data-statistics="false" data-temperature = "true"
                                  </#if>
                            >
                            </div>
                        </div>
                    </#if>
                </#list>
            </div>
        </#list>
    </#if>

    <div id="tooltip" class="hidden">
        <p id="value"></p>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("temperature/index.js")
</script>
</#assign>

<#include "/end.ftl">