package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.PalletizeStatus;
import com.wxzd.efcs.business.domain.enums.UserDefinedStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.wms.ddd.ExQuery;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27.
 */
public class FmPalletizeExQuery extends ExQuery {

    private UUID id;
    /**
     * 单据号
     */
    private String form_no;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;


    private UUID house_id;
    /**
     * 库编号
     */
    private String house_no;
    /**
     * 设备号
     */
    private String equip_no;
    /**
     * 托盘号
     */
    private String pallet_no;

    /**
     * 开始时间
     */
    private Date proc_start_time;
    /**
     * 完成时间
     */
    private Date proc_complete_time;

    /**
     * 组拆盘状态
     */
    private PalletizeStatus palletize_status;

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

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getEquip_no() {
        return equip_no;
    }

    public void setEquip_no(String equip_no) {
        this.equip_no = equip_no;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public Date getProc_start_time() {
        return proc_start_time;
    }

    public void setProc_start_time(Date proc_start_time) {
        this.proc_start_time = proc_start_time;
    }

    public Date getProc_complete_time() {
        return proc_complete_time;
    }

    public void setProc_complete_time(Date proc_complete_time) {
        this.proc_complete_time = proc_complete_time;
    }

    public PalletizeStatus getPalletize_status() {
        return palletize_status;
    }

    public void setPalletize_status(PalletizeStatus palletize_status) {
        this.palletize_status = palletize_status;
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
