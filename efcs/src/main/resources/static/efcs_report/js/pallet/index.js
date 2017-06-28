/**
 * Created by jade on 2017/4/18.
 */
define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");

    var dataUrl = appPath + "/efcs_report/pallet/grid",
        detailUrl = './detail_view?id=';
    var easyPage = new EasyPage({
        name: "pallet",
        search: [],
        grid: {
            url: dataUrl,
            "colModel": [
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("托盘条码"),
                    name: "container_no",
                    formatter: function (cellvalue, options, rows) {
                        return '<a class="ui-grid-link" href="' + detailUrl + rows.id + '" target="_blank">'
                            + cellvalue + '</a>'
                    }
                },
                {
                    label: $i18n("单据号"),
                    name: "current_form_no"
                },
                {
                    label: $i18n("工序状态"),
                    name: "work_procedure",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('WorkProcedure', cellvalue);
                    }
                },
                {
                    label: $i18n("托盘状态"),
                    name: "pallet_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('PalletStatus', cellvalue)
                    }
                },
                {
                    label: $i18n("当前位置"),
                    name: "current_pos"
                },
                {
                    label: $i18n("调度状态"),
                    name: "dispatch_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('PalletDispatchStatus', cellvalue)
                    }
                }
            ],
            shrinkToFit: true,
            paging: true,
            gridComplete: function () {

            },
            postData:{dispatchStatus:$("[name=dispatchStatus]").val()}
        }

    });
});