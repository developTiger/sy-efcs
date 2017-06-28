/**
 * Created by lyk on 2017/4/26
 */
define(function (require, exports, module) {


    $('body').on('click', '#DtsTemperatureInfo', function () {
        var houseNo = $("#houseNo").val();
        $.post(appPath + '/efcs_business/realtime/getDataJson', {houseNo: houseNo, type: '1'}, function (result) {
            $("#demo1").html(result);
        });
    }).on('click', '#DtsChannelStatusInfo', function () {
        var houseNo = $("#houseNo").val();
        $.post(appPath + '/efcs_business/realtime/getDataJson', {houseNo: houseNo, type: '2'}, function (result) {
            console.info(result);
        });
    }).on('click', '#FormationInfo', function () {
        var houseNo = $("#houseNo").val();
        $.post(appPath + '/efcs_business/realtime/getDataJson', {houseNo: houseNo, type: '3'}, function (result) {
            console.info(result);
        });
    }).on('click', '#HeartbeatInfo', function () {
        var houseNo = $("#houseNo").val();
        $.post(appPath + '/efcs_business/realtime/getDataJson', {houseNo: houseNo, type: '4'}, function (result) {
            console.info(result);
        });
    })


});