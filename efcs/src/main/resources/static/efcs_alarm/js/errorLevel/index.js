/**
 * Created by jade on 2017/4/14.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage');

    new EasyPage({
        name: 'xpos',
        search: {},
        grid: {
            url: appPath + '/core/errorLevel/grid',
            colModel: [
                {
                    label: $i18n('预警级别'),
                    name: 'xpos_id'
                },
                {
                    label: $i18n('预警级别描述'),
                    name: 'xpos_no'
                },
                {
                    label: $i18n('主题颜色'),
                    name: 'xpos_name',
                    formatter: function (cellvalue) {
                        return '<div style="background-color:' + cellvalue + ';width:25px;height:16px;"></div>'
                    }
                },
                {
                    label: $i18n('页面告警方式'),
                    name: 'max_zpos'
                },
                {
                    label: $i18n('弹窗持续时间'),
                    name: 'max_ypos'
                },
                {
                    label: $i18n('是否强制消警'),
                    name: 'associated_roadway'
                },
                {
                    label: $i18n('操作'),
                    formatter: function (cellvalue, options, rowObject) {
                        return '<a class="Js_warninglevel_edit  btn btn-link btn-xs" data-id="' + rowObject.id + '">设置</a>'
                    }
                }
            ],
            gridComplete: function () {

            },
            paging: false
        },
        events: {
            "click .Js_warninglevel_edit": function (event) {
                $target = $(event.currentTarget);
                var id = $target.data("id");
                this.dialog({
                    title: "新增",
                    url: appPath + "/core/errorLevel/set_view",
                    data: {id: id}
                })
            }
        }
    });

});

