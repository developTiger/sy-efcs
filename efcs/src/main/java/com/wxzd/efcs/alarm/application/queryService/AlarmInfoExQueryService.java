package com.wxzd.efcs.alarm.application.queryService;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.alarm.application.querys.AlarmInfoExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 异常信息
 */
public interface AlarmInfoExQueryService {

    /**
     * 获取异常信息列表分页
     * @param exQuery
     * @return
     */
    PageResult<AlarmInfoDto> getAlarmInfoPaged(AlarmInfoExQuery exQuery);


    /**
     * 获取异常信息列表无分页
     * @param exQuery
     * @return
     */
    List<AlarmInfoDto> getAlarmInfoList(AlarmInfoExQuery exQuery);



    /**
     * 查询展示异常信息(按异常级别和时间排序)
     */
    List<AlarmInfoDto> getAlarmInfoSortList(Date lastRefreshTime);




    /**
     * 根据配置推送数量--获取异常信息（最新推送且未被处理）
     * @return
     */
    List<AlarmInfoDto> getAlarmInfoWithPushConfig(Integer amount);


    /**
     * 根据ID获取异常信息
     * @param id
     * @return
     */
    AlarmInfoDto getAlarmInfoById(UUID id);


    /**
     * 根据ID获取异常信息
     * @return
     */
    AlarmInfoDto getAlarmInfoByLocId(UUID LocId);



    /**
     * 根据house_no获取house_id
     */
    UUID getHouseIdByNo(String houseNo);

}
