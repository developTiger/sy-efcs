package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.wms.ddd.ExQuery;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
public class PalletDispatchExQuery extends ExQuery {

    private UUID id;

    /**
     * 容器条码
     */
    private String container_no;

    /**
     * 库编号
     */
    private String house_no;

    /**
     * 容器类型
     */
    private String type_name;

    /**
     * 位置类型
     */
    private PositionType pos_type;
    /**
     * 当前位置
     */
    private String current_pos;

    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;

    /**
     * 调度状态
     */
    private PalletDispatchStatus dispatchStatus;

    /**
     * 工序
     */
    private WorkProcedure work_procedure;

    /**
     * 单据号
     */
    private String form_no;

    /**
     * 设备
     */
    private String equip_no;

    /**
     * 是否空托
     */
    private Integer is_empty = 1;


    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public PositionType getPos_type() {
        return pos_type;
    }

    public void setPos_type(PositionType pos_type) {
        this.pos_type = pos_type;
    }

    public String getCurrent_pos() {
        return current_pos;
    }

    public void setCurrent_pos(String current_pos) {
        this.current_pos = current_pos;
    }

    public PalletStatus getPallet_status() {
        return pallet_status;
    }

    public void setPallet_status(PalletStatus pallet_status) {
        this.pallet_status = pallet_status;
    }

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public Integer getIs_empty() {
        return is_empty;
    }

    public void setIs_empty(Integer is_empty) {
        this.is_empty = is_empty;
    }

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public String getEquip_no() {
        return equip_no;
    }

    public void setEquip_no(String equip_no) {
        this.equip_no = equip_no;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PalletDispatchStatus getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(PalletDispatchStatus dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }
}
