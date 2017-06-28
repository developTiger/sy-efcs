/**
 * Created by jade on 2017/4/22.
 */
define(function (require, exports, module) {
    var StorageView = require("./storageView2"),
        layer = require("layer"),
        Router = require("director"),
        Hash = require("hash"),
        Switchable = require("arale-switchable"),
        HeatMap = require("heatmap"),
        _ = require("lodash"),
        d3 = require("d3"),
        Uri = require("uri");

    var startColor = '#fff',
        endColor = '#ff0000',
        low = 0,
        high = 100,
        refreshRate = 20 * 1000;
    HeatMap.implement({
        //渲染温度
        renderTempLegend: function () {
            var lendStr = '<svg xmlns="http://www.w3.org/2000/svg" version="1.1">' +
                '<text transform="translate(15,30)">low</text>' +
                ($("#grad1").length > 0 ? "" :
                    ('<linearGradient id="temperature" x1="0%" y1="0%" x2="100%" y2="0%">' +
                    '<stop offset="0%" style="stop-color:' + startColor + ';stop-opacity:1" />' +
                    '<stop offset="100%" style="stop-color:' + endColor + ';stop-opacity:1" />' +
                    '</linearGradient>')) +
                '<rect width="200" height="30" fill="url(#temperature)" transform="translate(40,10)"/>' +
                '<text transform="translate(245,30)">high</text>' +
                '</svg>';
            this.$element.find(".Js_legend_btns").html(lendStr);
        },
        "colors": d3.scaleLinear().domain([low, high]).range([startColor, endColor]),

        "setFill": function (type, legends) {
            var self = this;

            if (type == "cw") {
                var colors = self.colors;
                self.rects && self.rects.transition().duration(1000)
                    .style("fill", function (d) {
                        return colors(d.temperature);
                    });
            } else {
                //设置填充色
                self.rects && self.rects.transition().duration(1000)
                    .style("fill", function (d) {
                        return _.result(_.find(legends || self.get("legend"), function (chx) {
                            if (d.is_empty && chx[self.alias["type"]] == "emptyPallet") {
                                return true;
                            }
                            return chx[self.alias["type"]] == d[self.alias["key"] || type];
                        }), self.alias["color"]);
                    });
            }
        }
    });

    //补充统计项
    var legends = {
        "hc": [
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
        ],
        "cw": {}
    };

    //初始化统计view
    var statusUrl = appPath + "/efcs_alarm/temperature/getLocationInfos",
        statisticsUrl = appPath + "/efcs_alarm/temperature/getStatusStatistics";
    var storageView = new StorageView({
        element: "body",
        legend: legends,
        type: 1,
        dynamicLegend: true,
        alias: {
            key: "storage_status"
        },
        statusUrl: statusUrl,
        statisticsUrl: statisticsUrl
    });


    //回调函数
    storageView.on("heatmap.init", function ($obj, heatmap) {

        if ($obj.data("temperature")) {
            heatmap.renderTempLegend();
        }

        heatmap.on("shown.cell", function () {

            setTimeout(function () {
                heatmap.set("url", Uri.setParams(heatmap.get("url"), {t: new Date().getTime()}));

                (heatmap.get("type") == "hc") && heatmap.set("statisticsParams", $.extend({}, heatmap.get("statisticsParams"), {t: new Date().getTime()}));

            }, refreshRate);

        });
    });

    //变量定义
    var hash = Hash.get(),
        $wareHouseBtn = $(".Js_wareHouse_btn"),
        activeIndex = $wareHouseBtn.index($wareHouseBtn.filter("[data-id=" + hash[1] + "]")),
        houseId;

    //路由初始化
    var routes = {
        "/(.*)/(.*)": function (activeId, houseId) {
            storageView.set("houseId", houseId);
            storageView.set("activeId", activeId);
        }
    };
    new Router(routes).init();

    //初始父级
    new Switchable({
        element: "body",
        triggers: ".Js_wareHouse_btn",
        triggerType: "click",
        panels: ".Js_location_container",
        activeIndex: activeIndex == -1 ? 0 : activeIndex,
        activeTriggerClass: 'active',
        onSwitched: function (toIndex) {
            var $element = $(this.get('triggers')).eq(toIndex),
                houseId = $element.data("id");

            Hash.set({0: houseId, 1: houseId})
        }
    });
})