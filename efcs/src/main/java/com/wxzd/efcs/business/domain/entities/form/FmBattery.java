package com.wxzd.efcs.business.domain.entities.form;

import com.wxzd.efcs.business.domain.entities.form.base.FmEntity;

import java.util.Date;
import java.util.UUID;

/**
 * 电池出入库记录表基类
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public class FmBattery extends FmEntity {

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
}
