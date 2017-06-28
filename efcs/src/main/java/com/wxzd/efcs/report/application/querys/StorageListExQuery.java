package com.wxzd.efcs.report.application.querys;

import com.wxzd.wms.ddd.ExQuery;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/18
 * 库存详情
 */
public class StorageListExQuery extends ExQuery {

    private UUID ID;

    //   实时库存查询   库×,货位编号×，托盘编号×，库存类型，容器编号，SKU条码×，SKU类型×，库存数量，单位，创建时间*
    /**
     * 库
     */
    private String house_name;

    /**
     * 库编号
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
     * 容器编号
     */
    private String sto_container_no;

    /**
     * SKU条码
     */
    private String sku_barcode;

    /**
     * sku类型ID
     */
    private String sku_id;


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

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
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

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
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

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }


}
