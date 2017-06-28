package com.wxzd.efcs.report.application.service.impl;

import com.wxzd.efcs.business.application.dtos.*;
import com.wxzd.efcs.business.application.queryService.*;
import com.wxzd.efcs.business.application.querys.*;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.UserDefinedStatus;
import com.wxzd.efcs.report.application.dtos.*;
import com.wxzd.efcs.report.application.queryService.StorageDetailExQueryService;
import com.wxzd.efcs.report.application.querys.DetailListExQuery;
import com.wxzd.efcs.report.application.querys.StorageListExQuery;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.efcs.report.domain.service.StorageDetailService;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.wms.core.application.dtos.LocationWithStatusDto;
import com.wxzd.wms.core.application.dtos.WarehouseRowDto;
import com.wxzd.wms.core.application.queryService.StorageLocationQueryService;
import com.wxzd.wms.core.application.queryService.WarehouseRowQueryService;
import com.wxzd.wms.core.application.querys.WarehouseRowQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by LYK on 2017/4/18
 * 库存明细
 */
@Service
public class StorageDetailAppServiceImpl implements StorageDetailAppService {

    @Autowired
    private StorageDetailService detailService;

    @Autowired
    private StorageDetailExQueryService exQueryService;

    @Autowired
    private BatteryInfoExQueryService batteryInfoExQueryService;

    @Autowired
    private PalletDetailExQueryService palletDetailExQueryService;

    @Autowired
    private PalletMoveDetailExQuery palletMoveDetailExQuery;

    @Autowired
    private WarehouseRowQueryService warehouseRowQueryService;

    @Autowired
    private StorageLocationQueryService storageLocationQueryService;

    @Override
    public Map<Integer, Map<String, Object>> getStatusStatistics(LocationWithNoPallet locationWithNoPallet) {
        return detailService.getStatusStatistics(locationWithNoPallet);
    }


    /**
     * 库存明细显示锁信息
     *
     * @param locationWithNoPallet
     * @return
     */
    @Override
    public Map<Integer, Map<String, Object>> getStatusStatisticsWithLock(LocationWithNoPallet locationWithNoPallet) {
        return detailService.getStatusStatisticsWithLock(locationWithNoPallet);
    }

    @Override
    public List<LocationChartExtendDto> getLocationInfosForCharts(UUID houseId, UUID zoneId, Integer xPos) {
        return detailService.getLocationInfosForCharts(houseId, zoneId, xPos);
    }

    @Override
    public Map<String, Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos) {
        return detailService.getSizebyhouseIdAndXpos(houseId, xPos);
    }

    @Override
    public PageResult<StorageListDto> getStorageRealTimePaged(StorageListExQuery exQuery) {
        return exQueryService.getStorageRealTimePaged(exQuery);
    }

    @Override
    public StorageBatteryDetailDto getBatteryDetailBylocationId(UUID locationId) {
        StorageBatteryDetailDto storageBatteryDetailDto = exQueryService.getBatteryDetailBylocationId(locationId);
        if (storageBatteryDetailDto != null && storageBatteryDetailDto.getId() != null) {
            storageBatteryDetailDto.setBetteryList(exQueryService.betteryList(storageBatteryDetailDto.getId()));
        }
        return storageBatteryDetailDto;
    }

    @Override
    public PageResult<DataSummaryDto> getDataSumPaged(DetailListExQuery exQuery) {
        return exQueryService.getDataSumPaged(exQuery);
    }


    @Override
    public PageResult<DetailListDto> getDetailListPaged(DetailListExQuery exQuery) {
        return exQueryService.getDetailListPaged(exQuery);
    }

    @Override
    public List<MeterCounts> getProductDashboardStatistics() {
        List<MeterCounts> list = new ArrayList<>();
        Map<String, Object> map = exQueryService.getProductDashboardStatistics();
        if (map != null) {
            Integer total = map.get("total") != null ? Integer.valueOf(map.get("total").toString()) : 0;
            Integer excellent = map.get("excellent") != null ? Integer.valueOf(map.get("excellent").toString()) : 0;
            Integer hasmade = map.get("hasmade") != null ? Integer.valueOf(map.get("hasmade").toString()) : 0;
            Integer inStorage = map.get("instorage") != null ? Integer.valueOf(map.get("instorage").toString()) : 0;
            list.add(new MeterCounts("在制", inStorage, total == 0 ? "0.00" : MeterFormat((float) inStorage * 100 / total)));
            list.add(new MeterCounts("已制", hasmade, total == 0 ? "0.00" : MeterFormat((float) hasmade * 100 / total)));
            list.add(new MeterCounts("优品", excellent, total == 0 ? "0.00" : MeterFormat((float) excellent * 100 / total)));
        }
        return list;
    }

    //百分比format
    private String MeterFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    @Override
    public List<DashBoardStatisticsDto> getDashBoardStatistics() {
        return exQueryService.getDashBoardStatistics();
    }


