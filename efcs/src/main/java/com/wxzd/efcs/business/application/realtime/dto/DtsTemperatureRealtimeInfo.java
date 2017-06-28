package com.wxzd.efcs.business.application.realtime.dto;

import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfo;
import com.wxzd.protocol.dts.TemperatureReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DTS 反馈的温度实时信息表，按排号管理温度信息
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
public class DtsTemperatureRealtimeInfo extends DeviceRealtimeInfo {

    private HashMap<Integer, List<TemperatureReport>> infos = new HashMap<>();

    /**
     * 创建对象时，需要预先创建每一排的数组对象
     * @param rows 排号集合
     */
    public DtsTemperatureRealtimeInfo(Integer[] rows) {
        for (Integer x : rows) {
            List<TemperatureReport> row = new ArrayList<>();
            infos.put(x, row);
        }
    }

    public void add(TemperatureReport temperatureReport) {
        // 向排索引中添加设备信息
        int x = temperatureReport.getX();
        if (infos.containsKey(x)) {
            infos.get(x).add(temperatureReport);
        }
    }

    public List<TemperatureReport> getRowInfo(int row) {
        return infos.get(row);
    }
}
