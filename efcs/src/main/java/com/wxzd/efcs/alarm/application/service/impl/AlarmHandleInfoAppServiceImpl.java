package com.wxzd.efcs.alarm.application.service.impl;

import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.queryService.AlarmHandleInfoExQueryService;
import com.wxzd.efcs.alarm.application.querys.AlarmHandleInfoExQuery;
import com.wxzd.efcs.alarm.application.service.AlarmHandleInfoAppService;
import com.wxzd.efcs.alarm.domain.entities.AlarmHandleInfo;
import com.wxzd.efcs.alarm.domain.service.AlarmHandleInfoService;
import com.wxzd.efcs.alarm.domain.service.AlarmInfoService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
@Service
public class AlarmHandleInfoAppServiceImpl implements AlarmHandleInfoAppService {

    @Resource
    private AlarmHandleInfoExQueryService handleInfoExQueryService;

    @Resource
    private AlarmHandleInfoService handleInfoService;

    @Resource
    private AlarmInfoService infoService;

    /**
     * 根据报警id获取消警记录
     *
     * @param exQuery
     */
    @Override
    public List<AlarmHandleInfoDto> getAlarmHandleInfoList(AlarmHandleInfoExQuery exQuery) {
        return handleInfoExQueryService.getAlarmHandleInfoList(exQuery);
    }

    /**
     * 根据报警id获取消警记录分页
     *
     * @param exQuery
     */
    @Override
    public PageResult<AlarmHandleInfoDto> getAlarmHandleInfoPaged(AlarmHandleInfoExQuery exQuery) {
        return handleInfoExQueryService.getAlarmHandleInfoPaged(exQuery);
    }

    /**
     * 根据ID获取消警记录
     *
     * @param id
     * @return
     */
    @Override
    public ObjectResult<AlarmHandleInfoDto> getById(UUID id) {
        AlarmHandleInfo alarmHandleInfo = handleInfoService.getById(id).getObject();
        if (alarmHandleInfo != null)
            return GaiaResultFactory.getObject(BeanUtl.copyProperties(alarmHandleInfo, AlarmHandleInfoDto.class));
        return null;
    }


    /**
     * 保存消警记录
     *
     * @param info
     * @return
     */
    @Override
    public GaiaResult saveAlarmHandleInfo(AlarmHandleInfo info) {
        GaiaResult result = handleInfoService.saveAlarmHandleInfo(info);
        if (result.isSuccess()) {
            infoService.updateAlarmInfoisHandled(info.getAlarm_id());
        }
        return result;
    }
}
