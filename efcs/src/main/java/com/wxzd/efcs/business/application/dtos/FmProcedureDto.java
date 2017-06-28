package com.wxzd.efcs.business.application.dtos;


import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 出入库表单实体
 */
public class FmProcedureDto  {

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
     * 货物托盘id
     */
    private UUID pallet_cargo_id;
    /**
     * 托盘号
     */
    private String pallet_no;
    /**
     * 创建方式
     */
    private FmCreateMode create_mode;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;

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
    private Date in_time;

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

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public UUID getPallet_cargo_id() {
        return pallet_cargo_id;
    }

    public void setPallet_cargo_id(UUID pallet_cargo_id) {
        this.pallet_cargo_id = pallet_cargo_id;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public FmCreateMode getCreate_mode() {
        return create_mode;
    }

    public void setCreate_mode(FmCreateMode create_mode) {
        this.create_mode = create_mode;
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