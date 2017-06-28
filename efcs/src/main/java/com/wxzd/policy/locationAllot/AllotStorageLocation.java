package com.wxzd.policy.locationAllot;

import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.wms.core.domain.entities.StorageLocation;

/**
 * Created by zhouzh on 2017/4/17.
 */
public class AllotStorageLocation {

    private String houseNo;

    private String locationCode;


    private Boolean isSplitAll;

    private PositionType positionType;

    private StorageLocation storageLocation;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public Boolean getSplitAll() {
        return isSplitAll;
    }

    public void setSplitAll(Boolean splitAll) {
        isSplitAll = splitAll;
    }
}
