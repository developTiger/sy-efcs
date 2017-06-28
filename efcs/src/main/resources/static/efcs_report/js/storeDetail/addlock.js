define(function(require,exports,module){

    var Messenger = require("messenger"),
        layer = require("layer");

    require("formvalidation");
    require("formvalidation.bootstrap");

    //添加与父窗口通讯
    var messenger = new Messenger(window.name),
        $lockForm = $("#Js_lock_form");
    messenger.addTarget(window.parent, "lock");
    messenger.listen(function (msg) {
        $lockForm.submit();
    });


    var $lockType = $("[name=lockType]"),
        $descContainer = $("[for=lock]");

    $("[name=isLock]").change(function () {
        resetLockType($(this).val());
    }).trigger('change');

    //根据操作类型更新操作项
    function resetLockType(val) {
        //是否加锁
        var isLock = val == 'lock' ? true : false;

        $lockType.each(function () {
            var $this = $(this),
                flag = $this.data("flag");

            $this.parent()[(isLock && (flag == 0))||(!isLock && (flag == 1)) ? "show" : "hide"]();
        })

        $descContainer[isLock?"show":"hide"]()
    }

    $lockForm.formValidation({
        icon: {
            valid: 'glyphicon glyphicon-ok',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            isLock: {
                validators: {
                   notEmpty:{

                   }
                }
            },
            lockType: {
                validators: {
                    notEmpty: {}
                }
            },
            lockDesc: {
                validators: {
                    notEmpty: {}
                }
            }
        }
    }).on('success.form.fv', function (e, data) {
        submit();

        return false;
    })
    .on('success.field.fv', function (e, data) {
        if (data.fv.getInvalidFields().length == 0) {

        }
    });

    function submit(){
        var sendObj = $lockForm.serialize();
        $.ajax({
            url:appPath+"/efcs_report/storeDetail/addLock",
            data:sendObj,
            dataType:"json"
        }).done(function(data){
            if(data.success){
                messenger.targets["lock"].send($lockForm.find("[name=id]").val());
            }else{
                layer.alert(data.message);
            }
        })
    }
});