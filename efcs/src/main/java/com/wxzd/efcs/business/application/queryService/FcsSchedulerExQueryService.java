package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.FcsSchedulerDto;
import com.wxzd.efcs.business.application.querys.FcsSchedulerExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;

/**
 * Created by LYK on 2017/5/18
 * 计划任务
 */
public interface FcsSchedulerExQueryService {

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


}
