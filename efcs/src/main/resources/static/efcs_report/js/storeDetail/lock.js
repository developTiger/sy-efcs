/**
 * Created by jade on 2017/5/8.
 */
define(function (require, exports, module) {
    var StorageView = require("./storageView2"),
        layer = require("layer"),
        Router = require("director"),
        Hash = require("hash"),
        Switchable = require("arale-switchable"),
        Uri = require('uri'),
        _ = require("lodash"),
        HeatMap = require("heatmap"),
        Messenger = require("messenger");
    //消息
    var messenger = new Messenger("lock"),
        index;
    messenger.listen(function (msg) {
        layer.close(index);

        $("body").trigger("setError." + msg);
    });


    //插入lock.svg
    $("body").append('<svg style="display:none;">\
            <defs>\
                <symbol id="icon-lock">\
                    <svg class="icon"  version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="611">\
                        <g xmlns="http://www.w3.org/2000/svg">\
                            <use xlink:href="#icon-lock-2" ">\
                            <use xlink:href="#icon-lock-1" ">\
                        </g>\
                    </svg>\
                </symbol>\
                <symbol id="icon-lock-1">\
                    <svg class="icon" width="20px" height="20px" style="vertical-align: middle;fill: currentColor;overflow: hidden;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="611">\
                        <g xmlns="http://www.w3.org/2000/svg">\
                            <path fill="#6fb1f9" d="M459.248737 864.712121c-14.481061 0-25.858586-11.378788-25.858585-25.858586L479.935606 700.252525c-26.892677-12.414141-46.545455-39.306818-46.545454-71.369949 0-43.441919 35.167929-78.611111 78.609848-78.611111V393.050505H353.744949V222.383838c0-65.162879 52.752525-117.915404 118.949495-117.915404h39.305556V0h-66.198232C336.161616 0 248.242424 87.919192 248.242424 196.525253v196.525252C190.319444 393.050505 142.739899 439.59596 142.739899 498.554293v419.941919c0 57.925505 47.579545 105.503788 105.502525 105.503788h263.757576V864.712121h-52.751263z" p-id="612"></path>\
                        </g>\
                    </svg>\
                </symbol>\
                <symbol id="icon-lock-2">\
                    <svg class="icon" width="20px" height="20px" style="vertical-align: middle;fill: currentColor;overflow: hidden;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="611">\
                        <g xmlns="http://www.w3.org/2000/svg" >\
                            <path fill="#9266f9" d="M775.756313 393.050505V196.525253C775.756313 87.919192 686.801768 0 578.19697 0h-66.198233v104.468434h39.305556c65.162879 0 118.950758 52.752525 118.950758 117.915404v170.665404H511.998737v157.22096c43.440657 0 78.609848 35.169192 78.609849 78.611111 0 32.064394-18.616162 58.955808-46.544192 71.36995l46.544192 138.599747c0 14.481061-11.376263 25.858586-25.858586 25.858586h-52.75v159.287879H775.756313c57.92298 0 105.503788-46.544192 105.503788-105.503788V497.518939c-0.001263-57.92298-47.580808-104.468434-105.503788-104.468434z" p-id="612"></path>\
                        </g>\
                    </svg>\
                </symbol>\
            </defs>\
        </svg>');

    //locktype
    var locLockIcon = {
        'doublelock': '#icon-lock',
        'artificial': '#icon-lock-1',
        'business': '#icon-lock-2'
    };

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
        },
        //legend渲染
        renderLegend: function () {
            var str = "",
                self = this,
                val = this.get("legend");
            if (val && val.length > 0) {
                for (var j = 0; j < val.length; j++) {

                    str += '<a style="margin-left:10px;" data-id="' + val[j][self.alias["type"]] + '" data-color="' + val[j][self.alias["color"]] + '">'

                    if (val[j]['svg']) {
                        str += '<span style="font-size:16px;margin-right:5px;display:inline-block;color:red;">' +
                            '<svg width="20px" height="20px"><g><use xlink:href="' + val[j]['svg'] + '"</g></svg>' +
                            '' +
                            '</span>'
                    }
                    else {
                        str += '<span style="width:24px;height:17px;display:inline-block;margin-right:5px;border:1px solid #e4e4e4;background-color:' + val[j][self.alias["color"]] + '"></span>'

                    }
                    str += '<span>' + val[j][self.alias["label"]] + '</span></a>'
                }

                this.$element.find(".Js_legend_btns").html(str);
            }
        },

        setError: function (val) {
            var self = this,
                keys = self.get("keys"),
                x = keys.x,
                y = keys.y,
                maxy = self.maxy,
                maxx = self.maxx,
                reverse = self.get("reverse"),
                minx = self.minx,
                datakeys = self.get("dataKeys");
            var cellContent = self.svg.select(".cell-content");

            var errorG = cellContent.selectAll("g").data(val, function (d) {
                return datakeys.map(function (item) {
                    return d[item];
                }).join(",");
            });

            errorG.enter()
                .filter(function (d) {
                return d["locationLockType"]
            })
                .append("g")
                .attr("transform", function (d) {
                    if (reverse) {
                        var _x = +(maxx - d[x]) * self.cellSize + 5;
                        return "translate(" + _x +
                            "," + ((maxy - d[y]) * self.cellSize + 5) + ")"
                    } else {
                        return "translate(" + (d[x] - minx) * self.cellSize + 5 +
                            "," + ((maxy - d[y]) * self.cellSize + 5) + ")"

                    }
                })
                .append("use")
                .attr("xmlns:xlink", "http://www.w3.org/1999/xlink")
                .attr("xlink:href", function (d) {
                    return locLockIcon[d['locationLockType']]
                });

            errorG.exit().remove();
        }
    });

    //补充统计项
    var legends = [
        {
            label: $i18n("人工锁"),
            color: "red",
            svg: "#icon-lock-1",
            type: "artificial"
        },
        {
            label: $i18n("业务锁"),
            color: "red",
            svg: "#icon-lock-2",
            type: "business"
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
        statisticsUrl = appPath + "/efcs_report/storeDetail/getStatusStatistics_lock";
    var storageView = new StorageView({
        element: "body",
        legend: legends,
        dynamicLegend: false,
        statusUrl: statusUrl,
        statisticsUrl: statisticsUrl,
        alias: {
            key: "storage_status"
        },
        dataKeys:["x_pos", "y_pos", "z_pos","locationLockType","storage_status"],
        mousedown: function (d) {
            index = layer.open({
                type: 2,
                title: false,
                area: ['440px', '400px'],
                btn: [
                    {label: '确认', class: "Js_layer_pallet_submit"},
                    {label: '取消', class: "Js_layer_pallet_cancel"}
                ],
                content: appPath + "/efcs_report/storeDetail/addLock_view?id=" + d.id +
                "&in_lock=" + d.artificial_in_lock + "&out_lock=" + d.artificial_out_lock,
                success: function ($layer, index) {
                    messenger.addTarget($layer.find("iframe")[0].contentWindow, "layui-layer-iframe" + index);
                },
                yes: function (index, $layer) {
                    messenger.targets["layui-layer-iframe" + index].send("submit");
                },
                btn2: function (index, $layer) {
                    layer.close(index);
                }
            });

            var self = this;

            $("body").one("setError." + d.id, function (e, val) {
                self.reloadStatusStatistics(self.get("statisticsParams"))
                self.set("url", Uri.setParams(self.get("url"), {t: new Date().getTime()}));
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