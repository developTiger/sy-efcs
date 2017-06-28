package com.wxzd.efcs.business.listener.battery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.gaia.common.base.core.exception.ExceptionUtl;
import com.wxzd.gaia.common.base.core.log.FileLogConfig;
import com.wxzd.gaia.common.base.core.log.FileLogFactory;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFeedbackEvent;

/**
 * 电池组盘拆盘移动事件响应
 * @author Leon Regulus on 2017/4/19.
 * @version 1.0
 * @since 1.0
 */
@Component
public class BatteryGrabFeedbackEventListener extends AbstractEventListener<BatteryGrabFeedbackEvent> {

    @Autowired
    private ProcedureAppService ProcedureAppService;

    // 还要考虑到不完全拆完的情况。机械手自动将Rework的电池在当前托盘进行排序
    @Override
    public void onEvent(BatteryGrabFeedbackEvent event) throws Exception {
        try {
        	BatteryGrabFeedbackEvent event2 = ProcedureAppService.batteryGrab(event);
        } catch (Exception ex) {
        	FileLogFactory.warnCustomer("/wcs/BatteryGrabFeedbackEventListener", "", ""//
        			, JsonUtl.parseWithoutException(event)//
        			,ExceptionUtl.getExceptionDetail(ex));//
        }
        // 向WCS返回消息

    }


}
