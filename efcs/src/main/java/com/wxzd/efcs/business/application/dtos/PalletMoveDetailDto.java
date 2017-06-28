package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.bean.MemberIgnore;

import java.util.Date;
import java.util.UUID;

/**
 * 托盘调度的移动记录
 */
public class PalletMoveDetailDto {

    private UUID ID;


    /**
     * 拉线
     */
    @MemberIgnore
    private String house_no;

    private UUID pallet_dispatch_id;

    /**
     * 托号
     */
    @MemberIgnore
    private String pallet_no;

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

    /**
     * 异常代码
     */
    private EfcsErrorCode error_code;

    /**
     * 异常描述
     */
    private String error_desc;

    /**
     * 时间
     */
    private Date create_datetime;


    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
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

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public UUID getPallet_dispatch_id() {
        return pallet_dispatch_id;
    }

    public void setPallet_dispatch_id(UUID pallet_dispatch_id) {
        this.pallet_dispatch_id = pallet_dispatch_id;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }
}