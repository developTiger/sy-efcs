package com.wxzd.efcs.alarm.domain.service;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.alarm.domain.entities.AlarmInfo;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 异常信息
 */
public interface AlarmInfoService {

    /**
     * 根据报警ID设置已处理状态
     * @param id
     * @return
     */
    GaiaResult  updateAlarmInfoisHandled(UUID id);


    /**
     * 根据ID获取异常信息
     * @param id
     * @return
     */
    ObjectResult<AlarmInfo> getById(UUID id);


    /**
     * 新增异常信息
     * @param alarmInfo
     * @return
     */
    GaiaResult savegetAlarmInfoList(AlarmInfo alarmInfo);

}
