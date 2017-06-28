package com.wxzd.efcs.business.domain.enums;

/**
 * 托盘状态枚举
 * <ol>
 * <li>待入</li>
 * <li>入中</li>
 * <li>已入</li>
 * <li>待出</li>
 * <li>出中</li>
 * <li>已出</li>
 * </ol>
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum PalletStatus {

    /**
     * 分配库位
     */
    AllotLocation,
    /**
     * 待入
     */
    In_Waiting,


    /**
     * 入中
     */
    In_Executing,
    /**
     * 已入
     */
    In_Finished,
    /**
     * 待出
     */
    Out_Waiting,
    /**
     * 出中
     */
    Out_Executing,
    /**
     * 已出
     */
    Out_Finished,

    Error_Finished
}
