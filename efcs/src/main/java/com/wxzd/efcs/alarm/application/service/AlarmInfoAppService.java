package com.wxzd.efcs.alarm.application.service;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.alarm.application.querys.AlarmInfoExQuery;
import com.wxzd.efcs.alarm.domain.events.AlarmEvent;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
public interface AlarmInfoAppService {

    /**
     * 获取异常信息列表分页
     *
     * @param exQuery
     * @return
     */
    PageResult<AlarmInfoDto> getAlarmInfoPaged(AlarmInfoExQuery exQuery);


    /**
     * 获取异常信息列表无分页
     *
     * @param exQuery
     * @return
     */
    List<AlarmInfoDto> getAlarmInfoList(AlarmInfoExQuery exQuery);


    /**
     * 查询展示异常信息(按异常级别和时间排序)
     *
     * @return
     */
    List<AlarmInfoDto> getAlarmInfoSortList();


    /**
     * 根据配置推送数量--获取异常信息（最新推送无视处理）
     *
     * @return
     */
    List<AlarmInfoDto> getAlarmInfoWithPushConfig();


    /**
     * 根据前端接受信息后的返回状态，新增用户拉取信息
     */
    GaiaResult updateAlarmReadStamp(Boolean state);


    /**
     * 根据异常ID获取信息
     *
     * @param id
     * @return
     */
    ObjectResult<AlarmInfoDto> getById(UUID id);


    /**
     * 根据库位号获取异常信息
     * @param locId
     * @return
     */
    ObjectResult<AlarmInfoDto> getByLocId(UUID locId);

    /**
     * 新增异常记录
     * @return
     */
    GaiaResult saveAlarmInfo(AlarmInfoDto alarmInfoDto);

    /**
     * 处理AlarmEvent事件，增加一条异常信息
     * @param event
     */
    void dealAlarmEvent(AlarmEvent event);
}
