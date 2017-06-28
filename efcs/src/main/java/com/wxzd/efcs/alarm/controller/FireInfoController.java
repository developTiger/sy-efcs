package com.wxzd.efcs.alarm.controller;

import com.wxzd.efcs.alarm.ModuleEfcsAlarm;
import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.dtos.FireAlarmInfoDto;
import com.wxzd.efcs.alarm.application.querys.FireAlarmInfoExQuery;
import com.wxzd.efcs.alarm.application.service.FireAlarmInfoAppService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 火警信息查询
 * Created by jade on 2017/4/22.
 */
@Module(ModuleEfcsAlarm.name)
@Controller
@RequestMapping
public class FireInfoController {

    @Resource
    private FireAlarmInfoAppService fireAlarmInfoAppService;

    @RequestMapping
    public void index_view() {

    }

    @RequestMapping
    @ResponseBody
    public PageResult<FireAlarmInfoDto> grid(FireAlarmInfoExQuery exQuery) {
       return fireAlarmInfoAppService.getFireAlarmInfoPaged(exQuery);
    }

    /**
     * 获取详情
     * @param id
     * @param model
     */
    @RequestMapping
    public void detail_view(UUID id, Model model) {
        FireAlarmInfoDto fireAlarmInfoDto = fireAlarmInfoAppService.getFireAlarmInfoDtoList(id);
        model.addAttribute("fireAlarmInfoDto", fireAlarmInfoDto);
    }

    @RequestMapping
    @ResponseBody
    public GaiaResult handle(AlarmHandleInfoDto dto) {
        return fireAlarmInfoAppService.saveAlarmHandleInfo(dto);
    }

}
