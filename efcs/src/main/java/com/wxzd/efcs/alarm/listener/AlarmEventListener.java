package com.wxzd.efcs.alarm.listener;

import com.wxzd.efcs.alarm.application.service.AlarmInfoAppService;
import com.wxzd.efcs.alarm.domain.events.AlarmEvent;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 向数据库中写入异常信息，推送到前台页面。
 * 异常的子类无需再将异常信息写入数据库。
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
@Component
public class AlarmEventListener extends AbstractEventListener<AlarmEvent> {

    @Autowired
    private AlarmInfoAppService alarmInfoAppService;

    @Override
    public void onEvent(AlarmEvent alarmEvent) throws Exception {
        alarmInfoAppService.dealAlarmEvent(alarmEvent);
    }
}
