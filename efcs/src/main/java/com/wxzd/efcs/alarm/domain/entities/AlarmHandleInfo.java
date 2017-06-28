package com.wxzd.efcs.alarm.domain.entities;

import com.wxzd.gaia.common.base.bean.AliasName;
import com.wxzd.wms.ddd.IEntity;

import java.util.Date;
import java.util.UUID;

/**
 * 异常报警处置记录
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
@AliasName("fcs_alarm_handle_info")
public class AlarmHandleInfo extends IEntity{

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
}
