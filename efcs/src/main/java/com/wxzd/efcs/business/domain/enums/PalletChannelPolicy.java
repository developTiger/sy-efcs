package com.wxzd.efcs.business.domain.enums;

/**
 * 通道策略枚举，指定托盘内货物的摆放排列方式
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum PalletChannelPolicy {

    /**
     * 无序
     */
    Disorder,
    /**
     * N型排序，按列计数
     */
    N,
    /**
     * Z型排序，按行计数
     */
    Z,
    /**
     * U型排序，按列头尾衔接计数
     */
    U
}
