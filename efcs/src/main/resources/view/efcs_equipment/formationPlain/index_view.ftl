
<#include "/begin.ftl">

<#assign style>
</#assign>

<#assign content>

<div class="page-search">
    <form class="form-inline" id="Js_search_form" method="post" action="../core/equipment/grid" >
        <div class="form-group">
            <label class="control-label">
                <@message>拉线</@message>：
            </label>
            <input type="text" class="form-control" name="equip_name">
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>任务类型</@message>：
            </label>
            <select class="form-control" name="equip_type">

            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>计划类型</@message>：
            </label>
            <select class="form-control" name="equip_type">

            </select>
        </div>
        <div class="form-group">
            <label class="control-label">
                <@message>状态</@message>：
            </label>
            <select class="form-control" name="house_no">
            </select>
        </div>
        <div class="form-group">
            <input type="submit" class="btn btn-success" value="<@message>查询</@message>">
        </div>
    </form>
</div>
<#--公共操作区-->
<div class="page-wrapper">
    <div class="page-buttons">
        <button type="button" class="btn btn-info Js_plain_add">
            创建周期计划
        </button>
    </div>
    <div class="page-content" id="Js_table_container">
        <table id="Js_table"></table>
        <div id="Js_pager"></div>
    </div>
</div>

</#assign>
<#assign script>
<script>
    seajs.use("formationPlain/index")
</script>

</#assign>

<#include "/end.ftl">