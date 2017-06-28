package com.wxzd.efcs.business.domain.entities;

import com.wxzd.gaia.common.base.bean.AliasName;
import com.wxzd.wms.ddd.IEntity;

import java.util.Date;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18.
 */
@AliasName("fcs_battery_info")
public class BatteryInfo extends IEntity {

    private UUID sku_id;

    private String battery_barcode;

    private String battery_status;

    private String remark;

    private Date test_complete_time;

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

    public String getBattery_status() {
        return battery_status;
    }

    public void setBattery_status(String battery_status) {
        this.battery_status = battery_status;
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
}