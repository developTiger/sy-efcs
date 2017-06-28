package com.wxzd.efcs.report.application.dtos;

import com.wxzd.efcs.alarm.domain.enums.FireAlarmDeviceType;
import com.wxzd.wms.core.application.dtos.LocationChartDto;

/**
 * Created by LYK on 2017/4/21.
 */
public class LocationChartExtendDto extends LocationChartDto {

    private Boolean is_empty;

    private FireAlarmDeviceType fireAlarmDeviceType;

    public Boolean getIs_empty() {
        return is_empty;
    }

    public void setIs_empty(Boolean is_empty) {
        this.is_empty = is_empty;
    }
}
