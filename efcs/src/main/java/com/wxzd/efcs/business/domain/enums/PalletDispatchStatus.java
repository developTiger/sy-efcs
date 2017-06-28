package com.wxzd.efcs.business.domain.enums;

/**
 * 调度状态，表明一次调度工作是否完成。每一个托盘只能同时存在一个调度工作。
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum PalletDispatchStatus {

    /**
     * 调度中
     */
    Dispatching,
    /**
     * 调度完成
     */
    Finished
}
