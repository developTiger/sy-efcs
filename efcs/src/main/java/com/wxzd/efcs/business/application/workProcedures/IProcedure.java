package com.wxzd.efcs.business.application.workProcedures;

import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.Warehouse;

import java.util.UUID;

/**
 * 工序基类
 */
public abstract class IProcedure {

    public IProcedure(){}

    public IProcedure(String house_no,String position_no, FmCreateMode createMode){
        this.houseNo=house_no;
        this.currentPos = position_no;
        this.fmCreateMode=createMode;

    }
    /**
     * 库号
     */
    private String houseNo;
    /**
     * 位置号 （设备号/库位号）
     */
    private String currentPos;


    private Warehouse warehouse;

    private StorageLocation storageLocation;
    /**
     * 制令号
     */
    private String com_no;

    private FmCreateMode fmCreateMode;

    private WorkProcedure workProcedure;



    private PalletDispatch palletDispatch;


    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getCom_no() {
        return com_no;
    }

    public void setCom_no(String com_no) {
        this.com_no = com_no;
    }

    public String getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(String currentPos) {
        this.currentPos = currentPos;
    }

    public FmCreateMode getFmCreateMode() {
        return fmCreateMode;
    }

    public void setFmCreateMode(FmCreateMode fmCreateMode) {
        this.fmCreateMode = fmCreateMode;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public WorkProcedure getWorkProcedure() {
        return workProcedure;
    }

    public void setWorkProcedure(WorkProcedure workProcedure) {
        this.workProcedure = workProcedure;
    }

    public PalletDispatch getPalletDispatch() {
        return palletDispatch;
    }

    public void setPalletDispatch(PalletDispatch palletDispatch) {
        this.palletDispatch = palletDispatch;
    }
}
