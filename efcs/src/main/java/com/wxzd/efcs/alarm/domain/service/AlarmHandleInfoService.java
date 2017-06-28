package com.wxzd.efcs.alarm.domain.service;

import com.wxzd.efcs.alarm.domain.entities.AlarmHandleInfo;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
public interface AlarmHandleInfoService {

    /**
     * 根据异常ID获取消警记录
     * @param id
     * @return
     */
    ObjectResult<AlarmHandleInfo> getById(UUID id);




    /**
     * 保存消警记录
     * @param info
     * @return
     */
    GaiaResult saveAlarmHandleInfo(AlarmHandleInfo info);





}
