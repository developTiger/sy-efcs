<#include '/begin.ftl'>

<#assign style>

</#assign>

<#assign content>
<form id="Js_form">
    <div class="form-group">
        <select class="form-control" name="work_procedure" id="Js_work_procedure">
            <option value="Formation_Split">
                <@message>化成拆盘</@message>
            </option>
            <option value="Test_Pallet_Split">
                <@message>测试拆盘</@message>
            </option>
        </select>
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>托盘号</@message>：</label>
        <input type="text" placeholder="请扫码" id="Js_palletNo" name="palletNo" class="form-control Js_split_palletNo">
    </div>

    <div class="form-group">
        <label class="col-sm-4"><@message>库位号</@message>：</label>
        <input type="text" placeholder="扫码后，自动读取，不可以修改" name="wareHouse" onfocus="this.blur()" class="form-control">
    </div>
</form>
<a class="btn btn-primary" id="Js_manual_btn" data-type="split" style="margin-top:10px;">拆盘</a>
</#assign>

<#assign script>
<script>
    seajs.use("manualProcedure/index.js")
</script>
</#assign>

<#include '/end.ftl'>