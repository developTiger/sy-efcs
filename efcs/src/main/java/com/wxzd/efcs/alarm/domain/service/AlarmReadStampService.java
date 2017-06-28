package com.wxzd.efcs.alarm.domain.service;

import com.wxzd.efcs.alarm.domain.entities.AlarmReadStamp;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/22.
 * 用户拉取记录
 */
public interface AlarmReadStampService {


    /**
     * 获取用户最新一次拉取记录
     *
     * @param userId
     * @return
     */
    ObjectResult<AlarmReadStamp> getByUserId(UUID userId);


    /**
     * 新增用户拉取记录
     * @return
     */
    GaiaResult saveAlarmReadStamp(AlarmReadStamp alarmReadStamp);


}
