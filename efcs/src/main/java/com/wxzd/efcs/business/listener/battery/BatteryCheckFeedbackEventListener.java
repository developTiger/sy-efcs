package com.wxzd.efcs.business.listener.battery;

import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.gaia.event.listener.AbstractEventListener;

import com.wxzd.protocol.wcs.battery.feedback.BatteryCheckFeedbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 机械手抓取前获取电池校验信息，需要执行电池入库
 * @author Leon Regulus on 2017/4/19.
 * @version 1.0
 * @since 1.0
 */
@Component
public class BatteryCheckFeedbackEventListener extends AbstractEventListener<BatteryCheckFeedbackEvent> {

    @Autowired
    private ProcedureAppService ProcedureAppService;

    @Override
    public void onEvent(BatteryCheckFeedbackEvent event) throws Exception {
        // 电池校验
        event = ProcedureAppService.batteryIn(event);
    }
}
