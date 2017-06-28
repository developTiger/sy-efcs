/**
 * Created by jade on 2017/4/14.
 */
define(function (require, exports, module) {
    var arttemplate = require("arttemplate"),
        cabelTpl = require("./cabel.tpl"),
        rate = 60 * 1000,
        echart = require("echarts"),
        statisticUrl = "../production/getProductDashboardStatistics",
        cabelStatistics = "../production/getCabelStatistics";
    require("countup");

    function interval(fn, time) {
        setInterval(fn, time);
    }

    //生产监控
    var $Countups = $(".Js_countup")
    $.each($Countups, function (i, item) {
        var $item = $(item);
        $item.countup({
            startVal: 0,
            separator: ',',
            decimal: ".",
            decimals: $item.is("h1") ? 0 : 2,
            endVal: $item.data("value")
        })
    })
    interval(function () {
        $.ajax({
            url: statisticUrl,
            dataType: "json"
        }).done(function (data) {
            update(data);
        })
    }, rate)
    function update(data) {
        var i = 0;
        $.each(data, function (index, item) {
            $Countups.eq(i++).data("countUp").update(item.counts)
            $Countups.eq(i++).data("countUp").update(item.percentage);
        })
    }

    //拉线
    interval(function () {
        $.ajax({
            url: cabelStatistics,
            dataType: "json"
        }).done(function (data) {
            renderCabel({cabel: data});
        })
    }, rate)
    function renderCabel() {
        var cabelfn;
        return function (data) {
            cabelfn || (cabelfn = arttemplate.compile(cabelTpl))
            cabelfn(data)
        }
    }


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
            name: '销量',
            type: 'line',
            symbolSize: 10
        }]
    };
    //产量趋势图
    var outputChart = echart.init(document.getElementById("Js_output_chart"));
    outputChart.setOption(options)
    outputChart.showLoading();

    //todo调整数据来源
    setTimeout(function () {
        outputChart.setOption({
            series: [
                {data: [["2017-02-25", 0], ["2017-02-26", 10], ["2017-02-27", 20], ["2017-02-28", 30], ["2017-02-29", 40]]}
            ]
        })
        outputChart.hideLoading();
    }, 1000)

    //产优率初始图
    var superiorChart = echart.init(document.getElementById("Js_superior_chart"));
    superiorChart.setOption(options);
    superiorChart.showLoading();
    //todo调整数据来源
    setTimeout(function () {
        superiorChart.setOption({
            series: [
                {data: [["2017-02-25", 0], ["2017-02-26", 10], ["2017-02-27", 20], ["2017-02-28", 30], ["2017-02-29", 40]]}
            ]
        })
        superiorChart.hideLoading();
    }, 1000)
});