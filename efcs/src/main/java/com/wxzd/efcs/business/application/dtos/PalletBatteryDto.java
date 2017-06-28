package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.report.domain.enums.BatteryStatus;
import com.wxzd.gaia.common.base.bean.MemberIgnore;
import com.wxzd.gaia.common.base.core.string.StringUtl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
public class PalletBatteryDto {

    private UUID id;

    private UUID sku_id;

    private String battery_barcode;

    private String battery_status;

    private String remark;

    private Date test_complete_time;
    /**
     * 拉线
     */
    @MemberIgnore
    private String house_no;

    @MemberIgnore
    private String sku_name;

    @MemberIgnore
    private String sku_no;

    /**
     * 位置类型
     */
    @MemberIgnore
    private PositionType pos_type;
    /**
     * 当前位置
     */
    @MemberIgnore
    private String current_pos;

    /**
     * 工序
     */
    @MemberIgnore
    private WorkProcedure work_procedure;

    /**
     * 托盘号
     */
    @MemberIgnore
    private String pallet_no;
    /**
     * 托盘ID
     */
    @MemberIgnore
    private UUID pallet_dispatch_id;

    /**
     * 单据号
     */
    @MemberIgnore
    private String form_no;

    /**
     * 托盘通道号
     */
    @MemberIgnore
    private String channel_no;

    /**
     * 来源设备号
     */
    @MemberIgnore
    private String from_equip_no;
    /**
     * 来源夹头编号
     */
    @MemberIgnore
    private String from_clamp_no;
    /**
     * 来源位置类型
     */
    @MemberIgnore
    private PositionType from_pos_type;
    /**
     * 来源位置编号
     */
    @MemberIgnore
    private String from_pos_no;
    /**
     * 来源位置通道号
     */
    @MemberIgnore
    private String from_pos_channel_no;
    /**
     * 去向设备号
     */
    @MemberIgnore
    private String to_equip_no;
    /**
     * 去向夹头编号
     */
    @MemberIgnore
    private String to_clamp_no;
    /**
     * 去向位置类型
     */
    @MemberIgnore
    private PositionType to_pos_type;
    /**
     * 去向位置编号
     */
    @MemberIgnore
    private String to_pos_no;
    /**
     * 去向位置通道号
     */
    @MemberIgnore
    private String to_pos_channel_no;
    /**
     * 重排编号
     */
    @MemberIgnore
    private Boolean is_resort;

    /**
     * 电池移动记录
     */
    @MemberIgnore
    private List<PalletMoveDetailDto> batteryMoveDetail;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPallet_dispatch_id() {
        return pallet_dispatch_id;
    }

    public void setPallet_dispatch_id(UUID pallet_dispatch_id) {
        this.pallet_dispatch_id = pallet_dispatch_id;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getTest_complete_time() {
        return test_complete_time;
    }

    public void setTest_complete_time(Date test_complete_time) {
        this.test_complete_time = test_complete_time;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
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

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
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

    public List<PalletMoveDetailDto> getBatteryMoveDetail() {
        return batteryMoveDetail;
    }

    public void setBatteryMoveDetail(List<PalletMoveDetailDto> batteryMoveDetail) {
        this.batteryMoveDetail = batteryMoveDetail;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public String getSku_no() {
        return sku_no;
    }

    public void setSku_no(String sku_no) {
        this.sku_no = sku_no;
    }

    public String getChannel_no() {
        return channel_no;
    }

    public void setChannel_no(String channel_no) {
        this.channel_no = channel_no;
    }

    public String getFrom_equip_no() {
        return from_equip_no;
    }

    public void setFrom_equip_no(String from_equip_no) {
        this.from_equip_no = from_equip_no;
    }

    public String getFrom_clamp_no() {
        return from_clamp_no;
    }

    public void setFrom_clamp_no(String from_clamp_no) {
        this.from_clamp_no = from_clamp_no;
    }

    public PositionType getFrom_pos_type() {
        return from_pos_type;
    }

    public void setFrom_pos_type(PositionType from_pos_type) {
        this.from_pos_type = from_pos_type;
    }

    public String getFrom_pos_no() {
        return from_pos_no;
    }

    public void setFrom_pos_no(String from_pos_no) {
        this.from_pos_no = from_pos_no;
    }

    public String getFrom_pos_channel_no() {
        return from_pos_channel_no;
    }

    public void setFrom_pos_channel_no(String from_pos_channel_no) {
        this.from_pos_channel_no = from_pos_channel_no;
    }

    public String getTo_equip_no() {
        return to_equip_no;
    }

    public void setTo_equip_no(String to_equip_no) {
        this.to_equip_no = to_equip_no;
    }

    public String getTo_clamp_no() {
        return to_clamp_no;
    }

    public void setTo_clamp_no(String to_clamp_no) {
        this.to_clamp_no = to_clamp_no;
    }

    public PositionType getTo_pos_type() {
        return to_pos_type;
    }

    public void setTo_pos_type(PositionType to_pos_type) {
        this.to_pos_type = to_pos_type;
    }

    public String getTo_pos_no() {
        return to_pos_no;
    }

    public void setTo_pos_no(String to_pos_no) {
        this.to_pos_no = to_pos_no;
    }

    public String getTo_pos_channel_no() {
        return to_pos_channel_no;
    }

    public void setTo_pos_channel_no(String to_pos_channel_no) {
        this.to_pos_channel_no = to_pos_channel_no;
    }

    public Boolean getIs_resort() {
        return is_resort;
    }

    public void setIs_resort(Boolean is_resort) {
        this.is_resort = is_resort;
    }
}
