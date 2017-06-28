package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;

import java.util.Date;
import java.util.UUID;

/**
 * 拆盘单实体
 */
public class FmPalletSplitDto {

    private UUID id;

    /**
     * 单据号
     */
    private String form_no;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 库id
     */
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
     * 货物托盘id
     */
    private UUID pallet_cargo_id;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;

    /**
     * 开始时间
     */
    private Date proc_start_time;
    /**
     * 完成时间
     */
    private Date proc_complete_time;
    /**
     * 通道策略
     */
    private PalletChannelPolicy channel_policy;
    /**
     * 组拆盘状态
     */
    private PalletizeStatus palletize_status;

    /**
     * 拆盘策略
     */
    private String split_policy;

    /**
     * 创建方式
     */
    private FmCreateMode create_mode;
    /**
     * 表单状态
     */
    private FmStatus fm_status;

    /**
     * 异常代码
     */
    public EfcsErrorCode error_code;
    /**
     * 异常信息
     */
    public String error_desc;
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date create_datetime;




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

    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
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

    public UUID getPallet_cargo_id() {
        return pallet_cargo_id;
    }

    public void setPallet_cargo_id(UUID pallet_cargo_id) {
        this.pallet_cargo_id = pallet_cargo_id;
    }

    public PalletStatus getPallet_status() {
        return pallet_status;
    }

    public void setPallet_status(PalletStatus pallet_status) {
        this.pallet_status = pallet_status;
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

    public PalletChannelPolicy getChannel_policy() {
        return channel_policy;
    }

    public void setChannel_policy(PalletChannelPolicy channel_policy) {
        this.channel_policy = channel_policy;
    }

    public PalletizeStatus getPalletize_status() {
        return palletize_status;
    }

    public void setPalletize_status(PalletizeStatus palletize_status) {
        this.palletize_status = palletize_status;
    }

    public String getSplit_policy() {
        return split_policy;
    }

    public void setSplit_policy(String split_policy) {
        this.split_policy = split_policy;
    }

    public FmCreateMode getCreate_mode() {
        return create_mode;
    }

    public void setCreate_mode(FmCreateMode create_mode) {
        this.create_mode = create_mode;
    }

    public FmStatus getFm_status() {
        return fm_status;
    }

    public void setFm_status(FmStatus fm_status) {
        this.fm_status = fm_status;
    }

    public EfcsErrorCode getError_code() {
        return error_code;
    }

    public void setError_code(EfcsErrorCode error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }
}