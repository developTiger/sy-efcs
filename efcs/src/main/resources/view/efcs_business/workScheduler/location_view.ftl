<#include "/begin.ftl">

<#assign layout="single">
<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
</#assign>

<#assign content>

<div class="page-wrapper heatmap-wrapper tab-content">
    <#if wareHouseDtos?? && wareHouseDtos?size gt 0>
        <#list wareHouseDtos as wareHouseDto>
            <div class="Js_location_container tab-pane  <#if wareHouseDto_index ==0>
                        active
                    </#if>" id="${wareHouseDto.id}">
                <#list wareHouseDto.warehouseRows as row>
                    <#if rowMap[(row_index+1)?string("0")] == "Formation">
                        <div class="panel panel-default Js_panel_container">
                            <div class="panel-heading clearfix">
                                <span style="float:left;">
                                    <label style="margin-right:20px;">
                                        全选
                                        <input type="checkbox" class="Js_location_selectAll" >
                                    </label>
                                    ${row.row_name}
                                </span>
                            </div>
                            <div class="chart-container"
                                 data-x="${(row.row_x)!""}"
                                 data-y="${(row.row_max_y)!""}"
                                 data-miny="${(row.row_min_y)!""}"
                                 data-z="${(row.row_max_z)!""}"
                                 data-minz="${(row.row_min_z)!""}"
                                 data-statistics="false"
                            >

                            </div>
                        </div>
                    </#if>
                </#list>
            </div>
        </#list>
    </#if>

    <form class="form-horizontal" method="post"  id="Js_workScheduler_form">
        <input type="hidden" name="location_ids">
        <#if locationDtos??>
            <input type="hidden"  name="location_ids_scheduler" value='${json(locationDtos)}'>
        </#if>
    </form>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("workScheduler/location.js")
</script>
</#assign>

<#include "/end.ftl">