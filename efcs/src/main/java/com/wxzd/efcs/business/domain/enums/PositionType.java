package com.wxzd.efcs.business.domain.enums;

/**
 * 位置类型枚举
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum PositionType {

    /**
     * 库位中的位置（关心库位号）
     */
    Storage_Location,
    /**
     * 输送线上的位置（关心设备号）
     */
    Transport_Location,
    /**
     * 托盘中的位置（关心托盘的通道号）
     */
    Pallet,
    Manual, /**
     * 拉带线中的位置（关心拉线的通道号）
     */
    Line
}
