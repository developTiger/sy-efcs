<#include "/begin.ftl">

<#assign style></#assign>

<#assign content>
<div class="page-search">
    <form class="form-inline" id="Js_search_form">
        <div class="form-group">
            <label class="control-label">
                <@message>库号</@message>：
            </label>
            <select name="house_no" class="form-control">
                <option value="">请选择</option>
                <#list wareHouseDtos as item>
                    <option value="${item.house_no}">${item.house_no}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>托盘编号</@message>：
            </label>
            <input type="text" class="form-control" name="tool_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>任务类型</@message>：
            </label>
            <select class="form-control" name="scheduler_type">
                <option value="">请选择</option>
                <option value="ExecutionOnce">单次执行</option>
                <option value="ExecutionLoop">循环执行</option>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>创建时间</@message>：
            </label>
            <input type="text" class="form-control" id="Js_create_time">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>最后时间</@message>：
            </label>
            <input type="text" class="form-control" id="Js_last_time">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>计划状态</@message>：
            </label>
            <select class="form-control" name="scheduler_status">
                <option value="">
                    请选择
                </option>
                <option value="Running">
                    执行中
                </option>
                <option value="Stop">
                    中止
                </option>
                <option value="Finish">
                    结束
                </option>
                <option value="OnScheduler">
                    计划中
                </option>
                <option value="ErrorFinished">
                    异常结束
                </option>
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
    <div class="page-buttons">
        <button type="button" class="btn btn-info Js_scheduler_add">
            创建周期计划
        </button>
    </div>
    <div class="page-content">
        <table id="Js_table"></table>
        <div id="Js_pager"></div>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("workScheduler/index")
</script>
</#assign>

<#include "/end.ftl">