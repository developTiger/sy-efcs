<#include '/begin.ftl'>
<#assign layout="single">

<#assign style>
<style>
    .control-label {
        text-align: right;
    }
</style>
</#assign>

<#assign content>
    <#assign isEdit = schedulerDto??>
    <form id="Js_workScheduler_form" class="form-horizontal" action="../workScheduler/create">
        <div class="form-group">

            <label class="col-xs-3 control-label">
                <@message>计划任务名称</@message>
            </label>
            <div class="col-xs-8">
                <input type="text" class="form-control" name="sc_name"
                <#if isEdit> value="${(schedulerDto.sc_name)!''}" </#if>
                >
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>库号</@message>
            </label>
            <div class="col-xs-8">
                <select class="form-control" name="house_no">
                    <#list wareHouseDtos as wareHouse>
                        <option value="${wareHouse.house_no}"
                            <#if isEdit && schedulerDto.house_no==wareHouse.house_no> selected </#if>
                        >${wareHouse.house_no}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>工装类型</@message>
            </label>
            <div class="col-xs-8">
                <select class="form-control" name="tool_type">
                    <#list containerTypeDtos as type>
                        <option value="${type.container_name}"
                            <#if isEdit && schedulerDto.tool_type==type.container_name> selected </#if>
                        >${type.container_name}
                        </option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>托盘编号</@message>
            </label>
            <div class="col-xs-4">
                <select class="form-control" name="tool_no" >

                </select>
            </div>
        </div>
        <div class="form-group" data-target="校准工装">
            <label class="col-xs-3 control-label">
                <@message>计划类型</@message>
            </label>
            <div class="col-xs-7">
                <select class="form-control" name="scheduler_type">
                    <option value="ExecutionOnce"
                        <#if isEdit && schedulerDto.scheduler_type=='ExecutionOnce'> selected</#if>
                    >
                        单次执行
                    </option>
                    <option value="ExecutionLoop"
                        <#if isEdit && schedulerDto.scheduler_type=='ExecutionLoop'> selected</#if>
                    >
                        循环执行
                    </option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>开始时间</@message>
            </label>
            <div class="col-xs-8">
                <input type="text" class="form-control" name="scheduler_start_time" id="Js_start_time"
                    <#if isEdit> value="${(schedulerDto.scheduler_start_time)?string("yyyy-MM-dd HH:mm:ss")}" </#if>
                >

            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>目标地址</@message>
            </label>
            <div class="col-xs-6">
                <input type="text" class="form-control" id="locationPreview" name="locationPreview" onfocus="this.blur()"
                    <#if isEdit> value="${(schedulerDto.total_loc_count)!''}" </#if>
                >
                <input type="hidden" name="ids" id="locationIds"
                <#if isEdit> value="${location_ids!""}" </#if>
                >
            </div>
            <div class="col-xs-2">
                <a class="btn btn-primary" id="Js_location_view">选择地址</a>
            </div>
        </div>
        <div class="form-group" data-target="ExecutionLoop">
            <label class="col-xs-3 control-label">
                <@message>执行间隔</@message>
            </label>
            <div class="col-xs-7">
                <input type="text" class="form-control" name="time_interval" min="0">
                <input class="form-control" name="time_interval_unit" type="hidden" value="DAY">
            </div>
            <div class="col-xs-1">
                天
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>有无结束时间</@message>
            </label>
            <div class="col-xs-8">
                <input type="checkbox" id="Js_change_endTime_type"
                    <#if isEdit && schedulerDto.scheduler_end_time??> checked</#if>
                >
            </div>
        </div>
        <div class="form-group" data-target=Js_change_endTime_type>
            <label class="col-xs-3 control-label">
                <@message>结束时间</@message>
            </label>
            <div class="col-xs-8">
                <input type="text" class="form-control" name="scheduler_end_time" id="Js_end_time"
                    <#if isEdit> value="${(schedulerDto.scheduler_end_time)?string("yyyy-MM-dd HH:mm:ss")}" </#if>
                >
            </div>
        </div>
        <#if isEdit>
        <input type="hidden" name="id" value="${schedulerDto.id}">
        <input type="hidden" name="sc_no" value="${schedulerDto.sc_no}">
        </#if>
    </form>
</#assign>

<#assign script>
<script>
    seajs.use("workScheduler/form")
</script>
</#assign>

<#include '/end.ftl'>