package com.wxzd.efcs.report.application.queryService;

import com.wxzd.efcs.business.application.dtos.*;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.report.application.dtos.*;
import com.wxzd.efcs.report.application.querys.DetailListExQuery;
import com.wxzd.efcs.report.application.querys.StorageListExQuery;
import com.wxzd.efcs.report.application.querys.enums.DashBoardCountType;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/18
 */
public interface StorageDetailExQueryService {


    /**
     * 即使库存明细查询
     */
    PageResult<StorageListDto> getStorageRealTimePaged(StorageListExQuery exQuery);

    /**
     * 获取库位电池列表
     *
     * @param locationId
     * @return
     */
    StorageBatteryDetailDto getBatteryDetailBylocationId(UUID locationId);

    /**
     * 分页数据汇总
     */
    PageResult<DataSummaryDto> getDataSumPaged(DetailListExQuery exQuery);

    /**
     * 库存明细-明细列表
     */
    PageResult<DetailListDto> getDetailListPaged(DetailListExQuery exQuery);


    /**
     * 生产仪表盘统计   已制/优品
     */
    Map<String, Object> getProductDashboardStatistics();


    /**
     * 首页仪表盘 拉线-统计
     * @return
     */
    List<DashBoardStatisticsDto> getDashBoardStatistics();

    /**
     * 拉线-化成段统计
     * @param houseId
     * @return
     */
    FormatStatisticsDto getFormatStatistics(UUID houseId);

    /**
     * 拉线-测试段统计
     * @param houseId
     * @return
     */
    TestStatisticsDto getTestStatistics(UUID houseId);

    /**
     * 托盘货物查询
     * @param id
     * @return
     */
    List<PalletDetailDto> betteryList(UUID id);


    /**
     * 查询sku个数
     * @param houseId
     * @return
     */
    Integer getSkuCounstByHouseId(UUID houseId);


}
