/**
 * Created by jade on 2017/4/28.
 */
define(function (require, exports, module) {

    require("datepicker");

    //日期range
    var $startTime = $("#Js_start_time"),
        $endTime = $("#Js_end_time");
    $startTime.bootstrapDP({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd',
        todayHighlight:true
    }).on('changeDate', function (e) {
        var startTime = e.date;
        $endTime.bootstrapDP('setStartDate', startTime);
    });

    $endTime.bootstrapDP({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd',
        todayHighlight:true
    }).on('changeDate', function (e) {
        var endTime = e.date;
        $startTime.bootstrapDP('setEndDate', endTime);
    });
});