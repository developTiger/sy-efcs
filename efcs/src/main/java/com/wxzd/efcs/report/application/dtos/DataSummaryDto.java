package com.wxzd.efcs.report.application.dtos;

/**
 * Created by LYK on 2017/4/18
 * 数据汇总
 */
public class DataSummaryDto {
    /**
     * 排号
     */
    private Integer x_pos;
    /**
     * 托盘数
     */
    private Integer pallet_total;
    /**
     * 已入(执行中)托盘数
     */
    private Integer dispatching;
    /**
     * 等待出托盘数
     */
    private Integer outwaiting;
    /**
     * 超期超过2小时
     */
    private Integer overdue_twohour;
    /**
     * 超期超过4小时
     */
    private Integer overdue_fourhour;

    /**
     * 超期超过8小时
     */
    private Integer overdue_eighthour;


    public Integer getX_pos() {
        return x_pos;
    }

    public void setX_pos(Integer x_pos) {
        this.x_pos = x_pos;
    }

    public Integer getPallet_total() {
        return pallet_total;
    }

    public void setPallet_total(Integer pallet_total) {
        this.pallet_total = pallet_total;
    }

    public Integer getDispatching() {
        return dispatching;
    }

    public void setDispatching(Integer dispatching) {
        this.dispatching = dispatching;
    }

    public Integer getOutwaiting() {
        return outwaiting;
    }

    public void setOutwaiting(Integer outwaiting) {
        this.outwaiting = outwaiting;
    }

    public Integer getOverdue_twohour() {
        return overdue_twohour;
    }

    public void setOverdue_twohour(Integer overdue_twohour) {
        this.overdue_twohour = overdue_twohour;
    }

    public Integer getOverdue_fourhour() {
        return overdue_fourhour;
    }

    public void setOverdue_fourhour(Integer overdue_fourhour) {
        this.overdue_fourhour = overdue_fourhour;
    }

    public Integer getOverdue_eighthour() {
        return overdue_eighthour;
    }

    public void setOverdue_eighthour(Integer overdue_eighthour) {
        this.overdue_eighthour = overdue_eighthour;
    }
}
