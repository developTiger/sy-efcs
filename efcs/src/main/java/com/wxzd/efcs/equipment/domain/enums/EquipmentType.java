package com.wxzd.efcs.equipment.domain.enums;

/**
 * 设备类型枚举
 *
 * TODO 根据需要细化设备类型
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum EquipmentType {

    /**
     * 堆垛机
     */
    Stocker,
    /**
     * 输送机
     */
    Conveyor,
    /**
     * 拉带线
     */
    Line,
    /**
     * 三坐标机器人
     */
    Robot_XYZ,
    /**
     * OCV系统
     */
    OCV_System,
    /**
     * 化成系统
     */
    Formation_System,
    /**
     * 光纤温感  todo 是否添加 @leon
     */
    DTS
}
