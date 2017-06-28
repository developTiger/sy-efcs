/**
 * Created by jade on 2017/6/13.
 */
define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");
    //table
    var dataUrl = appPath + "/efcs_report/storeClear/grid"
    var clearUrl = appPath + '/efcs_report/storeClear/clear'
    var easyPage = new EasyPage({
        name: "storeQuery",
        search: {},
        include: ['checkbox'],
        grid: {
            url: dataUrl,
            "colModel": [
                {
                    label: '<span class="checkbox checkbox-inline">\
                                <input type="checkbox" class="Js_checkall">\
                                <label>' + $i18n("全选") + '</label>\
                            </span>',
                    name: "pallet_no",
                    sortable: false,
                    align: "center",
                    formatter: function (cellvalue, options, rowObject) {
                        return '<div class="checkbox checkbox-inline">\
                                    <input type="checkbox" class="Js_checkitem" data-id="' + cellvalue + '">\
                                    <label></label>\
                               </div>'
                    }
                },
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("库位编号"),
                    name: "loc_no"
                },
                {
                    label: $i18n("托盘编号"),
                    name: "pallet_no"
                },
                {
                    label: $i18n("库存数量"),
                    name: "sto_count"
                },
                {
                    label: $i18n("单位"),
                    name: "sto_unit"
                },
                {
                    label: $i18n("库存类型"),
                    name: "sto_type",
                    formatter: function (cellvalue) {
                        return '托盘'
                    }
                },
                {
                    label: $i18n("入库时间"),
                    name: "create_datetime"
                }
            ],
            shrinkToFit: false,
            paging: false,
            gridComplete: function () {

            },
            postData: {loc_no: $("[name=loc_no]").val()}
        },
        events: {
            'click .Js_storeClear': function () {
                var data = {houseNo: $("[name=wareHouse]").val(),locNo: $("[name=loc_no]").val()};
                data["ids"] = this.checkbox.getCheckedProp("data-id").join(",")
                if (this.checkbox.getCheckedProp("data-id").length == 0) {
                    this.layer.alert($i18n("请先选择需要操作的行！"));
                    return false
                } else {
                    var self = this
                    var index = this.layer.confirm('是否确定移除', function () {
                        $.ajax({
                            url: clearUrl,
                            type: 'post',
                            data: data
                        }).done(function(){
                            self.layer.close(index)
                            self.reloadGrid()
                        }).fail(function(data){
                            self.layer.alert(data.message)
                        })
                    })
                }
            },
            'change [name=loc_no]': function (event) {
                this.reloadGrid({loc_no: $("[name=loc_no]").val()})
            }
        }
    })
})