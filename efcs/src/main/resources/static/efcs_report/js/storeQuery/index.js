/**
 * Created by jade on 2017/4/18.
 */
define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");
    require("datepicker");

    //日期
    $("[name=create_datetime]").bootstrapDP({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd'
    });

    //table
    var dataUrl = appPath + "/efcs_report/storeQuery/grid",
        detail = "./detail_view?id=";
    var easyPage = new EasyPage({
        name: "storeQuery",
        search: {},
        grid: {
            url: dataUrl,
            "colModel": [
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("库位编号"),
                    name: "loc_no"
                },
                {
                    label: $i18n("托盘编号"),
                    name: "pallet_no"
                },

                {
                    label: $i18n("sku条码"),
                    name: "sku_barcode"
                },
                {
                    label: $i18n("sku类型"),
                    name: "sku_name"
                },
                {
                    label: $i18n("库存数量"),
                    name: "sto_count"
                },
                {
                    label: $i18n("单位"),
                    name: "sto_unit"
                },
                {
                    label: $i18n("库存类型"),
                    name: "sto_type",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel("StorageType", cellvalue)
                    }
                },
                {
                    label: $i18n("入库时间"),
                    name: "create_datetime"
                }
            ],
            shrinkToFit: false,
            paging: true,
            gridComplete: function () {

            }
        }
    });
});