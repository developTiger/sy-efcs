package com.wxzd.efcs.equipment.application.queryService;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.equipment.application.querys.EquipmentExQuery;
import com.wxzd.efcs.report.application.dtos.EquipErrorDesc;
import com.wxzd.efcs.report.application.querys.enums.DashBoardCountType;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/16
 */
public interface EquipmentQueryService {

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
     * 获取所有厂家信息
     * @return
     */
    List<Map<String, Object>> getAllEquipVender();


    /**
     * 获取所有拉线
     * @return
     */
    List<Map<String, Object>> getAllHouseNo();


    /**
     * 拉线 - 化程段（测试段）- 设备异常信息
     * house_id   type
     */
    AlarmInfoDto getEquipErrorDesc(UUID houseId, UUID equipId);



}
