package com.wxzd.efcs.alarm.listener;

import com.wxzd.efcs.alarm.domain.enums.FireAlarmEventType;
import com.wxzd.efcs.alarm.domain.events.FireAlarmEvent;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.protocol.wcs.transport.command.StockerFireAlarmCommandEvent;
import com.wxzd.wms.core.SerialNoGenerator;
import org.springframework.stereotype.Component;

/**
 * 火警报警事件监听
 * @author Leon Regulus on 2017/4/21.
 * @version 1.0
 * @since 1.0
 */
@Component
public class FireAlarmEventListener extends AbstractEventListener<FireAlarmEvent> {
    // 添加报警业务，向堆垛机下发报警信息
    public void onEvent(FireAlarmEvent event) throws Exception {

        if (FireAlarmEventType.warn == event.getEventType()) {
            StockerFireAlarmCommandEvent fireEvent = new StockerFireAlarmCommandEvent();
            fireEvent.setCom_no(SerialNoGenerator.getSerialNo("INS"));
            fireEvent.setLocation(event.getLocation());
            fireEvent.setHouse_no(event.getHouseNo());
            fireEvent.setSource(this);

            ApplicationEventPublisher.trigger(fireEvent);
        }
    }
}
