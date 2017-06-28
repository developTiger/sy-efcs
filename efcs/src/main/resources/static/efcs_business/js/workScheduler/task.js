define(function (require, exports, module) {
    var EasyPage = require("easypage")
    require("../dateRange");
    var Enums = require('../enums')

    //table
    var dataUrl = appPath + "/efcs_business/workScheduler/task_grid"
    new EasyPage({
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: $i18n("任务编号"),
                    name: "sc_no"
                },
                {
                    label: $i18n("计划编号"),
                    name: "sc_no"
                },
                {
                    label: $i18n("工装类型"),
                    name: "tool_type"
                },
                {
                    label: $i18n("工装编号"),
                    name: "tool_no"
                },
                {
                    label: $i18n("库位编号"),
                    name: "location_no"
                },
                {
                    label: $i18n("工单状态"),
                    name: "appointment_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel(cellvalue)
                    }
                },
                {
                    label: $i18n("预约时间"),
                    name: "appointment_time"
                },
                {
                    label: $i18n("完成时间"),
                    name: "complete_time"
                }
            ],
            paging: true,
            shrinkToFit: false,
            postData: {
                sc_id: $('[name=sc_id]').val()
            }
        }
    })
});