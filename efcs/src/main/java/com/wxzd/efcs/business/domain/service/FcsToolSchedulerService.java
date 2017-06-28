package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.wms.core.domain.entities.StorageLocation;

import java.util.List;
import java.util.UUID;

/**
 * 化成工装调用接口
 * #Description
 * 提供外部实时获取需工装集合，接收工装完成处理后续事物。
 * #Description
 *  Created by zhouzh on 2017/5/29.
 */
public interface FcsToolSchedulerService {

    // region 工装调度接口

    /**
     根据状态获取工装计划
     * @param status
     * @return
     */
    List<FcsToolScheduler> getSchedulesByStatus(SchedulerStatus status);

    /**
     根据计划标识获取需处理工单
     * @param scheduleId
     * @param status
     * @return
     */
    List<FcsToolSchedulerLocations> getSchedulesByStatus(UUID scheduleId, AppointmentStatus status);

    /**
     根据计划编号获取需处理工单
     * @param scheduleNo
     * @param status
     * @return
     */
    List<FcsToolSchedulerLocations> getSchedulesByStatus(String scheduleNo, AppointmentStatus status);


    /**
     * 获取目前所有需要执行的计划
     * @return
     */
    List<FcsToolScheduler> getLiveSchedules();

    /**
     * 获取目前所有需要执行的工装单
     * @return
     */
    List<FcsToolSchedulerLocations> getLiveLocations(UUID scheduleId);

    /**
     * 获取目前所有需要执行的工装单
     * @return
     */
    List<FcsToolSchedulerLocations> getLiveLocations(String scheduleNo);


    /**
     * 调度设置当前工装所在位置
     * @param houseNo
     * @param toolNo
     * @param currentLocationNo
     * @return
     */
    ObjectResult<Boolean> setToolCurrentLocation(String houseNo,String toolNo,String currentLocationNo);


    /**
     * 根据组合条件返回设置工装单完成
     * @param houseNo 库号
     * @param toolNo 工装编号（拖盘）
     * @param locationNo 库位号
     * @return 返回是否需要继续执行，如果当前Location对应的Schedule已经全部完成表示不需要继续执行，
     */
    ObjectResult<Boolean> setLocationWorkDoneNeedContinue(String houseNo,String toolNo,String locationNo);

    /**
     按照工单标识获取工单信息
     * @param scheduleLocationId
     * @return
     */
    FcsToolSchedulerLocations getLocation(UUID scheduleLocationId);


    /**
        根据组合条件获取工装单
     * @param houseNo 库号
     * @param toolNo 工装编号（拖盘）
     * @param locationNo 库位号
     * @return
     */
    ObjectResult<FcsToolSchedulerLocations> getLocation(String houseNo,String toolNo,String locationNo);

    //endregion

    // region   start fetch
    /**
     * 按标识获取工装计划
     * @param id
     * @return
     */
    FcsToolScheduler getById(UUID id);


    /**
     按调度计划编号获取调度计划
     * @param scheduleNo
     * @return
     */
    FcsToolScheduler getByNo(String scheduleNo);


    //endregion end fetch

    //region start action
    /**
     * 新增计划
     *
     * @param scheduler
     * @param locationIds
     * @return
     */
    Boolean createToolScheduler(FcsToolScheduler scheduler, List<UUID> locationIds);


    /**
     * 新增计划
     *
     * @param schedulerName
     * @param houseNo
     * @param toolNo
     * @param schedulerType
     * @return
     */
    Boolean checkToolScheduler(String schedulerName, String houseNo, String toolNo, SchedulerType schedulerType);



    /**
     * 重置计划
     *
     * @param scheduler
     * @param locationIds
     * @return
     */
    Boolean resetToolScheduler(FcsToolScheduler scheduler, List<UUID> locationIds);


    /**
     * 绑定计划工单
     * @param scheduler
     * @param locationList
     * @return
     */
    Boolean bindToolSchedulerLocations(FcsToolScheduler scheduler, List<FcsToolSchedulerLocations> locationList);

//    /**
//     * 移除计划工单
//     * @param scId
//     * @param locationId
//     * @return
//     */
//    Boolean removeLocations(UUID scId,List<UUID> locationId);


    /**
     * 移除计划下所有工单
     * @param scheduleNo
     * @param description 移除原因
     * @return
     */
    Boolean removeAllLocations(String scheduleNo,String description);

    /**
     * 停止任务
     * @param schedulerNo 计划编号
     * @param description 停止原因
     * @return
     */
    Boolean stopScheduler(String schedulerNo,String description);

    /**
     * 启动任务
     * @param schedulerNo 计划编号
     * @return
     */
    Boolean startScheduler(String schedulerNo);

    /**
     * 删除计划
     *
     * @param schedulerId
     * @return
     */
    Boolean deleteScheduler(UUID schedulerId);


    /**
     * 更新计划状态
     *
     * @param schedulerId
     * @param schedulerStatus
     * @return
     */
    Boolean updateSchedulerStatus(UUID schedulerId, SchedulerStatus schedulerStatus);

    //endregion end action

    //region  start check
    /**
     * 计划是否已结束
     *
     * @param schedulerNo
     * @return
     */
    Boolean isSchedulerComplete(String schedulerNo);

    /**
     * 校验当前计划是否存在在执行及已预约的工单
     * @param schedulerNo
     * @return
     */
    Boolean isSchedulerHasRunningLocations(String schedulerNo);

    //endregion end check
}
