/**
 * Created by jade on 2017/4/21.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage');

    new EasyPage({
        name: 'formationPlain',
        search: {},
        grid: {
            url: appPath + '/efcs_equipment/formationPlain/grid',
            colModel: [
                {
                    label: $i18n('拉线'),
                    name: 'equip_name'
                },
                {
                    label: $i18n('任务类型'),
                    name: 'equip_no'
                },
                {
                    label: $i18n('计划类型'),
                    name: 'equip_img'
                },
                {
                    label: $i18n('设备类型'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        return equipType[cellvalue];
                    }
                },
                {
                    label: $i18n('状态'),
                    name: 'equip_model'
                },
                {
                    label: $i18n('操作'),
                    name: 'associated_roadway'
                },
                {
                    label: $i18n('操作'),
                    formatter: function (cellvalue, options, rowObject) {
                        return '<a class="Js_plain_edit btn btn-link btn-xs" data-id="' + rowObject.id + '">修改</a>';
                    }
                }
            ],
            gridComplete: function () {

            },
            shrinkToFit: false,
            paging: true
        },
        events: {
            "click .Js_plain_add,.Js_plain_edit": function (event) {
                $target = $(event.currentTarget);
                var id = $target.data("id");
                this.dialog({
                    title: $i18n("创建周期计划"),
                    url: appPath + "/efcs_equipment/formationPlain/addorupdate_view",
                    data: {id: id}
                })
            }
        }
    });
});
