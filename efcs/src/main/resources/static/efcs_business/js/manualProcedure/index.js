/**
 * Created by jade on 2017/4/24.
 */
define(function (require, exports, module) {
    var Messenger = require("messenger"),
        layer = require("layer"),
        URI = require("uri");

    require("formvalidation");
    require("formvalidation.bootstrap");

    var $palletNo = $("#Js_palletNo"),
        $workProcedure = $("#Js_work_procedure"),
        $wareHouse = $('[name = wareHouse]'),
        index,
        url = appPath + "/efcs_business/manualProcedure/form",
        $form = $("#Js_form");

    $form.keydown(function(e){
       if(e.keyCode == 13){
           return false
       }
    });

    //消息
    var messenger = new Messenger("manualProcedure");
    messenger.listen(function (msg) {
        layer.close(index);
    });

    //dialog
    var $manualBtn = $("#Js_manual_btn"),
        type = $manualBtn.data("type");

    //提交
    var isDisabled = false;
    $manualBtn.click(function () {

        if(isDisabled ){
            return false;
        }
        validate();
        isDisabled = true;
    });

    //托盘号回车
    var loading = false;
    $(".Js_split_palletNo,.Js_procedure_palletNo").keyup(function (e) {
        var kc = e.keyCode;
        console.log(kc);

        var $this = $(this),
            val = $this.val(),
            isSplit = $this.hasClass("Js_split_palletNo");
        loading = true;
        if (kc == 13 && val.trim()) {
            getPalletInfo($(this).val().trim()).done(function (data) {
                if (!data.house_no) {
                    layer.alert(isSplit?'不能拆盘':"不能更改工序")
                } else {
                    $wareHouse.val(data.house_no).trigger("input");
                    isDisabled = false;
                }
            }).always(function(){
                loading = false;
            });
        }
        return false;
    }).on("input",function(){
        $wareHouse.val("").trigger("input");
    });

    //获取托盘信息
    function getPalletInfo(palletNo) {
        return $.ajax({
            url: appPath + "/efcs_business/manualProcedure/getPalletInfo",
            type: "post",
            dataType: "json",
            data: {
                palletNo: palletNo
            }
        })
    }

    //信息验证
    function validate() {
        $form.trigger('submit');
    }

    //验证
    (function _validate() {
        $form.formValidation({
            icon: {
                valid: 'glyphicon glyphicon-ok',

                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                wareHouse: {
                    validators: {
                        notEmpty: {}
                    }
                },
                work_procedure: {
                    validators: {
                        notEmpty: {}
                    }
                },
                palletNo: {
                    validators: {
                        notEmpty: {}
                    }
                }
            }
        }).on('success.form.fv', function (e, data) {
            handle();
            return false;
        }).on('err.form.fv',function(){
            isDisabled = false;
        })
            .on('success.field.fv', function (e, data) {
            if (data.fv.getInvalidFields().length == 0) {

            }
        });
    })();

    //处理
    function handle() {
        return $.ajax({
            url: appPath + "/efcs_business/manualProcedure/validate",
            type: "post",
            dataType: "json",
            data: {
                palletNo: $palletNo.val().trim(),
                work_procedure: $workProcedure.val(),
                house_no: $wareHouse.val()
            }
        }).done(function (data) {
            if (data.success) {
                dialog(type)
            } else {
                layer.alert(data.message || '组盘请求失败！');
            }
        }).always(function(){
            isDisabled = false;
        });
    }

    //弹窗
    function dialog(type, options) {

        url = URI.setParams(url, $.extend(true, {
            palletNo: $palletNo.val().trim(),
            type: type,
            work_procedure: $workProcedure.val(),
            house_no: $wareHouse.val()
        }, options));

        index = layer.open({
            type: 2,
            title: type == 'group' ? '手动组盘' : '拆盘',
            content: url,
            area: ["800px", "100%"],
            btn: [
                {label: '确认', class: "Js_layer_pallet_submit"},
                {label: '取消', class: "Js_layer_pallet_cancel"}
            ],
            success: function ($layer, index) {
                messenger.addTarget($layer.find("iframe")[0].contentWindow, "layui-layer-iframe" + index);
            },
            yes: function (index, $layer) {
                messenger.targets["layui-layer-iframe" + index].send("submit");
            },
            btn2: function (index, $layer) {
                layer.close(index);
            }
        })
    }
});