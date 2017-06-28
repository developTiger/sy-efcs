package com.wxzd.efcs.report.domain.service;

import com.wxzd.efcs.report.application.dtos.DataSummaryDto;
import com.wxzd.efcs.report.application.dtos.DetailListDto;
import com.wxzd.efcs.report.application.dtos.LocationChartExtendDto;
import com.wxzd.efcs.report.application.dtos.LocationWithNoPallet;
import com.wxzd.efcs.report.application.querys.DetailListExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.wms.core.application.dtos.LocationChartDto;
import com.wxzd.wms.core.domain.entities.Storage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/18
 */
public interface StorageDetailService {

    /**
     * 库存明细统计(系统级别)
     * @return
     */
    Map<Integer, Map<String, Object>> getStatusStatistics(LocationWithNoPallet locationWithNoPallet);


    /**
     * 库存明细显示锁信息
     * @param locationWithNoPallet
     * @return
     */
    Map<Integer,Map<String,Object>> getStatusStatisticsWithLock(LocationWithNoPallet locationWithNoPallet);


    /**
     * 库存明细展示
     * @param houseId
     * @param xPos
     * @return
     */
    List<LocationChartExtendDto> getLocationInfosForCharts(UUID houseId,UUID zoneId, Integer xPos);

    /**
     * 根据houseId和x获取规格，y,z
     * @param houseId
     * @param xPos
     * @return
     */
    Map<String,Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos);





}
