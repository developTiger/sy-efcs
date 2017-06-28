/**
 * Created by jade on 2017/4/18.
 */
define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        enums = require("enums");

    var dataUrl = appPath + "/efcs_report/battery/grid",
        detail = "./detail_view?battery_barcode=";
    var easyPage = new EasyPage({
        name: "battery",
        search: [],
        grid: {
            url: dataUrl,

            "colModel": [
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("电池条码"),
                    name: "battery_barcode",
                    formatter: function (cellvalue, options, rows) {
                        return "<a class='ui-grid-link' href='" + detail + rows.battery_barcode + "' target='_blank'>"
                            + cellvalue + "</a>"
                    }
                },
                {
                    label: $i18n("单据号"),
                    name: "form_no"
                },
                {
                    label: $i18n("托盘号"),
                    name: "pallet_no"
                },
                {
                    label: $i18n("电池状态"),
                    name: "battery_status",
                    formatter: function (cellvalue) {
                        return enums.getEnumsLabelTpl('Battery_status', cellvalue);
                    }
                },
                {
                    label: $i18n("工序状态"),
                    name: "work_procedure",
                    formatter: function (cellvalue) {
                        return enums.getEnumsLabel('WorkProcedure', cellvalue);
                    }
                },
                {
                    label: $i18n("当前位置"),
                    name: "current_pos"
                },
                {
                    label: $i18n("位置类型"),
                    name: "pos_type",
                    formatter: function (cellvalue) {
                        return enums.getEnumsLabel("PositionType", cellvalue);
                    }
                }
            ],
            shrinkToFit: false,
            paging: true,
            gridComplete: function () {

            }
        }
    });
});