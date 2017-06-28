package com.wxzd.efcs.equipment.application.dtos;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/17
 */
public class EquipmentExtensionDto {

    private UUID id;
    /**
     * equip_id
     */
    private UUID equip_id;

    /**
     * ext_key
     */
    private String ext_key;
    /**
     * ext_value
     */
    private String ext_value;
    /**
     * ext_desc
     */
    private String ext_desc;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEquip_id() {
        return equip_id;
    }

    public void setEquip_id(UUID equip_id) {
        this.equip_id = equip_id;
    }

    public String getExt_key() {
        return ext_key;
    }

    public void setExt_key(String ext_key) {
        this.ext_key = ext_key;
    }

    public String getExt_value() {
        return ext_value;
    }

    public void setExt_value(String ext_value) {
        this.ext_value = ext_value;
    }

    public String getExt_desc() {
        return ext_desc;
    }

    public void setExt_desc(String ext_desc) {
        this.ext_desc = ext_desc;
    }
}
