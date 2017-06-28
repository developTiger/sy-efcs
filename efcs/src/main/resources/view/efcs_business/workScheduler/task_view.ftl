<#include "/begin.ftl">

<#assign style></#assign>

<#assign content>
<div class="page-search">
    <form class="form-inline" id="Js_search_form">
        <div class="form-group">
            <label class="control-label">
                <@message>任务编号</@message>：
            </label>
            <input type="text" class="form-control" name="house_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>计划编号</@message>：
            </label>
            <input type="text" class="form-control" name="house_no">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>工装类型</@message>：
            </label>
            <select class="form-control" name="scheduler_type">
                <option value="">请选择</option>
                <option value="ExecutionOnce">单次执行</option>
                <option value="ExecutionLoop">循环执行</option>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>工装编号</@message>：
            </label>
            <select class="form-control" name="scheduler_type">
                <option value="">请选择</option>
                <option value="ExecutionOnce">单次执行</option>
                <option value="ExecutionLoop">循环执行</option>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>任务结果</@message>：
            </label>
            <select class="form-control" name="scheduler_type">
                <option value="">全部</option>
                <option value="ExecutionOnce">未完成</option>
                <option value="ExecutionLoop">已完成</option>
            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>创建时间</@message>：
            </label>
            <input type="text" class="form-control">
        </div>
        <div class="form-group">
            <label class="form-label">
                <@message>创建人</@message>
            </label>
            <select class="form-control">
                <option value="">全部</option>
                <option value="admin">admin</option>
            </select>
        </div>
        <input type="hidden" name="sc_id" value="${sc_id}">
        <div class="form-group">
            <input type="submit" class="btn btn-success" value="<@message>查询</@message>">
        </div>
    </form>
</div>
<div class="page-wrapper">
    <div class="page-buttons">
        <button type="button" class="btn btn-info Js_storehouse_add">
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
    seajs.use("workScheduler/task")
</script>
</#assign>

<#include "/end.ftl">