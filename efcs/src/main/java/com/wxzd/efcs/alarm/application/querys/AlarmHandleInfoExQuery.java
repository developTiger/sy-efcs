package com.wxzd.efcs.alarm.application.querys;

import com.wxzd.wms.ddd.ExQuery;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 消警记录
 */
public class AlarmHandleInfoExQuery extends ExQuery {
    private UUID id;

    //报警ID
    private UUID alarmId;

    private UUID user_id;


    public AlarmHandleInfoExQuery() {
    }

    public AlarmHandleInfoExQuery(UUID alarmId) {
        this.alarmId = alarmId;
    }

    public UUID getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(UUID alarmId) {
        this.alarmId = alarmId;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
