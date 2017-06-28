package com.wxzd.efcs.business.application.realtime;

/**
 * @author Leon Regulus on 2017/4/22.
 * @version 1.0
 * @since 1.0
 */
public interface DeviceRealtimeInfoManager {

    <T extends DeviceRealtimeInfo> void updateInfo(T t);

    <T extends DeviceRealtimeInfo> T getInfos(String houseNo, Class<T> clazz);
}
