/**
 * Created by jade on 2017/4/14.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage');

    new EasyPage({
        name: 'equipment',
        include: ["checkbox"],
        grid: {
            url: appPath + '/efcs_equipment/manage/setRow_grid',
            colModel: [
                {
                    label: ' ',
                    name: 'id',
                    align: "center",
                    formatter: function (cellvalue, options, rowObject) {
                        return '<div class="checkbox checkbox-inline">' +
                            '<input type="checkbox" class="Js_checkitem" data-id="' + cellvalue + '">' +
                            '<label></label>' +
                            '</div>'
                    }
                },
                {
                    label: $i18n('叉车'),
                    name: 'xpos_id'
                },
                {
                    label: $i18n('左二'),
                    name: 'xpos_no'
                },
                {
                    label: $i18n('左一'),
                    name: 'xpos_name'
                },
                {
                    label: $i18n('右一'),
                    name: 'max_zpos'
                },
                {
                    label: $i18n('右二'),
                    name: 'max_ypos'
                }
            ],
            gridComplete: function () {

            }
        },
        events: {}
    });

});
