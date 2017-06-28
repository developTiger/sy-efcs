package com.wxzd.policy.locationAllot;

import com.wxzd.wms.core.domain.entities.enums.StorageType;

import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/17.
 */
public class AllotPolicyParam {

   private UUID houseId;

    /**
     * 库号1
     */
    private String houseNo;

    /**
     * 库存类型
     */
    private StorageType outStorageType;

    /**
     * 类型id
     * 托盘类型
     * SKU id
     */
    private String outId;

    /**
     * 排策略
     */
    private AllotPolicyType rowAllotPolicyType;

    /**
     * 层策略
     */
    private AllotPolicyType colAllotPolicyType;


    public UUID getHouseId() {
        return houseId;
    }

    public void setHouseId(UUID houseId) {
        this.houseId = houseId;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public StorageType getOutStorageType() {
        return outStorageType;
    }

    public void setOutStorageType(StorageType outStorageType) {
        this.outStorageType = outStorageType;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public AllotPolicyType getRowAllotPolicyType() {
        return rowAllotPolicyType;
    }

    public void setRowAllotPolicyType(AllotPolicyType rowAllotPolicyType) {
        this.rowAllotPolicyType = rowAllotPolicyType;
    }

    public AllotPolicyType getColAllotPolicyType() {
        return colAllotPolicyType;
    }

    public void setColAllotPolicyType(AllotPolicyType colAllotPolicyType) {
        this.colAllotPolicyType = colAllotPolicyType;
    }
}
