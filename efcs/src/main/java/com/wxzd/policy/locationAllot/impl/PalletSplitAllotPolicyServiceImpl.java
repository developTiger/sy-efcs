package com.wxzd.policy.locationAllot.impl;

import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.policy.locationAllot.*;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhouzh on 2017/4/17.
 * 拆盘位库位分配
 */
@Service("palletSplitAllotPolicyService")
public class PalletSplitAllotPolicyServiceImpl implements LocationAllotService<DefaultAllotPolicyParam> {

    @Autowired
    LocationAllotQyery locationAllotQyery;

    /**
     * 入库库位分配
     *
     * @param houseNo            库号
     * @param equitNo            设备号
     * @param rowAllotPolicyType 排分配策略
     * @param colAllotPolicyType 层分配策略
     * @return ObjectResult<AllotStorageLocation>
     */
    @Override
    public ObjectResult<AllotStorageLocation> allotStorageLocationIn(WorkProcedure procedure, String palletNo, UUID houseId, String houseNo, String equitNo, AllotPolicyType rowAllotPolicyType,
                                                                     AllotPolicyType colAllotPolicyType) {
        //拆盘缓存位
        if (equitNo.equals(DispatcherConfig.formation_pallet_split_catch_location)) {
            List<Storage> rightStorage = locationAllotQyery.getEquipLocationStorage(houseId, DispatcherConfig.formation_pallet_split_location_right);
            List<Storage> leftStorage = locationAllotQyery.getEquipLocationStorage(houseId, DispatcherConfig.formation_pallet_split_location_left);

            Boolean isSplitAll = true;
            String finalPos;
            Integer storagesWaitIn = this.checkWaitInLocation(palletNo);
            if (storagesWaitIn == 0) {
                isSplitAll = true;
                if (rightStorage.size() == 0) {
                    finalPos = DispatcherConfig.formation_pallet_split_location_right;
                } else if (leftStorage.size() == 0) {
                    finalPos = DispatcherConfig.formation_pallet_split_location_left;
                } else if (rightStorage.size() <= leftStorage.size()) {
                    finalPos = DispatcherConfig.formation_pallet_split_location_right;

                } else {
                    finalPos = DispatcherConfig.formation_pallet_split_location_left;
                }
            } else {

                //获取缓存为 可放置的检测数量
                List<Storage> catchStorage = locationAllotQyery.getEquipLocationStorage(houseId, DispatcherConfig.formation_rework_pallet_catch_location);
                Integer cacheCanSetCount = 0;
                if (catchStorage.size() > 0) {
                    cacheCanSetCount = DispatcherConfig.pallet_battery_count - catchStorage.size() + 1;//托盘去掉 所以+1
                }
                Integer reworkLocCount = locationAllotQyery.getEquipLocationStorage(houseId, DispatcherConfig.formation_split_equip_rework).size();
                Integer valableCount = 0;
                if (reworkLocCount > 0) {
                    //获取当前缓存位可存放rework 电池数量
                    valableCount = DispatcherConfig.pallet_battery_count - reworkLocCount + 1 + cacheCanSetCount;
                } else {
                    valableCount = cacheCanSetCount;
                }

                //待拆托盘的rework数量


                List<String> locs = new ArrayList<>();
                locs.add(DispatcherConfig.formation_pallet_split_location_left);
                locs.add(DispatcherConfig.formation_pallet_split_location_right);
                Integer onSplitReworkCount = checkLocation(locs);
                if (rightStorage.size() == 0) {
                    finalPos = DispatcherConfig.formation_pallet_split_location_right;
                }
                //货位上有货
                else {
                    //检查缓存位
                    if (catchStorage.size() == 0) {
                        //缓存位无托盘，托盘继续放到右侧库位（不管右侧有没有）
                        finalPos = DispatcherConfig.formation_pallet_split_location_right;

                    } else {

                        //算上1号位 自身的24个位置
                        if ((valableCount + 24 - onSplitReworkCount) < storagesWaitIn) {
                            finalPos = DispatcherConfig.formation_pallet_split_location_right;
                        } else {//随便分
                            if (leftStorage.size() == 0)//左侧无托盘了。
                            {
                                finalPos = DispatcherConfig.formation_pallet_split_location_left;

                            } else {
                                //左侧也有，哪个电池少分哪个（在拆），相等的话分右侧
                                if (leftStorage.size() < rightStorage.size()) {
                                    finalPos = DispatcherConfig.formation_pallet_split_location_left;

                                } else
                                    finalPos = DispatcherConfig.formation_pallet_split_location_right;
                            }
                        }
                    }
                }

                if (rightStorage.size() > 0) {
                    if ((valableCount + 24 - onSplitReworkCount) < storagesWaitIn) {
                        isSplitAll = false;
                    }
                } else {
                    if ((valableCount - onSplitReworkCount) < storagesWaitIn) {
                        isSplitAll = false;
                    }
                }
            }

            List<StorageLocation> locations = locationAllotQyery.getEquipLocation(houseId, finalPos);
            if (locations.size() > 0) {
                StorageLocation location = locations.get(0);
                AllotStorageLocation allotLocation = locationAllotQyery.createAllotStorageLocation(houseNo, location, finalPos, PositionType.Transport_Location);
                allotLocation.setSplitAll(isSplitAll);

                return GaiaResultFactory.getObject(allotLocation);
            } else {
                throw new RuntimeException("请给设备关联库位");
            }

        }
        return null;
    }

    @Override
    public ListResult<AllotOutResult> allotStorageOut(DefaultAllotPolicyParam locationAllot) {
        throw new RuntimeException("not implement");
    }

    private Integer checkLocation(List<String> locNos) {
        String sql = "Select count(*) from wms_storage s join wms_storage_location l on s.location_id = l.id  join fcs_battery_info b on s.sku_barcode = b.battery_barcode  where s.is_active=1 and  l.loc_no in(:lovNo) and b.battery_status in (:rework_battery_nos)";
        Map<String, Object> parems = new HashMap<>();
        parems.put("lovNo", locNos);
        parems.put("rework_battery_nos", DispatcherConfig.rework_battery_nos);
        return DatabaseExecuter.queryNumber(sql, parems).intValue();

    }

    private Integer checkWaitInLocation(String palletNo) {
        String sql = "Select count(*) from wms_storage s  join fcs_battery_info b on s.sku_barcode = b.battery_barcode  where s.is_active=1 and  s.pallet_no =:palletNo and b.battery_status in (:rework_battery_nos)";
        Map<String, Object> parems = new HashMap<>();
        parems.put("palletNo", palletNo);
        parems.put("rework_battery_nos", DispatcherConfig.rework_battery_nos);
        return DatabaseExecuter.queryNumber(sql, parems).intValue();

    }

    private Integer checkLocation(String locNo) {
        List<String> nos = new ArrayList<>();
        nos.add(locNo);
        return checkLocation(nos);

    }

}
