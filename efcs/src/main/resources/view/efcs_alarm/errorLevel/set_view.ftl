<#include "/begin.ftl">

<#assign layout="single">

<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
</#assign>

<#assign content>
<div class="model-body">
    <form class="form-horizontal" method="post" action="../equipment/addorupdate" id="Js_equipment_form">
        <input type="hidden" name="parent_id" value="">
        <div class="form-group">
            <div class="col-xs-7">
                <label class="col-xs-5 control-label"><@message>预警级别</@message></label>
                <div class="col-xs-7">
                    <input type="text" class="form-control" readonly name="xpos_no" value="严重">
                </div>
            </div>
            <div class="col-xs-4">
                <label class="col-xs-8 control-label"><@message>主题颜色设置</@message></label>
                <div class="col-xs-3">
                    <div style="background-color:orange;width:25px;height:16px;"></div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label"><@message>预警级别描述</@message></label>
            <div class="col-xs-7">
                <input type="text" class="form-control" name="xpos_name" value="">
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label"><@message>页面告警方式</@message></label>
            <div class="col-xs-2">
                <label>
                    <input type="radio"  name="xpos_name" value="">
                    不提醒
                </label>
            </div>
            <div class="col-xs-2">
                <label>
                    <input type="radio"  name="xpos_name" value="">
                    弹窗提醒
                </label>
            </div>
            <div class="col-xs-3">
                <label>
                    <input type="radio"  name="xpos_name" value="">
                    全屏弹窗提醒
                </label>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label"><@message>弹窗持续时间</@message></label>
            <div class="col-xs-5">
                <input type="text" class="form-control" name="max_zpos" value="">
            </div>
            <span>（例：1h 5min 20s）</span>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label"><@message>是否强制消警</@message></label>
            <div class="col-xs-2">
                <label>
                    <input type="radio"  name="xpos_name" value="">
                    是
                </label>
            </div>
            <div class="col-xs-2">
                <label>
                    <input type="radio"  name="xpos_name" value="">
                    否
                </label>
            </div>
        </div>
    </form>
</div>

</#assign>

<#assign script>
<script>
    seajs.use("errorLevel/set")
</script>
</#assign>

<#include "/end.ftl">



