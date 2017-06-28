package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.realtime.dto.FormationLocationInfo;
import com.wxzd.protocol.dts.TemperatureReport;

import java.util.List;

/**
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
public interface RealtimeQueryService {

    List<TemperatureReport> queryDtsTemperature(String house_no, int xPos);

    List<FormationLocationInfo> queryFormationStatus(String house_no, int xPos);

    FormationLocationInfo queryFormationStatus(String house_no, String locPos);
}
