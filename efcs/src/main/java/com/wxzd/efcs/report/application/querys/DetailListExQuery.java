package com.wxzd.efcs.report.application.querys;

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.report.application.querys.enums.OverdueState;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.ddd.ExQuery;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/18
 * 库存明细，明细列表
 */
public class DetailListExQuery extends ExQuery {

    private UUID house_id;

    private UUID zone_id;

    private Integer xPos;

    private Integer yPos;

    private Integer zPos;

    //托盘号
    private String pallet_no;

    //托盘状态
    private PalletStatus palletStatus;

    //超期状态
    private OverdueState overdueState;


    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
    }

    public UUID getZone_id() {
        return zone_id;
    }

    public void setZone_id(UUID zone_id) {
        this.zone_id = zone_id;
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }

    public Integer getzPos() {
        return zPos;
    }

    public void setzPos(Integer zPos) {
        this.zPos = zPos;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public PalletStatus getPalletStatus() {
        return palletStatus;
    }

    public void setPalletStatus(PalletStatus palletStatus) {
        this.palletStatus = palletStatus;
    }

    public OverdueState getOverdueState() {
        return overdueState;
    }

    public void setOverdueState(OverdueState overdueState) {
        this.overdueState = overdueState;
    }
}
