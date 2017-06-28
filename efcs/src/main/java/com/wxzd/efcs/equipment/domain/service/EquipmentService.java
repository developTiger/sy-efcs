package com.wxzd.efcs.equipment.domain.service;

import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.gaia.common.base.core.result.GaiaResult;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/16
 */
public interface EquipmentService {


    /**
     * 根据id获取设备信息
     *
     * @param id
     * @return
     */
    EquipmentDto getById(UUID id);


    /**
     * 新增/修改
     *
     * @param equipmentDto
     * @return
     */
    GaiaResult addOrUpdateEquipment(EquipmentDto equipmentDto);


    /**
     * 检测重复
     * @param id
     * @param equip_name
     * @param equip_no
     * @return
     */
    GaiaResult equipmentCheck(UUID id,String equip_name,String equip_no);


}
