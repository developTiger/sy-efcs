package com.wxzd.efcs.alarm.controller;

import com.wxzd.configration.catlConfig.AlarmInfoConfig;
import com.wxzd.efcs.alarm.ModuleEfcsAlarm;
import com.wxzd.efcs.alarm.application.dtos.LocationChartTemplate;
import com.wxzd.efcs.alarm.application.service.FireAlarmInfoAppService;
import com.wxzd.efcs.report.application.dtos.LocationWithNoPallet;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.wms.core.application.dtos.WarehouseDto;
import com.wxzd.wms.core.application.queryService.StorageLocationQueryService;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 库位温度监控
 * Created by jade on 2017/4/22.
 */
@Module(ModuleEfcsAlarm.name)
@Controller
@RequestMapping
public class TemperatureController {


    @Resource
    private WareHouseAppService wareHouseAppService;

    @Resource
    private StorageLocationQueryService storageLocationQueryService;

    @Resource
    private StorageDetailAppService storageDetailAppService;

    @Resource
    private FireAlarmInfoAppService fireAlarmInfoAppService;

    @RequestMapping
    public void index_view(Model model) {
        ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();

        model.addAttribute("rowMap", AlarmInfoConfig.getRowMap1());
        if (wareHouseDtos != null) {
            model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());
        }
    }

    /**
     * 获取库位信息
     *
     * @param houseId
     * @return
     */
    @RequestMapping
    @ResponseBody
    public List<LocationChartTemplate> getLocationInfos(UUID houseId, String x) {
        return fireAlarmInfoAppService.getNormalLocationInfos(houseId, Integer.valueOf(x));
    }

    /**
     * 库位统计信息
     *
     * @param houseId houseId
     * @param x       x轴信息
     */
    @RequestMapping
    @ResponseBody
    public Map<Integer, Map<String, Object>> getStatusStatistics(UUID houseId, Integer x) {
        LocationWithNoPallet locationWithNoPallet = new LocationWithNoPallet();
        locationWithNoPallet.setHouse_id(houseId);
        locationWithNoPallet.setX_pos(x);

        return fireAlarmInfoAppService.getStatisticsTemplated(houseId,x);
    }

}
