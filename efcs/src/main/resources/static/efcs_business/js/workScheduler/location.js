define(function (require) {
    var StorageView = require("./storageView2")
    var d3 = require('d3')
    var EasyForm = require('easyform')
    var map = {}
    var Messenger = require('messenger')
    var _ = require('lodash')
    var locationIds = []

    EasyForm.implement({
        submit: function () {
            var result = []
            for (var i in map) {
               result.push.apply(result, map[i])
            }
            this._sendMessenger("selected", {locationIds: result});
        },
        _initMessenger: function () {
            var that = this;
            var messenger = new Messenger(window.name);
            messenger.addTarget(window.parent, that.get("superName"));

            messenger.listen(function (result) {
                result = JSON.parse(result)
                if(result.type === 'sendIds') {
                    if(result.locationIds.length > 0 ){
                        locationIds = result.locationIds
                    } else {
                        locationIdsSc.forEach(function(item){
                            locationIds.push(item.location_id)
                        })
                    }
                    return
                }
                that.$element.trigger("submit")
            })

            this.messenger = messenger;
        }
    })

    var easyForm = new EasyForm({
        element: "#Js_workScheduler_form",
        superName: 'workScheduler1'
    })

    easyForm._sendMessenger('locationReady')

    //初始化统计view
    var statusUrl = appPath + "/efcs_business/workScheduler/getLocationInfos"
    var houseId = $('.Js_location_container').attr('id')
    var storageView = new StorageView({
        element: "body",
        type: 1,
        legend: [],
        houseId: houseId,
        statusUrl: statusUrl,
        alias: {
            key: "storage_status"
        }
    });
    storageView.set('activeId', houseId)

    var locationIdsSc = $('[name=location_ids_scheduler]').val() || []
    storageView.on("heatmap.init", function ($obj, heatmap) {
        var x = $obj.data('x')

        heatmap.on('shown.cell', function () {
            var self = this
            map[x] = []
            // 初始化选中状态
            self.rects.classed('selected', function (d) {
                if (locationIds.indexOf(d.id) > -1) {
                    map[x].push(d.id)
                    return true
                }
                return false
            }).style('fill', function(d){
                return locationIds.indexOf(d.id) > -1 ? '#269fd5' : '#fff'
            })

            // 初始化使用状态
            self.rects.filter(function (d) {
                var status = _.result(_.find(locationIdsSc, {location_id: d.id}), 'appointment_status')
                if(status && (status !== 'Waiting' && status !== 'ErrorFinished')){
                    return true
                }
                return false
            }).classed('disabled', true).style('fill', '#eee')

            setSelectAll($obj, self)

            $obj.parent().find(".Js_location_selectAll").off('click').on('click', function () {
                var isSelected = $(this).prop('checked')
                self.rects.filter(function (d) {
                   return !d3.select(this).classed('disabled')
                }).classed('selected', isSelected)
                    .style('fill', isSelected ? '#269fd5' : '#fff')

                map[x] = getIds(self.svg.selectAll('.selected'))
            })

            this.rects.filter(function (d) {
                return !d3.select(this).classed('disabled')
            }).on('click', function () {
                d3.select(this)
                    .classed('selected', !d3.select(this).classed('selected'))
                    .style('fill', d3.select(this).classed('selected') ? '#269fd5' : '#fff')

                setSelectAll($obj, self)
                map[x] = getIds(self.svg.selectAll('.selected'))
            })
        })
    })

    function setSelectAll ($obj, heatmap) {
        $obj.parent().find(".Js_location_selectAll").prop('checked',
            heatmap.svg.selectAll('.selected').size() === heatmap.rects.size())
    }

    function getIds (eles) {
        var result = [];
        eles.each(function (d, i) {
            result.push(d.id);
        });
        if (result.length == 0) {
            return [];
        }

        return result
    }
})