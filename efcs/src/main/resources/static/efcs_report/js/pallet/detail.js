/**
 * Created by jade on 2017/4/18
 */
define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");

    var id = $("#Js_pallet_id").val(),
        moveDetailUrl = appPath + "/efcs_report/pallet/grid_moveDetail?id=" + id,
        batteryUrl = appPath + "/efcs_report/pallet/grid_battery?palletId=" + id;

    //托盘移动记录
    var easyPage = new EasyPage({
        search: [],
        grid: {
            url: moveDetailUrl,
            "colModel": [
                {
                    label: $i18n("库号"),
                    name: "house_no"
                },
                {
                    label: $i18n("工序"),
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
                    label: $i18n("单据号"),
                    name: "form_no"
                },
                {
                    label: $i18n("托盘位置类型"),
                    name: "pos_type",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('PositionType', cellvalue);
                    }
                },
                {
                    label: $i18n("位置"),
                    name: "arrive_pos"
                },
                {
                    label: "异常代码",
                    name: "error_code"
                },
                {
                    label: "异常描述",
                    name: "error_desc"
                },
                {
                    label: $i18n("时间"),
                    name: "create_datetime"
                }
            ],
            shrinkToFit: false,
            paging: true
        }
    });

    //电池
    var detailUrl = '../battery/detail_view?battery_barcode=';
    var isSplit = $("#Js_battery_type").val() == "splitList" ? true : false;
    var easyPage = new EasyPage({
        gridEle: "#Js_table_battery",
        search: [],
        grid: {
            url: batteryUrl,

            "colModel": [
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("电池条码"),
                    name: "battery_barcode",
                    formatter: function (cellvalue, options, rows) {
                        return "<a class='ui-grid-link' href='" + detailUrl + rows.battery_barcode + "' target='_blank'>" + cellvalue + "</a>"
                    }
                },
                {
                    label: $i18n("电池状态"),
                    name: "battery_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabelTpl('Battery_status', cellvalue)
                    }
                },
                {
                    label: $i18n("sku类型"),
                    name: "sku_name"
                },
                {
                    label: $i18n("通道号"),
                    name: "channel_no"
                },
                {
                    label: !isSplit ? $i18n("来源设备号") : $i18n("去向设备号"),
                    name: !isSplit ? "from_equip_no" : "to_equip_no"
                },
                {
                    label: !isSplit ? $i18n("来源夹头编号") : $i18n("去向夹头编号"),
                    name: !isSplit ? "from_clamp_no" : "to_clamp_no"
                },
                {
                    label: !isSplit ? $i18n("来源位置类型") : $i18n("去向位置类型"),
                    name: !isSplit ? "from_pos_type" : "to_pos_type",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel("EquipmentType", cellvalue);
                    }
                },
                {
                    label: !isSplit ? $i18n("来源位置编号") : $i18n("去向位置编号"),
                    name: !isSplit ? "from_pos_no" : "to_pos_no"
                },
                {
                    label: !isSplit ? $i18n("来源位置通道号") : $i18n("去向位置通道号"),
                    name: !isSplit ? "from_pos_channel_no" : "to_pos_channel_no"
                },
                {
                    label: $i18n("托盘号"),
                    name: "pallet_no"
                }
            ],
            shrinkToFit: false,
            paging: true,
            pager: "#Js_pager_battery"
        }
    });
});