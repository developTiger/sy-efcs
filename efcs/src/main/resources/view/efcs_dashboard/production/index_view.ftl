<#include "/begin.ftl">

<#assign style>
    <style>
        .dashboard .table > thead > tr > th{line-height: 30px;}
        .dashboard .table > tbody > tr > th,
        .dashboard .table > tbody > tr > td{vertical-align: middle;line-height: 30px;}
    </style>
</#assign>

<#assign content>
<div class="ibox float-e-margins">
    <div class="ibox-title">
        <h5>统计</h5>
    </div>
    <div class="ibox-content clearfix">
        <#if meterCountses?? && meterCountses?size gt 0>
            <#list meterCountses as meterCountse>
                <div class="col-lg-${12/(meterCountses?size)}">
                    <div class="ibox">
                        <div class="ibox-content">
                            <h5>${meterCountse.meterName}</h5>
                            <h1 class="no-margins Js_p_doing Js_countup" data-value="${meterCountse.counts?c}">0</h1>
                            <div class="stat-percent font-bold text-navy">
                                <span class="Js_doing_percentage Js_countup" data-value="${meterCountse.percentage}">
                                    0
                                </span>%
                            </div>
                            <small>Total income</small>
                        </div>
                    </div>
                </div>
            </#list>
        </#if>
    </div>
</div>
<div class="ibox float-e-margins clearfix row">
    <div class="col-lg-6">
        <div class="ibox-title">
            <h5>
                <@message>产量趋势图</@message>
            </h5>
        </div>
        <div class="ibox-content clearfix">
            <div style="width:100%;height:300px" id="Js_output_chart">

            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="ibox-title">
            <h5>
                <@message>产优率趋势图</@message>
            </h5>
        </div>
        <div class="ibox-content clearfix">
            <div style="width: 100%;height:300px" id="Js_superior_chart">

            </div>
        </div>
    </div>
</div>

<div class="ibox float-e-margins">
    <#if dashBoardStatisticsDtos?? && dashBoardStatisticsDtos?size gt 0>
        <#list dashBoardStatisticsDtos as dashBoardStatisticsDto>
            <div class="ibox-title">
                <h5>${dashBoardStatisticsDto.houseNo}</h5>
            </div>
            <div class="ibox-content clearfix dashboard">
                <table class="table table-bordered">
                    <colgroup>
                        <col class="col-md-1">
                        <col class="col-md-1">
                        <col class="col-md-1">
                        <col class="col-md-1">
                        <col class="col-md-1">
                        <col class="col-md-1">
                        <col class="col-md-1">
                        <col class="col-md-1">
                        <col class="col-md-1">
                    </colgroup>
                    <thead>
                        <tr>
                            <th colspan="5">
                                <a target="_blank" href="./detail_view?id=${dashBoardStatisticsDto.houseId}&type=Formation&houseNo=${dashBoardStatisticsDto.houseNo}">化成段</a>
                            </th>
                            <th colspan="4">
                                <a target="_blank" href="./detail_view?id=${dashBoardStatisticsDto.houseId}&type=Test&houseNo=${dashBoardStatisticsDto.houseNo}">测试段</a>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>
                                <@message>组盘</@message>
                            </th>
                            <th>
                                <@message>高温</@message>
                            </th>
                            <th>
                                <@message>化成</@message>
                            </th>
                            <th>
                                <@message>化成Rework</@message>
                            </th>
                            <th>
                                <@message>拆盘</@message>
                            </th>

                            <th>
                                <@message>组盘</@message>
                            </th>
                            <th>
                                <@message>一次常温</@message>
                            </th>
                            <th>
                                <@message>二次常温</@message>
                            </th>
                            <th>
                                <@message>拆盘</@message>
                            </th>
                        </tr>

                        <tr id="${dashBoardStatisticsDto.houseId}">
                            <td>${dashBoardStatisticsDto.formation_Palletize}</td>
                            <td>${dashBoardStatisticsDto.highCount}</td>
                            <td>${dashBoardStatisticsDto.formationCount}</td>
                            <td>${dashBoardStatisticsDto.formationReworkCount}</td>
                            <td>${dashBoardStatisticsDto.formationSplit}</td>
                            <td>${dashBoardStatisticsDto.testPalletizeCount}</td>
                            <td>${dashBoardStatisticsDto.normalTemperature1Count}</td>
                            <td>${dashBoardStatisticsDto.normalTemperature2Count}</td>
                            <td>${dashBoardStatisticsDto.testSplitCount}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </#list>
    </#if>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("production/index")
</script>

</#assign>
<#include "/end.ftl">