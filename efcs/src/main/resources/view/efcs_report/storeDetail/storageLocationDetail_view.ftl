<#include "/begin.ftl">

<#assign layout="single">

<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
<style>
    .wrapper, .wrapper-content {
        padding: 0;
    }

    .panel-body.locationDetail a {
        min-width: 200px;
        border: 1px solid #d7d7d7;
        height: 28px;
        line-height: 28px;
        display: inline-block;
        text-align: center;
        margin-top: 4px;
    }
    .locationDetail dd label{width:30px;text-align: right;}
    .ibox{margin-bottom: 0;}
    .ibox dd,.ibox dt{line-height: 30px;}
</style>
</#assign>

<#assign content>
<div class="model-body">
    <#if storageBatteryDetailDto??>
        <div>
            <div class="layui-layer-title">
                库位${(storageBatteryDetailDto.xPos)!""}-
                    ${(storageBatteryDetailDto.yPos)!""}-
                    ${(storageBatteryDetailDto.zPos)!""}
                托盘${(storageBatteryDetailDto.pallet_no)!""}
            </div>
            <div class="ibox">
                <div class="ibox-content clear">
                    <div class="col-sm-5">
                        <dl class="dl-horizontal">
                            <dt>
                                <@message>库编号</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.house_no)!""}
                            </dd>
                            <dt>
                                <@message>单据号</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.current_form_no)!""}
                            </dd>
                            <dt>
                                <@message>容器条码</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.pallet_no)!""}
                            </dd>
                            <dt>
                                <@message>当前位置</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.current_pos)!""}
                            </dd>
                            <dt>
                                <@message>入线时间</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.enter_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                            </dd>
                            <dt>
                                <@message>托盘状态</@message>：</dt>
                            <dd>
                                <#if (storageBatteryDetailDto.pallet_status)??>
                                    <#if storageBatteryDetailDto.pallet_status == "In_Waiting">
                                        待入
                                    <#elseif storageBatteryDetailDto.pallet_status == "In_Executing">
                                        入中
                                    <#elseif storageBatteryDetailDto.pallet_status == "In_Finished">
                                        已入
                                    <#elseif storageBatteryDetailDto.pallet_status == "Out_Waiting">
                                        待出
                                    <#elseif storageBatteryDetailDto.pallet_status == "Out_Executing">
                                        出中
                                    <#elseif storageBatteryDetailDto.pallet_status == "Out_Finished">
                                        已出
                                    <#else >
                                        异常
                                    </#if>
                                </#if>
                            </dd>
                        </dl>
                    </div>
                    <div class="col-sm-7" id="cluster_info">
                        <dl class="dl-horizontal">

                            <dt>
                                <@message>通道策略</@message>：
                            </dt>
                            <dd>
                                <#if (storageBatteryDetailDto.channel_policy)??>
                                    <#if storageBatteryDetailDto.channel_policy == "Disorder">
                                        无序
                                    <#elseif storageBatteryDetailDto.channel_policy == "N">
                                        N型排序，按列计数
                                    <#elseif storageBatteryDetailDto.channel_policy == "Z">
                                        Z型排序，按行计数
                                    <#else >
                                        U型排序，按列头尾衔接计数
                                    </#if>
                                </#if>
                            </dd>
                            <dt>
                                <@message>是否空托</@message>：
                            </dt>
                            <dd>
                                <#if (storageBatteryDetailDto.is_empty)?? && !(storageBatteryDetailDto.is_empty)>
                                    否
                                <#else >
                                    是
                                </#if>
                            </dd>
                            <dt>
                                <@message>调度状态</@message>：
                            </dt>
                            <dd>
                                <#if (storageBatteryDetailDto.dispatch_status)??>
                                    <#if storageBatteryDetailDto.dispatch_status=="Dispatching" >
                                        调度中
                                    <#else >
                                        调度完成
                                    </#if>
                                </#if>
                            </dd>
                            <dt>
                                <@message>组盘时间</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.palletize_complete_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                            </dd>
                            <dt>
                                <@message>当前工序开始时间</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.current_procedure_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                            </dd>
                            <dt>
                                <@message>拆盘时间</@message>：
                            </dt>
                            <dd>
                            ${(storageBatteryDetailDto.pallet_split_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                            </dd>
                               <dd>
                                                       <input type="hidden" id="Jss_pallet_no" value="${(storageBatteryDetailDto.pallet_no)!""}">
                               
                                <input type="button" id="chargeNumber" value="收数">
                                <input type="button" id="errorExport" value="异常排出">
                                <input type="button" id="downLocation" value="下架/下一步">

                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="panel-body locationDetail">
                <#assign index =1>
                <dl class="col-sm-6">
                    <#if (storageBatteryDetailDto.betteryList)?size gt 0>
                        <#list storageBatteryDetailDto.betteryList as bettery>
                            <#if bettery_index lt 12>
                                <dd>
                                    <label>${bettery_index + 1}:</label>
                                    <a href="../battery/detail_view?id=${bettery.id}" target="_blank">
                                        ${bettery.battery_barcode}
                                    </a>
                                </dd>
                            </#if>
                        </#list>
                    </#if>
                </dl>
                <dl class="col-sm-6">
                    <#if (storageBatteryDetailDto.betteryList)?size gt 0>
                        <#list storageBatteryDetailDto.betteryList as bettery>
                            <#if bettery_index gt 11>
                                <dd>
                                    <label>${bettery_index + 1}:</label>
                                    <a href="../battery/detail_view?id=${bettery.id}" target="_blank">
                                        ${bettery.battery_barcode}
                                    </a>
                                </dd>
                            </#if>
                        </#list>
                    </#if>
                   
                </dl>
            </div>
               
        </div>
    <#else>
        <div style="line-height: 40px;text-align: center;">
            <@message>此库位为空！</@message>
        </div>
    </#if>
    <input type="hidden" id="Jss_locationId" value="${(locationId)!""}">
    <input type="button" id="clearLocation" value="重置为空库位">


</div>

</#assign>

<#assign script>



<script>
$("#chargeNumber").click(function () {
    var con=confirm("确定要进行收数操作？");
    if(con==true) {
        var palletNo = $("#Jss_pallet_no").val();

        $.ajax({
            type: "POST",
            url: appPath + '/efcs_report/pallet/charge_number',
            data: {palletNo: palletNo},
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    alert("收数成功！");

                } else {
                    alert("操作失败！！" + result.message)
                }
            }
        });
    }
});

