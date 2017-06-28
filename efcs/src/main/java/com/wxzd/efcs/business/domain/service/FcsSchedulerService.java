package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.List;
import java.util.UUID;

/**
 * 计划任务
 * <p>
 * Created by zhouzh on 2017/4/18.
 */
public interface FcsSchedulerService {

    /**
     * 根据id获取 计划信息
     *
     * @param id 主键id
     * @return ObjectResult<FcsScheduler>
     */
    FcsScheduler getById(UUID id);


    /**
     * 新增计划
     *
     * @param fcsScheduler
     * @return
     */
    GaiaResult saveScheduler(FcsScheduler fcsScheduler);


    /**
     * 设置计划状态
     *
     * @param id
     * @param schedulerStatus
     * @return
     */
    GaiaResult setSchedulerStatus(UUID id, SchedulerStatus schedulerStatus);

    /**
     * 设置计划状态
     *
     * @param fcsScheduler
     * @param schedulerStatus
     * @return
     */
    GaiaResult setSchedulerStatus(FcsScheduler fcsScheduler, SchedulerStatus schedulerStatus);


    ;

    /**
     * 请求次数加1
     */
    GaiaResult addRunTimes(UUID id);

    ;

    /**
     * 请求次数加1
     */
    GaiaResult addRunTimes(FcsScheduler fcsScheduler);

    /**
     * 异常
     * 连续异常一定的次数 则结束任务计划
     */
    GaiaResult setSchedulerError(UUID id, EfcsErrorCode error_code, String error_message);

    /**
     * 异常
     * 连续异常一定的次数 则结束任务计划
     */
    GaiaResult setSchedulerError(FcsScheduler fcsScheduler, EfcsErrorCode error_code, String error_message);

    /**
     * 获取活动状态的任务计划
     * 状态为runing 的任务
     */
    List<FcsScheduler> getLiveScheduler();

    /**
     * 根据设备号 获取任务
     *
     * @param house_id
     * @param device_no
     * @return
     */
    List<FcsScheduler> getLiveSchedulerByDevicce(UUID house_id, String device_no);

    GaiaResult closeALlSchedulerByDevice(String houseNo, String device_no);

    GaiaResult closeALlSchedulerByDevice(UUID house_id, String device_no);


}
