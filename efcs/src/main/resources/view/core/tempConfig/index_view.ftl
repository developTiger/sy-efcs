<#include "/begin.ftl">

<#assign style></#assign>

<#assign content>

    <form class="form-inline" action="changeHouseNo">
        <div class="form-group">
            <label class="control-label">
                <@message>库编号</@message>：
            </label>
            <input type="text" class="form-control" name="house_no" value="${house_no!''}">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>变更</@message>">
        </div>
     </form>
     
    <form class="form-inline" action="changeBattery">
        <div class="form-group">
            <label class="control-label">
                <@message>电芯前缀</@message>：
            </label>
            <input type="text" class="form-control" name="house_battery" value="${house_battery!''}">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>变更</@message>">
        </div>
     </form>
     
    <form class="form-inline" action="changeTray">
        <div class="form-group">
            <label class="control-label">
                <@message>托盘前缀</@message>：
            </label>
            <input type="text" class="form-control" name="house_tray" value="${house_tray!''}">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>变更</@message>">
        </div>
     </form>
     
    <form class="form-inline" action="changeTrayRule">
        <div class="form-group">
            <label class="control-label">
                <@message>托盘化成规则</@message>：
            </label>
            <input type="text" class="form-control" name="house_tray_rule" value="${house_tray_rule!''}">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>变更</@message>">
        </div>
     </form>
    <form class="form-inline" action="changeHigh">
        <div class="form-group">
            <label class="control-label">
                <@message>高温前缀</@message>：
            </label>
            <input type="text" class="form-control" name="house_high_temperature" value="${house_high_temperature!''}">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>变更</@message>">
        </div>
     </form>
    <form class="form-inline" action="changeNormal">
        <div class="form-group">
            <label class="control-label">
                <@message>常温前缀</@message>：
            </label>
            <input type="text" class="form-control" name="house_normal_temperature" value="${house_normal_temperature!''}">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>变更</@message>">
        </div>
     </form>
    <form class="form-inline" action="changeNormalNext">
        <div class="form-group">
            <label class="control-label">
                <@message>常温2前缀</@message>：
            </label>
            <input type="text" class="form-control" name="house_normal_next_temperature" value="${house_normal_next_temperature!''}">
        </div>
        <div class="form-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-success" value="<@message>变更</@message>">
        </div>
     </form>
     
</#assign>

<#assign script>
</#assign>

<#include "/end.ftl">