package com.wxzd.efcs.business.application.workProcedures.dto;

import com.wxzd.efcs.business.application.workProcedures.IProcedure;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;

/**
 * Created by zhouzh on 2017/4/20.
 */
public class DefaultProcedure extends IProcedure {


    public DefaultProcedure(){

    }

    private Integer stayTime;


    private String pallet_no;
    /**
     * 托盘操作
     * 默认 到达，其他操作有特定的方法
     */
    private PalletOperate palletOperate = PalletOperate.Arrived;

    public DefaultProcedure(String house_no, String position_no,FmCreateMode createMode) {
        super(house_no, position_no,createMode);
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public Integer getStayTime() {
        return stayTime;
    }

    public void setStayTime(Integer stayTime) {
        this.stayTime = stayTime;
    }


    public PalletOperate getPalletOperate() {
        return palletOperate;
    }

    public void setPalletOperate(PalletOperate palletOperate) {
        this.palletOperate = palletOperate;
    }
}
