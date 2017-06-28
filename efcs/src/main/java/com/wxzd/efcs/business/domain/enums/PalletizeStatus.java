package com.wxzd.efcs.business.domain.enums;

/**
 * 组拆盘状态枚举，描述组拆盘的执行情况。组盘完成后并不会马上完成组盘单据任务，所以需要额外记录组盘的实际情况。
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum PalletizeStatus {

    /**
     * 等待
     */
    Waiting,
    /**
     * 执行中
     */
    Executing,
    /**
     * 完成
     */
    Finished
}
