package com.wxzd.efcs.report.application.dtos;

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;

/**
 * Created by LYK on 2017/4/19.
 */
public class TestStatisticsDto {

    //组盘中
    private Integer testPalletize;

    //常温1待入
    private Integer normalTemperature1Wait;

    //常温1静置
    private Integer normalTemperature1Finish;

    //常温1待出
    private Integer normalTemperature1Out;

    //OCV1待入
    private Integer testOCV1Wait;

    //OCV1
    private Integer testOCV1;

    //常温2待入
    private Integer normalTemperature2Wait;

    //常温2静置
    private Integer normalTemperature2Finish;

    //常温2待入
    private Integer normalTemperature2Out;

    //OCV2待入
    private Integer testOCV2Wait;

    //OCV2
    private Integer testOCV2;

    //待拆
    private Integer testPalletSplitWait;

    //拆盘
    private Integer testPalletSplitFinish;

    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;


    private Integer counts;

    public Integer getTestPalletize() {
        return testPalletize;
    }

    public void setTestPalletize(Integer testPalletize) {
        this.testPalletize = testPalletize;
    }

    public Integer getNormalTemperature1Wait() {
        return normalTemperature1Wait;
    }

    public void setNormalTemperature1Wait(Integer normalTemperature1Wait) {
        this.normalTemperature1Wait = normalTemperature1Wait;
    }

    public Integer getNormalTemperature1Finish() {
        return normalTemperature1Finish;
    }

    public void setNormalTemperature1Finish(Integer normalTemperature1Finish) {
        this.normalTemperature1Finish = normalTemperature1Finish;
    }

    public Integer getNormalTemperature1Out() {
        return normalTemperature1Out;
    }

    public void setNormalTemperature1Out(Integer normalTemperature1Out) {
        this.normalTemperature1Out = normalTemperature1Out;
    }

    public Integer getTestOCV1Wait() {
        return testOCV1Wait;
    }

    public void setTestOCV1Wait(Integer testOCV1Wait) {
        this.testOCV1Wait = testOCV1Wait;
    }

    public Integer getTestOCV1() {
        return testOCV1;
    }

    public void setTestOCV1(Integer testOCV1) {
        this.testOCV1 = testOCV1;
    }

    public Integer getNormalTemperature2Wait() {
        return normalTemperature2Wait;
    }

    public void setNormalTemperature2Wait(Integer normalTemperature2Wait) {
        this.normalTemperature2Wait = normalTemperature2Wait;
    }

    public Integer getNormalTemperature2Finish() {
        return normalTemperature2Finish;
    }

    public void setNormalTemperature2Finish(Integer normalTemperature2Finish) {
        this.normalTemperature2Finish = normalTemperature2Finish;
    }

    public Integer getNormalTemperature2Out() {
        return normalTemperature2Out;
    }

    public void setNormalTemperature2Out(Integer normalTemperature2Out) {
        this.normalTemperature2Out = normalTemperature2Out;
    }

    public Integer getTestOCV2Wait() {
        return testOCV2Wait;
    }

    public void setTestOCV2Wait(Integer testOCV2Wait) {
        this.testOCV2Wait = testOCV2Wait;
    }

    public Integer getTestOCV2() {
        return testOCV2;
    }

    public void setTestOCV2(Integer testOCV2) {
        this.testOCV2 = testOCV2;
    }

    public Integer getTestPalletSplitWait() {
        return testPalletSplitWait;
    }

    public void setTestPalletSplitWait(Integer testPalletSplitWait) {
        this.testPalletSplitWait = testPalletSplitWait;
    }

    public Integer getTestPalletSplitFinish() {
        return testPalletSplitFinish;
    }

    public void setTestPalletSplitFinish(Integer testPalletSplitFinish) {
        this.testPalletSplitFinish = testPalletSplitFinish;
    }

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public PalletStatus getPallet_status() {
        return pallet_status;
    }

    public void setPallet_status(PalletStatus pallet_status) {
        this.pallet_status = pallet_status;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }
}
