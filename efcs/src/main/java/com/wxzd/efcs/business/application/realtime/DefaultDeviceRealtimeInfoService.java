package com.wxzd.efcs.business.application.realtime;

import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.configration.catlConfig.RealTimeConfig;
import com.wxzd.efcs.business.application.realtime.dto.*;
import com.wxzd.protocol.dts.TemperatureReport;
import com.wxzd.protocol.dts.feedback.EventType;
import com.wxzd.protocol.dts.feedback.EventsFeedbackEvent;
import com.wxzd.protocol.dts.feedback.ResetFeedbackEvent;
import com.wxzd.protocol.dts.feedback.TemperatureFeedbackEvent;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.LocationProperties;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.PushLocationStatus;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.PushLocationStatusRequest;
import com.wxzd.wms.core.application.service.StorageLocationAppService;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
@Service
public class DefaultDeviceRealtimeInfoService implements DeviceRealtimeInfoService {

    @Autowired
    private DeviceRealtimeInfoManager deviceRealtimeInfoManager;

    @Autowired
    private StorageLocationService storageLocationService;

    @Autowired
    private StorageLocationAppService storageLocationAppService;

    @Override
    public void updateDtsTemperatureInfo(TemperatureFeedbackEvent event) {
        DtsTemperatureRealtimeInfo info = new DtsTemperatureRealtimeInfo(RealTimeConfig.High_Temperature_Row_No);
        info.setHouseNo(event.getHouse_no());
        info.setLastUpdateTime(event.getFeedbackTimestamp());

        // 解析温度事件返回的数据，将数据拆解到每个拉线中。
        for (TemperatureReport tr : event.getReports()) {
            info.add(tr);
        }

        // 按拉线保存数据
        deviceRealtimeInfoManager.updateInfo(info);
        // 更新心跳
        updateHeartbeat(event.getHouse_no(), event.getDevice_no());
    }

    @Override
    public DtsTemperatureRealtimeInfo getDtsTemperatureInfo(String houseNo) {
        return deviceRealtimeInfoManager.getInfos(houseNo, DtsTemperatureRealtimeInfo.class);
    }

    @Override
    public void updateDtsChannelStatus(EventsFeedbackEvent event) {
        DtsStatusRealtimeInfo info = getDtsChannelStatusInfo(event.getHouse_no());
        int channelId = event.getChannelId();
        EventType eventType = event.getEventType();
        // 更新通道状态
        switch (eventType) {
            case fiber_pull_out:
                info.setDeviceStatus(event.getDevice_no(), channelId, "光纤拔出");
                break;
            case fiber_breakage:
                info.setDeviceStatus(event.getDevice_no(), channelId, "光纤断裂");
                break;
            case fiber_too_long:
                info.setDeviceStatus(event.getDevice_no(), channelId, "光纤过长");
                break;
            case loss_too_large:
                info.setDeviceStatus(event.getDevice_no(), channelId, "损耗过大");
                break;
            case fiber_insertion:
                info.setDeviceStatus(event.getDevice_no(), channelId, "光纤插入");
                break;
        }
        // 更新心跳
        updateHeartbeat(event.getHouse_no(), event.getDevice_no());
    }

    @Override
    public void resetDtsChannelStatus(ResetFeedbackEvent event) {
        DtsStatusRealtimeInfo info = getDtsChannelStatusInfo(event.getHouse_no());
        info.resetDeviceStatus(event.getDevice_no());

        // 更新心跳
        updateHeartbeat(event.getHouse_no(), event.getDevice_no());
    }

    @Override
    public DtsStatusRealtimeInfo getDtsChannelStatusInfo(String houseNo) {
        DtsStatusRealtimeInfo info = deviceRealtimeInfoManager.getInfos(houseNo, DtsStatusRealtimeInfo.class);
        if (info == null) {
            info = new DtsStatusRealtimeInfo();
            deviceRealtimeInfoManager.updateInfo(info);
        }
        return info;
    }

    @Override
    public void updateFormationInfo(PushLocationStatusRequest request) {
        if (request.getList().size() > 0) {
            FormationRealtimeInfo formationRealtimeInfo = new FormationRealtimeInfo(RealTimeConfig.Formation_Row_No);
            String houseNo = request.getList().get(0).getEquipDto().getLine();
            formationRealtimeInfo.setHouseNo(houseNo);
            formationRealtimeInfo.setLastUpdateTime(new Date());

            for (PushLocationStatus item : request.getList()) {
                FormationLocationInfo info = new FormationLocationInfo();
                info.setLine(item.getEquipDto().getLine());
                info.setId(item.getEquipDto().getId());
                info.setTray_no(item.getEquipDto().getTray_no());
                info.setX(item.getEquipDto().getX());
                info.setY(item.getEquipDto().getY());
                info.setZ(item.getEquipDto().getZ());
                info.setStatus(item.getStatus());
                info.setProperties(item.getProperties());
                info.setPressBedStatus(item.getPressBedStatus());
                info.setDeviceErrorMessage(item.getDeviceErrorMessage());
                info.setDeviceStatusDesc(item.getDeviceStatusDesc());

                formationRealtimeInfo.add(info);

                // 获取当前库位的状态
//                String locationNo = ApplicationConfig.getLocationNo(item.getEquipDto().getX(), item.getEquipDto().getY(), item.getEquipDto().getZ());
//                StorageLocation currentLocation = storageLocationService.getLocationByLocationNo(houseNo, locationNo);
//                // 更新库位状态
//                if (item.getProperties() == LocationProperties.enable) {
//                    // 解除锁定
//                    storageLocationAppService.setIsLocationError(currentLocation.getId(), false, "设备异常恢复");
//                } else {
//                    // 增加锁定
//                    storageLocationAppService.setIsLocationError(currentLocation.getId(), true, item.getDeviceErrorMessage());
//                }
            }

            deviceRealtimeInfoManager.updateInfo(formationRealtimeInfo);
        }
    }

    @Override
    public FormationRealtimeInfo getFormationInfo(String houseNo) {
        return deviceRealtimeInfoManager.getInfos(houseNo, FormationRealtimeInfo.class);
    }

    @Override
    public void updateHeartbeat(String houseNo, String deviceNo) {
        HeartbeatRealtimeInfo info = getHeartbeatInfo(houseNo);

        if (info == null) {
            info = new HeartbeatRealtimeInfo(houseNo);
            deviceRealtimeInfoManager.updateInfo(info);
        }
        info.heartbeat(deviceNo);
    }

    @Override
    public HeartbeatRealtimeInfo getHeartbeatInfo(String houseNo) {
        return deviceRealtimeInfoManager.getInfos(houseNo, HeartbeatRealtimeInfo.class);
    }
}
