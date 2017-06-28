package com.wxzd.efcs.report.application.dtos;

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;

/**
 * Created by LYK on 2017/4/19
 */
public class FormatStatisticsDto {

    /**
     * 化成
     */
    //化成组盘
    private Integer formation_Palletize=0;

    //高温带入
    private Integer highWait=0;

    //高温静置
    private Integer highFinish=0;

    //高温库待出
    private Integer highOut=0;

    //化成带入
    private Integer formatWait=0;

    //化成静置
    private Integer formatFinish=0;

    //化成待出
    private Integer formatOut=0;

    //化成REWORK组盘
    private Integer formationReworkPalletize=0;

    //化成REWORK带入
    private Integer formationReworkWait=0;

    //化成REWORK静置
    private Integer formationReworkFinish=0;

    //化成REWORK带出
    private Integer formationReworkOut=0;

    //拆盘带入
    private Integer formationSplitWait=0;

    //拆盘中
    private Integer formationSplitFinish=0;

    //空托盘
    private Integer isEmpty=0;


    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;


    private Integer counts;


    public Integer getFormation_Palletize() {
        return formation_Palletize;
    }

    public void setFormation_Palletize(Integer formation_Palletize) {
        this.formation_Palletize = formation_Palletize;
    }

    public Integer getHighWait() {
        return highWait;
    }

    public void setHighWait(Integer highWait) {
        this.highWait = highWait;
    }

    public Integer getHighFinish() {
        return highFinish;
    }

    public void setHighFinish(Integer highFinish) {
        this.highFinish = highFinish;
    }

    public Integer getHighOut() {
        return highOut;
    }

    public void setHighOut(Integer highOut) {
        this.highOut = highOut;
    }

    public Integer getFormatWait() {
        return formatWait;
    }

    public void setFormatWait(Integer formatWait) {
        this.formatWait = formatWait;
    }

    public Integer getFormatFinish() {
        return formatFinish;
    }

    public void setFormatFinish(Integer formatFinish) {
        this.formatFinish = formatFinish;
    }

    public Integer getFormatOut() {
        return formatOut;
    }

    public void setFormatOut(Integer formatOut) {
        this.formatOut = formatOut;
    }

    public Integer getFormationReworkPalletize() {
        return formationReworkPalletize;
    }

    public void setFormationReworkPalletize(Integer formationReworkPalletize) {
        this.formationReworkPalletize = formationReworkPalletize;
    }

    public Integer getFormationReworkWait() {
        return formationReworkWait;
    }

    public void setFormationReworkWait(Integer formationReworkWait) {
        this.formationReworkWait = formationReworkWait;
    }

    public Integer getFormationReworkFinish() {
        return formationReworkFinish;
    }

    public void setFormationReworkFinish(Integer formationReworkFinish) {
        this.formationReworkFinish = formationReworkFinish;
    }

    public Integer getFormationReworkOut() {
        return formationReworkOut;
    }

    public void setFormationReworkOut(Integer formationReworkOut) {
        this.formationReworkOut = formationReworkOut;
    }

    public Integer getFormationSplitWait() {
        return formationSplitWait;
    }

    public void setFormationSplitWait(Integer formationSplitWait) {
        this.formationSplitWait = formationSplitWait;
    }

    public Integer getFormationSplitFinish() {
        return formationSplitFinish;
    }

    public void setFormationSplitFinish(Integer formationSplitFinish) {
        this.formationSplitFinish = formationSplitFinish;
    }

    public Integer getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(Integer isEmpty) {
        this.isEmpty = isEmpty;
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
