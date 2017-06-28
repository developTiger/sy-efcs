package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.gaia.common.base.bean.MemberIgnore;
import com.wxzd.gaia.common.base.core.string.StringUtl;

import java.util.List;
import java.util.UUID;

/**
 * 一次调度过程中的组盘记录
 */
public class PalletDetailDto {

    private UUID id;

    /**
     * 托盘号
     */
    private UUID pallet_dispatch_id;
    /**
     * sku_id
     */
    private UUID sku_id;
    /**
     * 托盘通道号
     */
    private String channel_no;
    /**
     * 单据号
     */
    private String form_no;
    /**
     * 电池条码
     */
    private String battery_barcode;

    /**
     * 电池状态
     */
    private String battery_status;

    /**
     * 托盘号
     */
    private String pallet_no;
    /**
     * 来源设备号
     */
    private String from_equip_no;
    /**
     * 来源夹头编号
     */
    private String from_clamp_no;
    /**
     * 来源位置类型
     */
    private PositionType from_pos_type;
    /**
     * 来源位置编号
     */
    private String from_pos_no;
    /**
     * 来源位置通道号
     */
    private String from_pos_channel_no;
    /**
     * 去向设备号
     */
    private String to_equip_no;
    /**
     * 去向夹头编号
     */
    private String to_clamp_no;
    /**
     * 去向位置类型
     */
    private PositionType to_pos_type;
    /**
     * 去向位置编号
     */
    private String to_pos_no;
    /**
     * 去向位置通道号
     */
    private String to_pos_channel_no;

    /**
     * 重排编号
     */
    private Boolean is_resort;

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

    public String getChannel_no() {
        return channel_no;
    }

    public void setChannel_no(String channel_no) {
        this.channel_no = channel_no;
    }

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public String getBattery_barcode() {
        return battery_barcode;
    }

    public void setBattery_barcode(String battery_barcode) {
        this.battery_barcode = battery_barcode;
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
        }else
            this.battery_status = battery_status;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
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

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
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

    public List<PalletMoveDetailDto> getBatteryMoveDetail() {
        return batteryMoveDetail;
    }

    public void setBatteryMoveDetail(List<PalletMoveDetailDto> batteryMoveDetail) {
        this.batteryMoveDetail = batteryMoveDetail;
    }
}