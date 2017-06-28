package com.wxzd.efcs.alarm.application.dtos;

import com.wxzd.gaia.common.base.bean.MemberIgnore;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 异常报警处置记录
 */
public class AlarmHandleInfoDto {

    private UUID id;
    /**
     * 报警id
     */
    private UUID alarm_id;
    /**
     * 用户id
     */
    private UUID user_id;

    /**
     * 处置信息
     */
    private String handle_info;
    /**
     * 处置时间
     */
    private Date handle_time;

    private String create_by;

    private Date create_datetime;

    public UUID getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(UUID alarm_id) {
        this.alarm_id = alarm_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getHandle_info() {
        return handle_info;
    }

    public void setHandle_info(String handle_info) {
        this.handle_info = handle_info;
    }

    public Date getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(Date handle_time) {
        this.handle_time = handle_time;
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

}
