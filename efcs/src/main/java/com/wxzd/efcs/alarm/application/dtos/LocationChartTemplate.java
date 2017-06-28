package com.wxzd.efcs.alarm.application.dtos;

import com.wxzd.efcs.alarm.domain.enums.FireAlarmDeviceType;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.gaia.common.base.bean.MemberIgnore;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
public class LocationChartTemplate {

    private UUID id;

    //库号
    private UUID house_id;

    //拉线
    @MemberIgnore
    private String house_no;

    //库位号
    private String loc_no;

    //所在工序
    @MemberIgnore
    private String locInfo;

    @MemberIgnore
    private FireAlarmDeviceType deviceType;

    @MemberIgnore
    private Boolean is_empty;

    //库位状态
    private String storage_status;

    private Integer x_pos;

    private Integer y_pos;

    private Integer z_pos;

    private Boolean loc_is_error;

    private String loc_error_reason;

    @MemberIgnore
    private double temperature;

    @MemberIgnore
    private List<PalletDetail> betteryList;


    public LocationChartTemplate() {
    }


    public LocationChartTemplate(UUID house_id, String house_no, double temperature) {
        this.house_id = house_id;
        this.house_no = house_no;
        this.temperature = temperature;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
    }

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

    public String getLocInfo() {
        return locInfo;
    }

    public void setLocInfo(String locInfo) {
        this.locInfo = locInfo;
    }

    public FireAlarmDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(FireAlarmDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getStorage_status() {
        return storage_status;
    }

    public void setStorage_status(String storage_status) {
        this.storage_status = storage_status;
    }

    public Integer getX_pos() {
        return x_pos;
    }

    public void setX_pos(Integer x_pos) {
        this.x_pos = x_pos;
    }

    public Integer getY_pos() {
        return y_pos;
    }

    public void setY_pos(Integer y_pos) {
        this.y_pos = y_pos;
    }

    public Integer getZ_pos() {
        return z_pos;
    }

    public void setZ_pos(Integer z_pos) {
        this.z_pos = z_pos;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Boolean getLoc_is_error() {
        return loc_is_error;
    }

    public void setLoc_is_error(Boolean loc_is_error) {
        this.loc_is_error = loc_is_error;
    }

    public String getLoc_error_reason() {
        return loc_error_reason;
    }

    public void setLoc_error_reason(String loc_error_reason) {
        this.loc_error_reason = loc_error_reason;
    }

    public List<PalletDetail> getBetteryList() {
        return betteryList;
    }

    public void setBetteryList(List<PalletDetail> betteryList) {
        this.betteryList = betteryList;
    }

    public Boolean getIs_empty() {
        return is_empty;
    }

    public void setIs_empty(Boolean is_empty) {
        this.is_empty = is_empty;
    }
}
