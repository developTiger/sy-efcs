package com.wxzd.efcs.alarm.domain.events;

import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.AlarmType;
import com.wxzd.efcs.alarm.domain.enums.LocationType;

/**
 * 电池NG校验过多，提醒需要停线的事件
 * @author Leon Regulus on 2017/4/26.
 * @version 1.0
 * @since 1.0
 */
public class BatteryNgOverRangeEvent extends AlarmEvent {

    @Override
    public String getTitle() {
        return "连续NG数量报警";
    }

    @Override
    public AlarmType getAlarmType() {
        return AlarmType.ErrorAlarm;
    }

    @Override
    public AlarmLevel getAlarmLevel() {
        return AlarmLevel.High;
    }

    @Override
    public LocationType getLocationType() {
        return LocationType.Line;
    }
}
