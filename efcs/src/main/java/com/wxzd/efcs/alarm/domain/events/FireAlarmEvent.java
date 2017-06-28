package com.wxzd.efcs.alarm.domain.events;

import com.wxzd.efcs.alarm.domain.enums.*;

/**
 * 火警异常
 * 
 * @author Leon Regulus on 2017/4/21.
 * @version 1.0
 * @since 1.0
 */
public class FireAlarmEvent extends AlarmEvent {

	private FireAlarmDeviceType fireAlarmDeviceType;

	private FireAlarmEventType eventType;

	private String tray_no;//托盘号

	//    private int x;//排
	//
	//    private int y;//列
	//
	//    private int z;//层

	@Override
	public String getTitle() {
		switch (fireAlarmDeviceType) {
		case Smoke_Sensor:
			return "烟感报警";
		case DTS:
			return "温感报警";
		}
		return "火灾报警";
	}

	@Override
	public AlarmType getAlarmType() {
		return AlarmType.FireAlarm;
	}

	@Override
	public AlarmLevel getAlarmLevel() {
		return AlarmLevel.High;
	}

	@Override
	public LocationType getLocationType() {
		return LocationType.Storage;
	}

	public FireAlarmEvent() {
		super();
	}

	public FireAlarmEvent(String houseNo, String deviceNo, FireAlarmDeviceType type, String location, String content) {
		super(houseNo, deviceNo, location, content);
		this.fireAlarmDeviceType = type;
	}

	public FireAlarmEventType getEventType() {
		return eventType;
	}

	public void setEventType(FireAlarmEventType eventType) {
		this.eventType = eventType;
	}

	public FireAlarmDeviceType getFireAlarmDeviceType() {
		return fireAlarmDeviceType;
	}

	public void setFireAlarmDeviceType(FireAlarmDeviceType fireAlarmDeviceType) {
		this.fireAlarmDeviceType = fireAlarmDeviceType;
	}

	public String getTray_no() {
		return tray_no;
	}

	public void setTray_no(String tray_no) {
		this.tray_no = tray_no;
	}

	//    public int getX() {
	//        return x;
	//    }
	//
	//    public void setX(int x) {
	//        this.x = x;
	//    }
	//
	//    public int getY() {
	//        return y;
	//    }
	//
	//    public void setY(int y) {
	//        this.y = y;
	//    }
	//
	//    public int getZ() {
	//        return z;
	//    }
	//
	//    public void setZ(int z) {
	//        this.z = z;
	//    }
}
