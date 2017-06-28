package com.wxzd.efcs.alarm.application.service;

import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.querys.AlarmHandleInfoExQuery;
import com.wxzd.efcs.alarm.domain.entities.AlarmHandleInfo;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 消警记录
 */
public interface AlarmHandleInfoAppService {

    /**
     * 根据报警id获取消警记录
     */
    List<AlarmHandleInfoDto> getAlarmHandleInfoList(AlarmHandleInfoExQuery exQuery);


    /**
     * 根据报警id获取消警记录分页
     */
    PageResult<AlarmHandleInfoDto> getAlarmHandleInfoPaged(AlarmHandleInfoExQuery exQuery);


    /**
     * 根据ID获取消警记录
     *
     * @param id
     * @return
     */
    ObjectResult<AlarmHandleInfoDto> getById(UUID id);


    /**
     * 保存消警记录
     *
     * @param info
     * @return
     */
    GaiaResult saveAlarmHandleInfo(AlarmHandleInfo info);


}
