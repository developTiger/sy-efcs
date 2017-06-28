package com.wxzd.efcs.alarm.application.queryService;

import com.wxzd.efcs.alarm.application.dtos.FireAlarmInfoDto;
import com.wxzd.efcs.alarm.application.dtos.LocationChartTemplate;
import com.wxzd.efcs.alarm.application.querys.FireAlarmInfoExQuery;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
public interface FireAlarmInfoExQueryService {


    /**
     * 根据houseId和x获取规格，y,z
     *
     * @param houseId
     * @param xPos
     * @return
     */
    Map<String, Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos);


    /**
     * 获取化成库位信息
     *
     * @param
     */
    List<LocationChartTemplate> getLocationInfosForCharts(UUID houseId, Integer xPos);


    /**
     * 化成统计
     *
     * @param houseId
     * @param xPos
     * @return
     */
    Map<Integer, Map<String, Object>> getStatisticsTemplated(UUID houseId, Integer xPos);


    /**
     * 根据ID获取库位信息
     * @param locId
     * @return
     */
    LocationChartTemplate getLoctionInfoByLocId(UUID locId);


    /**
     * 根据库位ID获取电池详情
     */
    List<PalletDetail> getbetteryLists(UUID locId);

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


    FireAlarmInfoDto getById(UUID id);


    /**
     * 获取拉线
     *
     * @param id
     * @return
     */
    Map<String, Object> getHouseNo(UUID id);


}
