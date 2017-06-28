package com.wxzd.efcs.business.application.realtime;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
@Service
public class DefaultDeviceRealtimeInfoManager implements DeviceRealtimeInfoManager{

    /**
     * 第一层索引：库号
     * 第二层索引：实时数据索引号（数据存储对象的类名）
     */
    private static HashMap<String, HashMap<String, DeviceRealtimeInfo>> realtimeInfos = new HashMap<>();

    @Override
    public <T extends DeviceRealtimeInfo> void updateInfo(T t) {
        if (!realtimeInfos.containsKey(t.getHouseNo())) {
            HashMap<String, DeviceRealtimeInfo> houseRealtimeInfos = new HashMap<>();
            realtimeInfos.put(t.getHouseNo(), houseRealtimeInfos);
        }

        t.setLastUpdateTime(new Date());
        realtimeInfos.get(t.getHouseNo()).put(t.getClass().getCanonicalName(), t);
    }

    @Override
    public <T extends DeviceRealtimeInfo> T getInfos(String houseNo, Class<T> clazz) {
        if (realtimeInfos.containsKey(houseNo)) {
            return (T) realtimeInfos.get(houseNo).get(clazz.getCanonicalName());
        }
        return null;
    }
}
