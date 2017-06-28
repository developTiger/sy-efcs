package com.wxzd.efcs.business.controller;

import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.FmBatteryDto;
import com.wxzd.efcs.business.application.querys.FmBatteryExQuery;
import com.wxzd.efcs.business.application.service.FmBatteryAppService;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 *  电池入库单
 *
 * Created by jade on 2017/4/27.
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class BatteryInListController {

    @Resource
    private FmBatteryAppService fmBatteryAppService;

    @RequestMapping
    public void index_view(){

    }

    @RequestMapping
    @ResponseBody
    public PageResult<FmBatteryDto> grid(FmBatteryExQuery fmBatteryExQuery){
        return fmBatteryAppService.getFmBatteryInPaged(fmBatteryExQuery);
    }

    @RequestMapping
    public void detail_view(UUID id,Model model){
        FmBatteryDto fmBatteryDto = fmBatteryAppService.getFmBatteryInById(id);
        model.addAttribute("FmBatteryDto",fmBatteryDto);
    }
}
