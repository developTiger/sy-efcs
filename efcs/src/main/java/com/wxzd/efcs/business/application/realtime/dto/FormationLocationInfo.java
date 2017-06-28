package com.wxzd.efcs.business.application.realtime.dto;

import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.EquipDto;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.LocationProperties;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.LocationStatus;

/**
 * 化成柜实时状态类
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
public class FormationLocationInfo extends EquipDto {

    private LocationProperties properties;
    private LocationStatus status;
    private String pressBedStatus;
    private String deviceStatusDesc;
    private String deviceErrorMessage;

    public LocationProperties getProperties() {
        return properties;
    }

    public void setProperties(LocationProperties properties) {
        this.properties = properties;
    }

    public LocationStatus getStatus() {
        return status;
    }

    public void setStatus(LocationStatus status) {
        this.status = status;
    }

    public String getPressBedStatus() {
        return pressBedStatus;
    }

    public void setPressBedStatus(String pressBedStatus) {
        this.pressBedStatus = pressBedStatus;
    }

    public String getDeviceStatusDesc() {
        return deviceStatusDesc;
    }

    public void setDeviceStatusDesc(String deviceStatusDesc) {
        this.deviceStatusDesc = deviceStatusDesc;
    }

    public String getDeviceErrorMessage() {
        return deviceErrorMessage;
    }

    public void setDeviceErrorMessage(String deviceErrorMessage) {
        this.deviceErrorMessage = deviceErrorMessage;
    }

    public void setEquipDto(EquipDto equipDto) {
        setLine(equipDto.getLine());
        setId(equipDto.getId());
        setTray_no(equipDto.getTray_no());
        setX(equipDto.getX());
        setY(equipDto.getY());
        setZ(equipDto.getZ());
    }
}
