<#include "/begin.ftl">

<#assign style>
<style>
    .dl-horizontal dd, .dl-horizontal dt {
        line-height: 24px;
    }
</style>
</#assign>

<#assign content>
    <#if palletDispatchDto??>
        <div class="ibox">
            <div class="ibox-title">
                <@message>托盘详情</@message>
            </div>
            <div class="ibox-content clear">
                <div class="col-sm-5">
                    <dl class="dl-horizontal">
                        <dt>
                            <@message>库编号</@message>：
                        </dt>
                        <dd>
                            ${(palletDispatchDto.house_no)!""}
                        </dd>
                        <dt>
                            <@message>单据号</@message>：
                        </dt>
                        <dd>
                            ${(palletDispatchDto.current_form_no)!""}
                        </dd>
                        <dt>
                            <@message>容器条码</@message>：
                        </dt>
                        <dd>
                            ${(palletDispatchDto.container_no)!""}
                        </dd>
                        <dt>
                            <@message>当前位置</@message>：
                        </dt>
                        <dd>
                            ${(palletDispatchDto.current_pos)!""}
                        </dd>
                        <dt>
                            <@message>入线时间</@message>：
                        </dt>
                        <dd>
                            ${(palletDispatchDto.enter_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                        </dd>
                        <dt>
                            <@message>托盘状态</@message>：</dt>
                        <dd>
                            <#if (palletDispatchDto.pallet_status)??>
                                <#if palletDispatchDto.pallet_status == "In_Waiting">
                                    待入
                                <#elseif palletDispatchDto.pallet_status == "In_Executing">
                                    入中
                                <#elseif palletDispatchDto.pallet_status == "In_Finished">
                                    已入
                                <#elseif palletDispatchDto.pallet_status == "Out_Waiting">
                                    待出
                                <#elseif palletDispatchDto.pallet_status == "Out_Executing">
                                    出中
                                <#elseif palletDispatchDto.pallet_status == "Out_Finished">
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
                            <#if (palletDispatchDto.channel_policy)??>
                                <#if palletDispatchDto.channel_policy == "Disorder">
                                    无序
                                <#elseif palletDispatchDto.channel_policy == "N">
                                    N型排序，按列计数
                                <#elseif palletDispatchDto.channel_policy == "Z">
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
                            <#if (palletDispatchDto.is_empty)?? && !(palletDispatchDto.is_empty)>
                                否
                            <#else >
                                是
                            </#if>
                        </dd>
                        <dt>
                            <@message>调度状态</@message>：
                        </dt>
                        <dd>
                            <#if (palletDispatchDto.dispatch_status)??>
                                <#if palletDispatchDto.dispatch_status=="Dispatching" >
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
                            ${(palletDispatchDto.palletize_complete_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                        </dd>
                        <dt>
                            <@message>当前工序开始时间</@message>：
                        </dt>
                        <dd>
                            ${(palletDispatchDto.current_procedure_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                        </dd>
                        <dt>
                            <@message>拆盘时间</@message>：
                        </dt>
                        <dd>
                            ${(palletDispatchDto.pallet_split_time?string('yyyy-MM-dd HH:mm:ss'))!""}
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
        <input type="hidden" id="Js_pallet_id" value="${palletDispatchDto.id}">
    </#if>

    <div class="ibox">
        <div class="ibox-title">
            <@message>电池详情</@message>
        </div>
        <div class="ibox-content clear">
            <div id="Js_table_container_battery">
                <table id="Js_table_battery"></table>
                <div id="Js_pager_battery"></div>
            </div>
        </div>
        <input type="hidden" id="Js_battery_type" value="${type!""}">
    </div>

    <div class="ibox">
        <div class="ibox-title">
            <@message>托盘移动记录</@message>
        </div>
        <div class="ibox-content clear">
            <div id="Js_table_container">
                <table id="Js_table"></table>
                <div id="Js_pager"></div>
            </div>
        </div>
    </div>
</#assign>

<#assign script>
<script>
    seajs.use("pallet/detail")
</script>
</#assign>

<#include "/end.ftl">