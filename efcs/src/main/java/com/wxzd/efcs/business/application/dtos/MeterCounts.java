package com.wxzd.efcs.business.application.dtos;

/**
 * Created by LYK on 2017/4/20.
 */
public class MeterCounts {

    /**
     * 仪表统计
     */
    private String meterName;

    /**
     * 统计
     */
    private Integer counts;

    /**
     * 百分比
     */
    private String percentage;

    public MeterCounts() {
    }

    public MeterCounts(String meterName, Integer counts, String percentage) {
        this.meterName = meterName;
        this.counts = counts;
        this.percentage = percentage;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
