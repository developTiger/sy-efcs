/**
 * Created by jade on 2017/4/16.
 */
define(function (require, exports) {
    var HeatMap = require("heatmap")
    var Uri = require("uri");

    //补充统计项
    var legends = [
        {
            label: $i18n("异常"),
            color: "red",
            icon: "fa fa-exclamation-circle",
            type: "error"
        },
        {
            label: $i18n("空托盘"),
            color: "#bfdab9",
            type: "emptyPallet"
        },
        {
            label: $i18n("有货"),
            "color": '#dbbfb3',
            type: "haveGoods"
        },
        {
            label: $i18n("无货"),
            color: "#f8f8f8",
            type: "empty"
        }
    ];

    //heatmap
    var $container = $(".chart-container"),
        data = $container.data(),
        houseId = $container.hid,
        x = data.x,
        y = data.y,
        z = data.z,
        type = x,
        locInfoUrl = appPath + "/efcs_report/storeDetail/getLocationInfos",
        statisticsUrl = appPath + "/efcs_report/storeDetail/getStatusStatistics";
    var heatmap = new HeatMap({
        element: ".chart-container",
        row_number: y,
        col_number: z,
        legend: legends,
        type: type,
        alias: {
            label: "label",
            key: "storage_status"
        },
        url: Uri.setParams(locInfoUrl, {
            houseId: houseId,
            x: x,
            type: type
        }),
        statisticsUrl: statisticsUrl,
        statisticsParams: {
            houseId: houseId,
            type: type,
            x: x
        }
    });
})