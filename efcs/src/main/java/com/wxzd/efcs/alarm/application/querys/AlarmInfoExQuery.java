package com.wxzd.efcs.alarm.application.querys;

import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;
import com.wxzd.wms.ddd.ExQuery;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 异常信息
 */
public class AlarmInfoExQuery extends ExQuery {

    private UUID id;

    /**
     * 报警类型
     */
    private AlarmType alarm_type;
    /**
     * 库号
     */
    private String house_no;
    /**
     * 位置类型
     */
    private LocationType location_type;
    /**
     * 库位号/设备号
     */
    private String location;

    /**
     * 报警分组
     */
    private String alarm_group;
    /**
     * 报警等级
     */
    private AlarmLevel alarm_level;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否已处理
     */
    private Boolean handled;

    public AlarmType getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(AlarmType alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public LocationType getLocation_type() {
        return location_type;
    }

    public void setLocation_type(LocationType location_type) {
        this.location_type = location_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAlarm_group() {
        return alarm_group;
    }

    public void setAlarm_group(String alarm_group) {
        this.alarm_group = alarm_group;
    }

    public AlarmLevel getAlarm_level() {
        return alarm_level;
    }

    public void setAlarm_level(AlarmLevel alarm_level) {
        this.alarm_level = alarm_level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getHandled() {
        return handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
