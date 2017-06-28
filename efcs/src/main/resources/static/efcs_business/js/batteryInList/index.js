define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");
    require("../dateRange.js");

    //table
    var dataUrl = appPath + "/efcs_business/batteryInList/grid",
        detailUrl = appPath + '/efcs_report/battery/detail_view?id=';
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
                    label: $i18n("设备号"),
                    name: "equip_no"
                },
                {
                    label: $i18n("电池条码"),
                    name: "battery_barcode",
                    formatter: function (cellvalue, options, rows) {
                        return '<a class="ui-grid-link" href="' + detailUrl + rows.pallet_detail_id +
                                '" target="_blank">' + cellvalue + '</a>'
                    }
                },
                {
                    label: $i18n("拉线通道号"),
                    name: "line_channel_no"
                },
                {
                    label:$i18n("电池状态"),
                    name:"battery_status",
                    formatter:function(cellvalue){
                        return Enums.getEnumsLabel("Battery_status",cellvalue)
                    }
                },
                {
                    label: $i18n("工序"),
                    name: "work_procedure",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('WorkProcedure', cellvalue);
                    }
                },
                {
                    label: $i18n("sku类型"),
                    name: "sku_name"
                },
                {
                    label: $i18n("入库时间"),
                    name: "operate_datetime"
                },
                {
                    label: $i18n("创建方式"),
                    name: "create_mode",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('FmCreateMode', cellvalue);
                    }
                },
                {
                    label: $i18n("表单状态"),
                    name: "fm_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabelTpl('FmStatus',cellvalue);
                    }
                },
                {
                    label: $i18n("异常代码"),
                    name: "error_code",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('EfcsErrorCode', cellvalue);
                    }
                },
                {
                    label: $i18n("异常信息"),
                    name: "error_desc"
                },
                {
                    label: $i18n("备注"),
                    name: "remark"
                }
            ],
            paging: true,
            shrinkToFit: false
        }
    })
});