package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.queryService.RealtimeQueryService;
import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.efcs.business.application.realtime.dto.DtsTemperatureRealtimeInfo;
import com.wxzd.efcs.business.application.realtime.dto.FormationLocationInfo;
import com.wxzd.efcs.business.application.realtime.dto.FormationRealtimeInfo;
import com.wxzd.protocol.dts.TemperatureReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
@Service
public class RealtimeQueryServiceImpl implements RealtimeQueryService {

    @Autowired
    private DeviceRealtimeInfoService deviceRealtimeInfoService;

    @Override
    public List<TemperatureReport> queryDtsTemperature(String house_no, int xPos) {
        try {
            DtsTemperatureRealtimeInfo dtsTemperatureRealtimeInfo = deviceRealtimeInfoService.getDtsTemperatureInfo(house_no);
            return dtsTemperatureRealtimeInfo.getRowInfo(xPos);

        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public List<FormationLocationInfo> queryFormationStatus(String house_no, int xPos) {
        try {
            FormationRealtimeInfo formationRealtimeInfo = deviceRealtimeInfoService.getFormationInfo(house_no);
            return formationRealtimeInfo.getRowInfo(xPos);

        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public FormationLocationInfo queryFormationStatus(String house_no, String locPos) {
        try {
            FormationRealtimeInfo formationRealtimeInfo = deviceRealtimeInfoService.getFormationInfo(house_no);
            return formationRealtimeInfo.getLocationInfo(locPos);

        } catch (Exception ex) {
        }

        return null;
    }
}
