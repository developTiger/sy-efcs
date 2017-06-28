<#include '/begin.ftl'>

<#assign layout="single">
<#assign style>

</#assign>

<#assign content>
<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5>实时数据</h5>
            </div>
            <div class="ibox-content">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">库号</label>
                        <div class="col-sm-4">
                            <select class="form-control" id="houseNo">
                                <#if wareHouseDtos?? && wareHouseDtos?size gt 0 >
                                    <#list wareHouseDtos as wareHouseDto>
                                        <option value="${wareHouseDto.house_no}">${wareHouseDto.house_name}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">按钮</label>
                        <div class="col-sm-6">
                            <a href="javascript:;" id="DtsTemperatureInfo"
                               class="btn btn-outline btn-default">DTS-温度获取</a>
                            <a href="javascript:;" id="DtsChannelStatusInfo" class="btn btn-outline btn-default">获取DTS设备状态</a>
                            <a href="javascript:;" id="FormationInfo" class="btn btn-outline btn-default">化成柜-状态获取</a>
                            <a href="javascript:;" id="HeartbeatInfo" class="btn btn-outline btn-default">心跳实时信息获取</a>
                        </div>
                    </div>
                </form>
                <pre style="height:300px;overflow:auto;" id="demo1"></pre>

            </div>
        </div>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("realtime/index")
</script>
</#assign>

<#include '/end.ftl'>