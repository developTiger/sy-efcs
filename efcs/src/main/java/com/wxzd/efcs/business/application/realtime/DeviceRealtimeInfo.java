package com.wxzd.efcs.business.application.realtime;


import java.util.Date;

/**
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
public abstract class DeviceRealtimeInfo {

    /**
     * 库号
     */
    private String houseNo;
    /**
     * 上一次更新时间
     */
    private Date lastUpdateTime;

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
