package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.WorkProcedure;

import java.util.Map;
import java.util.UUID;

/**
 * Created by jade on 2017/4/24.
 */
public class PalletizeDto {

    private String houseNo;

    private UUID houseId;

    private String palletNo;

    private String battery_barcode;

    private Integer channel_no;

    private WorkProcedure work_procedure;

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public UUID getHouseId() {
        return houseId;
    }

    public void setHouseId(UUID houseId) {
        this.houseId = houseId;
    }

    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    public String getBattery_barcode() {
        return battery_barcode;
    }

    public void setBattery_barcode(String battery_barcode) {
        this.battery_barcode = battery_barcode;
    }

    public Integer getChannel_no() {
        return channel_no;
    }

    public void setChannel_no(Integer channel_no) {
        this.channel_no = channel_no;
    }

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }
}
