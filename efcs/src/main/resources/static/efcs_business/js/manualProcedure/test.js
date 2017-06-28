/**
 * Created by jade on 2017/4/24.
 */
define(function (require, exports, module) {
    var layer = require("layer");

    require("formvalidation");
    require("formvalidation.bootstrap");

    var $form = $("#Js_form");
    var $manualBtn = $("#Js_manual_btn"),
        type = $manualBtn.data("type");

    //提交
    var isDisabled = false;
    $manualBtn.click(function () {

        if(isDisabled ){
            return false;
        }
        $form.trigger('submit');
        isDisabled = true;
    });

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
            url: appPath + "/efcs_business/manualProcedure/procedure_test",
            type: "post",
            dataType: "json",
            data: $form.serialize()
        }).done(function (data) {
            if (data.success) {
                layer.alert("操作成功!")
            } else {
                layer.alert(data.message || '异常！');
            }
        }).always(function(){
            isDisabled = false;
        });
    }

});