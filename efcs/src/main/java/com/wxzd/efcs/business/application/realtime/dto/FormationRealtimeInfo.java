package com.wxzd.efcs.business.application.realtime.dto;

import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
public class FormationRealtimeInfo extends DeviceRealtimeInfo {

    /**
     * 每一排化成柜的状态
     * Key：排号
     * Value：所在排的所有库位的化成柜状态
     */
    private HashMap<Integer, List<FormationLocationInfo>> rowInfos = new HashMap<>();

    /**
     * 每一个化成柜库位的状态
     * Key：库位号
     * Value：所在库位的化成柜状态信息
     */
    private HashMap<String, FormationLocationInfo> deviceInfos = new HashMap<>();

    /**
     * 创建对象时，需要预先创建每一排的数组对象
     * @param rows 排号集合
     */
    public FormationRealtimeInfo(Integer[] rows) {
        for (Integer x : rows) {
            List<FormationLocationInfo> row = new ArrayList<>();
            rowInfos.put(x, row);
        }
    }

    /**
     * 添加一个设备信息
     * @param deviceInfo
     */
    public void add(FormationLocationInfo deviceInfo) {
        // 向排索引中添加设备信息
        int x = deviceInfo.getX();
        if (rowInfos.containsKey(x)) {
            rowInfos.get(x).add(deviceInfo);
        }
        // 向库位号索引中添加设备信息
        deviceInfos.put(ApplicationConfig.getLocationNo(x, deviceInfo.getY(), deviceInfo.getZ()), deviceInfo);
    }

    public List<FormationLocationInfo> getRowInfo(int xPos) {
        return rowInfos.get(xPos);
    }

    public FormationLocationInfo getLocationInfo(String locPos) {
        return deviceInfos.get(locPos);
    }
}
