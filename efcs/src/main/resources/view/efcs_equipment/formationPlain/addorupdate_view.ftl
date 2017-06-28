<#include "/begin.ftl">

<#assign layout="single">

<#assign style>
    <style>
        .form-separate{
            border-bottom: 2px dashed #dae1ed;
            margin:5px 30px;
        }
        .form-group-label{
            padding-left:50px;font-weight: bold;margin-bottom: ;
        }
    </style>
</#assign>

<#assign content>
<div class="model-body">
    <form class="form-horizontal" method="post" action="../formationPlain/addorupdate" id="Js_formationPlain_form">
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>拉线</@message>
            </label>
            <div class="col-xs-7">
                <select class="form-control">

                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>任务类型</@message>
            </label>
            <div class="col-xs-7">
                <select class="form-control">

                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>计划类型</@message>
            </label>
            <div class="col-xs-5">
                <select class="form-control">

                </select>
            </div>
            <div class="col-xs-2">
                <label>
                    <input type="checkbox" >启用
                </label>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label"><@message>开始时间</@message></label>
            <div class="col-xs-7">
                <input type="text" class="form-control">
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label"><@message>目标地址</@message></label>
            <div class="col-xs-7">
                <select class="form-control" multiple>

                </select>
            </div>
        </div>
        <div class="form-separate"></div>
        <div class="form-group-label">
            <@message>频率</@message>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label"><@message>执行</@message></label>
            <div class="col-xs-7">
                <select class="form-control">
                    <option value="">每周</option>
                    <option value="">每月</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>执行间隔</@message>
            </label>
            <div class="col-xs-7">
                <input type="text" class="form-control" >
            </div>
        </div>
        <div  class="form-group-label">持续时间</div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>执行间隔</@message>
            </label>
            <div class="col-xs-7">
                <label>
                    <input type="radio"> 有结束时间
                </label>
                <label style="margin-left:5px;">
                    <input type="radio"> 无结束时间
                </label>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 control-label">
                <@message>执行间隔</@message>
            </label>
            <div class="col-xs-7">
                <input type="text" class="form-control">
            </div>
        </div>
    </form>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("formationPlain/form")
</script>
</#assign>

<#include "/end.ftl">