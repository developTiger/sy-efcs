package com.wxzd.policy.locationAllot;

import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/17.
 * 库位分配接口
 */
public interface LocationAllotService<T extends AllotPolicyParam> {

    /**
     * 入库库位分配
     *
     * @param houseNo            库号
     * @param equitNo            设备号
     * @param rowAllotPolicyType 排分配策略
     * @param colAllotPolicyType 层分配策略
     * @return   ObjectResult<AllotStorageLocation>
     */
    ObjectResult<AllotStorageLocation> allotStorageLocationIn(WorkProcedure procedure,String palletNo,UUID houseId, String houseNo, String equitNo, AllotPolicyType rowAllotPolicyType, AllotPolicyType colAllotPolicyType);


    /**
     * 出库库位/库存分配
     * @param locationAllot 分配参数
     * @return  ListResult<Storage>
     */
    ListResult<AllotOutResult> allotStorageOut(T locationAllot);

}
