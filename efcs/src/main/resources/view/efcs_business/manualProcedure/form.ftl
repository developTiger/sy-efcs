<#include '/begin.ftl'>
<#assign layout="single">

<#assign style>
<style>
    .pallet-info dt {
        width: 100px
    }

    .pallet-info dd {
        margin-left: 110px;
    }

    .pallet-info dt, .pallet-info dd {
        line-height: 42px;
    }

    .pallet-info label{text-align: right;}
    .battery-status {
        width: 24px;
        height: 14px;
        display: inline-block;
    }

    .battery-status.OK, .battery-list input.OK {
        background-color: #2bd256;
    }

    .battery-status.rework, .battery-list input.rework {
        background-color: #47c8fe;
    }

    .battery-status.NG, .battery-list input.NG {
        background-color: #ff6633;
    }

    .battery-status.fake, .battery-list input.fake {
        background-color: #868282;
    }
    .battery-status.NC, .battery-list input.NC {
        background-color: #735d5d;
    }
    .battery-status-container label {
        color: #696969;
        margin: 10px 30px 10px 6px;
    }

    .battery-list input.OK, .battery-list input.NG,
        .battery-list input.rework, .battery-list input.NC,
        .battery-list input.fake {
        color: #fff;
    }

    .battery-list .form-body {
        padding-right: 20px;
        position: relative;
    }

    .battery-list .btn-remove {
        position: absolute;
        top: 7px;
        right: 1px;
    }
    .dn{display:none;}
</style>
</#assign>

