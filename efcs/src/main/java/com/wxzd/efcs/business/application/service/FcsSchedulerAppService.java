package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.FcsSchedulerDto;
import com.wxzd.efcs.business.application.querys.FcsSchedulerExQuery;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/5/18
 * 计划任务
 */
public interface FcsSchedulerAppService {

    /**
     * 根绝ID 查询调度信息
     * @param id
     * @return
     */
    FcsSchedulerDto getSchedulerById(UUID id);


    /**
     * 分页查询调度信息
     *
     * @param exQuery
     * @return
     */
    PageResult<FcsSchedulerDto> getSchedulerPaged(FcsSchedulerExQuery exQuery);


    /**
     * 查询调度信息不分页
     *
     * @param exQuery
     * @return
     */
    List<FcsSchedulerDto> getSchedulerNoPaged(FcsSchedulerExQuery exQuery);


    /**
     * 设置计划状态
     *
     * @param id
     * @param schedulerStatus
     * @return
     */
    GaiaResult setSchedulerStatus(UUID id, SchedulerStatus schedulerStatus);
}
