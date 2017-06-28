/**
 * Created by jade on 2017/4/24.
 */
define(function (require, exports, module) {
    var Messenger = require("messenger"),
        layer = require("layer");

    require("formvalidation");
    require("formvalidation.bootstrap");

    var _icon = {
        valid: 'glyphicon glyphicon-ok',
        validating: 'glyphicon glyphicon-refresh'
    };


    //添加与父窗口通讯
    var messenger = new Messenger(window.name);
    messenger.addTarget(window.parent, "manualProcedure");
    messenger.listen(function (msg) {
        $("#Js_group_form").submit();
    });

    var groupForm = $("#Js_group_form"),
        $battery = $("[name=battery_barcode]"),
        index = 0;

    //调整enter为tab
    //$battery.eq(index).focus();
    $battery.keyup(function (e) {
        var kc = e.keyCode;
        console.log(kc);
        if (kc == 13) {
            index++;
            if (index < $battery.length) {
                $battery.eq(index).focus();
            }
        }
        return false;
    }).focus(function (e) {
        index = $battery.index($(this));
    }).on("input", function () {
        $(this).next().addClass("dn");
    });

    //清除
    groupForm.on("click", ".Js_battery_clear", function () {
        var $input = $(this).prev(),
            value = $input.val();
        value.trim() && handle['removePalletizeBattery'](value).done(function (data) {
            if (data.success) {
                $input.val("").focus().trigger("input").attr('class', 'form-control');
                $input.prop("disabled", false);
            } else {
                layer.alert(data.message);
            }
        });
    });

    var type = $("#Js_handle_type").val(),
        container_no = groupForm.find("[name=container_no]").val();
    //next = $("#Js_handle_next").val();
    //提交
    function submit() {
        if (groupForm.length == 0) {
            messenger.targets["manualProcedure"].send("close");
            return false;
        }

        handle[type] && handle[type]();
    }

    var houseNo = $("#Js_house_no").val(),
        palletNo = $("#Js_pallet_no").val(),
        workProcedure = $("#Js_work_procedure").val(),
        houseId = $("#Js_house_id").val();
    var validator = {
        group: function () {
            $("#Js_group_form").formValidation({
                icon: _icon,
                fields: {
                    battery_barcode: {
                        validators: {
                            remote: {
                                type: 'POST',//默认get
                                url: appPath + '/efcs_business/manualProcedure/save',
                                dataType: "json",//默认
                                delay: 200,
                                data: function (validator, $field) {
                                    return {
                                        houseNo: houseNo,
                                        houseId: houseId,
                                        palletNo: palletNo,
                                        workProcedure: workProcedure,
                                        channel_no: $field.data('channel')
                                    }
                                },
                                validKey: "success",//服务端返回表示状态的key值,服务端成功true，失败false（类型为boolean）
                                message: '服务错误',
                                callback: function ($field, response) {
                                    if (response.success) {
                                        $field.next().removeClass("dn");
                                        $field.prop("disabled", true);
                                    }
                                }
                            }
                        },
                        trigger: "blur"
                    },
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
                    container_no: {
                        notEmpty: {}
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
        },
        fake: function () {
            $("#Js_group_form").formValidation({
                icon: _icon,
                fields: {
                    battery_barcode: {
                        validators: {
                            remote: {
                                type: 'POST',//默认get
                                url: appPath + '/efcs_business/manualProcedure/addfeek',
                                dataType: "json",//默认
                                delay: 200,
                                data: function (validator, $field) {
                                    return {
                                        houseNo: houseNo,
                                        houseId: houseId,
                                        palletNo: palletNo,
                                        workProcedure: workProcedure,
                                        channel_no: $field.data('channel')
                                    }
                                },
                                validKey: "success",//服务端返回表示状态的key值,服务端成功true，失败false（类型为boolean）
                                message: '服务错误',
                                callback: function ($field, response) {
                                    if (response.success) {
                                        $field.next().removeClass("dn");
                                        $field.prop("disabled", true);
                                    }
                                }
                            }
                        },
                        trigger: "blur"
                    },
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
                    container_no: {
                        notEmpty: {}
                    }
                }
            }).on('success.form.fv', function (e, data) {
                submit();
                return false;
            }).on('success.field.fv', function (e, data) {
                if (data.fv.getInvalidFields().length == 0) {

                }
            });

        },
        split: function () {
            $("#Js_group_form").formValidation({
                icon: _icon,
                fields: {
                    work_procedure: {
                        notEmpty: {}
                    }
                }
            }).on('success.form.fv', function (e, data) {
                submit();
                return false;
            }).on('success.field.fv', function (e, data) {
                if (data.fv.getInvalidFields().length == 0) {

                }
            });
        },
        changeProcedure: function () {
            $("#Js_group_form").formValidation({
                icon: _icon,
                fields: {
                    container_no: {
                        notEmpty: {}
                    },
                    work_procedure: {
                        notEmpty: {}
                    }
                }
            }).on('success.form.fv', function (e, data) {
                submit();
                return false;
            }).on('success.field.fv', function (e, data) {
                if (data.fv.getInvalidFields().length == 0) {

                }
            });
        }
    };

    //处理
    var handle = {
        group: function () {
            _handlePost({
                url: appPath + "/efcs_business/manualProcedure/finish",
                data: {houseNo: houseNo, palletNo: palletNo, workProcedure: workProcedure}
            });
        },
        fake: function () {
            _handlePost({
                url: appPath + "/efcs_business/manualProcedure/finish",
                data: {houseNo: houseNo, palletNo: palletNo, workProcedure: workProcedure}
            });
        },
        split: function () {
            _handlePost({
                url: appPath + "/efcs_business/manualProcedure/finish",
                data: {houseNo: houseNo, palletNo: palletNo, workProcedure: workProcedure}
            });
        },
        changeProcedure: function () {
            _handlePost({
                url: appPath + "/efcs_business/manualProcedure/changeProcedure",
                data: {houseNo: houseNo, palletNo: palletNo, workProcedure: workProcedure}
            });
        },
        removePalletizeBattery: function (battery_barcode) {
            return $.ajax({
                url: appPath + "/efcs_business/manualProcedure/removeBattery",
                type: "post",
                data: {battery_barcode: battery_barcode, palletNo: palletNo},
                dataType: "json"
            })
        }
    };

    var _handlePost = function (options) {
        $.ajax({
            url: options.url,
            type: "post",
            data: options.data,
            dataType: "json"
        }).done(function (data) {
            if (data.success) {
                messenger.targets["manualProcedure"].send("success");
            } else {
                layer.alert(data.message || "操作失败");
            }
        })
    };

    validator[type] && validator[type]();
    $("#Js_group_form").keydown(function (e) {
        if (e.keyCode == 13) {
            return false
        }
    });
});