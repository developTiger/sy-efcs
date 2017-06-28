package com.wxzd.efcs.equipment.application.service.impl;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.equipment.application.dtos.EquipmentExtensionDto;
import com.wxzd.efcs.equipment.application.queryService.EquipmentExtensionQueryService;
import com.wxzd.efcs.equipment.application.queryService.EquipmentQueryService;
import com.wxzd.efcs.equipment.application.querys.EquipmentExQuery;
import com.wxzd.efcs.equipment.application.service.EquipmentAppService;
import com.wxzd.efcs.equipment.domain.entities.EquipmentExtension;
import com.wxzd.efcs.equipment.domain.service.EquipmentExtensionService;
import com.wxzd.efcs.equipment.domain.service.EquipmentService;
import com.wxzd.efcs.report.application.dtos.EquipErrorDesc;
import com.wxzd.efcs.report.application.querys.enums.DashBoardCountType;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by LYK on 2017/4/16
 */
@Service
public class EquipmentAppServiceImpl implements EquipmentAppService {

    @Resource
    private EquipmentQueryService queryService;

    @Resource
    private EquipmentService service;

    @Resource
    private EquipmentExtensionQueryService extensionQueryService;

    @Resource
    private EquipmentExtensionService equipmentExtensionService;

    @Autowired
    private DeviceRealtimeInfoService deviceRealtimeInfoService;

    @Override
    public EquipmentDto getById(UUID id) {
        EquipmentDto equipmentDto = service.getById(id);
        equipmentDto.setEquipmentExtension(extensionQueryService.queryByEquipId(id));
        return equipmentDto;
    }

    @Override
    public PageResult<EquipmentDto> getAllEquipmentsPaged(EquipmentExQuery exQuery) {
        PageResult<EquipmentDto> pageResult = queryService.getAllEquipmentsPaged(exQuery);
        List<EquipmentDto> list = pageResult.getRows();
        for (EquipmentDto dto : list) {
            HashMap<Integer, String> map = deviceRealtimeInfoService.getDtsChannelStatusInfo(dto.getHouse_no()).getDeviceStatus(dto.getEquip_no()).getChannelEvent();
            dto.setChannelEvent(map);
        }
        pageResult.setRows(list);
        return pageResult;
    }

    @Override
    public ListResult<EquipmentDto> getAllEquipments(EquipmentExQuery exQuery) {
        return queryService.getAllEquipments(exQuery);
    }

    @Override
    public GaiaResult addOrUpdateEquipment(EquipmentDto equipmentDto) {
        GaiaResult result = service.equipmentCheck(equipmentDto.getId(), equipmentDto.getEquip_name(), equipmentDto.getEquip_no());
        if (result.isSuccess()) {
            result = service.addOrUpdateEquipment(equipmentDto);
        }
        return result;
    }

    @Override
    public List<String> getAllEquipVender() {
        List<Map<String, Object>> list = queryService.getAllEquipVender();
        List<String> stringList = new ArrayList<>();
        if (list != null) {
            for (Map<String, Object> map : list) {
                if (map.get("equip_vender") != null)
                    stringList.add(map.get("equip_vender").toString());
            }
        }
        return stringList;
    }

    @Override
    public List<String> getAllHouseNo() {
        List<Map<String, Object>> list = queryService.getAllHouseNo();
        List<String> stringList = new ArrayList<>();
        if (list != null) {
            for (Map<String, Object> map : list) {
                if (map.get("house_no") != null)
                    stringList.add(map.get("house_no").toString());
            }
        }
        return stringList;
    }


    @Override
    public GaiaResult addOrUpdateEquipmentExtension(EquipmentExtensionDto dto) {
        if (dto.getEquip_id() != null) {
            GaiaResult result = equipmentExtensionService.extensionCheck(dto.getEquip_id(), dto.getExt_key());
            if (result.isSuccess()) result = equipmentExtensionService.addOrUpdateEquipmentExtension(dto);
            return result;
        }
        return GaiaResultFactory.getError("设备ID为空!");
    }

    @Override
    public GaiaResult deleteEquipmentExtension(UUID id) {
        return equipmentExtensionService.deleteEquipmentExtensionById(id);
    }


    @Override
    public AlarmInfoDto getEquipErrorDesc(UUID houseId, UUID equipId) {
        return queryService.getEquipErrorDesc(houseId, equipId);
    }
}
