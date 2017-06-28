package com.wxzd.efcs.business.application.workProcedures.dto;

import com.wxzd.efcs.business.application.workProcedures.IProcedure;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;

import java.util.List;

/**
 * Created by zhouzh on 2017/4/19.
 */
public class BatteryInPrecedureInfo extends IProcedure {


    public BatteryInPrecedureInfo(String houseNo, String positionNo, FmCreateMode createMode) {
        super(houseNo, positionNo,createMode);
    }


    private List<BatteryInContainer> listBattery;

    public List<BatteryInContainer> getListBattery() {
        return listBattery;
    }

    public void setListBattery(List<BatteryInContainer> listBattery) {
        this.listBattery = listBattery;
    }
}
