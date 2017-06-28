define(function (require) {
    var EasyForm = require("easyform")
    var layer = require('layer')
    var Messenger = require('messenger')

    require("datetimepicker")

    //日期range
    $("#Js_start_time").datetimepicker({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd hh:ii:ss',
        todayHighlight:true
    }).on('changeDate', function (e) {
       $(this).trigger("input")
    });

    $("#Js_end_time").datetimepicker({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd hh:ii:ss',
        todayHighlight:true
    }).on('changeDate', function (e) {
        $(this).trigger("input")
    });

    EasyForm.implement({
        _initMessenger: function () {
            var that = this;
            var messenger = new Messenger(window.name);
            messenger.addTarget(window.parent, that.get("superName"));

            messenger.listen(function (result) {
                result = JSON.parse(result)
                if(result.type === 'initLocation') {
                    $('#locationPreview').val(result.locationIds.length)
                    $('#locationIds').val(result.locationIds)
                    return
                }
                that.$element.trigger("submit")
            })

            this.messenger = messenger;
        }
    })

    var easyFrom = new EasyForm({
        element: "#Js_workScheduler_form",
        superName: "workScheduler",
        fields: {
            sc_name: {
                validators: {
                    notEmpty: {

                    }
                }
            },
            tool_no: {
              validators: {
                  notEmpty: {

                  }
              }
            },
            scheduler_start_time: {
                validators: {
                    notEmpty: {

                    }
                }
            },
            time_interval: {
                validators: {
                    notEmpty: {

                    },
                    integer: {

                    }
                }
            },
            locationPreview: {
                validators: {
                    notEmpty: {

                    }
                }
            },
            scheduler_end_time: {
                validators: {
                    notEmpty: {

                    }
                }
            }
        }
    })

    // 库位选择
    $('#Js_location_view').click(function () {
        easyFrom._sendMessenger('open', {
            locationIds: $('#locationIds').val().split(','),
            sc_id: $('[name=id]').val()

        })



    })

    // 根据工装类型获取工装托盘号
    var $toolNo = $('[name=tool_no]')
    var $endTimeType = $('#Js_change_endTime_type'),
        $schedulerType = $('[name=scheduler_type]'),
        $toolType = $('[name=tool_type]')
    $toolType.change(function(){
        var val =$(this).val();
        $.ajax({
            url: appPath + '/efcs_business/workScheduler/getContainer',
            type:"post",
            data: {
                type_name:val
            },
            dataType: 'json'
        }).done(function(data){
            var str = ''
            data.items.forEach(function (item) {
                str += '<option vlaue="' + item.container_barcode + '">' + item.container_barcode + '</option>'
            })
            $toolNo.html(str)
        })

        $('[data-target=校准工装]').toggle(val === '校准工装')
        if (val !== '校准工装') {
            $schedulerType.val('ExecutionOnce').trigger('change')
        }
    }).trigger("change")

    // 根据工装类型进行 计划类型，时间间隔切换

    $endTimeType.change(function () {
        $('[data-target=Js_change_endTime_type]').toggle($(this).prop('checked'))
    }).trigger('change')

    $schedulerType.change(function () {
        $('[data-target=ExecutionLoop]').toggle($(this).val() === 'ExecutionLoop')
    }).trigger('change')
})