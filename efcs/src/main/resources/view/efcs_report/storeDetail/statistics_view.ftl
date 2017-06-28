<#include "/begin.ftl">

<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
</#assign>

<#assign content>
<div class="ibox">
    <div class="ibox-title">
        N2T1常温库 2017-02-23 15：23：20
    </div>
    <div class="ibox-content">
        <div id="Js_table_container">
            <input type="hidden" id="Js_zone_id" value="${id}">
            <table id="Js_table"></table>
            <div id="Js_pager"></div>
        </div>
    </div>
</div>

<div class="ibox">
    <div class="ibox-title">
        <@message>明细列表</@message>
    </div>
    <div class="ibox-content">
        <div class="page-search">
            <form class="form-inline" id="Js_search_form" method="post">
                <div class="form-group">
                    <label class="control-label"><@message>列</@message>：</label>
                    <input type="text" class="form-control" name="yPos">
                </div>
                <div class="form-group">
                    <label class="control-label"><@message>层</@message>：</label>
                    <input type="text" class="form-control" name="zPos">
                </div>
                <div class="form-group">
                    <label class="control-label"><@message>托盘状态</@message>：</label>
                    <select class="form-control" name="palletStatus">
                        <option value="">请选择</option>
                        <option value="In_Waiting">待入</option>
                        <option value="In_Executing">入中</option>
                        <option value="In_Finished">已入</option>
                        <option value="Out_Waiting">待出</option>
                        <option value="Out_Executing">出中</option>
                        <option value="Out_Finished">已出</option>
                    </select>
                </div>
                <div class="form-group">
                    <input type="submit" class="btn btn-success" value="<@message>查询</@message>">
                </div>
            </form>
        </div>

        <div id="Js_table_container_detail">
            <table id="Js_table_detail"></table>
            <div id="Js_pager_detail"></div>
        </div>
    </div>
</div>

</#assign>

<#assign script>
<script>
    seajs.use("storeDetail/statistics_view")
</script>
</#assign>

<#include "/end.ftl">