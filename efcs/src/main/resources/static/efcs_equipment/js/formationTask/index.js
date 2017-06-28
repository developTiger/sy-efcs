/**
 * Created by jade on 2017/4/21.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage');

    new EasyPage({
        name: 'formationTask',
        search: {},
        grid: {
            url: appPath + '/efcs_equipment/formationTask/grid',
            colModel: [
                {
                    label: $i18n('任务ID'),
                    name: 'equip_name'
                },
                {
                    label: $i18n('任务触发'),
                    name: 'equip_no'
                },
                {
                    label: $i18n('任务类型'),
                    name: 'equip_img'
                },
                {
                    label: $i18n('工装ID'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        return equipType[cellvalue];
                    }
                },
                {
                    label: $i18n('设备ID'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        return equipType[cellvalue];
                    }
                },
                {
                    label: $i18n('排队'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        return equipType[cellvalue];
                    }
                },
                {
                    label: $i18n('开始时间'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        return equipType[cellvalue];
                    }
                },
                {
                    label: $i18n('结束时间'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        return equipType[cellvalue];
                    }
                },
                {
                    label: $i18n('任务结果'),
                    name: 'equip_type',
                    formatter: function (cellvalue) {
                        '<i class="fa fa-check-circle" style="color:#28a852;font-size:20px;"></i>'
                        '<i class="fa fa-exclamation-circle" style="color:#f00;font-size:20px;"></i>'
                        return '';
                    }
                },
                {
                    label: $i18n('操作'),
                    formatter: function (cellvalue, options, rowObject) {
                        return '<a class="Js_task_sort btn btn-link btn-xs" data-id="' + rowObject.id + '">调整次序</a>' +
                            '<a class="Js_task_del btn btn-link btn-xs" data-id="' + rowObject.id + '">删除</a>';
                    }
                }
            ],
            gridComplete: function () {

            },
            shrinkToFit: false,
            paging: true
        },
        events: {
            "click .Js_task_add,.Js_task_edit": function (event) {
                $target = $(event.currentTarget);
                var id = $target.data("id");
                this.dialog({
                    title: $.i18n("创建工装任务"),
                    url: "./addorupdate_view",
                    data: {id: id}
                })
            },
            "click .Js_task_sort": function () {
                this.confirm(
                    {
                        content: "是否调整顺序",
                        "ok": function (callback) {
                            $.ajax().done(function (data) {
                                callback(data)
                            });
                        }
                    })
            }
        }
    });

});
