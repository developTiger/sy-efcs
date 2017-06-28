package com.wxzd.efcs.business.domain.entities;

/***********************************************************************
 * Module:  fcs_pallet_move_detail.java
 * Author:  zhouzh
 * Purpose: Defines the Class fcs_pallet_move_detail
 ***********************************************************************/

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.entities.EfcsErrorEntity;
import com.wxzd.gaia.common.base.bean.AliasName;

import java.util.UUID;

/**
 * 托盘调度的移动记录
 */
@AliasName("fcs_pallet_move_detail")
public class PalletMoveDetail extends EfcsErrorEntity {

    private UUID pallet_dispatch_id;

    /**
     * 名称
     */
    private String procedure_name;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;
    /**
     * 单据号
     */
    private String form_no;
    /**
     * 位置类型
     */
    private PositionType pos_type;
    /**
     * 到达位置
     */
    private String arrive_pos;


    // TODO 需要检查下面自动生成的代码是否合适


    public UUID getPallet_dispatch_id() {
        return pallet_dispatch_id;
    }

    public void setPallet_dispatch_id(UUID pallet_dispatch_id) {
        this.pallet_dispatch_id = pallet_dispatch_id;
    }

    public String getProcedure_name() {
        return procedure_name;
    }

    public void setProcedure_name(String procedure_name) {
        this.procedure_name = procedure_name;
    }

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public PalletStatus getPallet_status() {
        return pallet_status;
    }

    public void setPallet_status(PalletStatus pallet_status) {
        this.pallet_status = pallet_status;
    }

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public PositionType getPos_type() {
        return pos_type;
    }

    public void setPos_type(PositionType pos_type) {
        this.pos_type = pos_type;
    }

    public String getArrive_pos() {
        return arrive_pos;
    }

    public void setArrive_pos(String arrive_pos) {
        this.arrive_pos = arrive_pos;
    }



}