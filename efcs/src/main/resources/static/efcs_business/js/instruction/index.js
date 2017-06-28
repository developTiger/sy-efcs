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
    var dataUrl = appPath + "/efcs_business/instruction/grid";
    new EasyPage({
        search: {},
        grid: {
            url: dataUrl,
            colModel: [
                {
                    label: $i18n("指令号"),
                    name: "instr_no"
                },
                {
                    label: $i18n("库编号"),
                    name: "house_no"
                },
                {
                    label: $i18n("设备号"),
                    name: "equip_no"
                },
                {
                    label: $i18n("托盘号"),
                    name: "pallet_no"
                },
                {
                    label: $i18n("单据号"),
                    name: "form_no"
                },
                {
                    label: $i18n("指令类型"),
                    name: "instr_type",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('InstructionType', cellvalue);
                    }
                },
                {
                    label: $i18n("工序类型"),
                    name: "work_procedure",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('WorkProcedure', cellvalue);
                    }
                },
                {
                    label: $i18n("起点位置"),
                    name: "from_pos"
                },
                {
                    label: $i18n("终点位置"),
                    name: "to_pos"
                },
                {
                    label: $i18n("移动策略"),
                    name: "move_policy",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('InstructionMovePolicy', cellvalue);
                    }
                }, {
                    label: $i18n("执行状态"),
                    name: "instr_status",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabelTpl('InstructionStatus', cellvalue);
                    }
                },
                {
                    label: $i18n("异常代码"),
                    name: "error_code",
                    formatter: function (cellvalue) {
                        return Enums.getEnumsLabel('EfcsErrorCode', cellvalue);
                    }
                },
                {
                    label: $i18n("备注"),
                    name: "remark"
                },
                {
                    label: $i18n('操作'),
                    formatter: function (cell, options, rowObject) {
                        var str =  '<a class="Js_instruction_resend  btn btn-link btn-xs" data-id="' + rowObject.id + '">指令重发</a>';
                        str+='<a class="Js_instruction_deprecated  btn btn-link btn-xs" data-id="' + rowObject.instr_no + '">指令作废</a>';
                        return str;
                    }
                }
            ],
            paging: true,
            shrinkToFit: false
        },
        events: {
            'click .Js_instruction_resend': function (event) {
                var $target = $(event.target),
                    self = this;

                $.ajax({
                    url: '../instruction/resend',
                    data: {
                        id: $target.data('id')
                    }
                }).done(function(){
                    self.reloadGrid({});

                })
            },
        'click .Js_instruction_deprecated': function (event) {
        	var $target = $(event.target),
        	self = this;
        	
//        	console.info("作废");
//        	console.info($target.data('id'));
        	$.ajax({
        		url: '../instruction/deprecated',
        		data: {
        			instrNo: $target.data('id')
        		}
        	}).done(function(){
        		self.reloadGrid({});
        	})
        }
        }
    })
});