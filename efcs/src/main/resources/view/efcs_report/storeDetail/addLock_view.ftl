<#include "/begin.ftl">

<#assign layout="single">

<#assign content>

<div class="Js_location_container">
    <form id="Js_lock_form">
        <div class="form-group">
            <label class="col-sm-4">
                <@message>操作类型</@message>：
            </label>
            <div class="col-sm-8">
                <select class="form-control" name="isLock">
                    <#if in_lock == 0 || out_lock == 0>
                        <option value="lock">加锁</option>
                    </#if>
                    <#if in_lock == 1 || out_lock == 1>
                        <option value="unlock">解锁</option>
                    </#if>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4">
                <@message>加／解锁类型</@message>：
            </label>
            <div class="col-sm-8">
                <span style="display: none">
                    <label>入</label>
                    <input type="checkbox" name="lockType" value="in" data-flag="${in_lock}">
                </span>
                <span style="display: none">
                    <label>出</label>
                    <input type="checkbox" name="lockType" value="out" data-flag="${out_lock}">
                </span>
            </div>
        </div>
        <div class="form-group" for="lock">
            <label class="col-sm-4">
                <@message>加锁说明</@message>：
            </label>
            <div class="col-sm-8">
                <textarea class="form-control" name="lockDesc"></textarea>
            </div>
        </div>
        <input type="hidden" name="id" value="${id}">
    </form>
</div>
</#assign>

<#assign script>
<script>
   seajs.use("storeDetail/addlock")
</script>
</#assign>

<#include "/end.ftl">