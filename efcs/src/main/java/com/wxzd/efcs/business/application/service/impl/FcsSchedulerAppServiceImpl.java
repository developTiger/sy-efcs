package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.FcsSchedulerDto;
import com.wxzd.efcs.business.application.queryService.FcsSchedulerExQueryService;
import com.wxzd.efcs.business.application.querys.FcsSchedulerExQuery;
import com.wxzd.efcs.business.application.service.FcsSchedulerAppService;
import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/5/18
 */
@Service
public class FcsSchedulerAppServiceImpl implements FcsSchedulerAppService {

    @Resource
    private FcsSchedulerExQueryService fcsSchedulerExQueryService;

    @Resource
    private FcsSchedulerService fcsSchedulerService;


    /**
     * 根绝ID 查询调度信息
     *
     * @param id
     * @return
     */
    @Override
    public FcsSchedulerDto getSchedulerById(UUID id) {
        FcsScheduler fcsScheduler = fcsSchedulerService.getById(id);
        if (fcsScheduler != null)
            return BeanUtl.copyProperties(fcsScheduler, FcsSchedulerDto.class);
        return null;
    }

    /**
     * 分页查询调度信息
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FcsSchedulerDto> getSchedulerPaged(FcsSchedulerExQuery exQuery) {
        return fcsSchedulerExQueryService.getSchedulerPaged(exQuery);
    }

    /**
     * 查询调度信息不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FcsSchedulerDto> getSchedulerNoPaged(FcsSchedulerExQuery exQuery) {
        return fcsSchedulerExQueryService.getSchedulerNoPaged(exQuery);
    }

    /**
     * 设置计划状态
     *
     * @param id
     * @param schedulerStatus
     * @return
     */
    @Override
    public GaiaResult setSchedulerStatus(UUID id, SchedulerStatus schedulerStatus) {
        return fcsSchedulerService.setSchedulerStatus(id, schedulerStatus);
    }
}
