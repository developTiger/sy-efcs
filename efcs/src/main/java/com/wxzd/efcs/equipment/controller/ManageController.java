package com.wxzd.efcs.equipment.controller;

/**
 * 设备管理
 * Created by jade on 2017/4/14.
 */

import com.wxzd.efcs.equipment.ModuleEfcsEquipment;
import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.equipment.application.querys.EquipmentExQuery;
import com.wxzd.efcs.equipment.application.service.EquipmentAppService;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.wms.core.application.dtos.WarehouseDto;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Module(ModuleEfcsEquipment.name)
@Controller
@RequestMapping
public class ManageController {

    @Resource
    private WareHouseAppService wareHouseAppService;

    @Resource
    private EquipmentAppService equipmentAppService;

    @RequestMapping
    public void index_view(Model model) {
        ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();
        List<String> equipDtos = equipmentAppService.getAllEquipVender();

        model.addAttribute("equipList", equipDtos);
        if (wareHouseDtos != null) {
            model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());
        }
    }

    @RequestMapping
    @ResponseBody
    public PageResult<EquipmentDto> grid(EquipmentExQuery query) {
        return equipmentAppService.getAllEquipmentsPaged(query);
    }

    @RequestMapping
    public void setrow_view() {

    }

    @RequestMapping
    @ResponseBody
    public Object setRow_grid() {
        Map map = new HashMap<>();
        map.put("id", "123");
        map.put("xpos_no", "测试");
        return map;
    }
}
