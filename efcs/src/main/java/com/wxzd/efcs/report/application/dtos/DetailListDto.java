package com.wxzd.efcs.report.application.dtos;

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.report.application.querys.enums.OverdueState;

import java.util.Date;

/**
 * Created by LYK on 2017/4/18.
 * 库存明细-明细列表
 */
public class DetailListDto {

    private Integer xPos;

    private Integer yPos;

    private Integer zPos;

    //托盘号
    private String container_no;

    //托盘状态
    private PalletStatus palletStatus;

    //超期状态
    private OverdueState overdueState;

    //预计出库时间
    private Date out_plan_time;

    //等待时长
    private Double stay_time;

    //超期时长
    private Double overdue_time;

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

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
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

    public Date getOut_plan_time() {
        return out_plan_time;
    }

    public void setOut_plan_time(Date out_plan_time) {
        this.out_plan_time = out_plan_time;
    }

    public Double getStay_time() {
        return stay_time;
    }

    public void setStay_time(Double stay_time) {
        this.stay_time = stay_time;
    }

    public Double getOverdue_time() {
        return overdue_time;
    }

    public void setOverdue_time(Double overdue_time) {
        this.overdue_time = overdue_time;
    }
}
