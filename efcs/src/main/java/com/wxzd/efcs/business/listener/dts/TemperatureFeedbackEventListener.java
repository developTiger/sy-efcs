package com.wxzd.efcs.business.listener.dts;

import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.ProtocolError;
import com.wxzd.protocol.dts.feedback.TemperatureFeedbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
@Component
public class TemperatureFeedbackEventListener extends AbstractEventListener<TemperatureFeedbackEvent> {

    @Autowired
    private DeviceRealtimeInfoService deviceRealtimeInfoService;

    @Override
    public void onEvent(TemperatureFeedbackEvent event) throws Exception {
        try {
            deviceRealtimeInfoService.updateDtsTemperatureInfo(event);
        } catch (Exception ex) {
            event.setCode(ProtocolError.Unknown);
            event.setError(ex.getMessage());
        }
    }
}
