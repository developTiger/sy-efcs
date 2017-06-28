package com.wxzd.efcs.business.controller;

import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.FmPalletSplitDto;
import com.wxzd.efcs.business.application.querys.FmPalletSplitExQuery;
import com.wxzd.efcs.business.application.service.FmPalletSplitAppService;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 拆盘单
 * Created by jade on 2017/4/27.
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class SplitListController {

    @Resource
    private FmPalletSplitAppService fmPalletSplitAppService;

    @RequestMapping
    public void index_view(){

    }

    @RequestMapping
    @ResponseBody
    public PageResult<FmPalletSplitDto> grid(FmPalletSplitExQuery fmPalletSplitExQuery){
        PageResult<FmPalletSplitDto> fmPalletSplitPaged = fmPalletSplitAppService.getFmPalletSplitPaged(fmPalletSplitExQuery);
        return fmPalletSplitPaged;
    }
}
