define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");

    //table
    var dataUrl = appPath + "/efcs_business/workProcedureList/grid",
        detailUrl = appPath + '/efcs_report/pallet/detail_view?id=';
    new EasyPage({
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: $i18n("单据号"),
                    name: "form_no"
                },
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("托盘号"),
                    name: "pallet_no",
                    formatter: function (cellvalue, options, rows) {
                        return '<a class="ui-grid-link" href="' + detailUrl + rows.pallet_cargo_id +
                                '" target="_blank">' + cellvalue + '</a>'
                    }
                },
                {
                    label: $i18n("工序类型"),
                    name: "work_procedure",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('WorkProcedure', cellvalue);
                    }
                },
                {
                    label: $i18n("托盘状态"),
                    name: "pallet_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('PalletStatus', cellvalue);
                    }
                },
                {
                    label: $i18n("入库库位"),
                    name: "in_loc_no"
                },
                {
                    label: $i18n("入库时间"),
                    name: "in_time"
                },
                {
                    label: $i18n("出库库位"),
                    name: "out_loc_no"
                },
                {
                    label: $i18n("计划出库时间"),
                    name: "out_plan_time"
                },
                {
                    label: $i18n("实际出库时间"),
                    name: "out_time"
                },
                {
                    label: $i18n("计划存放时长"),
                    name: "stay_plan_time"
                },
                {
                    label: $i18n("实际存放时长"),
                    name: "stay_time"
                },
                {
                    label: $i18n("单据状态"),
                    name: "fm_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabelTpl('FmStatus',cellvalue);
                    }
                }
            ],
            paging: true,
            shrinkToFit: false
        }
    })


});