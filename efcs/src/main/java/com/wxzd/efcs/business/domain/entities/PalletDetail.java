package com.wxzd.efcs.business.domain.entities;

/***********************************************************************
 * Module:  fcs_pallet_detail.java
 * Author:  zhouzh
 * Purpose: Defines the Class fcs_pallet_detail
 ***********************************************************************/

import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.gaia.common.base.bean.AliasName;
import com.wxzd.wms.ddd.IEntity;

import java.util.UUID;

/**
 * 一次调度过程中的组盘记录
 */
@AliasName("fcs_pallet_inner_detail")
public class PalletDetail extends IEntity {

    public PalletDetail(){}

    public PalletDetail(UUID skuid,UUID dispatchId,String channelNo,String formNo,String batteryBarcode,String status,String palletNo){

        this.sku_id= skuid;
        this.pallet_dispatch_id = dispatchId;
        this.setChannel_no(channelNo);
        this.setForm_no(formNo);
        this.setBattery_barcode(batteryBarcode);
        this.setBattery_status(status);
        this.setPallet_no(palletNo);
    }


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
    private PositionType  from_pos_type;
    /**
     * 来源位置编号
     */
    private String  from_pos_no;
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
    private Boolean is_resort=false;




    public void setFromPos(String equitNo,String clamp_no, String pos,String channel_no,PositionType positionType ){
        this.from_equip_no = equitNo;
        this.from_clamp_no= clamp_no;
        this.from_pos_no= pos;
        this.from_pos_channel_no= channel_no;
        this.from_pos_type= positionType;

    }

    public void setToPos(String equitNo,String clamp_no, String pos,String channel_no,PositionType positionType ){
        this.to_equip_no = equitNo;
        this.to_clamp_no= clamp_no;
        this.to_pos_no= pos;
        this.to_pos_channel_no= channel_no;
        this.to_pos_type= positionType;
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

    public String getBattery_status() {
        return battery_status;
    }

    public void setBattery_status(String battery_status) {
        this.battery_status = battery_status;
    }
}