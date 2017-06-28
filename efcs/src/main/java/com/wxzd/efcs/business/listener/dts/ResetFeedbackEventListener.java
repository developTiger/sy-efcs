package com.wxzd.efcs.business.listener.dts;

import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.ProtocolError;
import com.wxzd.protocol.dts.feedback.ResetFeedbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leon Regulus on 2017/4/25.
 * @version 1.0
 * @since 1.0
 */
@Component
public class ResetFeedbackEventListener extends AbstractEventListener<ResetFeedbackEvent>{

    @Autowired
    DeviceRealtimeInfoService deviceRealtimeInfoService;

    @Override
    public void onEvent(ResetFeedbackEvent resetFeedbackEvent) throws Exception {
        try {
            deviceRealtimeInfoService.resetDtsChannelStatus(resetFeedbackEvent);
        } catch (Exception ex) {
            resetFeedbackEvent.setCode(ProtocolError.Unknown);
            resetFeedbackEvent.setError(ex.getMessage());
        }
    }
}
