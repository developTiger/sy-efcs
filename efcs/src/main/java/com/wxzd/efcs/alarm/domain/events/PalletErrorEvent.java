package com.wxzd.efcs.alarm.domain.events;

import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;

/**
 * 托盘异常
 * 
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
public class PalletErrorEvent extends AlarmEvent {

	@Override
	public String getTitle() {
		return "托盘异常";
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
		return LocationType.Pallet;
	}
}
