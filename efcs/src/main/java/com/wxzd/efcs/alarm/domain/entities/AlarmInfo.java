package com.wxzd.efcs.alarm.domain.entities;

import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;
import com.wxzd.gaia.common.base.bean.AliasName;
import com.wxzd.wms.ddd.IEntity;

import java.util.UUID;


/**
 * 异常报警信息
 *
 * @author Leon Regulus on 2017/4/21.
 * @version 1.0
 * @since 1.0
 */
@AliasName("fcs_alarm_info")
public class AlarmInfo extends IEntity {

    /**
     * 报警类型
     */
    private AlarmType alarm_type;
    /**
     * 库ID
     */
    private UUID house_id;
    /**
     * 库编号
     */
    private String house_no;
    /**
     * 设备号（该异常是从哪一个设备上报的）
     */
    private String device_no;
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
    private Boolean handled = Boolean.FALSE;


    public AlarmInfo() {
    }

    public AlarmInfo(UUID house_id, String house_no, String device_no, String location, String alarm_group, String content) {
        this.house_id = house_id;
        this.house_no = house_no;
        this.device_no = device_no;
        this.location = location;
        this.alarm_group = alarm_group;
        this.content = content;
    }

    public AlarmType getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(AlarmType alarm_type) {
        this.alarm_type = alarm_type;
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

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
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
}
