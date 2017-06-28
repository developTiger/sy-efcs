/**
 * Created by jade on 2017/4/22.
 */
define(function (require, exports, module) {
    var arttemplate = require("arttemplate"),
        moment = require("moment"),
        handleInfo = require("./handleInfos.tpl"),
        handleFn = arttemplate.compile(handleInfo),
        toastr = require("toastr");
    require("formvalidation");
    require("formvalidation.bootstrap");
    require("jqueryform");

    arttemplate.helper("dateFormat", function (date, format) {
        return moment(date).format(format || "YYYY-MM-DD HH:mm:ss")
    });

    var $form = $("#Js_handle_form"),
        $handleInfo = $("#Js_error_handleInfos");
    $("[name=handle_info]").on("keydown", function (e) {
        if (e.keyCode == 13) {
            if ($(this).val().trim()) {
                $form.ajaxSubmit({
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            try {
                                $handleInfo.prepend(handleFn({handleInfo: JSON.parse(data.message)}));
                                $form[0].reset();
                            } catch (e) {

                            }
                        } else {
                            toastr("error")(data.message || "添加异常！");
                        }
                    }
                });
            }
            return false;
        }
    });
});