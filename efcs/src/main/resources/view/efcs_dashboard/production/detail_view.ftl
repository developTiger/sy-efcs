<#include "/begin.ftl">

<#assign style>
<style>
    .ts-skin .ibox-content-img {
        overflow: auto;
        position: relative;
    }
    .ibox-content-img .fa-exclamation-circle{color:red;font-size:20px;}
    .dashboard table{table-layout: fixed;}
    .dashboard .table > thead > tr > th{line-height: 30px;}
    .dashboard .table > tbody > tr > th,
    .dashboard .table > tbody > tr > td{vertical-align: middle;line-height: 30px;}
</style>
</#assign>

<#assign content>

<input type="hidden" value="${id!""}" id="Js_house_id">
<div class="ibox float-e-margins">
    <div class="ibox-title">
        <h5>${houseNo!""}</h5>
    </div>
    <div class="ibox-content clearfix dashboard">
        <#if type?? && type == "Formation">
            <div>
                <@message>化成段</@message>
            </div>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>
                            <@message>组盘中</@message>
                        </th>
                        <th>
                            <@message>高温待入</@message>
                        </th>
                        <th>
                            <@message>高温静置</@message>
                        </th>
                        <th>
                            <@message>高温待出</@message>
                        </th>
                        <th>
                            <@message>化成待入</@message>
                        </th>
                        <th>
                            <@message>化成静置</@message>
                        </th>
                        <th>
                            <@message>化成待出</@message>
                        </th>
                        <th>
                            <@message>Rework组盘</@message>
                        </th>
                        <th>
                            <@message>Rework待入</@message>
                        </th>
                        <th>
                            <@message>Rework静置</@message>
                        </th>
                        <th>
                            <@message>Rework待出</@message>
                        </th>
                        <th>
                            <@message>拆盘待入</@message>
                        </th>
                        <th>
                            <@message>拆盘中</@message>
                        </th>
                        <th>
                            <@message>空托盘</@message>
                        </th>
                    </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${(formatStatisticsDto.formation_Palletize)!""}</td>
                    <td>${(formatStatisticsDto.highWait)!""}</td>
                    <td>${(formatStatisticsDto.highFinish)!""}</td>
                    <td>${(formatStatisticsDto.highOut)!""}</td>
                    <td>${(formatStatisticsDto.formatWait)!""}</td>
                    <td>${(formatStatisticsDto.formatFinish)!""}</td>
                    <td>${(formatStatisticsDto.formatOut)!""}</td>
                    <td>${(formatStatisticsDto.formationReworkPalletize)!""}</td>
                    <td>${(formatStatisticsDto.formationReworkWait)!""}</td>
                    <td>${(formatStatisticsDto.formationReworkFinish)!""}</td>
                    <td>${(formatStatisticsDto.formationReworkOut)!""}</td>
                    <td>${(formatStatisticsDto.formationSplitWait)!""}</td>
                    <td>${(formatStatisticsDto.formationSplitFinish)!""}</td>
                    <td>${(formatStatisticsDto.isEmpty)!""}</td>
                </tr>
                </tbody>
            </table>
        </#if>

        <#if type?? && type == "Test">
            <div>
                <@message>测试段</@message>
            </div>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>组盘中</th>
                        <th>常温1待入</th>
                        <th>常温1静置</th>
                        <th>常温1待出</th>
                        <th>OCV1待入</th>
                        <th>OCV1</th>
                        <th>常温2待入</th>
                        <th>常温2静置</th>
                        <th>常温2待出</th>
                        <th>OCV2待入</th>
                        <th>OCV1</th>
                        <th>拆盘待入</th>
                        <th>拆盘中</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${(testStatisticsDto.testPalletize)!""}</td>
                        <td>${(testStatisticsDto.normalTemperature1Wait)!""}</td>
                        <td>${(testStatisticsDto.normalTemperature1Finish)!""}</td>
                        <td>${(testStatisticsDto.normalTemperature1Out)!""}</td>
                        <td>${(testStatisticsDto.testOCV1Wait)!""}</td>
                        <td>${(testStatisticsDto.testOCV1)!""}</td>
                        <td>${(testStatisticsDto.normalTemperature2Wait)!""}</td>
                        <td>${(testStatisticsDto.normalTemperature2Finish)!""}</td>
                        <td>${(testStatisticsDto.normalTemperature2Out)!""}</td>
                        <td>${(testStatisticsDto.testOCV2Wait)!""}</td>
                        <td>${(testStatisticsDto.testOCV2)!""}</td>
                        <td>${(testStatisticsDto.testPalletSplitWait)!""}</td>
                        <td>${(testStatisticsDto.testPalletSplitFinish)!""}</td>
                    </tr>
                </tbody>
            </table>
        </#if>
    </div>
</div>

<div class="ibox float-e-margins">
    <div class="ibox-title">
        <h5>
            <@message>全景图</@message>
        </h5>
    </div>
    <div class="ibox-content ibox-content-img">
        <#if type?? && type == "Formation">
            <img src="${contextPath}/efcs_dashboard/images/hc.jpg" style="width:1242px;height:275px;" usemap="#hcMap">
            <map name="hcMap">
                <area shape="rect" coords="172,2,584,68" data-x="1" class="Js_area">
                <area shape="rect" coords="172,101,584,165" data-x="2" class="Js_area">
                <area shape="rect" coords="663,101,1074,165" data-x="3" class="Js_area">
                <area shape="rect" coords="406,198,1074,266" data-x="4" class="Js_area">
            </map>

            <div class="Js_pallet_error" style="position: absolute;left:106px;top:89px;">
                <i class="fa fa-exclamation-circle"></i>
            </div>
            <div class="Js_pallet_error" style="position: absolute;left:170px;top:175px;">
                <i class="fa fa-exclamation-circle"></i>
            </div>
        </#if>

        <#if type?? && type =="Test">
            <img src="${contextPath}/efcs_dashboard/images/cs.jpg" style="width:1567px;height:409px;" usemap="#csMap">
            <map name="csMap">
                <area shape="rect" coords="150,1,1420,67" data-x="5" class="Js_area">
                <area shape="rect" coords="150,69,1420,171" data-x="6" class="Js_area">
                <area shape="rect" coords="150,134,1420,266" data-x="7" class="Js_area">
                <area shape="rect" coords="150,203,1420,368" data-x="8" class="Js_area">
            </map>

            <div class="Js_pallet_error" style="position: absolute;left:125px;top:86px;">
                <i class="fa fa-exclamation-circle"></i>
            </div>
            <div class="Js_pallet_error" style="position: absolute;left:144px;top:275px;">
                <i class="fa fa-exclamation-circle"></i>
            </div>
        </#if>
    </div>
</div>

<div class="ibox float-e-margins">
    <div class="col-lg-6">
        <div class="ibox-title">
            <h5>复测率</h5>
        </div>
        <div class="ibox-content clearfix">
            <div style="width:100%;height:300px" id="Js_rework_chart">

            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="ibox-title">
            <h5>NG率</h5>
        </div>
        <div class="ibox-content clearfix">
            <div style="width: 100%;height:300px" id="Js_ng_chart">

            </div>
        </div>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("production/detail")
</script>

</#assign>
<#include "/end.ftl">