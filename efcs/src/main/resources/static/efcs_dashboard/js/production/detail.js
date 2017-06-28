/**
 * Created by jade on 2017/4/21.
 */
define(function (require) {
    var layer = require("layer"),
        Tip = require("arale-tip"),
        echart = require("echarts"),
        id = $("#Js_house_id").val();

    //库位排事件
    $(".Js_area").click(function () {
        layer.open({
            type: 2,
            title: "排信息",
            area:["80%","80%"],
            content: appPath +"/efcs_report/storeDetail/single_view?id=" + id + "&x=" + $(this).data("x")
        })
    })

    //异常信息
    var tip = new Tip({
        trigger: ".Js_pallet_error"
    })
    tip.before("show", function () {
        var self = this,
            triggerData = self.activeTrigger.data();

        var records = triggerData["records"],
            loading = triggerData["loading"];

        if (records) {
            self.set('content', records);
            return true;
        }
        if (loading) {
            return true;
        }

        loading = true;

        self.set("content", "异常信息");

        //todo调整获取数据方法
        // $.post("./pallet_error_view", {
        //     targetId: self.activeTrigger.data("id"),
        //     type: self.activeTrigger.data("type")
        // }, function (data) {
        //
        //     self.activeTrigger.data("records", records);
        //     self.set('content', records);
        // }, "json");
    })


    // 趋势图配置
    var options = {
        tooltip: {
            formatter: function (params) {
                var data = params.data || [0, 0];
                return data[1];
            }
        },
        xAxis: {
            type: "time"
        },
        yAxis: {},
        series: [{
            type: 'line',
            areaStyle: {normal: {}},
            symbolSize: 10
        }]
    };
    //产量趋势图
    var reworkChart = echart.init(document.getElementById("Js_rework_chart"));
    reworkChart.setOption(options)
    reworkChart.showLoading();

    //todo调整数据来源
    setTimeout(function () {
        reworkChart.setOption({
            series: [
                {data: [["2017-02-25", 10], ["2017-02-26", 20], ["2017-02-27", 22], ["2017-02-28", 30], ["2017-02-29", 40]]}
            ]
        })
        reworkChart.hideLoading();
    }, 1000)

    //产优率初始图
    var ngChart = echart.init(document.getElementById("Js_ng_chart"));
    ngChart.setOption(options);
    ngChart.showLoading();
    //todo调整数据来源
    setTimeout(function () {
        ngChart.setOption({
            series: [
                {data: [["2017-02-25", 0], ["2017-02-26", 10], ["2017-02-27", 20], ["2017-02-28", 30], ["2017-02-29", 40]]}
            ]
        })
        ngChart.hideLoading();
    }, 1000)
})