package com.wxzd.efcs.alarm.domain.events;

import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;

/**
 * 化成设备异常
 * 
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
public class FormationDeviceErrorEvent extends AlarmEvent {

	private String tray_no;//托盘号

	private int x;//排

	private int y;//列

	private int z;//层

	@Override
	public String getTitle() {
		return "化成设备异常";
	}

	@Override
	public AlarmType getAlarmType() {
		return AlarmType.ErrorAlarm;
	}

	@Override
	public AlarmLevel getAlarmLevel() {
		return AlarmLevel.Normal;
	}

	@Override
	public LocationType getLocationType() {
		return LocationType.Storage;
	}

	@Override
	public String getLocation() {
		return ApplicationConfig.getLocationNo(x, y, z);
	}

	public String getTray_no() {
		return tray_no;
	}

	public void setTray_no(String tray_no) {
		this.tray_no = tray_no;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
}
