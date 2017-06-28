package com.wxzd.efcs.business.controller;

import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.FcsSchedulerDto;
import com.wxzd.efcs.business.application.querys.FcsSchedulerExQuery;
import com.wxzd.efcs.business.application.service.FcsSchedulerAppService;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by jade on 2017/5/18.
 * 任务计划
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class SchedulerController {
    @Resource
    private FcsSchedulerAppService fcsSchedulerAppService;

    @RequestMapping
    public void index_view(){


    }

    @RequestMapping
    @ResponseBody
    public PageResult<FcsSchedulerDto> grid(FcsSchedulerExQuery exQuery){
        return fcsSchedulerAppService.getSchedulerPaged(exQuery);
    }

    @RequestMapping
    @ResponseBody
    public GaiaResult setSchedulerStatus(UUID id, SchedulerStatus schedulerStatus){
        return fcsSchedulerAppService.setSchedulerStatus(id,schedulerStatus);
    }

}
