package com.wxzd.efcs.business.domain.entities.form;

/***********************************************************************
 * Module:  fsc_fm_procedure.java
 * Author:  zhouzh
 * Purpose: Defines the Class fsc_fm_procedure
 ***********************************************************************/

import java.util.*;

import com.wxzd.efcs.business.domain.entities.form.base.FmPalletEntity;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.bean.AliasName;

/**
 * 出入库表单实体
 */
@AliasName("fsc_fm_procedure")
public class FmProcedure extends FmPalletEntity {

    /**
     * 库位分配时间
     */
    private Date loc_assign_time;
    /**
     * 入库库位
     */
    private String in_loc_no;
    /**
     * 入库时间
     */
    private Date in_time ;
    /**
     * 出库库位
     */
    private String out_loc_no;
    /**
     * 计划出库时间
     */
    private Date out_plan_time;
    /**
     * 是否自动出库
     */
    private int is_auto_out = 0;
    /**
     * 实际出库时间
     */
    private Date out_time;
    /**
     * 计划存放时长
     */
    private int stay_plan_time = 0;
    /**
     * 实际存放时长
     */
    private int stay_time = 0;


    public void newProcedure(WorkProcedure workProcedure,String palletNo, UUID dispatchPalletId,String equipNo) {
        this.setPallet_status(PalletStatus.In_Waiting);
        this.setWork_procedure(workProcedure);
        this.setFm_status(FmStatus.Executing);
        this.setPallet_no(palletNo);
        this.setPallet_cargo_id(dispatchPalletId);
        this.setEquip_no(equipNo);

    }


    public Date getLoc_assign_time() {
        return loc_assign_time;
    }

    public void setLoc_assign_time(Date loc_assign_time) {
        this.loc_assign_time = loc_assign_time;
    }

    public String getIn_loc_no() {
        return in_loc_no;
    }

    public void setIn_loc_no(String in_loc_no) {
        this.in_loc_no = in_loc_no;
    }

    public Date getIn_time() {
        return in_time;
    }

    public void setIn_time(Date in_time) {
        this.in_time = in_time;
    }

    public String getOut_loc_no() {
        return out_loc_no;
    }

    public void setOut_loc_no(String out_loc_no) {
        this.out_loc_no = out_loc_no;
    }

    public Date getOut_plan_time() {
        return out_plan_time;
    }

    public void setOut_plan_time(Date out_plan_time) {
        this.out_plan_time = out_plan_time;
    }

    public int getIs_auto_out() {
        return is_auto_out;
    }

    public void setIs_auto_out(int is_auto_out) {
        this.is_auto_out = is_auto_out;
    }

    public Date getOut_time() {
        return out_time;
    }

    public void setOut_time(Date out_time) {
        this.out_time = out_time;
    }

    public int getStay_plan_time() {
        return stay_plan_time;
    }

    public void setStay_plan_time(int stay_plan_time) {
        this.stay_plan_time = stay_plan_time;
    }

    public int getStay_time() {
        return stay_time;
    }

    public void setStay_time(int stay_time) {
        this.stay_time = stay_time;
    }


}