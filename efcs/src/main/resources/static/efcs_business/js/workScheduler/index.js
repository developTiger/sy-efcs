define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Messenger = require('messenger'),
        layer = require('layer'),
        Enums = require('../enums')
    require("datetimepicker")
    var _layerId = "#layui-layer"
    var _iframe = "layui-layer-iframe"

    var locationIds =[]
    EasyPage.implement({
        _initMessenger: function () {

            var that = this;
            that.messenger = new Messenger(that.get("name"));

            that.messenger.listen(function (result) {
                result = JSON.parse(result);
                if (result.type == "ready") {
                    $(_layerId + that._index).find(".Js_layer_confirm").removeAttr("disabled");
                    return;
                }

                if (result.type == "loading") {
                    $(_layerId + that._index).find(".Js_layer_confirm").attr("disabled", "disabled");
                    return
                }
                if (result.type === 'open') {
                    easyPage1.dialog({
                        title: '库位选择',
                        url: locationUrl,
                        area: ['800px', '600px'],
                        data: {sc_id: result.sc_id}
                    })
                    locationIds = result.locationIds || []
                    return
                }

                if(result.type === 'locationReady') {
                    easyPage1._sendMessenger(_iframe + easyPage1._index, 'sendIds', {locationIds: locationIds})
                    return
                }

                if (result.type === 'selected') {
                    that.layer.close(that._index);
                    easyPage._sendMessenger(_iframe + easyPage._index, 'initLocation', {locationIds: result.locationIds})
                    return
                }

                $(_layerId + that._index).find(".Js_layer_confirm").removeAttr("disabled");
                if (result.success) {
                    that.reloadGrid();
                    that.layer.close(that._index);
                }

                that.tipMessenger(result);

            })
        }
    })

    //日期range
    $("#Js_create_time").datetimepicker({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd hh:ii:ss',
        todayHighlight:true
    })

    $("#Js_last_time").datetimepicker({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd hh:ii:ss',
        todayHighlight:true
    })

    //table
    var dataUrl = appPath + "/efcs_business/workScheduler/grid"
    var createUrl = appPath + '/efcs_business/workScheduler/form_view'
    var taskViewUrl = appPath + '/efcs_business/workScheduler/task_view'
    var easyPage = new EasyPage({
        name: 'workScheduler',
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: $i18n("计划编号"),
                    name: "sc_no"
                },
                {
                    label: $i18n('计划任务名称'),
                    name: 'sc_name'
                },
                {
                    label: $i18n("任务类型"),
                    name: "tool_type"
                },
                {
                    label: $i18n("托盘编号"),
                    name: "tool_no"
                },
                {
                    label: $i18n("计划类型"),
                    name: "scheduler_type",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('SchedulerType', cellvalue)

                    }
                },
                {
                    label: $i18n("当前位置"),
                    name: "current_location_no"
                },
                {
                    label: $i18n("计划开始时间"),
                    name: "scheduler_start_time"
                },
                {
                    label: $i18n("计划间隔时间"),
                    name: "time_interval",
                    formatter: function(cellvalue, options, row){
                        return cellvalue + Enums.getEnumsLabel('TimeIntervalUnit', row.time_interval_unit)

}
                },
                {
                    label: $i18n("作业库位数量"),
                    name: "total_loc_count"
                },
                {
                    label: $i18n("已完成数量"),
                    name: "complete_loc_count"
                },
                {
                    label: $i18n("最后请求时间"),
                    name: "last_run_time"
                },
                {
                    label: $i18n("状态"),
                    name: "scheduler_status",
                    formatter: function(cellvalue, options, rowObject){
                        var opvalue=Enums.getEnumsLabel("SchedulerStatus", cellvalue);
                        var scno=rowObject.sc_no;//计划编号
                        // return  '<select class="select_Status" id='+rowObject.id+' data-id='+rowObject.id+'  autocomplete="off" name="scheduler_type"> ' +
                        return  '<select class="select_Status" id='+scno+' data-id='+scno+'  autocomplete="off" name="scheduler_type"> ' +
                            '<option value="opvalue">'+opvalue+'</option>'+
                            '<option value="Running">启动计划</option>' +
                            '<option value="Stop">停止计划</option>'+
                            +'</select>'
                        // return Enums.getEnumsLabel("SchedulerStatus", cellvalue)
                    }
                },
                {
                    label: $i18n("操作"),
                    formatter: function (cellvalue, options, rowObject) {
                        return '<a class="Js_workScheduler_edit btn btn-link btn-xs" data-id="' + rowObject.id + '">' + $i18n("修改") + '</a>' +
                            '<a class="btn btn-link btn-xs" href="' + taskViewUrl + '?sc_id=' + rowObject.id + '" target="_blank">' + $i18n("查看") + '</a>' +
                        '<a class="Js_workScheduler_reset btn btn-link btn-xs" data-id="' + rowObject.id + '">' + $i18n("重置") + '</a>'
                    }
                }
            ],
            paging: true,
            shrinkToFit: false
        },
        events: {
            'click .Js_scheduler_add,.Js_workScheduler_edit': function (event) {
                var target=$(event.currentTarget);
                var  id=target.data("id");
                this.dialog({
                    title: id?'修改工装计划':"创建工装计划",
                    data:{
                        id:id
                    },
                    url: createUrl,
                    area: ['600px', '600px']
                })
            },
            'change .select_Status':function () {//状态改变事件
                var selectscno=event.target.getAttribute("data-id");//修改状态的scno
                var selectvalue= $(document.getElementById(selectscno)).find("option:selected").val();//修改状态的value
                var changeUrl= appPath + '/efcs_business/workScheduler/updateststus'
                $.ajax({
                    type: "get",
                    url: changeUrl,//要发送的后台地址
                    data: {sc_no:selectscno,selectvalue:selectvalue},//要发送的数据
                    dataType: "json",//后台处理后返回的数据格式
                    success: function (data) {//ajax请求成功后触发的方法
                        alert('修改成功');
                    },
                    error: function (msg) {//ajax请求失败后触发的方法
                        alert(msg);//弹出错误信息
                    }
                });
            },
            'click .Js_workScheduler_reset': function () {
                this.confirm({
                    title: '重置',
                    ok: function (callback) {
                        $.ajax({
                          // TODO 添加配置
                        }).done(function (data) {
                            callback(data)
                        })
                    }
                })
            }
        }
    })

    var locationUrl = appPath + '/efcs_business/workScheduler/location_view'

    var easyPage1 = new EasyPage({
        name: 'workScheduler1',
        grid: {}
    })
})