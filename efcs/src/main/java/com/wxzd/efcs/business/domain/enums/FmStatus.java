package com.wxzd.efcs.business.domain.enums;

/**
 * 单据状态枚举
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum FmStatus {

    /**
     * 待执行（已创建）
     */
    Created,

    /**
     * 执行中
     */
    Executing,


    /**
     * 执行完成
     */
    Finished,

    /**
     * 异常结束
     */
    ErrorFinished
}
