define(function (require, exports, module) {
    var EasyPage = require("easypage"),
        Enums = require("enums");
    require("datepicker");

    $("#Js_operate_datetime").bootstrapDP({
        language: "zh-CN",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayHighlight: true
    });

    //table
    var dataUrl = appPath + "/efcs_business/scheduler/grid";
    new EasyPage({
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },

                {
                    label: $i18n("设备号"),
                    name: "device_no"
                },
                {
                    label:$i18n('计划类型'),
                    name:"scheduler_type",
                    formatter:function(cellvalue){
                        return Enums.getEnumsLabel('SchedulerType', cellvalue);
                    }
                },
                {
                    label:$i18n('任务类型'),
                    name:"task_type",
                    formatter:function(cellvalue){
                        return Enums.getEnumsLabel('TaskType', cellvalue);
                    }
                },
                {
                    label:$i18n('计划状态'),
                    name:"scheduler_status",
                    formatter:function(cellvalue){
                        return Enums.getEnumsLabel('SchedulerStatus', cellvalue);
                    }
                },
                {
                    label: $i18n("工序类型"),
                    name: "workProcedure",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('WorkProcedure', cellvalue);
                    }
                },
                {
                    label: $i18n("请求次数"),
                    name: "run_times"
                },
                {
                    label: $i18n("开始时间"),
                    name: "scheduler_start_time"
                },
                {
                    label: $i18n("计划结束时间"),
                    name: "scheduler_end_time"
                },
                {
                    label: $i18n("间隔时间"),
                    name: "time_interval"
                },
                {
                    label: $i18n("间隔时间单位"),
                    name: "time_interval_unit",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabelTpl('TimeIntervalUnit', cellvalue);
                    }
                },
                {
                    label: $i18n("下次执行时间"),
                    name: "next_run_time"
                },
                {
                    label: $i18n("异常次数"),
                    name: "error_times"
                },
                {
                    label: $i18n("异常代码"),
                    name: "error_code"
                },
                {
                    label: $i18n("异常信息"),
                    name: "error_msg"
                },

                {
                    label: $i18n('操作'),
                    formatter: function (cell, options, rowObject) {

                        if(rowObject.task_type == "EmptyPalletOut"){
                            return "";
                        }
                        if(rowObject.scheduler_status == "Running"){
                            return '<a class="Js_scheduler_status  btn btn-link btn-xs" ' +
                                'data-id="' + rowObject.id + '" data-type="Finish">结束</a>'
                        }
                        return '<a class="Js_scheduler_status  btn btn-link btn-xs"' +
                            ' data-id="' + rowObject.id + '" data-type="Running">启动</a>';


                    }
                }
            ],
            paging: true,
            shrinkToFit: false
        },
        events: {
            'click .Js_scheduler_status': function (event) {
                var $target = $(event.target),
                    self = this;

                $.ajax({
                    url: '../scheduler/setSchedulerStatus',
                    data: {
                        id: $target.data('id'),
                        schedulerStatus:$target.data('type'),
                    }
                }).done(function(){
                    self.reloadGrid({});

                })
            }
        }
    })
});
