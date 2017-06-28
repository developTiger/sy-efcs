package com.wxzd.policy.locationAllot.impl;

import com.wxzd.configration.catlConfig.EquitRelateLocationRowConfig;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.policy.locationAllot.*;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.Deep;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhouzh on 2017/4/17.
 */
@Service("emptyPalletAllotPolicyService")
public class EmptyPalletAllotPolicyServiceImpl implements LocationAllotService<DefaultAllotPolicyParam> {


    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    LocationAllotQyery locationAllotQyery;
    /**
     * 入库库位分配
     *
     * @param houseNo            库号
     * @param equitNo            设备号
     * @param rowAllotPolicyType 排分配策略
     * @param colAllotPolicyType 层分配策略
     * @return   ObjectResult<AllotStorageLocation>
     */
    @Override
    public ObjectResult<AllotStorageLocation> allotStorageLocationIn(WorkProcedure procedure,String palletNo, UUID houseId, String houseNo, String equitNo, AllotPolicyType rowAllotPolicyType, AllotPolicyType colAllotPolicyType) {
        Map<Deep, Integer> row = EquitRelateLocationRowConfig.getEquipRowMap(procedure,houseNo, equitNo);
        Collection<Integer> rows = row.values();

        //判断 化成组盘缓存位 空托数量是否大于设置缓存数量
        if (locationAllotQyery.getEquipLocationEmptyPalletCount(houseId, DispatcherConfig.formation_palletize_catch_location) >= DispatcherConfig.formation_palletize_catch_location_count) {
            if (locationAllotQyery.getEmptyLocationCount(houseId,rows) < DispatcherConfig.free_heightmp_storagelocation_count) {
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(houseId, DispatcherConfig.formation_palletize_catch_location);

                if (locations.size() > 0) {
                    StorageLocation location = locations.get(0);//只一个
                    AllotStorageLocation allotLocation = locationAllotQyery.createAllotStorageLocation(houseNo,location, DispatcherConfig.formation_palletize_catch_location,PositionType.Transport_Location);
                    return GaiaResultFactory.getObject(allotLocation);
                } else {
                    throw new RuntimeException("请给设备关联库位");
                }
            } else {
                ObjectResult result = locationAllotQyery.getStorageLocation(procedure,houseId,rows, rowAllotPolicyType, colAllotPolicyType);
                if(result.isSuccess()) {
                    StorageLocation location = (StorageLocation) result.getObject();
                    AllotStorageLocation allotLocation = locationAllotQyery.createAllotStorageLocation(houseNo, location, location.getLoc_no(), PositionType.Storage_Location);
                    //锁定库位
                    storageLocationService.setInFobiden(location, LockDirection.inLock, true);
                    return GaiaResultFactory.getObject(allotLocation);
                }else{
                    return result;
                }
            }
        } else {
            List<StorageLocation> locations = locationAllotQyery.getEquipLocation(houseId, DispatcherConfig.formation_palletize_catch_location);

            if (locations.size() > 0) {
                StorageLocation location = locations.get(0);//只一个
                AllotStorageLocation allotLocation = locationAllotQyery.createAllotStorageLocation(houseNo,location, DispatcherConfig.formation_palletize_catch_location,PositionType.Transport_Location);
                return GaiaResultFactory.getObject(allotLocation);
            } else {
                throw new RuntimeException("请给设备关联库位");
            }
        }

    }

    @Override
    public ListResult<AllotOutResult> allotStorageOut(DefaultAllotPolicyParam locationAllot) {
        Map<Deep, Integer> row = EquitRelateLocationRowConfig.getRowMapByKey("highTm");
        Collection<Integer> rows = row.values();
        //TODO * 待完善houseId
        StringBuilder sql = new StringBuilder("select  s.house_id,s.location_id,l.loc_no,l.x_pos,s.pallet_no,s.sto_container_no,s.sku_id,s.sku_no from wms_storage s join wms_storage_location l " +
                " on s.location_id = l.id join wms_container c on s.sto_container_no = c.CONTAINER_BARCODE where s.sto_type='container' and s.house_id =:houseId" +
                " and child_count=0 and loc_type='cubic' and forbid_out=0 and X_POS in (:pos_x) ");
        Map<String, Object> parems = new HashMap<>();
        parems.put("pos_x", rows);
        parems.put("houseId", locationAllot.getHouseId());
        try {
            StringBuilder orderSql = new StringBuilder();
            if (locationAllot.getRowAllotPolicyType() == AllotPolicyType.FromBigToSmall) {
                orderSql.append(" x_pos desc,");
            } else {
                orderSql.append(" x_pos asc,");
            }

            if (locationAllot.getColAllotPolicyType() == AllotPolicyType.FromBigToSmall) {
                orderSql.append(" y_pos desc,");
            } else {
                orderSql.append(" y_pos asc,");

            }
            orderSql.append(" z_pos desc ");

            String topOneSql="select * from ( "+ sql +") where  rownum<2 ";
            List<AllotOutResult> storageList = DatabaseExecuter.queryBeanList(topOneSql, parems, AllotOutResult.class);
            if (storageList.size() > 0) {
                //注意，如果是货物，可能来自同意货位，这里并未处理
                for (AllotOutResult storage : storageList)
                    storageLocationService.setInFobiden(storage.getLocation_id(), LockDirection.outLock, true);
            }else{
                return new ListResult<>(false,"库位中没有空托盘！");
            }

            return GaiaResultFactory.getCollection(storageList);

        } catch (Exception ex) {
            throw new RuntimeException("查询失败！");
        }

    }


}
