/**
 * Created by jade on 2017/4/14.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage');

    new EasyPage({
        name: 'xpos',
        search: {},
        grid: {
            url: appPath + '/core/errorEvent/grid',
            colModel: [
                {
                    label: $i18n('事件ID'),
                    name: 'xpos_id'
                },
                {
                    label: $i18n('主题'),
                    name: 'xpos_no'
                },
                {
                    label: $i18n('预警时间'),
                    name: 'xpos_name'
                },
                {
                    label: $i18n('预警地点'),
                    name: 'max_zpos'
                },
                {
                    label: $i18n('预警级别'),
                    name: 'max_ypos'
                },
                {
                    label: $i18n('预警类别'),
                    name: 'xpos_name'
                },
                {
                    label: $i18n('消警级别'),
                    name: 'max_zpos'
                },
                {
                    label: $i18n('消警人'),
                    name: 'max_ypos'
                },
                {
                    label: $i18n('消警时间'),
                    name: 'max_ypos'
                }
            ],
            gridComplete: function () {

            },
            paging: false
        },
        events: {
        }
    });
});

