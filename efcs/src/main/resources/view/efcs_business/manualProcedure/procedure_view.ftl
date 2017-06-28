<#include '/begin.ftl'>
<#assign style>

</#assign>

<#assign content>
<form id="Js_form">
    <div class="form-group">
        <label class="col-sm-4"><@message>托盘号</@message>：</label>
        <input type="text" placeholder="请扫码" id="Js_palletNo" name="palletNo"
               class="form-control Js_procedure_palletNo">
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>工序</@message>：</label>
        <input type="text" name="work_procedure" class="form-control">
    </div>
    <div class="form-group">
        <label class="col-sm-4"><@message>库编号</@message>：</label>
        <input type="text" placeholder="扫码后，自动读取，不可以修改" name="wareHouse" onfocus="this.blur()" class="form-control">
    </div>
</form>
<a class="btn btn-primary" style="margin-top:10px;" id="Js_manual_btn" data-type="changeProcedure">补假电池</a>
</#assign>

<#assign script>
<script>
    seajs.use("manualProcedure/index.js")
</script>
</#assign>

<#include '/end.ftl'>