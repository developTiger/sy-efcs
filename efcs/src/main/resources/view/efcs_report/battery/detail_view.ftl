<#include "/begin.ftl">
<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
<style>
    .timeline-item .date i.timeline-icon {
        width: 40px;
        height: 40px;
        color: #fff;
        font-size: 12px;
        font-weight: bold;
    }

    .timeline-item .date i.timeline-icon-BND {
        background-color: #28b294;
    }

    .timeline-item .date i.timeline-icon-HT {
        background-color: #2a88c4;
    }

    .timeline-item .date i.timeline-icon-FM {
        background-color: #32c5c7;
    }

    .timeline-item .date i.timeline-icon-SRT {
        background-color: #f7ab61;
    }

    .timeline-item .date i.timeline-icon-ST1 {
        background-color: #32c5c7;
    }

    .timeline-item .date i.timeline-icon-ocv1 {
        background-color: #28b294;
    }

    .vertical-timeline-content {
        position: relative;
        margin-left: 60px;
        background: #fff;
        border-radius: .25em;
        padding: 1em
    }

    .vertical-timeline-content:after {
        content: "";
        display: table;
        clear: both
    }

    .vertical-timeline-content {
        background: #f5f5f5;
    }

    .vertical-timeline-content:before {
        border-color: transparent #f5f5f5 transparent transparent;
    }

</style>
</#assign>

<#assign content>
<div class="ibox-content inspinia-timeline">
    <div class="ibox">
        <div class="ibox-title">
            <@message>电池信息</@message>
        </div>
        <div class="ibox-content clearfix">
            <div class="col-sm-5">
                <dl class="dl-horizontal">
                    <dt>
                        <@message>电池条码</@message>：
                    </dt>
                    <dd>${(palletBatteryDto.battery_barcode)!""}</dd>
                    <dt>
                        <@message>电池状态</@message>：
                    </dt>
                    <dd>
                        <#if (palletBatteryDto.battery_status)??>
                            <#if palletBatteryDto.battery_status == "OK">
                                优品
                            <#else >
                                NG
                            </#if>
                        </#if>
                    </dd>
                    <dt>
                        <@message>单据号</@message>：
                    </dt>
                    <dd>${(palletBatteryDto.form_no)!""}</dd>
                    <dt>
                        <@message>托盘号</@message>：
                    </dt>
                    <dd>${(palletBatteryDto.pallet_no)!""}</dd>
                </dl>
            </div>
            <div class="col-sm-7" id="cluster_info">
                <dl class="dl-horizontal">
                    <dt>
                        <@message>库编号</@message>：
                    </dt>
                    <dd>${(palletBatteryDto.house_no)!""}</dd>
                    <dt>
                        <@message>当前位置</@message>：
                    </dt>
                    <dd>${(palletBatteryDto.current_pos)!""}</dd>
                    <dt>
                        <@message>位置类型</@message>：
                    </dt>
                    <dd>
                        <#if (palletBatteryDto.pos_type)??>
                            <#if palletBatteryDto.pos_type == "Storage_Location">
                                库位中的位置
                            <#elseif  palletBatteryDto.pos_type == "Transport_Location">
                                输送线上的位置
                            <#elseif  palletBatteryDto.pos_type == "Pallet">
                                托盘中的位置
                            <#elseif  palletBatteryDto.pos_type == "Line">
                                拉带线中的位置
                            </#if>
                        </#if>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
    <div class="ibox">
        <div class="ibox-title">
            <@message>流程信息</@message>
        </div>
        <div class="ibox-content clearfix">
            <div id="vertical-timeline" class="vertical-container dark-timeline">
                <#if palletBatteryDto??
                && (palletBatteryDto.batteryMoveDetail)??
                && (palletBatteryDto.batteryMoveDetail)?size gt 0>
                    <#list palletBatteryDto.batteryMoveDetail as palletMoveDetailDto>

                        <div class="vertical-timeline-block">
                            <div class="vertical-timeline-icon navy-bg">
                                <i class="fa fa-briefcase"></i>
                            </div>

                            <div class="vertical-timeline-content">
                                <#if palletMoveDetailDto.work_procedure == "Formation_In">
                                    <h2>
                                        化成段电池入库
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Formation_Palletize">
                                    <h2>
                                        化成组盘
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "High_Temperature">
                                    <h2>
                                        高温静置
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Formation">
                                    <h2>
                                        化成
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Formation_Split">
                                    <h2>
                                        化成拆盘
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Formation_Rework_Palletize">
                                    <h2>
                                        化成REWORK组盘
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Formation_Rework">
                                    <h2>
                                        化成REWORK
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Formation_Out">
                                    <h2>
                                        化成电池出库
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Test_In">
                                    <h2>
                                        测试段电池入库
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Test_Palletize">
                                    <h2>
                                        测试组盘
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Normal_Temperature_1">
                                    <h2>
                                        常温静置1
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Test_OCV_1">
                                    <h2>
                                        测试OCV1
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Normal_Temperature_2">
                                    <h2>
                                        常温静置2
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Test_OCV_2">
                                    <h2>
                                        测试OCV2
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Test_Out">
                                    <h2>
                                        测试段电池出库
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Test_Pallet_Split">
                                    <h2>
                                        测试拆盘
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Palletize_Cache">
                                    <h2>
                                        移动到组盘缓存
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "FORMATION_ERROR_EXPORT">
                                    <h2>
                                        化成段异常排除
                                    </h2>
                                <#elseif palletMoveDetailDto.work_procedure == "Formation_PalletMove">
                                    <h2>
                                        无业务工序托盘流转移动
                                    </h2>
                                </#if>
                                <p>
                                时间：${(palletMoveDetailDto.create_datetime?string('yyyy-MM-dd HH:mm:ss'))!""}<br>
                                位置：${(palletMoveDetailDto.house_no)!""}
                                    <#if ((palletMoveDetailDto.pos_type)!"") == "Storage_Location" >
                                        库位中的位置
                                    <#elseif ((palletMoveDetailDto.pos_type)!"") == "Transport_Location">
                                        输送线上的位置
                                    <#elseif ((palletMoveDetailDto.pos_type)!"") == "Pallet">
                                        托盘中的位置
                                    <#elseif ((palletMoveDetailDto.pos_type)!"") == "Line">
                                        拉带线中的位置
                                    </#if>
                                    <br>
                                单据号：${(palletMoveDetailDto.form_no)!""}
                                </p>
                            </div>
                        </div>
                    </#list>
                </#if>
            </div>
        </div>
    </div>
</div>
</#assign>

<#assign script>
</#assign>

<#include "/end.ftl">