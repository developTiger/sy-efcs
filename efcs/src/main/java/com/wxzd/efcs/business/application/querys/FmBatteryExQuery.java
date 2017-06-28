package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.wms.ddd.ExQuery;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27
 */
public class FmBatteryExQuery extends ExQuery {

    private UUID id;

    /**
     * 单据号
     */
    private String form_no;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;

    private String house_no;
    /**
     * 设备号
     */
    private String equip_no;

    /**
     * 电池条码
     */
    private String battery_barcode;

    /**
     * 电池状态
     */
    private String battery_status;

    /**
     * 拉线位置
     */
    private String line_pos;
    /**
     * 拉线通道号
     */
    private String line_channel_no;
    /**
     * 操作时间
     */
    private Date operate_datetime;

    /**
     * 异常代码
     */
    private EfcsErrorCode error_code;


    private Date start_time;

    private Date complete_time;


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

    public String getBattery_barcode() {
        return battery_barcode;
    }

    public void setBattery_barcode(String battery_barcode) {
        this.battery_barcode = battery_barcode;
    }

    public String getLine_pos() {
        return line_pos;
    }

    public void setLine_pos(String line_pos) {
        this.line_pos = line_pos;
    }

    public String getLine_channel_no() {
        return line_channel_no;
    }

    public void setLine_channel_no(String line_channel_no) {
        this.line_channel_no = line_channel_no;
    }

    public Date getOperate_datetime() {
        return operate_datetime;
    }

    public void setOperate_datetime(Date operate_datetime) {
        this.operate_datetime = operate_datetime;
    }

    public EfcsErrorCode getError_code() {
        return error_code;
    }

    public void setError_code(EfcsErrorCode error_code) {
        this.error_code = error_code;
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
}
