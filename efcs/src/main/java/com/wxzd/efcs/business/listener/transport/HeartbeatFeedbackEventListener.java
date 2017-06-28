package com.wxzd.efcs.business.listener.transport;

import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.wcs.transport.feedback.HeartbeatFeedbackEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Leon Regulus on 2017/4/25.
 * @version 1.0
 * @since 1.0
 */
public class HeartbeatFeedbackEventListener extends AbstractEventListener<HeartbeatFeedbackEvent> {

    @Autowired
    private DeviceRealtimeInfoService deviceRealtimeInfoService;

    @Override
    public void onEvent(HeartbeatFeedbackEvent event) throws Exception {
        List<String> device = event.getDevices();
        for (String s : device) {
            deviceRealtimeInfoService.updateHeartbeat(event.getHouse_no(), s);
        }
    }

}
