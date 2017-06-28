<#include "/begin.ftl">

<#assign style></#assign>

<#assign content>
<div class="page-search">
    <form class="form-inline" id="Js_search_form">
        <div class="form-group">
            <label class="control-label">
                <@message>库编号</@message>：
            </label>
            <input type="text" class="form-control" name="house_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>计划类型</@message>：
            </label>
            <select class="form-control" name="scheduler_type">
                <option value="">请选择</option>
                <option value="ExecutionOnce">单次执行</option>
                <option value="ExecutionLoop">循环执行</option>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>设备号</@message>：
            </label>
            <input type="text" class="form-control" name="device_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>任务类型</@message>：
            </label>
            <select class="form-control" name="task_type">
                <#include "/efcs_business/common/taskType.ftl">
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>计划状态</@message>：
            </label>
            <select class="form-control" name="scheduler_status">
                <option value="">请选择</option>
                <option value="Running">执行中</option>
                <option value="Finish">完成</option>
            </select>
        </div>

        <div class="form-group">
            <label class="control-label">

            </label>
            <input type="submit" class="btn btn-success" value="<@message>查询</@message>">
        </div>
    </form>
</div>
<div class="page-wrapper">
    <div class="page-content">
        <table id="Js_table"></table>
        <div id="Js_pager"></div>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("scheduler/index")
</script>
</#assign>

<#include "/end.ftl">