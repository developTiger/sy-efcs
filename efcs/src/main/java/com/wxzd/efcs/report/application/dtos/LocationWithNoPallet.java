package com.wxzd.efcs.report.application.dtos;

import com.wxzd.wms.core.application.dtos.LocationChartDto;

/**
 * Created by LYK on 2017/4/17
 */
public class LocationWithNoPallet extends LocationChartDto {

    /**
     * 有无空托盘
     */
    private Boolean emptyPallet = Boolean.TRUE;


    public Boolean getEmptyPallet() {
        return emptyPallet;
    }

    public void setEmptyPallet(Boolean emptyPallet) {
        this.emptyPallet = emptyPallet;
    }
}
