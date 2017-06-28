package com.wxzd.configration.catlConfig;

/**
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
public class RealTimeConfig {

    // DTS 所属库位的排号
    public static Integer[] High_Temperature_Row_No = {5,6,7,8};
    public static Integer[] Formation_Row_No = {5,6,7,8};

    /**
     * 心跳计算间隔，单位（毫秒）
     */
    public static int Heartbeat_Interval = 3000;

    /**
     * DTS正常状态名称
     */
    public static String DTS_Normal_Status = "无事件";
}
