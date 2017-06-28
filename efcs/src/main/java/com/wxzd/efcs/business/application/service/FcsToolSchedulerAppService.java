package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerDto;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerLocationDto;
import com.wxzd.efcs.business.application.querys.FcsToolSchedulerExQuery;
import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.wms.core.domain.entities.StorageLocation;

import java.util.List;
import java.util.UUID;

/**
 * 化成工装Application对外调用接口
 * #Description
 * 提供外部实时获取需工装集合，接收工装完成处理后续事物，取消读写分离结构。
 * #Description
 *  Created by zhaowy on 2017/6/16.
 */
public interface FcsToolSchedulerAppService {

    // region 工装调度接口

    /**
     * 获取目前所有在执行的计划
     * @return
     */
    ListResult<FcsToolSchedulerDto> getLiveToolSchedules();

    /**
     * 获取目前所有需要执行的工装单
     * @return
     */
    ListResult<FcsToolSchedulerLocationDto> getLiveToolSchedulerLocations(String scheduleNo);

    //endregion

    // region   start fetch
    /**
     * 按标识获取工装计划
     * @param id
     * @return
     */
    ObjectResult<FcsToolSchedulerDto> getById(UUID id);

    /**
     按调度计划编号获取调度计划
     * @param scheduleNo
     * @return
     */
    ObjectResult<FcsToolSchedulerDto> getByNo(String scheduleNo);


    /**
     按照指定条件组合查询ToolSchedule
     * @param query 工装计划查询对象
     * @return
     */
    ListResult<FcsToolSchedulerDto> findToolSchedules(FcsToolSchedulerExQuery query);

    /**
     * 按照条件分页获取ToolSchedule
     * @param query 工装计划查询对象
     * @return
     */
    PageResult<FcsToolSchedulerDto> findToolSchedulesPaged(FcsToolSchedulerExQuery query);


    /**
     * 按工单标识获取绑定的工装单
     * @param locationId 工单标识
     * @return
     */
    ObjectResult<FcsToolSchedulerLocationDto> getScheduleLocation(UUID locationId);

    /**
     * 按计划标识获取工装单
     * @param sc_id 计划标识
     * @return
     */
    ListResult<FcsToolSchedulerLocationDto> getScheduleLocations(UUID sc_id);


    /**
     * 按计划编号获取工装单
     * @param sc_no 计划编号
     * @return
     */
    ListResult<FcsToolSchedulerLocationDto> getScheduleLocations(String sc_no);

    /**
     * 按工装单状态获取工装单
     * @param scId 计划标识
     * @param status 工装单状态
     * @return
     */
    ListResult<FcsToolSchedulerLocationDto> getScheduleLocationsByStatus(UUID scId,AppointmentStatus status);


    //endregion end fetch

    //region start action

    /**
     * 新增计划
     *
     * @param scheduler
     * @param locationIds
     * @return
     */
    GaiaResult addToolScheduler(FcsToolSchedulerDto scheduler, List<UUID> locationIds);

    /**
     重置已存在计划工作单
     * @param scheduler
     * @param locationIds
     * @return
     */
    GaiaResult resetScheduleLocation(FcsToolSchedulerDto scheduler,List<UUID> locationIds);

    /**
     * 停止任务
     *
     * @param schedulerNo
     * @return
     */

    GaiaResult stopScheduler(String schedulerNo);

    /**
     * 启动任务
     *
     * @param schedulerNo
     * @return
     */

    GaiaResult startScheduler(String schedulerNo);

    /**
     * 删除计划
     *
     * @param schedulerId
     * @return
     */
    GaiaResult deleteScheduler(UUID schedulerId);

//    /**
//     * 更新计划状态
//     *
//     * @param schedulerId
//     * @param schedulerStatus
//     * @return
//     */
//    GaiaResult updateSchedulerStatus(UUID schedulerId, SchedulerStatus schedulerStatus);


    //endregion end action

    //region  start check
    /**
     * 计划是否已结束
     *
     * @param schedulerNo
     * @return
     */
    GaiaResult isSchedulerComplete(String schedulerNo);
    //endregion end check
}
