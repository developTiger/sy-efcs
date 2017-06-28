package com.wxzd.efcs.report.application.dtos;

import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.report.application.querys.enums.DashBoardCountType;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/21.
 * 设备异常信息
 */
public class EquipErrorDesc extends EquipmentDto{

    private UUID  houseId;

    /**
     * 序号
     */
    private Integer serialNo;
    /**
     *
     */
    private DashBoardCountType type;

    /**
     * 异常代码
     */
    private EfcsErrorCode error_code;
    /**
     * 异常信息
     */
    private String error_msg;


    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public UUID getHouseId() {
        return houseId;
    }

    public void setHouseId(UUID houseId) {
        this.houseId = houseId;
    }

    public DashBoardCountType getType() {
        return type;
    }

    public void setType(DashBoardCountType type) {
        this.type = type;
    }

    public EfcsErrorCode getError_code() {
        return error_code;
    }

    public void setError_code(EfcsErrorCode error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
