package com.wxzd.efcs.business.listener.battery;

import com.wxzd.efcs.business.application.service.BatteryAppService;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.wcs.battery.feedback.BatteryInPalletFeedbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取托盘内部的电芯条码
 * @author Leon Regulus on 2017/4/20.
 * @version 1.0
 * @since 1.0
 */
@Component
public class BatteryInPalletFeedbackEventListener extends AbstractEventListener<BatteryInPalletFeedbackEvent> {

    @Autowired
    private BatteryAppService BatteryAppService;

    @Override
    public void onEvent(BatteryInPalletFeedbackEvent event) throws Exception {
        event = BatteryAppService.getPalletInnerBatteries(event);

    }
}
