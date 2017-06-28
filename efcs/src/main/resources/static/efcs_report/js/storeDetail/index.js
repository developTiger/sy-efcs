/**
 * Created by jade on 2017/4/17.
 */
define(function (require, exports, module) {
    var StorageView = require("./storageView2"),
        layer = require("layer"),
        Router = require("director"),
        Hash = require("hash"),
        Switchable = require("arale-switchable"),
        Uri = require('uri'),
        _ = require("lodash"),
        HeatMap = require("heatmap");

    //重写heatmap的setfill
    HeatMap.implement({
        "setFill": function (type, legends) {
            var self = this;

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
    });

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

    //初始化统计view
    var statusUrl = appPath + "/efcs_report/storeDetail/getLocationInfos",
        statisticsUrl = appPath + "/efcs_report/storeDetail/getStatusStatistics",
        locDetailUrl = appPath + "/efcs_report/storeDetail/storageLocationDetail_view?uuid=";
    var storageView = new StorageView({
        element: "body",
        legend: legends,
        dynamicLegend: false,
        statusUrl: statusUrl,
        statisticsUrl: statisticsUrl,
        alias: {
            key: "storage_status"
        },
        mousedown: function (d) {
            layer.open({
                type: 2,
                title: false,
                area: ['800px', '500px'],
                content: locDetailUrl + d.id
            })
        }
    });

    //变量定义
    var hash = Hash.get(),
        $wareHouseBtn = $(".Js_wareHouse_btn"),
        $wareHouse = $(".Js_wareHouse_x"),
        $warseHousePanel = $(".Js_warseHouse_panel"),
        activeIndex = $wareHouseBtn.index($wareHouseBtn.filter("[data-id=" + hash[1] + "]")),
        subActiveIndex = $wareHouse.index($wareHouse.filter("[data-id=" + hash[0] + "]")),
        houseId,
        $linkStatistics = $(".Js_link_statistics"),
        statistics_url = "./statistics_view";

    //路由初始化
    var routes = {
        "/(.*)/(.*)/(.*)": function (activeId, houseId, type) {
            storageView.set("houseId", houseId);
            storageView.set("type", type);
            storageView.set("activeId", activeId);

            //初始化统计链接
            var _newUrl = Uri.setParams(statistics_url, {id: type});
            $linkStatistics.attr("href", "." + _newUrl);
        }
    };
    new Router(routes).init();

    //初始父级
    new Switchable({
        element: "body",
        triggers: ".Js_wareHouse_btn",
        triggerType: "click",
        panels: ".Js_warseHouse_panel",
        activeIndex: activeIndex == -1 ? 0 : activeIndex,
        activeTriggerClass: 'active',
        onSwitched: function (toIndex) {

            houseId = $(this.get('triggers')).eq(toIndex).data("id");
            var _$activeX = $warseHousePanel.eq(toIndex).find(".Js_wareHouse_x");
            if (_$activeX.filter(".active").length == 0 && subSwitchable) {
                subSwitchable.set("activeIndex", $wareHouse.index(_$activeX.eq(0)));
            }
        }
    });

    //初始父级
    var subSwitchable = new Switchable({
        element: "body",
        triggers: ".Js_wareHouse_x",
        triggerType: "click",
        panels: ".Js_location_container",
        activeTriggerClass: 'active',
        activeIndex: subActiveIndex == -1 ? 0 : subActiveIndex,
        onSwitched: function (toIndex) {
            var $element = $(this.get('triggers')).eq(toIndex);
            Hash.set({0: $element.data("id"), 1: houseId, 2: $element.data("type")});
        }
    });


});