/**
 * Created by jade on 2017/4/22.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage'),
        Enums = require("enums"),
        dataUrl = appPath + '/efcs_alarm/errorInfo/grid';

    new EasyPage({
        name: 'errorInfo',
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: " ",
                    width:"50px",
                    name: "handled",
                    formatter: function (cellvalue) {
                        return !cellvalue ? '<i class="fa fa-envelope fa-fw" style="color:#e49824;"></i>' : "已读"
                    }
                },
                {
                    width:"200px",
                    label:"创建时间",
                    name:"create_datetime"
                },
                {
                    label: $i18n('库编号'),
                    name: 'house_no'
                },
                {
                    label: $i18n('库位号/设备号'),
                    name: 'location'
                },
                {
                    label: $i18n("位置类型"),
                    name: "location_type",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('LocationType', cellvalue);
                    }
                },
                {
                    width:"200px",
                    label: $i18n('标题'),
                    name: 'title'
                },
                {
                    width:"300px",
                    label: $i18n('内容'),
                    name: 'content'
                },
                {
                    label: $i18n('操作'),
                    formatter: function (cellvalue, options, rowObject) {
                        return '<a class="btn btn-link btn-xs" target="_blank" href="' +
                                    appPath + '/efcs_alarm/errorInfo/detail_view?id=' + rowObject.id + '">详情</a>';
                    }
                }
            ],
            gridComplete: function () {

            },
            shrinkToFit: true,
            paging: true
        },
        events: {}
    });
});

