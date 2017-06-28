package com.wxzd.efcs.business.controller;

import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.FmProcedureDto;
import com.wxzd.efcs.business.application.querys.FmProcedureExQuery;
import com.wxzd.efcs.business.application.service.FmProcedureAppService;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *
 * 工序执行单
 * Created by jade on 2017/4/27.
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class WorkProcedureListController {

    @Resource
    private FmProcedureAppService fmProcedureAppService;

    @RequestMapping
    public void index_view(){

    }

    @RequestMapping
    @ResponseBody
    public PageResult<FmProcedureDto> grid(FmProcedureExQuery fmProcedureExQuery){
        return fmProcedureAppService.getFmProcedurePaged(fmProcedureExQuery);
    }
}
