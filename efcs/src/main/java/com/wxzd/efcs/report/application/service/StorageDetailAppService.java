package com.wxzd.efcs.report.application.service;


import com.wxzd.efcs.business.application.dtos.*;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
import com.wxzd.efcs.report.application.dtos.*;
import com.wxzd.efcs.report.application.querys.DetailListExQuery;
import com.wxzd.efcs.report.application.querys.StorageListExQuery;
import com.wxzd.efcs.report.application.querys.enums.DashBoardCountType;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.wms.core.application.dtos.LocationWithStatusDto;
import com.wxzd.wms.core.application.dtos.WarehouseRowDto;
import com.wxzd.wms.core.application.querys.StorageLocationExQuery;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/17
 */
public interface StorageDetailAppService {

    /**
     * 库存明细统计(系统级别)
     *
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
     *
     * @param houseId
     * @param xPos
     * @return
     */
    List<LocationChartExtendDto> getLocationInfosForCharts(UUID houseId, UUID zoneId, Integer xPos);


    /**
     * 根据houseId和x获取规格，y,z
     *
     * @param houseId
     * @param xPos
     * @return
     */
    Map<String, Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos);


    /**
     * 实时库存查询   库×,货位编号×，托盘编号×，库存类型，容器编号，SKU条码×，SKU类型×，库存数量，单位，创建时间*
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
     * 分页数据汇总   TODO
     */
    PageResult<DataSummaryDto> getDataSumPaged(DetailListExQuery exQuery);

    /**
     * 库存明细-明细列表   TODO
     */
    PageResult<DetailListDto> getDetailListPaged(DetailListExQuery exQuery);


    /**
     * 生产仪表盘统计  (在制，已制，优品)
     */
    List<MeterCounts> getProductDashboardStatistics();

    /**
     * 首页仪表盘 拉线-统计 List
     */
    List<DashBoardStatisticsDto> getDashBoardStatistics();

//    /**
//     * 拉线-详情统计  化成-测试
//     */
//    Object getFormatStatistics(UUID houseId, DashBoardCountType type);

    /**
     * 拉线-化成段统计
     *
     * @param houseId
     * @return
     */
    FormatStatisticsDto getFormatStatistics(UUID houseId);

    /**
     * 拉线-测试段统计
     *
     * @param houseId
     * @return
     */
    TestStatisticsDto getTestStatistics(UUID houseId);


    /******* todo  待修改   **********/


    /**
     * 根据ID托盘详情
     */
    PalletDispatchDto getPalletDispatch(UUID id);


    /**
     * 电池查询全部列表
     */
    PageResult<PalletBatteryDto> getPalletBatteryPaged(PalletBatteryExQuery exQuery);

    /**
     * 托盘查询全部列表
     */
    PageResult<PalletDispatchDto> getPalletDispatchPaged(PalletDispatchExQuery exQuery);

    /**
     * 托盘移动记录查询
     */
    PageResult<PalletMoveDetailDto> getPalletMoveDetailPaged(PalletDispatchExQuery exQuery);


    /**
     * 获取排信息
     *
     * @param houseId
     * @param xPos
     * @return
     */
    WarehouseRowDto getWarehouseRow(UUID houseId, Integer xPos);


    /**
     * 获取库位异常信息
     */
    LocationWithStatusDto getLocationById(UUID id);


    /**
     * 根据库位号获取sku种类，执行中的表单、指令
     *
     * @return
     */
    Map<String, Integer> getExecutingByHouseId(UUID houseId);


}
