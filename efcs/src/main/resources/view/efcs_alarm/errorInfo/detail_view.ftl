<#include "/begin.ftl">

<#assign style>
<style>
    .error-detail .dl-horizontal dd, .error-detail .dl-horizontal dt {
        line-height: 24px;
    }

    .alarm-level {
        display: inline-block;
        vertical-align: middle;
        width: 28px;
        height: 18px;
        line-height: 18px;
        color: #fff;
        text-align: center;
    }

    .alarm-level.high {
        background-color: rgba(204, 0, 0, 1)
    }

    .alarm-level.normal {
        background-color: rgba(241, 82, 70, 1)
    }

    .alarm-level.low {
        background-color: green;
    }
</style>
</#assign>

<#assign content>
<div class="page-wrapper clearfix">
    <div class="page-content">
        <div class="">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5><@message>异常详情</@message></h5>
                </div>
                <div class="ibox-content clear error-detail">
                    <#if alarmInfo??>
                        <div class="row">
                            <div class="col-sm-5">
                                <dl class="dl-horizontal">

                                    <dt><@message>拉线号</@message>：</dt>
                                    <dd>${(alarmInfo.house_no)!""}</dd>
                                    <dt>异常类型：</dt>
                                    <dd>
                                        <#if alarmInfo.alarm_type == "FireAlarm">
                                            <@message>火灾报警</@message>
                                        <#else >
                                            <@message>异常报警</@message>
                                        </#if>
                                    </dd>
                                    <dt><@message>异常级别</@message>：</dt>
                                    <dd>
                                        <#if alarmInfo.alarm_level == "High">
                                            <span class="alarm-level high"><@message>高</@message></span>
                                        <#elseif alarmInfo.alarm_level == "Normal">
                                            <span class="alarm-level normal"><@message>中</@message></span>
                                        <#else >
                                            <span class="alarm-level low"><@message>低</@message></span>
                                        </#if>
                                    </dd>
                                    <dt><@message>设备号</@message>：</dt>
                                    <dd>
                                        ${(alarmInfo.location)!""}
                                    </dd>
                                </dl>
                            </div>
                            <div class="col-sm-7" id="cluster_info">
                                <dl class="dl-horizontal">
                                    <dt><@message>异常名称</@message>：</dt>
                                    <dd>${(alarmInfo.title)!""}</dd>
                                    <dt><@message>异常消息</@message>：</dt>
                                    <dd>${(alarmInfo.content)!""}</dd>
                                    <dt><@message>创建时间</@message>：</dt>
                                    <dd class="project-people">
                                        ${(alarmInfo.create_datetime?string('yyyy-MM-dd HH:mm:ss'))!""}
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </#if>
                    <div class="col-sm-12">
                        <div class="panel blank-panel">
                            <div class="panel-heading">
                                <ul class="nav nav-tabs">
                                    <li>
                                        <a><@message>操作记录</@message></a>
                                    </li>
                                </ul>
                            </div>
                            <div class="panel-body">
                                <div>
                                    <form class="form-horizontal" id="Js_handle_form" method="post"
                                          action="../errorInfo/handle">
                                        <div class="form-group">
                                            <input type="hidden" value="${(alarmInfo.id)!""}" name="alarm_id"
                                                   id="Js_error_id">
                                            <textarea class="form-control" name="handle_info"
                                                      placeholder="<@message>请输入操作记录，回车保存</@message>"></textarea>
                                        </div>
                                    </form>
                                </div>
                                <div>
                                    <div class="feed-activity-list" id="Js_error_handleInfos">
                                        <#if (alarmInfo.handleInfos)?? && (alarmInfo.handleInfos)?size gt 0 >
                                            <#list alarmInfo.handleInfos as handleInfo>
                                                <div class="feed-element">
                                                    <a>${(handleInfo.create_by)!""}</a>
                                                ${(handleInfo.handle_info)!""}
                                                    <br>
                                                    <small class="text-muted">${(handleInfo.handle_time)?string('yyyy-MM-dd HH:mm:ss')!""}</small>
                                                </div>
                                            </#list>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</#assign>

<#assign script>
<script>
    seajs.use("errorInfo/detail")
</script>
</#assign>

<#include "/end.ftl">