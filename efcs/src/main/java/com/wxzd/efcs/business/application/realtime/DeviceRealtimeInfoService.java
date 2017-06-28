package com.wxzd.efcs.business.application.realtime;

import com.wxzd.efcs.business.application.realtime.dto.DtsStatusRealtimeInfo;
import com.wxzd.efcs.business.application.realtime.dto.DtsTemperatureRealtimeInfo;
import com.wxzd.efcs.business.application.realtime.dto.FormationRealtimeInfo;
import com.wxzd.efcs.business.application.realtime.dto.HeartbeatRealtimeInfo;
import com.wxzd.efcs.business.listener.dts.EventsFeedbackEventListener;
import com.wxzd.protocol.dts.feedback.EventsFeedbackEvent;
import com.wxzd.protocol.dts.feedback.ResetFeedbackEvent;
import com.wxzd.protocol.dts.feedback.TemperatureFeedbackEvent;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.PushLocationStatusRequest;

/**
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
public interface DeviceRealtimeInfoService {

    /**
     * DTS 温度更新
     * @param event
     */
    void updateDtsTemperatureInfo(TemperatureFeedbackEvent event);

    /**
     * DTS 温度获取
     * @param houseNo
     * @return
     */
    DtsTemperatureRealtimeInfo getDtsTemperatureInfo(String houseNo);

    /**
     * 更新DTS通道状态
     * @param event
     */
    void updateDtsChannelStatus(EventsFeedbackEvent event);

    /**
     * 重置DTS通道状态
     * @param event
     */
    void resetDtsChannelStatus(ResetFeedbackEvent event);

    /**
     * 获取DTS设备状态
     * @param houseNo
     * @return
     */
    DtsStatusRealtimeInfo getDtsChannelStatusInfo(String houseNo);

    /**
     * 化成柜 状态更新
     * @param request
     */
    void updateFormationInfo(PushLocationStatusRequest request);

    /**
     * 化成柜 状态获取
     * @param houseNo
     * @return
     */
    FormationRealtimeInfo getFormationInfo(String houseNo);

    /**
     * 更新设备心跳
     * @param houseNo
     * @param deviceNo
     */
    void updateHeartbeat(String houseNo, String deviceNo);

    /**
     * 获取心跳实时信息
     * @param houseNo
     * @return
     */
    HeartbeatRealtimeInfo getHeartbeatInfo(String houseNo);
}
