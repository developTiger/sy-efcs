package com.wxzd.efcs.business.controller;

import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.FmPalletizeDto;
import com.wxzd.efcs.business.application.querys.FmPalletizeExQuery;
import com.wxzd.efcs.business.application.service.FmPalletizeAppService;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *
 * 组盘单
 * Created by jade on 2017/4/27.
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class PalletizeListController {

    @Resource
    private FmPalletizeAppService palletizeAppService;

    @RequestMapping
    public void index_view(){

    }

    @RequestMapping
    @ResponseBody
    public PageResult<FmPalletizeDto> grid(FmPalletizeExQuery fmPalletizeExQuery){
        return palletizeAppService.getFmPalletizePaged(fmPalletizeExQuery);
    }

}
