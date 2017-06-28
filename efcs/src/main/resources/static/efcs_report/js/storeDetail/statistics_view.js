/**
 * Created by jade on 2017/4/18.
 */
define(function (require, exports, module) {
    var EasyPage = require('easypage');

    var rowUrl = appPath + '/efcs_report/storeDetail/grid_x?id=' + $("#Js_zone_id").val();

    //排汇总
    var easyPage = new EasyPage({
        grid: {
            url: rowUrl,
            colModel: [
                {
                    label: $i18n('排数'),
                    name: 'xPos'
                },
                {
                    label: $i18n('托盘数'),
                    name: 'palletCount'
                },
                {
                    label: $i18n('执行中托盘数'),
                    name: 'finishedCount'
                },
                {
                    label: $i18n('等待出库托盘数'),
                    name: 'outWaitingCount'
                },
                {
                    label: $i18n('超期超过2小时数'),
                    name: 'OverdueTwoHour'
                },
                {
                    label: $i18n('超期超过4小时数'),
                    name: 'OverdueFourHour'
                },
                {
                    label: $i18n('超期超过8小时数'),
                    name: 'OverdueEightHour'
                }
            ],
            sortable: false,
            shrinkToFit: false,
            gridComplete: function () {
                var $grid = easyPage.$grid;
                var rowIds = $grid.jqGrid('getDataIDs');
                $grid.jqGrid("setSelection", rowIds[0]);
            },
            onSelectRow: function (rowid) {
                initDetail(rowid);
            }

        },
        events: {
            "click .Js_warehouserow_add,.Js_warehouserow_edit": function (event) {
                $target = $(event.currentTarget);
                var id = $target.data("id");
                this.dialog({
                    title: "新增",
                    url: appPath + "/efcs_report/warehouserow/addorupdate_view",
                    data: {id: id}
                })
            }
        }
    });

    //排明细
    var initDetail = (function () {
        var result;
        return function (id) {
            if (result) {
                result.reloadGrid({page: 1, id: id})
            } else {
                result = new EasyPage({
                    search: {},
                    searchEle: "Js_search_form",
                    gridEle: "#Js_table_detail",
                    gridContainer: "#Js_table_container_detail",
                    grid: {
                        url: appPath + "/efcs_report/storeDetail/grid_x_detail",
                        colModel: [
                            {
                                label: $i18n('排数'),
                                name: 'xPos'
                            },
                            {
                                label: $i18n('列数'),
                                name: 'row_name'
                            },
                            {
                                label: $i18n('层数'),
                                name: 'row_x'
                            },
                            {
                                label: $i18n('托盘号'),
                                name: 'row_max_y'
                            },
                            {
                                label: $i18n('状态'),
                                name: 'row_max_z'
                            },
                            {
                                label: $i18n('超期状态'),
                                name: 'row_pos'
                            },
                            {
                                label: $i18n('预计出库时间'),
                                name: 'associated_roadway'
                            },
                            {
                                label: $i18n('等待时长（h)'),
                                name: 'associated_roadway'
                            },
                            {
                                label: $i18n('超期时长（h）'),
                                name: 'associated_roadway'
                            }
                        ],
                        shrinkToFit: false,
                        gridComplete: function () {

                        },
                        postData: {id: id},
                        paging: true,
                        pager: "#Js_pager_detail"
                    },
                    events: {}
                })
            }
        }

    })();
})