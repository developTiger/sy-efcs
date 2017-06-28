package com.wxzd.policy.locationAllot;

import com.wxzd.wms.core.domain.entities.enums.StorageType;

import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/16.
 */
public class AllotOutResult {

    private UUID house_id;
    /**
     * 货位_id
     */
    private UUID location_id;

    /**
     * 库编码
     */
    private String loc_no;

    /**
     * X坐标
     */
    private Integer x_pos;


    /**
     * 托盘编号
     */
    private String pallet_no;

    /**
     * 容器编号
     */
    private String sto_container_no;


    /**
     * Sku_id
     */
    private UUID sku_id;

    /**
     * SKU编号
     */
    private String sku_no;


    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
    }

    public UUID getLocation_id() {
        return location_id;
    }

    public void setLocation_id(UUID location_id) {
        this.location_id = location_id;
    }

    public String getLoc_no() {
        return loc_no;
    }

    public void setLoc_no(String loc_no) {
        this.loc_no = loc_no;
    }

    public Integer getX_pos() {
        return x_pos;
    }

    public void setX_pos(Integer x_pos) {
        this.x_pos = x_pos;
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

    public UUID getSku_id() {
        return sku_id;
    }

    public void setSku_id(UUID sku_id) {
        this.sku_id = sku_id;
    }

    public String getSku_no() {
        return sku_no;
    }

    public void setSku_no(String sku_no) {
        this.sku_no = sku_no;
    }
}
