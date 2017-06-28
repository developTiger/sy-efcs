package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.efcs.business.repositorys.BatteryInfoRepository;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/19.
 */
@Service
public class BatteryInfoServiceImpl implements BatteryInfoService {
    @Autowired
    BatteryInfoRepository batteryInfoRepository;

    /**
     * 根据Id 获取电池信息
     *
     * @param id
     * @return
     */
    @Override
    public BatteryInfo getById(UUID id) {
        return batteryInfoRepository.getById(id);
    }

    /**
     * 根据barcode获取barcode信息
     *
     * @param barcode
     * @return
     */
    @Override
    public BatteryInfo getById(String barcode) {
        return batteryInfoRepository.getByBarcode(barcode);

    }

    /**
     * 保存电池记录
     *
     * @param batteryInfo
     * @return
     */
    @Override
    public GaiaResult saveBatteryInfo(BatteryInfo batteryInfo) {
        BatteryInfo battery = batteryInfoRepository.getByBarcode(batteryInfo.getBattery_barcode());
        if (battery != null) {
            battery.setBattery_status(batteryInfo.getBattery_status());
            battery.setRemark(batteryInfo.getRemark());
            battery.setTest_complete_time(batteryInfo.getTest_complete_time());
            batteryInfoRepository.saveById(battery);
        } else
            batteryInfoRepository.saveById(batteryInfo);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult updateBatteryStatus(String barcode, String status, String remark) {
        BatteryInfo battery = batteryInfoRepository.getByBarcode(barcode);
        if (battery == null) {
            return GaiaResultFactory.getError("电池信息不存在");
        }

        battery.setBattery_status(status);
        battery.setRemark(remark);
        batteryInfoRepository.saveById(battery);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 保存电池记录
     *
     * @param batteryInfos
     * @return
     */
    @Override
    public GaiaResult saveBatteryInfo(List<BatteryInfo> batteryInfos) {
        List<String> barcodes = new ArrayList<>();
        for (BatteryInfo battery : batteryInfos) {
            barcodes.add(battery.getBattery_barcode());
        }
        List<BatteryInfo> dbBatterys = batteryInfoRepository.getByBarcodes(barcodes);

        if (dbBatterys.size() > 0) {
            for (BatteryInfo batteryInfo : batteryInfos) {


                Boolean isExist = false;
                for (BatteryInfo existBattery : dbBatterys) {
                    if (batteryInfo.getBattery_barcode().equals(existBattery.getBattery_barcode())) {
                        isExist = true;
                        existBattery.setBattery_status(batteryInfo.getBattery_status());
                        existBattery.setRemark(batteryInfo.getRemark());
                        existBattery.setTest_complete_time(batteryInfo.getTest_complete_time());
                    }
                }
                if (!isExist) {
                    dbBatterys.add(batteryInfo);
                }


            }
        } else {
            dbBatterys=batteryInfos;
        }

        batteryInfoRepository.saveById(dbBatterys);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 根据barcode列表获取电池信息
     *
     * @param barcodeList
     * @return
     */
    @Override
    public List<BatteryInfo> batteryInfo(List<String> barcodeList) {

        return batteryInfoRepository.getByBarcodes(barcodeList);

    }


}
