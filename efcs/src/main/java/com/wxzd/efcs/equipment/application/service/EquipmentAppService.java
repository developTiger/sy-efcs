package com.wxzd.efcs.equipment.application.service;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.equipment.application.dtos.EquipmentExtensionDto;
import com.wxzd.efcs.equipment.application.querys.EquipmentExQuery;
import com.wxzd.efcs.report.application.dtos.EquipErrorDesc;
import com.wxzd.efcs.report.application.querys.enums.DashBoardCountType;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/16
 */
public interface EquipmentAppService {


    /**
     * 根据id获取设备信息
     *
     * @param id
     * @return
     */
    EquipmentDto getById(UUID id);


    //TODO  测试



    //TODO  设置关联排

    /**
     * 分页查询设备信息
     *
     * @param exQuery
     * @return
     */
    PageResult<EquipmentDto> getAllEquipmentsPaged(EquipmentExQuery exQuery);

    /**
     * 无分页查询设备信息
     *
     * @param exQuery
     * @return
     */
    ListResult<EquipmentDto> getAllEquipments(EquipmentExQuery exQuery);

    /**
     * 新增/修改
     *
     * @param equipmentDto
     * @return
     */
    GaiaResult addOrUpdateEquipment(EquipmentDto equipmentDto);

    /**
     * 获取所有厂家信息
     *
     * @return
     */
    List<String> getAllEquipVender();

    /**
     * 获取所有拉线
     *
     * @return
     */
    List<String> getAllHouseNo();

    /**
     * 新增/修改扩展信息
     *
     * @param dto
     * @return
     */
    GaiaResult addOrUpdateEquipmentExtension(EquipmentExtensionDto dto);

    /**
     * 删除扩展信息
     * @param id
     * @return
     */
    GaiaResult deleteEquipmentExtension(UUID id);

    /**
     * 拉线 - 化程段（测试段）- 设备异常信息
     * house_id   type
     */
    AlarmInfoDto getEquipErrorDesc(UUID houseId, UUID equipId);


}



