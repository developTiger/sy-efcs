package com.wxzd.policy.locationAllot.impl;

import com.wxzd.configration.catlConfig.EquitRelateLocationRowConfig;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.policy.locationAllot.*;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.Deep;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/17.
 */
@Service("detaultLocationAllotService")
public class DetaultLocationAllotServiceImpl implements LocationAllotService<DefaultAllotPolicyParam> {


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
     * @return ObjectResult<AllotStorageLocation>
     */
    @Override
    public ObjectResult<AllotStorageLocation> allotStorageLocationIn(WorkProcedure procedure,String palletNo, UUID huoseId, String houseNo, String equitNo, AllotPolicyType rowAllotPolicyType, AllotPolicyType colAllotPolicyType) {

        Map<Deep, Integer> row = EquitRelateLocationRowConfig.getEquipRowMap(procedure,houseNo, equitNo);
        Collection<Integer> rows =  row.values();

        ObjectResult result = locationAllotQyery.getStorageLocation(procedure,huoseId,rows, rowAllotPolicyType, colAllotPolicyType);

        if (!result.isSuccess()) {
            return result;
        } else {
            StorageLocation location = (StorageLocation) result.getObject();
            AllotStorageLocation allotLocation = new AllotStorageLocation();
            allotLocation.setLocationCode(location.getLoc_no());
            allotLocation.setStorageLocation(location);
            storageLocationService.setInFobiden(location, LockDirection.inLock, true);
            return GaiaResultFactory.getObject(allotLocation);
        }
    }

    @Override
    public ListResult<AllotOutResult> allotStorageOut(DefaultAllotPolicyParam locationAllot) {
        throw new RuntimeException("not implement");
    }

}
