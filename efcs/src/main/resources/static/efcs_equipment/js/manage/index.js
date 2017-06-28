/**
 * Created by jade on 2017/4/14.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage'),
        Enums = require("enums");

    var dataUrl = appPath + '/efcs_equipment/manage/grid';
    new EasyPage({
        name: 'equipment',
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: $i18n('设备名称'),
                    name: 'equip_name'
                },
                {
                    label: $i18n('运行状态'),
                    name: 'channelEvent',
                    formatter: function (cellvalue) {
                        '<i class="glyphicon glyphicon-record"></i>' + '<i class="glyphicon glyphicon-exclamation-sign"></i>'
                        return '';
                    }
                },
                {
                    label: $i18n('库编号'),
                    name: 'house_no'
                },
                {
                    label: $i18n('货位号'),
                    name: 'location_no'
                },
                {
                    label: $i18n('设备号'),
                    name: 'equip_no'
                },
                {
                    label: $i18n('图片'),
                    name: 'equip_img',
                    formatter: function (cellvalue) {
                        return cellvalue ? '<img style="width:60px;height:60px;"  src="' + gaiaFile.showBase + cellvalue + '">' : "";
                    }
                },
                {
                    label: $i18n('设备类型'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('EquipmentType',cellvalue);
                    }
                },
                {
                    label: $i18n('设备型号'),
                    name: 'equip_model'
                },

                {
                    label: $i18n('设备描述'),
                    name: 'equip_desc'
                },
                {
                    label: $i18n('厂家名称'),
                    name: 'equip_vender'
                },
                {
                    label: $i18n('厂家联系方式'),
                    name: 'vender_phone'
                },

                {
                    label: $i18n('操作'),
                    formatter: function (cellvalue, options, rowObject) {
                        return '<a class="Js_xpos_test  btn btn-link btn-xs" data-id="' + rowObject.id + '">测试</a>' +
                            '<a class="Js_xpos_detail  btn btn-link btn-xs" data-id="' + rowObject.id + '">详情</a>' +
                            '<a class="Js_xpos_set btn btn-link btn-xs" data-id="' + rowObject.id + '">设置关联排</a>';
                    }
                }
            ],
            gridComplete: function () {

            },
            shrinkToFit: false,
            paging: true
        },
        events: {
            "click .Js_xpos_set": function (event) {
                $target = $(event.currentTarget);
                var id = $target.data("id");
                this.dialog({
                    title: "设置关联排",
                    url: appPath + "/efcs_equipment/manage/setrow_view",
                    data: {id: id}
                })
            }
        }
    });

});
