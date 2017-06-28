package com.wxzd.efcs.business.application.workProcedures.testIn;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.application.workProcedures.dto.BatteryInPrecedureInfo;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.efcs.business.domain.service.FmBatteryInService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.core.domain.service.StorageService;
import com.wxzd.wms.core.domain.service.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testInExecutorService")
public class TestInExecutorServiceImpl implements TestInExecutorService<BatteryInPrecedureInfo, GaiaResult> {


    @Autowired
    FmBatteryInService fmBatteryInService;

    @Autowired
    StorageService storageService;

    @Autowired
    LocationAllotQyery locationAllotQyery;


    @Autowired
    BatteryInfoService batteryInfoService;

    @Autowired
    WareHouseService wareHouseService;


    @Override
    public GaiaResult doWorkProcedure(BatteryInPrecedureInfo procedureInfo) {
        //TODO  出库 测试代码  测试完成删除  important
//——————————————————测试代码——————————————————————————
        for (BatteryInContainer s : procedureInfo.getListBattery()) {
            if (s.getBattery_no() != null) {
                try {
                    Storage storage = storageService.getStorageByBarcode(s.getBattery_no());
                    if (storage != null) {
                        List<Storage> storages = new ArrayList<>();
                        storages.add(storage);
                        Storage fromContainer = new Storage(storage.getLocation_id(), "", StorageType.container);
                        fromContainer.setHouse_id(procedureInfo.getWarehouse().getId());
                        storageService.storageOut(fromContainer, StorageType.sku, storages, WorkProcedure.Test_Out.toString(), null, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }
//---------------------------------------------------------------------------------------------

        if (procedureInfo.getStorageLocation() == null) {
            procedureInfo.setStorageLocation(locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), DispatcherConfig.test_palletize_device_no).get(0));
        }
        List<String> barcodes = new ArrayList<>();
        for (BatteryInContainer s : procedureInfo.getListBattery()) {
            barcodes.add(s.getBattery_no());
        }
        if (barcodes == null || barcodes.size() == 0) {
            return GaiaResultFactory.getSuccess();
        }
        List<Storage> list = storageService.getStorageByBarcode(barcodes);

        List<BatteryInContainer> batteryInContainers = new ArrayList<>();

        if (list.size() > 0) {
            for (BatteryInContainer container : procedureInfo.getListBattery()) {
                Boolean isExist = false;
                for (Storage storage : list) {
                    if (storage.getSku_barcode().equals(container.getBattery_no())) {
                        if (!storage.getLocation_id().equals(procedureInfo.getStorageLocation().getId())) {
                            container.setBattery_status("NG");
                            container.setInfo("battery exist error");

                        }
                        isExist = true;
                    }
                }
                if (!isExist) {
                    batteryInContainers.add(container);
                }
            }
        } else {
            batteryInContainers = procedureInfo.getListBattery();
        }
        if (batteryInContainers.size() > 0) {
            List<Storage> needAdd = ConvertFactory.convertToStorage(procedureInfo.getWarehouse().getId(), procedureInfo.getStorageLocation().getId(), "", "", procedureInfo.getHouseNo(), batteryInContainers);

            //获取设备库位信息
            Storage toContainer = new Storage(procedureInfo.getStorageLocation().getId(), null, StorageType.container);
            storageService.storageIn(toContainer, StorageType.sku, needAdd, WorkProcedure.Formation_In.toString(), null, null);
            //生成入库单
            fmBatteryInService.save(ConvertFactory.convertToFmBatteryIn(procedureInfo.getWarehouse().getId(), WorkProcedure.Formation_In, procedureInfo, batteryInContainers));
            //保存电池信息
            List<BatteryInfo> batteryInfos = ConvertFactory.ConvertToBatteryInfo(batteryInContainers, procedureInfo.getHouseNo());
            batteryInfoService.saveBatteryInfo(batteryInfos);
        }
        return GaiaResultFactory.getSuccess();
    }


    /**
     * 是否需要创建下一道工序
     *
     * @param workProcedure
     * @param palletStatus
     * @param currentPos
     * @return
     */
    @Override
    public Boolean needActiveNextProcedure(WorkProcedure workProcedure, PalletStatus palletStatus, String currentPos) {

        return false;
    }

    /**
     * 获取下一道工序
     *
     * @param workProcedure
     * @return
     */
    @Override
    public WorkProcedure getNextProcedure(WorkProcedure workProcedure) {
        return null;
    }

    /**
     * 异常结束工序
     *
     * @param workProcedure
     * @param palletNo
     * @param remark        @return
     */
    @Override
    public GaiaResult errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
        return null;
    }

    /**
     * 非正常的开始一个新工序
     *
     * @param workProcedure
     * @param procedureInfo
     * @return
     */
    @Override
    public GaiaResult initProcedureCurrenPosImproper(WorkProcedure workProcedure, BatteryInPrecedureInfo procedureInfo) {
        return null;
    }

}
