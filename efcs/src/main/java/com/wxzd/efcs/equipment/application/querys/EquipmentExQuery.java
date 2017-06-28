package com.wxzd.efcs.equipment.application.querys;

import com.wxzd.efcs.equipment.domain.enums.EquipmentType;
import com.wxzd.wms.ddd.ExQuery;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/16
 */
public class EquipmentExQuery extends ExQuery {
    private UUID id;
    /**
     * 设备名称
     */
    private String equip_name;
    /**
     * 设备号（调度号）
     */
    private String equip_no;
    /**
     * 库id
     */
    private String house_id;
    /**
     * 库编号
     */
    private String house_no;
    /**
     * 库位号
     */
    private String location_no;
    /**
     * 排
     */
    private int pos_x = 0;
    /**
     * 列
     */
    private int pos_y = 0;
    /**
     * 层
     */
    private int pos_z = 0;
    /**
     * 设备图片
     */
    private String equip_img;
    /**
     * 设备类型
     */
    private EquipmentType equip_type;
    /**
     * 设备型号
     */
    private String equip_model;
    /**
     * 设备描述
     */
    private String equip_desc;
    /**
     * 厂商
     */
    private String equip_vender;
    /**
     * 厂商联系方式
     */
    private String vender_phone;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public void setEquip_name(String equip_name) {
        this.equip_name = equip_name;
    }

    public String getEquip_no() {
        return equip_no;
    }

    public void setEquip_no(String equip_no) {
        this.equip_no = equip_no;
    }

    public String getHouse_id() {
        return house_id;
    }

    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getLocation_no() {
        return location_no;
    }

    public void setLocation_no(String location_no) {
        this.location_no = location_no;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public int getPos_z() {
        return pos_z;
    }

    public void setPos_z(int pos_z) {
        this.pos_z = pos_z;
    }

    public String getEquip_img() {
        return equip_img;
    }

    public void setEquip_img(String equip_img) {
        this.equip_img = equip_img;
    }

    public EquipmentType getEquip_type() {
        return equip_type;
    }

    public void setEquip_type(EquipmentType equip_type) {
        this.equip_type = equip_type;
    }

    public String getEquip_model() {
        return equip_model;
    }

    public void setEquip_model(String equip_model) {
        this.equip_model = equip_model;
    }

    public String getEquip_desc() {
        return equip_desc;
    }

    public void setEquip_desc(String equip_desc) {
        this.equip_desc = equip_desc;
    }

    public String getEquip_vender() {
        return equip_vender;
    }

    public void setEquip_vender(String equip_vender) {
        this.equip_vender = equip_vender;
    }

    public String getVender_phone() {
        return vender_phone;
    }

    public void setVender_phone(String vender_phone) {
        this.vender_phone = vender_phone;
    }
}
