package com.wxzd.efcs.alarm.domain.events;

import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;
import com.wxzd.gaia.event.EventObject;

/**
 * 报警事件的基类，所有需要生成报警数据的异常事件都需要继承本类
 * @author Leon Regulus on 2017/4/21.
 * @version 1.0
 * @since 1.0
 */
public abstract class AlarmEvent extends EventObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 库号
     */
    private String houseNo;
    /**
     * 设备号（异常信息是从哪一个设备上报来的）
     */
    private String deviceNo;
    /**
     * 托盘编号
     */
    private String palletNo;
    /**
     * 报警位置
     */
    private String location;
    /**
     * 报警信息
     */
    private String content;

    public AlarmEvent() {
        super();
    }

    public AlarmEvent(String houseNo, String deviceNo, String location, String content) {
        super();
        this.houseNo = houseNo;
        this.deviceNo = deviceNo;
        this.location = location;
        this.content = content;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 警报分组，通常是当前类的类名全称
     * @return
     */
    public String getGroup() {
        return this.getClass().getCanonicalName();
    }

    /**
     * 每个子类根据情况确定默认的标题
     * @return 警报标题
     */
    public abstract String getTitle();

    /**
     * 每个子类根据情况确定固定的报警类型
     * @return 报警类型
     */
    public abstract AlarmType getAlarmType();

    /**
     * 每个子类根据情况确定固定的报警等级
     * @return 报警等级
     */
    public abstract AlarmLevel getAlarmLevel();

    /**
     * 每个子类根据情况确定默认的位置类型
     * @return 位置类型
     */
    public abstract LocationType getLocationType();
}
