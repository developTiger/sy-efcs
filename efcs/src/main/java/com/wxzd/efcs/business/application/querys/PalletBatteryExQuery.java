package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.report.domain.enums.BatteryStatus;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.wms.ddd.ExQuery;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
public class PalletBatteryExQuery extends ExQuery {

    private UUID id;

    /**
     * 电池条码
     */
    private String battery_barcode;

    /**
     * 托盘Id
     */
    private UUID palletId;

    /**
     * 电池状态
     */
    private String battery_status;

    /**
     * 单据号
     */
    private String form_no;

    /**
     * 工序
     */
    private WorkProcedure work_procedure;

    /**
     * 托盘编号
     */
    private String container_no;

    /**
     * 位置类型
     */
    private PositionType pos_type;

    /**
     * 当前位置
     */
    private String current_pos;


    /**
     * 调度状态
     */
    private PalletDispatchStatus dispatch_status;


    /**
     * 库号
     */
    private String house_no;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBattery_status() {
        if (!StringUtl.isEmpty(battery_status)) {
            switch (battery_status) {
                case "OK":
                    return "0";
                case "rework":
                    return "1,2,4";
                case "NG":
                    return "3";
                case "NC":
                    return "-1";
                case "fake":
                    return "-2";
                default:
                    return battery_status;
            }
        }
        return null;
    }

    public void setBattery_status(String battery_status) {
        this.battery_status = battery_status;
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

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
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

    public UUID getPalletId() {
        return palletId;
    }

    public void setPalletId(UUID palletId) {
        this.palletId = palletId;
    }


    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getBattery_barcode() {
        return battery_barcode;
    }

    public void setBattery_barcode(String battery_barcode) {
        this.battery_barcode = battery_barcode;
    }

    public PalletDispatchStatus getDispatch_status() {
        return dispatch_status;
    }

    public void setDispatch_status(PalletDispatchStatus dispatch_status) {
        this.dispatch_status = dispatch_status;
    }
}
