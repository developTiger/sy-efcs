define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");
    require("../dateRange");

    //table
    var dataUrl = appPath + "/efcs_business/splitList/grid",
        detailUrl = appPath + '/efcs_report/pallet/detail_view?id=';
    new EasyPage({
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("单据号"),
                    name: "form_no"
                },
                {
                    label: $i18n("托盘号"),
                    name: "pallet_no",
                    formatter: function (cellvalue, options, rows) {
                        return '<a class="ui-grid-link" href="' + detailUrl + rows.pallet_cargo_id + "&type=splitList" +
                            '" target="_blank">' + cellvalue + '</a>';
                    }
                },
                {
                    label: $i18n("设备号"),
                    name: "equip_no"
                },
                {
                    label: $i18n("工序"),
                    name: "work_procedure",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('WorkProcedure', cellvalue);
                    }
                },
                {
                    label: $i18n("开始时间"),
                    name: "proc_start_time"
                },
                {
                    label: $i18n("完成时间"),
                    name: "proc_complete_time"
                },
                {
                    label: $i18n("拆盘状态"),
                    name: "palletize_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabelTpl('PalletizeStatus', cellvalue);
                    }
                }
            ],
            paging: true,
            shrinkToFit: false
        }
    })
});