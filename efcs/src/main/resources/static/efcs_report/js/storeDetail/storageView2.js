/**
 * Created by Administrator on 2017/3/28.
 *
 * 基于heatmap的库位管理组件
 *
 * 支持多heatmap
 */
define(function (require, exports, module) {
    var HeatMap = require("heatmap"),
        _ = require("lodash"),
        Widget = require("arale-widget"),
        Uri = require("uri");

    var events = {
        "click .Js_legend_btns a": function (event) {
            var self = this;
            var $target = $(event.currentTarget);
            var zone_id = $target.data("id");
            if (zone_id == "error") {
                return;
            }
            var color = $target.data("color"),
                type = self.get("type"),
                x = self.get("x")

            var selectedCell = self.getSelectedCell();

            var result = [];
            selectedCell.each(function (d, i) {
                result.push(d.id);
            });

            if (result.length == 0) {
                return;
            }

            $.post(self.get("url"), {
                zoneId: zone_id,
                zoneTypeId: type,
                ids: result.join(",")
            }, function () {
                selectedCell.style("fill", color);
                self.resetSelected();
                self.reloadStatusStatistics(self.get("statisticsParams"))
            }, "json");
        }
    };

    //优化
    function timeChunk(ary, fn, count) {

        var start = function () {
            for (var i = 0; i < Math.min(count || 1, ary.length); i++) {
                var obj = [].shift.apply(ary);
                fn(obj);
            }
        }

        return function (callback) {
            var timer = setInterval(function () {
                if (ary.length == 0) {
                    clearInterval(timer);
                    callback && callback();
                }

                start();
            }, 500)
        }
    }

    var StorageView = Widget.extend({
        attrs: {
            container: ".Js_location_container",

            activeId: {
                value: null,
                setter: function (val) {
                    var res = val.split("-");

                    if (res.length == 6) {
                        this.x = +res.pop();
                    }

                    this.id = res.join("-")

                    return val;
                }
            },
            //是否是动态legend
            dynamicLegend: true,
            alias: {
                value: {
                    label: 'label',
                    color: "color",
                    type: "type"
                },
                setter: function (val) {
                    return $.extend(true, {}, this.get("alias"), val);
                }
            },

            type: null
            //动态的type
            //,dynamicType

        },
        setup: function () {
            this.alias = this.get("alias");
            this.render();
        },

        _onRenderActiveId: function (val) {

            var $activeContainer = this.$activeContainer = $(this.get("container")).filter("[id=" + val + "]"),
                render = $activeContainer.data("render");
            if (render) return;
            $activeContainer.data("render", "rendered");

            this.initChart();
        },

        /**
         *
         * @param houseId
         * @param type
         * @param x 排
         * @param row 容器索引 默认与x一致
         */

        initChart: function () {
            var self = this,
                type = self.get("type"),
                houseId = self.get("houseId"),
                legend = self.get("legend"),
                alias = self.get("alias");

            timeChunk(this.$activeContainer.find(".chart-container"), function (obj) {
                var $obj = $(obj),
                    _legend,
                    dataSet = $obj.data(),
                    x = dataSet["x"],
                    y = dataSet["y"],
                    z = dataSet["z"],
                    miny = dataSet["miny"],
                    minz = dataSet["minz"],
                    drag = dataSet["drag"],
                    _type = type;

                if (self.get("dynamicLegend")) {
                    _legend = $.isNumeric(type) ? legend[dataSet["id"]] : legend[type]
                } else {
                    _legend = legend;
                }

                $.isNumeric(type) && (_type = $obj.data("id"));

                var heatmap = new HeatMap({
                    element: obj,
                    x: x,
                    min_row_number: miny,
                    row_number: y,
                    min_col_number: minz,
                    col_number: z,
                    legend: _legend,
                    type: _type,
                    alias: alias,
                    drag: drag,
                    url: Uri.setParams(self.get("statusUrl"), {
                        houseId: houseId,
                        x: x,
                        type: _type
                    }),
                    statisticsUrl: self.get("statisticsUrl"),
                    statisticsParams: {houseId: houseId, type: _type, x: x},
                    events: self.get("legendTrigger") ? events : {},
                    mousedown: self.get("mousedown"),
                    dataKeys:self.get("dataKeys")
                });

                $obj.data("heatmap", heatmap);

                self.trigger("heatmap.init",heatmap);
            }, 1)()
        },

        changeLegend: function (type) {
            var legends = this.get("legend");
            this.$activeContainer.find(".chart-container").each(function (index, item) {
                var heatmap = $(item).data("heatmap");
                heatmap.set("legend", legends[type])
                heatmap.renderLegend();
                heatmap.setFill(type);
            })
        },
        getStatusStatistics: function () {
            var self = this,
                type = self.get("type"),
                houseId = self.get("houseId");

            this.$activeContainer.find(".chart-container").each(function (index, item) {
                var $item = $(item);
                var heatmap = $item.data("heatmap");
                var x = $item.data("x");

                heatmap.set("statisticsParams", {houseId: houseId, type: type, x: x});
            })
        }
    });

    module.exports = StorageView;

});