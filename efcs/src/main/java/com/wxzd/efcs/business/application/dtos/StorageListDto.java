package com.wxzd.efcs.business.application.dtos;

import com.wxzd.wms.core.domain.entities.enums.StorageType;

import java.util.Date;

/**
 * Created by LYK on 2017/4/19
 */
public class StorageListDto {
    /**
     * 库
     */
    private String house_no;

    /**
     * 货位编号
     */
    private String loc_no;

    /**
     * 托盘编号
     */
    private String pallet_no;

    /**
     * 库存类型
     */
    private StorageType sto_type;

    /**
     * 容器编号
     */
    private String sto_container_no;

    /**
     * SKU条码
     */
    private String sku_barcode;

    /**
     * sku类型
     */
    private String sku_name;

    /**
     * 库存数量
     */
    private Integer sto_count;

    /**
     * 单位
     */
    private String sto_unit;
    /**
     * 入库时间
     */
    private Date create_datetime;


    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getLoc_no() {
        return loc_no;
    }

    public void setLoc_no(String loc_no) {
        this.loc_no = loc_no;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public StorageType getSto_type() {
        return sto_type;
    }

    public void setSto_type(StorageType sto_type) {
        this.sto_type = sto_type;
    }

    public String getSto_container_no() {
        return sto_container_no;
    }

    public void setSto_container_no(String sto_container_no) {
        this.sto_container_no = sto_container_no;
    }

    public String getSku_barcode() {
        return sku_barcode;
    }

    public void setSku_barcode(String sku_barcode) {
        this.sku_barcode = sku_barcode;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public Integer getSto_count() {
        return sto_count;
    }

    public void setSto_count(Integer sto_count) {
        this.sto_count = sto_count;
    }

    public String getSto_unit() {
        return sto_unit;
    }

    public void setSto_unit(String sto_unit) {
        this.sto_unit = sto_unit;
    }
}
