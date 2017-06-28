package com.wxzd.efcs.alarm.domain.entities;

import com.wxzd.gaia.common.base.bean.AliasName;
import com.wxzd.wms.ddd.IEntity;

import java.util.Date;
import java.util.UUID;

/**
 * 报警信息的阅读标识
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
@AliasName("fcs_alarm_read_stamp")
public class AlarmReadStamp extends IEntity{

    /**
     * 用户id
     */
    private UUID user_id;
    /**
     * 上一次该用户信息拉取时间
     */
    private Date last_refresh_time;

    public AlarmReadStamp() {
    }

    public AlarmReadStamp(Date last_refresh_time, UUID user_id) {
        this.last_refresh_time = last_refresh_time;
        this.user_id = user_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public Date getLast_refresh_time() {
        return last_refresh_time;
    }

    public void setLast_refresh_time(Date last_refresh_time) {
        this.last_refresh_time = last_refresh_time;
    }
}