//    @Override
//    public Object getFormatStatistics(UUID houseId, DashBoardCountType type) {
//        if (houseId != null && type != null) {
//            switch (type) {
//                case Formation:
//                    return exQueryService.getFormatStatistics(houseId);
//                case Test:
//                    return exQueryService.getTestStatistics(houseId);
//            }
//        }
//        return null;
//    }


    @Override
    public FormatStatisticsDto getFormatStatistics(UUID houseId) {
        return exQueryService.getFormatStatistics(houseId);
    }

    @Override
    public TestStatisticsDto getTestStatistics(UUID houseId) {
        return exQueryService.getTestStatistics(houseId);
    }

    @Override
    public PalletDispatchDto getPalletDispatch(UUID id) {
        PalletDispatchDto dispatchDto = palletDetailExQueryService.getPalletDispatch(id);
        if (dispatchDto != null) {
            List<PalletDetailDto> list = palletDetailExQueryService.getInnerDetailById(id);
            dispatchDto.setPalletDetails(list);
            List<PalletMoveDetailDto> list1 = palletMoveDetailExQuery.getPalletMoveDetailByDisId(id);
            dispatchDto.setPalletMoveDetailDtos(list1);
        }
        return dispatchDto;
    }




    @Override
    public PageResult<PalletBatteryDto> getPalletBatteryPaged(PalletBatteryExQuery exQuery) {
        return batteryInfoExQueryService.getPalletBatteryPaged(exQuery);
    }

    @Override
    public PageResult<PalletDispatchDto> getPalletDispatchPaged(PalletDispatchExQuery exQuery) {
        return palletDetailExQueryService.getPalletDispatchPaged(exQuery);
    }

    @Override
    public PageResult<PalletMoveDetailDto> getPalletMoveDetailPaged(PalletDispatchExQuery exQuery) {
        return palletMoveDetailExQuery.getPalletMoveDetailPaged(exQuery);
    }


    @Override
    public WarehouseRowDto getWarehouseRow(UUID houseId, Integer xPos) {
        WarehouseRowQuery exQuery = new WarehouseRowQuery(houseId, xPos);
        List<WarehouseRowDto> list = warehouseRowQueryService.getWarehouseRowList(exQuery);
//        List<WarehouseRowDto> list = warehouseRowQueryService.getWarehouseMaxMinRows(exQuery);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public LocationWithStatusDto getLocationById(UUID id) {
        return storageLocationQueryService.getLocationById(id);
    }


    @Autowired
    private FmPalletizeExQueryService fmPalletizeExQueryService;

    @Autowired
    private FmPalletSplitExQueryService fmPalletSplitExQueryService;

    @Autowired
    private FmProcedureExQueryService fmProcedureExQueryService;

    @Autowired
    private MemoryInstructionAppService memoryInstructionAppServicel;

    /**
     * 根据库位号获取sku种类，执行中的表单、指令
     *
     * @return
     */
    @Override
    public Map<String, Integer> getExecutingByHouseId(UUID houseId) {
        //SKU个数
        Integer skuCount = 0;
        Integer fmPalletizeCount = 0;
        Integer fmPalletSplitCount = 0;
        Integer fmProcedureCount = 0;
        Integer instructionCount = 0;
        //sku
        Integer count = exQueryService.getSkuCounstByHouseId(houseId);
        if (count != null)
            skuCount = count;

        //组盘表
        FmPalletizeExQuery exQuery = new FmPalletizeExQuery();
        exQuery.setHouse_id(houseId);
        exQuery.setUserDefinedStatus(UserDefinedStatus.Executing);
        List<FmPalletizeDto> fmPalletizeDtos = fmPalletizeExQueryService.getFmPalletizeList(exQuery);
        if (fmPalletizeDtos != null)
            fmPalletizeCount = fmPalletizeDtos.size();

        //拆盘单
        FmPalletSplitExQuery fmPalletSplitExQuery = new FmPalletSplitExQuery();
        fmPalletSplitExQuery.setHouse_id(houseId);
        fmPalletSplitExQuery.setUserDefinedStatus(UserDefinedStatus.Executing);
        List<FmPalletSplitDto> fmPalletSplitDtos = fmPalletSplitExQueryService.getFmPalletSplitList(fmPalletSplitExQuery);
        if (fmPalletSplitDtos != null)
            fmPalletSplitCount = fmPalletSplitDtos.size();

        //工序单
        FmProcedureExQuery fmProcedureExQuery = new FmProcedureExQuery();
        fmProcedureExQuery.setHouse_id(houseId);
        fmProcedureExQuery.setUserDefinedStatus(UserDefinedStatus.Executing);
        List<FmProcedureDto> fmProcedureDtos = fmProcedureExQueryService.getFmProcedureList(fmProcedureExQuery);
        if (fmProcedureDtos != null)
            fmProcedureCount = fmProcedureDtos.size();

        //指令
        Map<String, Object> map = palletDetailExQueryService.getHouseNobyId(houseId);
        if (map != null) {
            String houseNo = map.get("house_no").toString();
            List<Instruction> instructionDtos = memoryInstructionAppServicel.getInstructionByHouseNo(houseNo).getItems();
            if (instructionDtos != null)
                instructionCount = instructionDtos.size();
        }
        Map<String, Integer> objectMap = new HashMap<>();
        objectMap.put("sku", skuCount);
        objectMap.put("form", fmPalletizeCount + fmPalletSplitCount + fmProcedureCount);
        objectMap.put("instruction", instructionCount);
        return objectMap;
    }
}
