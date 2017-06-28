package com.wxzd.efcs.business.controller;

import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.wms.core.application.dtos.WarehouseDto;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by LYK on 2017/4/26
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class RealtimeController {

    @Resource
    private WareHouseAppService wareHouseAppService;

    @Resource
    private DeviceRealtimeInfoService service;

    @RequestMapping
    public void index_view(Model model) {
        ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();
        model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());
    }

    @RequestMapping
    @ResponseBody
    public Object getDataJson(String houseNo, String type) {
//        return wareHouseAppService.getAllStorehouse();
        switch (type) {
            case "1":
                //DTS 温度获取
                return service.getDtsTemperatureInfo(houseNo);
            case "2":
                //获取DTS设备状态
                return service.getDtsChannelStatusInfo(houseNo);
            case "3":
                //化成柜 状态获取
                return service.getFormationInfo(houseNo);
            case "4":
                //获取心跳实时信息
                return service.getHeartbeatInfo(houseNo);
        }
        return null;
    }

}
