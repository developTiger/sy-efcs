package com.wxzd.configration.catlConfig;

import com.wxzd.efcs.business.domain.enums.TimeIntervalUnit;

/**
 * Created by zhouzh on 2017/5/16.
 */
public class SchedulerConfig {


    public static TimeIntervalUnit allotLocation_interval_unit = TimeIntervalUnit.SECOND;

    /**
     * 分配库位
     */
    public static Integer allotLocation_interval = 30;


    /**
     * 收数间隔时间单位
     */
    public static TimeIntervalUnit MESChargeNumber_interval_unit = TimeIntervalUnit.SECOND;

    /**
     * 收数间隔时间
     */
    public static Integer MESChargeNumber_interval = 60;


    /**
     * 空托出库间隔时间单位
     */
    public static TimeIntervalUnit EmptyPalletOut_interval_unit = TimeIntervalUnit.SECOND;

    /**
     * 空托出库间隔时间
     */
    public static Integer EmptyPalletOut_interval = 180;


    /**
     * 高温静置间隔时间单位
     */
    public static TimeIntervalUnit highTemperatureStay_interval_unit = TimeIntervalUnit.SECOND;

    /**
     * 高温静置间隔时间
     */
    public static Integer highTemperatureStay_interval = 30;


    /**
     * 高温静置间隔时间单位
     */
    public static TimeIntervalUnit normalTemperatureStay_interval_unit = TimeIntervalUnit.SECOND;

    /**
     * 高温静置间隔时间
     */
    public static Integer normalTemperatureStay_interval = 30;


}
