package com.wxzd.efcs.business.listener.dts;

import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.ProtocolError;
import com.wxzd.protocol.dts.feedback.EventsFeedbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leon Regulus on 2017/4/25.
 * @version 1.0
 * @since 1.0
 */
@Component
public class EventsFeedbackEventListener extends AbstractEventListener<EventsFeedbackEvent> {

    @Autowired
    private DeviceRealtimeInfoService deviceRealtimeInfoService;

    @Override
    public void onEvent(EventsFeedbackEvent event) throws Exception {
        try {
            deviceRealtimeInfoService.updateDtsChannelStatus(event);
        } catch (Exception ex) {
            event.setCode(ProtocolError.Unknown);
            event.setError(ex.getMessage());
        }
    }
}
