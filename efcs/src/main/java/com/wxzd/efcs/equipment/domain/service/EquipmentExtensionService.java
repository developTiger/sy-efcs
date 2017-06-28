package com.wxzd.efcs.equipment.domain.service;

import com.wxzd.efcs.equipment.application.dtos.EquipmentExtensionDto;
import com.wxzd.efcs.equipment.domain.entities.EquipmentExtension;
import com.wxzd.gaia.common.base.core.result.GaiaResult;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/17
 */
public interface EquipmentExtensionService {


    /**
     * 根据Id获取信息
     *
     * @param id
     * @return
     */
    EquipmentExtensionDto queryById(UUID id);

    /**
     * 添加设备扩展信息
     *
     * @param dto
     * @return
     */
    GaiaResult addOrUpdateEquipmentExtension(EquipmentExtensionDto dto);


    /**
     * 扩展信息检测
     *
     * @param equipId
     * @param extKey
     * @return
     */
    GaiaResult extensionCheck(UUID equipId, String extKey);


    /**
     * 删除扩展信息
     * @param id
     * @return
     */
    GaiaResult deleteEquipmentExtensionById(UUID id);






}
