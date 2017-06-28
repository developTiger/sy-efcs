<#include "/begin.ftl">
<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">

</#assign>

<#assign content>

<div class="page-search">
    <div class="mt10">
        <span>库：</span>

        <#if wareHouseDtos?? && wareHouseDtos?size gt 0>
            <#list wareHouseDtos as wareHouseDto>
                <a class="btn btn-default Js_wareHouse_btn" data-id="${wareHouseDto.id}"
                >${wareHouseDto.house_name}</a>
            </#list>
        </#if>
    </div>
    <div class="mt10">
        <span>类型：</span>
        <div style="display: inline-block;" class="tab-content">
            <#list wareHouseDtos as wareHouseDto>
                <div id="${wareHouseDto.id}" class="tab-pane Js_warseHouse_panel">
                    <#if zonesDtos?? && zonesDtos?size gt 0>
                        <#list zonesDtos as zonesDto>
                            <#if (zonesDto.childs)?? && zonesDto.childs?size gt 0 >
                                <#list zonesDto.childs as child>
                                    <a href="#${wareHouseDto.id}-${child.id}" data-type="${child.id}"
                                       data-id="${wareHouseDto.id}-${child.id}"
                                       class="btn btn-default Js_wareHouse_x"
                                    >${child.zone_name}</a>
                                </#list>
                            </#if>
                        </#list>
                    </#if>
                </div>
            </#list>
        </div>
        <a class="btn btn-success Js_link_statistics" style="float:right" target="_blank">数据统计</a>
    </div>
</div>

<input type="hidden" id="Js_zones_dtos" value='${json(zonesDtos)!""}'>

<div class="page-wrapper heatmap-wrapper tab-content">
    <#list wareHouseDtos as wareHouseDto>
        <#if zonesDtos?? && zonesDtos?size gt 0>
            <#list zonesDtos as zonesDto>
                <#if (zonesDto.childs)?? && zonesDto.childs?size gt 0>
                    <#list zonesDto.childs as child>
                        <div class="Js_location_container tab-pane" id="${wareHouseDto.id}-${child.id}">
                            <#if rowMaps?? && rowMaps[wareHouseDto.id]?? && rowMaps[wareHouseDto.id][child.id]??>
                                <#list rowMaps[wareHouseDto.id][child.id] as row>
                                    <div class="panel panel-default Js_panel_container">
                                        <div class="panel-heading clearfix">
                                            <span style="float:left;">${row.row_name}</span>
                                        </div>
                                        <div class="chart-container" data-x="${(row.row_x)!""}"
                                             data-y="${(row.row_max_y)!""}" data-miny="${(row.row_min_y)!""}"
                                             data-z="${(row.row_max_z)!""}" data-minz="${(row.row_min_z)!""}"
                                             data-hid="${wareHouseDto.id}">
                                        </div>
                                    </div>
                                </#list>
                            </#if>
                        </div>
                    </#list>
                </#if>
            </#list>
        </#if>
    </#list>
    <div id="tooltip" class="hidden">
        <p id="value"></p>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("storeDetail/lock")
</script>
</#assign>

<#include "/end.ftl">