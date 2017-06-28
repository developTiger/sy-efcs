package com.wxzd.efcs.alarm.application.dtos;

import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;
import com.wxzd.gaia.common.base.bean.MemberIgnore;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 异常信息
 */
public class AlarmInfoDto {

    private UUID id;
    /**
     * 报警类型
     */
    private AlarmType alarm_type;

    private UUID house_id;
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
    private Boolean handled = Boolean.FALSE;

    private Date create_datetime;

    private String create_by;

    private List<AlarmHandleInfoDto> handleInfos;


    @MemberIgnore
    private Integer x_pos;

    @MemberIgnore
    private Integer y_pos;

    @MemberIgnore
    private Integer z_pos;

    @MemberIgnore
    private String warning_source;


    public AlarmInfoDto() {
    }


    public AlarmInfoDto(AlarmType alarm_type, UUID house_id, String house_no, LocationType location_type, String location, AlarmLevel alarm_level, String title, String content) {
        this.alarm_type = alarm_type;
        this.house_id = house_id;
        this.house_no = house_no;
        this.location_type = location_type;
        this.location = location;
        this.alarm_level = alarm_level;
        this.title = title;
        this.content = content;
    }

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

    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
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

    public String getWarning_source() {
        return warning_source;
    }

    public void setWarning_source(String warning_source) {
        this.warning_source = warning_source;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public List<AlarmHandleInfoDto> getHandleInfos() {
        return handleInfos;
    }

    public void setHandleInfos(List<AlarmHandleInfoDto> handleInfos) {
        this.handleInfos = handleInfos;
    }
}
