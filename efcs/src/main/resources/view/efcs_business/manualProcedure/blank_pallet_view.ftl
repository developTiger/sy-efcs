<#include '/begin.ftl'>
<#assign style>

</#assign>

<#assign content>
<form id="Js_form">
    <div class="form-group">
        <label class="col-sm-4"><@message>托盘号</@message>：</label>
        <input type="text" placeholder="请扫码" id="Js_palletNo" name="palletNo"
               class="form-control Js_blank_palletNo">
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>库编号</@message>：</label>
        <input type="text" placeholder="扫码后，自动读取，不可以修改" name="wareHouse" onfocus="this.blur()" class="form-control">
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>当前位置</@message>：</label>
        <input type="text" id="Js_position_label" name="position_label" class="form-control" onfocus="this.blur()">
        <input type="hidden" id="Js_position_info" name="position" class="form-control">
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>工序</@message>：</label>
        <select name="work_procedure" class="form-control">
            <option value="Formation_PalletMove">化成</option>

            <option value="Test_PalletMove">测试</option>
        </select>
    </div>
</form>
<a class="btn btn-primary" style="margin-top:10px;" id="Js_manual_btn" data-type="changeProcedure">注册空拖</a>
</#assign>

<#assign script>
<script>
    seajs.use("manualProcedure/blank_pallet.js")
</script>
</#assign>

<#include '/end.ftl'>