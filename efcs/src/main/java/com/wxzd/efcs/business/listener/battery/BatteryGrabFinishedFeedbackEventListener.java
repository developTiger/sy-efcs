package com.wxzd.efcs.business.listener.battery;

import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.gaia.event.listener.AbstractEventListener;

import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFinishedFeedbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leon Regulus on 2017/4/19.
 * @version 1.0
 * @since 1.0
 */
@Component
public class BatteryGrabFinishedFeedbackEventListener extends AbstractEventListener<BatteryGrabFinishedFeedbackEvent> {

    @Autowired
    private ProcedureAppService ProcedureAppService;

    @Override
    public void onEvent(BatteryGrabFinishedFeedbackEvent event) throws Exception {
        event = ProcedureAppService.batteryGrabFinished(event);
    }

}
