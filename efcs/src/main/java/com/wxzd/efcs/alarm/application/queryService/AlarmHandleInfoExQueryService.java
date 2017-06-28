package com.wxzd.efcs.alarm.application.queryService;

import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.querys.AlarmHandleInfoExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22.
 * 消警记录
 */
public interface AlarmHandleInfoExQueryService {

    /**
     * 根据报警id获取消警记录
     */
    List<AlarmHandleInfoDto> getAlarmHandleInfoList(AlarmHandleInfoExQuery exQuery);


    /**
     * 根据报警id获取消警记录分页
     */
    PageResult<AlarmHandleInfoDto> getAlarmHandleInfoPaged(AlarmHandleInfoExQuery exQuery);





}
