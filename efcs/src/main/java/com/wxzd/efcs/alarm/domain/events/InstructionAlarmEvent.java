package com.wxzd.efcs.alarm.domain.events;

import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-23
 */
public class InstructionAlarmEvent extends AlarmEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LocationType locationType = LocationType.Device;

	@Override
	public String getTitle() {
		return "指令发送异常";
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
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

}
