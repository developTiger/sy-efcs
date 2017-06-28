package com.wxzd.efcs.report.controller;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.efcs.business.application.service.BatteryAppService;
import com.wxzd.efcs.report.ModuleEfcsReport;
import com.wxzd.efcs.report.application.dtos.*;
import com.wxzd.efcs.report.application.querys.DetailListExQuery;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.wms.core.application.dtos.WarehouseDto;
import com.wxzd.wms.core.application.dtos.WarehouseRowDto;
import com.wxzd.wms.core.application.dtos.ZonesDto;
import com.wxzd.wms.core.application.queryService.StorageLocationQueryService;
import com.wxzd.wms.core.application.queryService.WarehouseRowQueryService;
import com.wxzd.wms.core.application.queryService.ZonesQueryService;
import com.wxzd.wms.core.application.service.StorageLocationAppService;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;
import com.wxzd.wms.core.domain.entities.enums.OpeLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 库存明细
 * Created by jade on 2017/4/17.
 */
@Module(ModuleEfcsReport.name)
@Controller
@RequestMapping
public class StoreDetailController {

    @Resource
    private StorageDetailAppService storageDetailAppService;

    @Resource
    private ZonesQueryService zonesQueryService;

    @Resource
    private WareHouseAppService wareHouseAppService;

    @Resource
    private WarehouseRowQueryService warehouseRowQueryService;

    @Resource
    private StorageLocationAppService storageLocationAppService;

    @Resource
    private BatteryAppService batteryAppService;

    /**
     * 批量展示
     *
     * @param model
     */
    @RequestMapping
    public void index_view(Model model) {
        ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();
        ListResult<ZonesDto> zonesDtos = zonesQueryService.getAllZonesWithChilds(OpeLevel.system_level);
        Map<String, Map<String, List<WarehouseRowDto>>> rowMaps = warehouseRowQueryService.getWarehouseRowsByZoneId();

        if (wareHouseDtos != null) {
            model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());
        }
        if (zonesDtos != null) {
            model.addAttribute("zonesDtos", zonesDtos.getItems());
        }
        if (rowMaps != null) {
            model.addAttribute("rowMaps", rowMaps);
        }
    }

    /**
     * 单个展示
     */
    @RequestMapping
    public void single_view(UUID id, String x, Model model) {
        WarehouseRowDto warehouseRowDto = storageDetailAppService.getWarehouseRow(id, Integer.valueOf(x));

        model.addAttribute("wareHouseDto", warehouseRowDto);
    }

    /**
     * 获取库位信息
     *
     * @param houseId
     * @param type
     * @return
     */
    @RequestMapping
    @ResponseBody
    public List<LocationChartExtendDto> getLocationInfos(UUID houseId, UUID type, String x) {
        return storageDetailAppService.getLocationInfosForCharts(houseId, type, Integer.valueOf(x));
    }

    /**
     * @param type    zoneId
     * @param houseId houseId
     * @param x       x轴信息
     */
    @RequestMapping
    @ResponseBody
    public Map<Integer, Map<String, Object>> getStatusStatistics(UUID houseId, UUID type, Integer x) {
        LocationWithNoPallet locationWithNoPallet = new LocationWithNoPallet();
        locationWithNoPallet.setHouse_id(houseId);
        locationWithNoPallet.setZone_id(type);
        locationWithNoPallet.setX_pos(x);

        return storageDetailAppService.getStatusStatistics(locationWithNoPallet);
    }

    /**
     * 库位详情
     */
    @RequestMapping
    public void storageLocationDetail_view(UUID uuid, Model model) {
        StorageBatteryDetailDto storageBatteryDetailDto = storageDetailAppService.getBatteryDetailBylocationId(uuid);

        model.addAttribute("locationId", uuid.toString());
        model.addAttribute("storageBatteryDetailDto", storageBatteryDetailDto);
    }

    /**
     * 汇总统计
     */
    @RequestMapping
    public void statistics_view(UUID id, Model model) {
        model.addAttribute("id", id);
    }

    /**
     * 排数据统计
     */
    @RequestMapping
    @ResponseBody
    public PageResult<DataSummaryDto> grid_x(DetailListExQuery exQuery) {
        PageResult<DataSummaryDto> dataSummaryDtoPageResult = storageDetailAppService.getDataSumPaged(exQuery);
        return dataSummaryDtoPageResult;
    }

    /**
     * 排详情统计
     */
    @RequestMapping
    @ResponseBody
    public PageResult<DetailListDto> grid_x_detail(DetailListExQuery exQuery) {
        return storageDetailAppService.getDetailListPaged(exQuery);
    }

    /**
     * 电池详情
     */
    @RequestMapping
    public void battery_view(String barcode, Model model) {
        PalletBatteryDto PalletBatteryDto = batteryAppService.getBatteryMoveDetail(barcode);
        model.addAttribute("palletMoveDetailDtos", PalletBatteryDto);
    }

    /**
     * 库位锁信息
     */
    @RequestMapping
    public void lock_view(Model model) {
        ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();
        ListResult<ZonesDto> zonesDtos = zonesQueryService.getAllZonesWithChilds(OpeLevel.system_level);
        Map<String, Map<String, List<WarehouseRowDto>>> rowMaps = warehouseRowQueryService.getWarehouseRowsByZoneId();

        if (wareHouseDtos != null) {
            model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());
        }
        if (zonesDtos != null) {
            model.addAttribute("zonesDtos", zonesDtos.getItems());
        }
        if (rowMaps != null) {
            model.addAttribute("rowMaps", rowMaps);
        }
    }

    /**
     * 锁-排数据统计
     */
    @RequestMapping
    @ResponseBody
    public Map<Integer, Map<String, Object>> getStatusStatistics_lock(UUID houseId, UUID type, Integer x) {
        LocationWithNoPallet locationWithNoPallet = new LocationWithNoPallet();
        locationWithNoPallet.setHouse_id(houseId);
        locationWithNoPallet.setZone_id(type);
        locationWithNoPallet.setX_pos(x);

        return storageDetailAppService.getStatusStatisticsWithLock(locationWithNoPallet);
    }

    /**
     * 库位枷锁
     *
     * @param in_lock
     * @param out_lock
     * @param model
     */
    @RequestMapping
    public void addLock_view(UUID id,int in_lock, int out_lock, Model model) {
        model.addAttribute("in_lock", in_lock);
        model.addAttribute("out_lock", out_lock);
        model.addAttribute("id",id);
    }

    /**
     *  设置锁
     * @param id
     * @param isLock
     * @param lockType
     * @param lockDesc
     * @return
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult addLock(UUID id, String isLock, String lockType, String lockDesc) {

        Boolean var1 = StringUtl.isEqual(isLock,"lock");

        Boolean isSuccess = false;
        List<String> message = new ArrayList<>();

        if (lockType.contains("in")) {
            GaiaResult gaiaResult =  storageLocationAppService.setArtificialLocationLock(id, LockDirection.inLock
                    , var1, lockDesc);
            isSuccess = gaiaResult.isSuccess();
            message.add(gaiaResult.getMessage());

        }
        if(lockType.contains("out")) {
            GaiaResult gaiaResult = storageLocationAppService.setArtificialLocationLock(id, LockDirection.outLock
                    , var1, lockDesc);

            isSuccess = gaiaResult.isSuccess();
            message.add(gaiaResult.getMessage());
        }


        GaiaResult gaiaResult = new GaiaResult(isSuccess, StringUtils.join(message,","));

        return gaiaResult;

    }

}
