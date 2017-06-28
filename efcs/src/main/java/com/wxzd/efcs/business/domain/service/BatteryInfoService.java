package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.gaia.common.base.core.result.GaiaResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/19.
 */
public interface BatteryInfoService {

    /**
     * 根据Id 获取电池信息
     * @param id
     * @return
     */
    BatteryInfo getById(UUID id);


    /**
     * 根据barcode获取barcode信息
     * @param barcode
     * @return
     */
    BatteryInfo getById(String barcode);
    /**
     * 保存电池记录
     * @param batteryInfo
     * @return
     */
    GaiaResult saveBatteryInfo(BatteryInfo batteryInfo);

    /**
     * 保存电池记录
     * @param batteryInfos
     * @return
     */
    GaiaResult saveBatteryInfo(List<BatteryInfo> batteryInfos);

    /**
     * 更新电池状态
     * @param barcode
     * @param status
     * @param remark
     * @return
     */
    GaiaResult updateBatteryStatus(String barcode, String status, String remark);

    /**
     * 根据barcode列表获取电池信息
     * @param barcodeList
     * @return
     */
    List<BatteryInfo> batteryInfo(List<String> barcodeList);

}
