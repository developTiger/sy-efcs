package com.wxzd.efcs.business.application.querys;


import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.UserDefinedStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.wms.ddd.ExQuery;

import java.util.Date;
import java.util.UUID;

/**
 * 出入库表单实体
 */
public class FmProcedureExQuery extends ExQuery{

    private UUID id;
    /**
     * 单据号
     */
    private String form_no;

    /**
     * 库编号
     */
    private String house_no;

    /**
     * 托盘号
     */
    private String pallet_no;


    private UUID house_id;

    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;

    /**
     * 入库库位
     */
    private String in_loc_no;
    /**
     * 入库时间
     */
    private Date in_time;

    /**
     * 出库库位
     */
    private String out_loc_no;
    /**
     * 计划出库时间
     */
    private Date out_plan_time;

    private Date start_time;

    private Date complete_time;


    /**
     * 表单状态
     */
    private FmStatus fm_status;


    /**
     * 用户自定义执行状态(暂时用于仓库执行指令统计)
     */
    private UserDefinedStatus userDefinedStatus;


    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(Date complete_time) {
        this.complete_time = complete_time;
    }

    public FmStatus getFm_status() {
        return fm_status;
    }

    public void setFm_status(FmStatus fm_status) {
        this.fm_status = fm_status;
    }

    public UserDefinedStatus getUserDefinedStatus() {
        return userDefinedStatus;
    }

    public void setUserDefinedStatus(UserDefinedStatus userDefinedStatus) {
        this.userDefinedStatus = userDefinedStatus;
    }
}