package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerDto;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerLocationDto;
import com.wxzd.efcs.business.application.dtos.ToolSchedulerDto;
import com.wxzd.efcs.business.application.querys.ToolSchedulerExQuery;
import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/29.
 */
public interface ToolSchedulerAppService {

//    /**
//     按标识获取工装计划
//     * @param scheduleId
//     * @return
//     */
//    ToolSchedulerDto getSchedulerByid(UUID scheduleId);
//
//    /**
//     * 新建工装任务计划
//     * @param dto
//     * @return
//     */
//    GaiaResult addToolScheduler(ToolSchedulerDto dto,List<UUID> locationId);
//
//    /**
//     * 更改计划状态
//     * @param scheduleId
//     * @param schedulerStatus
//     * @return
//     */
//    GaiaResult updateSchedulerStatus(UUID scheduleId , SchedulerStatus schedulerStatus);
//
//    /**
//     * 删除任务计划
//     * @param scheduleId
//     * @return
//     */
//    GaiaResult deleteScheduler(UUID scheduleId);
//
//    /**
//     * 重新设置计划库位
//     * @param schedulerId
//     * @param locationIds
//     * @return
//     */
//    GaiaResult resetSchedulerLocations(UUID schedulerId, List<UUID> locationIds);
//
//    /**
//     * 获取计划
//     * @return
//     */
//    List<FcsToolSchedulerDto> getAllScheduler(ToolSchedulerExQuery query);
//
//
//    /**
//     * 分页获取计划
//     * @param query
//     * @return
//     */
//    PageResult<FcsToolSchedulerDto> getSchedulerPaged(ToolSchedulerExQuery query);
//
//    /**
//     *
//     * @param scheduleId
//     * @return
//     */
//    List<FcsToolSchedulerLocationDto> getSchedulerLoations(UUID scheduleId);
//
//    /**
//     *
//     * @param scheduleId
//     * @return
//     */
//    PageResult<FcsToolSchedulerLocationDto> getSchedulerLoationsPaged(UUID scheduleId);


}
