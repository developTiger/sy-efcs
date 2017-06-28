package com.wxzd.efcs.business.listener.dts;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.efcs.alarm.domain.enums.FireAlarmDeviceType;
import com.wxzd.efcs.alarm.domain.enums.FireAlarmEventType;
import com.wxzd.efcs.alarm.domain.events.FireAlarmEvent;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.protocol.ProtocolError;
import com.wxzd.protocol.dts.FireAlarm;
import com.wxzd.protocol.dts.feedback.FireAlarmFeedbackEvent;

/**
 * @author Leon Regulus on 2017/4/25.
 * @version 1.0
 * @since 1.0
 */
@Component
public class FireAlarmFeedbackEventListener extends AbstractEventListener<FireAlarmFeedbackEvent> {
	
	private Map<String, Date> map = new ConcurrentHashMap<>();
	
    @Override
    public void onEvent(FireAlarmFeedbackEvent event) throws Exception {

        List<FireAlarm> alarms = event.getAlarms();
        for (FireAlarm fa : alarms) {
            try {
                FireAlarmEvent alarmEvent = new FireAlarmEvent();
                alarmEvent.setHouseNo(event.getHouse_no());
                alarmEvent.setDeviceNo(event.getDevice_no());
                alarmEvent.setFireAlarmDeviceType(FireAlarmDeviceType.DTS);
                alarmEvent.setEventType(FireAlarmEventType.warn);
                
                String location = ApplicationConfig.getLocationNo(fa.getX(), fa.getY(), fa.getZ());
                alarmEvent.setLocation(location);
                
                alarmEvent.setContent("报警温度：" + fa.getTemperature());
                alarmEvent.setSource(this);
                
                Date date = map.get(location);
                Date date2 = new Date();
                //把不间断的发送限制为至少10秒发送一次
				if (date == null || date2.getTime() - date.getTime() > 15 * 1000) {
					// 发布事件
					ApplicationEventPublisher.trigger(alarmEvent);
					map.put(location, date2);
				}
                
                
                
                
            } catch (Exception ex) {
                event.setCode(ProtocolError.Unknown);
                event.setError(event.getError() + "\b\t" + ex.getMessage());
            }
        }
    }
}
