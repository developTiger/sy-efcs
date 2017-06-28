package com.wxzd.efcs.report.application.dtos;

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;

import java.util.UUID;

/**
 * Created by LYK on 2017/4/20
 * 首页仪表盘统计
 */
public class DashBoardStatisticsDto {

    private UUID ID;
    /**
     * 库id
     */
    private UUID houseId;

    /**
     * 拉线
     */
    private String houseNo;

    /**
     * 库名称
     */
    private String houseName;


    /**
     * 组盘中
     */
    private Integer formation_Palletize = 0;
    /**
     * 高温
     */
    private Integer highCount = 0;

    /**
     * 化成
     */
    private Integer formationCount = 0;

    /**
     * 化成REWORK
     */
    private Integer formationReworkCount = 0;

    /**
     * 化成拆盘
     */
    private Integer formationSplit = 0;


    /**
     * 测试组盘
     */
    private Integer testPalletizeCount = 0;

    /**
     * 1次常温
     */
    private Integer normalTemperature1Count = 0;

    /**
     * 2次常温
     */
    private Integer normalTemperature2Count = 0;

    /**
     * 测试拆盘
     */
    private Integer testSplitCount = 0;

    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;

    /**
     * 数量
     */
    private Integer counts;

    public DashBoardStatisticsDto() {
    }

    public DashBoardStatisticsDto(UUID houseId, String houseNo, String houseName) {
        this.houseId = houseId;
        this.houseNo = houseNo;
        this.houseName = houseName;
    }


    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public UUID getHouseId() {
        return houseId;
    }

    public void setHouseId(UUID houseId) {
        this.houseId = houseId;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public Integer getFormation_Palletize() {
        return formation_Palletize;
    }

    public void setFormation_Palletize(Integer formation_Palletize) {
        this.formation_Palletize = formation_Palletize;
    }

    public Integer getHighCount() {
        return highCount;
    }

    public void setHighCount(Integer highCount) {
        this.highCount = highCount;
    }

    public Integer getFormationCount() {
        return formationCount;
    }

    public void setFormationCount(Integer formationCount) {
        this.formationCount = formationCount;
    }

    public Integer getFormationReworkCount() {
        return formationReworkCount;
    }

    public void setFormationReworkCount(Integer formationReworkCount) {
        this.formationReworkCount = formationReworkCount;
    }

    public Integer getFormationSplit() {
        return formationSplit;
    }

    public void setFormationSplit(Integer formationSplit) {
        this.formationSplit = formationSplit;
    }

    public Integer getTestPalletizeCount() {
        return testPalletizeCount;
    }

    public void setTestPalletizeCount(Integer testPalletizeCount) {
        this.testPalletizeCount = testPalletizeCount;
    }

    public Integer getNormalTemperature1Count() {
        return normalTemperature1Count;
    }

    public void setNormalTemperature1Count(Integer normalTemperature1Count) {
        this.normalTemperature1Count = normalTemperature1Count;
    }

    public Integer getNormalTemperature2Count() {
        return normalTemperature2Count;
    }

    public void setNormalTemperature2Count(Integer normalTemperature2Count) {
        this.normalTemperature2Count = normalTemperature2Count;
    }

    public Integer getTestSplitCount() {
        return testSplitCount;
    }

    public void setTestSplitCount(Integer testSplitCount) {
        this.testSplitCount = testSplitCount;
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
