package com.wxzd.configration.catlConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LYK on 2017/4/22.
 */
public class AlarmInfoConfig {


    private static Integer Alarm_Push_Amount = 5;

//    private static String Formation = "化成库";
//    private static String HighTemplate = "高温库";
//    private static String NorlmalTemplate = "常温库";

    private static Map<String, Object> mapName = new HashMap<String, Object>() {
        {
            put("HighTemplate", "高温库");
            put("Formation", "化成库");
            put("NorlmalTemplate", "常温库");
        }
    };

    private static Map<Integer, Object> rowMap = new HashMap<Integer, Object>() {
        {
            put(1, "HighTemplate");
            put(2, "HighTemplate");
            put(3, "Formation");
            put(4, "Formation");
            put(5, "NorlmalTemplate");
            put(6, "NorlmalTemplate");
            put(7, "NorlmalTemplate");
            put(8, "NorlmalTemplate");
        }
    };

    private static Map<String, Object> rowMap1 = new HashMap<String, Object>() {
        {
            put("1", "HighTemplate");
            put("2", "HighTemplate");
            put("3", "Formation");
            put("4", "Formation");
            put("5", "NorlmalTemplate");
            put("6", "NorlmalTemplate");
            put("7", "NorlmalTemplate");
            put("8", "NorlmalTemplate");
        }
    };

    public static Map<String, Object> getRowMap1() {
        return rowMap1;
    }

    public static void setRowMap1(Map<String, Object> rowMap1) {
        AlarmInfoConfig.rowMap1 = rowMap1;
    }

    public static Integer getAlarm_Push_Amount() {
        return Alarm_Push_Amount;
    }

    public static void setAlarm_Push_Amount(Integer alarm_Push_Amount) {
        Alarm_Push_Amount = alarm_Push_Amount;
    }

    public static Map<Integer, Object> getRowMap() {
        return rowMap;
    }

    public static void setRowMap(Map<Integer, Object> rowMap) {
        AlarmInfoConfig.rowMap = rowMap;
    }

    public static Map<String, Object> getMapName() {
        return mapName;
    }

    public static void setMapName(Map<String, Object> mapName) {
        AlarmInfoConfig.mapName = mapName;
    }
}
