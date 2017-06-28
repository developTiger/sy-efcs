package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.efcs.report.domain.enums.BatteryStatus;
import com.wxzd.gaia.common.base.bean.MemberIgnore;
import com.wxzd.gaia.common.base.core.string.StringUtl;

import java.util.Date;
import java.util.UUID;

/**
 * 电池出入库记录表
 */
public class FmBatteryDto {


    private UUID id;

    /**
     * 托盘货物ID
     */
    @MemberIgnore
    private UUID pallet_detail_id;

    /**
     * 品类
     */
    @MemberIgnore
    private String sku_name;

    /**
     * 电池
     */
    @MemberIgnore
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
     * skuid
     */
    private UUID sku_id;
    /**
     * 电池条码
     */
    private String battery_barcode;
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
     * 创建方式
     */
    private FmCreateMode create_mode;
    /**
     * 表单状态
     */
    private FmStatus fm_status;
    /**
     * 备注
     */
    private String remark;

    /**
     * 异常代码
     */
    private EfcsErrorCode error_code;
    /**
     * 异常信息
     */
    private String error_desc;

    /**
     * 创建时间
     */
    private Date create_datetime;


    public UUID getPallet_detail_id() {
        return pallet_detail_id;
    }

    public void setPallet_detail_id(UUID pallet_detail_id) {
        this.pallet_detail_id = pallet_detail_id;
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

    public UUID getSku_id() {
        return sku_id;
    }

    public void setSku_id(UUID sku_id) {
        this.sku_id = sku_id;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public String getBattery_status() {
        return battery_status;
    }

    public void setBattery_status(String battery_status) {
        if (!StringUtl.isEmpty(battery_status)) {
            switch (battery_status) {
                case "0":
                    this.battery_status = "OK";
                    break;
                case "1":
                    this.battery_status = "rework";
                    break;
                case "2":
                    this.battery_status = "rework";
                    break;
                case "3":
                    this.battery_status = "NG";
                    break;
                case "4":
                    this.battery_status = "rework";
                    break;
                case "-1":
                    this.battery_status = "NC";
                    break;
                case "-2":
                    this.battery_status = "fake";
                    break;
                default:
                    this.battery_status = battery_status;
            }
        } else
            this.battery_status = battery_status;
    }
}
