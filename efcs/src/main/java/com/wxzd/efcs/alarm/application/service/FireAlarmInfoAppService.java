package com.wxzd.efcs.alarm.application.service;

import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.dtos.FireAlarmInfoDto;
import com.wxzd.efcs.alarm.application.dtos.LocationChartTemplate;
import com.wxzd.efcs.alarm.application.querys.FireAlarmInfoExQuery;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.protocol.dts.TemperatureReport;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 火警
 */
public interface FireAlarmInfoAppService {


    /**
     * 获取库信息(常温有温度)
     *
     * @param
     */
    List<LocationChartTemplate> getNormalLocationInfos(UUID houseId, Integer xPos);


    /**
     * 获取温度信息
     * @param houseId
     * @param xPos
     * @return
     */
    List<TemperatureReport> getTemperatureInfo(UUID houseId, Integer xPos);


    /**
     * 根据库位ID获取电池列表
     * @param locId
     * @return
     */
    LocationChartTemplate getLoctionInfoByLocId(UUID locId);



    /**
     * 根据houseId和x获取规格，y,z
     *
     * @param houseId
     * @param xPos
     * @return
     */
    Map<String, Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos);


    /**
     * 实时监控统计
     * @param houseId
     * @param xPos
     * @return
     */
    Map<Integer,Map<String, Object>> getStatisticsTemplated(UUID houseId, Integer xPos);


    /**
     * 获取火警分页信息
     *
     * @param exQuery
     * @return
     */
    PageResult<FireAlarmInfoDto> getFireAlarmInfoPaged(FireAlarmInfoExQuery exQuery);

    /**
     * 获取火警不分页信息
     *
     * @param exQuery
     * @return
     */
    List<FireAlarmInfoDto> getFireAlarmInfoList(FireAlarmInfoExQuery exQuery);



    /**
     * 根据报警id获取火警记录
     */
    FireAlarmInfoDto getFireAlarmInfoDtoList(UUID id);


    /**
     * 新增消警记录
     */
    GaiaResult saveAlarmHandleInfo(AlarmHandleInfoDto dto);

}