$("#downLocation").click(function () {
    var con=confirm("确定要强制下架并进行下一步操作？");
    if(con==true){
        var palletNo = $("#Jss_pallet_no").val();
        $.ajax({
            type: "POST",
            url: appPath + '/efcs_report/pallet/step_next',
            data: {palletNo: palletNo},
            dataType: "json",
            success: function (result) {
                if(result.success){
                    alert("操作成功！");
                }else{
                    alert("操作失败！！"+result.message)
                }
            }
        });

    }


});

$("#errorExport").click(function () {
    var con=confirm("确定要进行异常排出？");
    if(con==true){
        var palletNo = $("#Jss_pallet_no").val();
        $.ajax({
            type: "POST",
            url: appPath + '/efcs_report/pallet/error_export',
            data: {palletNo: palletNo},
            dataType: "json",
            success: function (result) {
                if(result.success){
                    alert("操作成功！");

                }else{
                    alert("操作失败！！"+result.message)
                }
            }
        });
    }
});


$("#clearLocation").click(function () {
    var con=confirm("确定要重置库位为空库位？");
    if(con==true){
        var lid = $("#Jss_locationId").val();
        $.ajax({
            type: "POST",
            url: appPath + '/efcs_report/pallet/clearLocationStatus',
            data: {locationId: lid},
            dataType: "json",
            success: function (result) {
                if(result.success){
                    alert("操作成功！");

                }else{
                    alert("操作失败！！"+result.message)
                }
            }
        });
    }
});


</script>


</#assign>

<#include "/end.ftl">