<#assign content>
<div class="col-md-12">
    <#if palletDispatchDto??>
        <form id="Js_group_form">
            <div class="ibox ">
                <div class="ibox-content clear">
                    <!--托盘信息-->
                    <div>
                        <strong><@message>托盘信息</@message></strong>
                    </div>

                    <div class="col-sm-12 pallet-info">
                        <#if type == 'work_procedure'>
                            <div class="col-sm-6 form-group">
                                <label class="col-sm-4"><@message>库编号</@message>：</label>
                                <div class="col-sm-8">
                                    <select name="wareHouse" id="Js_house_no" class="form-control">
                                        <#list houseDto as item>
                                            <option value="${item.id}"
                                                <#if (palletDispatchDto.house_no)?? && (palletDispatchDto.house_no) == item.house_no> selected</#if>
                                                    data-no = ${item.house_no}>${item.house_name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="col-sm-6 form-group">

                                <label class="col-sm-4">
                                    <@message>托盘条码</@message>：
                                </label>
                                <div class="col-sm-8">
                                <input type="text" class="form-control" name="container_no" id="Js_pallet_no"
                                     disabled  value="${(palletNo)!''}">
                                </div>
                            </div>
                            <div class="col-sm-6 form-group">
                                <label class="col-sm-4">
                                    <@message>工序</@message>：
                                </label>
                                <div class="col-sm-8">
                                    <select class="form-control" name="work_procedure" id="Js_work_procedure">
                                        <#include "/efcs_business/common/work_procedure.ftl">
                                    </select>
                                </div>
                            </div>
                        <#else >
                            <div class="col-sm-6 form-group">
                                <label class="col-sm-4"><@message>库编号</@message>：</label>
                                <div class="col-sm-8">
                                     ${(house_no)!""}
                                </div>
                            </div>
                            <div class="col-sm-6 form-group">
                                <label class="col-sm-4">
                                    <@message>托盘条码</@message>：
                                </label>
                                <div class="col-sm-8">
                                    ${(palletNo)!""}
                                </div>
                            </div>
                            <div class="col-sm-6 form-group">
                                <label class="col-sm-4">
                                    <@message>工序</@message>：
                                </label>
                                <div class="col-sm-8">
                                    <#if work_procedure??>
                                            <#if work_procedure == 'Formation_In'>
                                                化成段电池入库
                                            <#elseif work_procedure == 'Formation_Palletize'>
                                                化成组盘
                                            <#elseif work_procedure == 'High_Temperature'>
                                                高温静置
                                            <#elseif work_procedure == 'Formation'>
                                                化成
                                            <#elseif work_procedure == 'Formation_Split'>
                                                化成拆盘
                                            <#elseif work_procedure == 'Formation_Rework_Palletize'>
                                                化成REWORK组盘
                                            <#elseif work_procedure == 'Formation_Rework'>
                                                化成REWORK
                                            <#elseif work_procedure == 'Formation_Out'>
                                                化成电池出库
                                            <#elseif work_procedure == 'Test_In'>
                                                测试段电池入库
                                            <#elseif work_procedure == 'Test_Palletize'>
                                                测试组盘
                                            <#elseif work_procedure == 'Normal_Temperature_1'>
                                                常温静置1
                                            <#elseif work_procedure == 'Test_OCV_1'>
                                                测试OCV1
                                            <#elseif work_procedure == 'Normal_Temperature_2'>
                                                常温静置2
                                            <#elseif work_procedure == 'Test_OCV_2'>
                                                测试OCV2
                                            <#elseif work_procedure == 'Test_Out'>
                                                测试段电池出库
                                            <#elseif work_procedure == 'Test_Pallet_Split'>
                                                测试拆盘
                                            <#elseif work_procedure == 'Palletize_Cache'>
                                                移动到组盘缓存
                                            <#elseif work_procedure == 'FORMATION_ERROR_EXPORT'>
                                                化成段异常排除
                                            <#elseif work_procedure == 'TEST_ERROR_EXPORT'>
                                                测试段异常排除
                                            <#elseif work_procedure == 'Formation_PalletMove'>
                                                无业务工序托盘流转移动
                                            <#elseif work_procedure == 'Manual'>
                                                人工操作托盘
                                            </#if>
                                        </#if>
                                </div>
                            </div>
                            <input type="hidden" id="Js_house_no" value="${house_no!""}">
                            <input type="hidden" id="Js_pallet_no" value="${palletNo!""}">
                            <input type="hidden" id="Js_work_procedure" value="${work_procedure!""}">
                            <input type="hidden" id="Js_house_id" value="${house_id!""}">
                        </#if>
                    </div>
                    <input type="hidden" id="Js_handle_type" value="${type!""}">
                    <!--托盘信息-->

                    <!--电池信息-->
                    <#assign batteryCount = (palletDispatchDto.sku_max_count)!24>
                    <div class="col-sm-12 battery-container">
                        <div>
                            <strong>
                                <@message>电池信息</@message>
                            </strong>
                        </div>
                        <div class="battery-status-container">
                            <a>
                                <span class="battery-status OK"></span>
                                <label>优品</label>
                            </a>
                            <a>
                                <span class="battery-status rework"></span>
                                <label>复测</label>
                            </a>
                            <a>
                                <span class="battery-status NG"></span>
                                <label>NG</label>
                            </a>
                            <a>
                                <span class="battery-status NC"></span>
                                <label>NC</label>
                            </a>
                            <a>
                                <span class="battery-status fake"></span>
                                <label>fake</label>
                            </a>
                        </div>
                        <#if type == 'split' || type == 'work_procedure'>
                            <div class="row battery-list">
                                <#if batteryCount gt 1>
                                    <div class="col-sm-6">
                                        <dl>
                                            <#list 1..batteryCount/2 as index>
                                                <dd class="form-group clear">
                                                    <label class="form-label col-sm-1">${index}</label>
                                                    <div class="col-sm-8 form-body">
                                                        <input type="text" class="form-control ${(palletDispatchDto.palletDetailMap[index?string("0")].battery_status)!""}"
                                                               data-channel="${index}" disabled value="${(palletDispatchDto.palletDetailMap[index?string("0")].battery_barcode)!""}">

                                                    </div>
                                                </dd>
                                            </#list>
                                        </dl>
                                    </div>
                                </#if>
                                <#if batteryCount gt 0>
                                    <div class="col-sm-6">
                                            <dl>
                                                <#list (batteryCount/2+1)..batteryCount as index>
                                                    <dd class="form-group clear">
                                                        <label class="form-label col-sm-1">${index}</label>
                                                        <div class="col-sm-8 form-body">
                                                            <input type="text" class="form-control ${(palletDispatchDto.palletDetailMap[index?string("0")].battery_status)!""}"
                                                                   data-channel="${index}" disabled value="${(palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)!""}"
                                                            >
                                                        </div>
                                                    </dd>
                                                </#list>
                                            </dl>
                                        </div>
                                </#if>
                            </div>
                        <#elseif type=='group'>
                            <div class="row battery-list">
                                <#if batteryCount gt 1>
                                    <div class="col-sm-6">
                                        <dl>
                                            <#list 1..batteryCount/2 as index>
                                                <dd class="form-group clear">
                                                    <label class="form-label col-sm-1">${index}</label>
                                                    <div class="col-sm-8 form-body">
                                                        <input type="text" class="form-control ${(palletDispatchDto.palletDetailMap[index?string("0")].battery_status)!""}"
                                                               name="battery_barcode"
                                                                <#if (palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                                   disabled
                                                                </#if>
                                                               data-channel="${index}" value="${(palletDispatchDto.palletDetailMap[index?string("0")].battery_barcode)!""}">
                                                        <i class="glyphicon glyphicon-remove btn-remove Js_battery_clear
                                                            <#if !(palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                                dn
                                                            </#if>
                                                            "></i>
                                                    </div>
                                                </dd>
                                            </#list>
                                        </dl>
                                    </div>
                                </#if>
                                <#if batteryCount gt 0>
                                    <div class="col-sm-6">
                                                <dl>
                                                    <#list (batteryCount/2+1)..batteryCount as index>
                                                        <dd class="form-group clear">
                                                            <label class="form-label col-sm-1">${index}</label>
                                                            <div class="col-sm-8 form-body">
                                                                <input type="text" class="form-control ${(palletDispatchDto.palletDetailMap[index?string("0")].battery_status)!""}"
                                                                       name="battery_barcode"
                                                                        <#if (palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                                           disabled
                                                                        </#if>
                                                                       data-channel="${index}" value="${(palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)!""}"
                                                                >
                                                                <i class="glyphicon glyphicon-remove btn-remove Js_battery_clear
                                                                     <#if !(palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                                        dn
                                                                    </#if>
                                                                "></i>
                                                            </div>
                                                        </dd>
                                                    </#list>
                                                </dl>
                                            </div>
                                </#if>
                            </div>
                        <#elseif type=='fake'>
                            <div class="row battery-list">
                                <#if batteryCount gt 1>
                                    <div class="col-sm-6">
                                        <dl>
                                            <#list 1..batteryCount/2 as index>
                                                <dd class="form-group clear">
                                                    <label class="form-label col-sm-1">${index}</label>
                                                    <div class="col-sm-8 form-body">
                                                        <input type="text" class="form-control ${(palletDispatchDto.palletDetailMap[index?string("0")].battery_status)!""}"
                                                            <#if (palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                               disabled
                                                            <#else >
                                                               name="battery_barcode"
                                                            </#if>
                                                               data-channel="${index}" value="${(palletDispatchDto.palletDetailMap[index?string("0")].battery_barcode)!""}">
                                                        <#if !(palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                            <i class="glyphicon glyphicon-remove btn-remove Js_battery_clear dn"></i>
                                                        </#if>
                                                    </div>
                                                </dd>
                                            </#list>
                                        </dl>
                                    </div>
                                </#if>
                                <#if batteryCount gt 0>
                                    <div class="col-sm-6">
                                    <dl>
                                        <#list (batteryCount/2+1)..batteryCount as index>
                                            <dd class="form-group clear">
                                                <label class="form-label col-sm-1">${index}</label>
                                                <div class="col-sm-8 form-body">
                                                    <input type="text" class="form-control ${(palletDispatchDto.palletDetailMap[index?string("0")].battery_status)!""}"
                                                           <#if (palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                                disabled
                                                           <#else >
                                                                name="battery_barcode"
                                                           </#if>
                                                           data-channel="${index}" value="${(palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)!""}"
                                                    >
                                                    <#if !(palletDispatchDto.palletDetailMap[(index)?string("0")].battery_barcode)??>
                                                        <i class="glyphicon glyphicon-remove btn-remove Js_battery_clear dn"></i>
                                                    </#if>
                                                </div>
                                            </dd>
                                        </#list>
                                    </dl>
                                </div>
                                </#if>
                            </div>
                        </#if>
                    </div>
                    <!--电池信息-->
                </div>
            </div>
        </form>
    <#else>
        <div>未找到该托盘！</div>
    </#if>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("manualProcedure/form")
</script>
</#assign>

<#include '/end.ftl'>