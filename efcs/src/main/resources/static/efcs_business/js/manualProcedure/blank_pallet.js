/**
 * Created by jade on 2017/5/18.
 */
define(function (require, exports, module) {
    var layer = require("layer"),
       Enums =  require("enums");
    require("formvalidation");
    require("formvalidation.bootstrap");

    var $palletNo = $("#Js_palletNo"),
        $workProcedure = $("[name=work_procedure]"),
        $wareHouse = $('[name = wareHouse]'),
        $positionLabel = $('#Js_position_label'),
        $position= $('#Js_position_info'),
        $erroInfo=$("#Js_error_info"),
        $form = $("#Js_form");

    $form.keydown(function(e){
        if(e.keyCode == 13){
            return false
        }
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
        submit();
        isDisabled = true;
    });

    //托盘号回车
    var loading = false;
    //空拖注册
    $(".Js_blank_palletNo").keyup(function (e) {
        var kc = e.keyCode;
        console.log(kc);

        var $this = $(this),
            val = $this.val();
        loading = true;
        if (kc == 13 && val.trim()) {
            getPalletInfo($(this).val().trim()).done(function (data) {
                if (!data.house_no) {
                    layer.alert('不能空拖注册')
                } else {
                    $wareHouse.val(data.house_no).trigger("input");
                    $positionLabel.val(data.current_pos);
                    isDisabled = false;
                }
            }).always(function(){
                loading = false;
            });
        }
        return false;
    }).on("input",function(){
        $wareHouse.val("").trigger("input");
        $erroInfo.val("").trigger("input");
        $positionLabel.val("").trigger("input");
        $position.val("").trigger("input");
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
    function submit() {
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
            url: appPath + "/efcs_business/manualProcedure/blank_pallet",
            type: "post",
            dataType: "json",
            data: {
                palletNo: $palletNo.val().trim(),
                work_procedure: $workProcedure.val(),
                house_no: $wareHouse.val()
            }
        }).done(function (data) {
            if (data.success) {
                layer.alert("操作成功!");
                setTimeout(function(){
                    window.location = window.location;
                },2000);
            } else {
                layer.alert(data.message || '组盘请求失败！');
            }
        }).always(function(){
            isDisabled = false;
        });
    }
});