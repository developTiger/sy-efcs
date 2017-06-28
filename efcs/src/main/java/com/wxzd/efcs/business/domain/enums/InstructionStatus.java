package com.wxzd.efcs.business.domain.enums;

/**
 * 指令的执行状态枚举
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum InstructionStatus {


    /**
     *  等待,指令没有下发时间，待更新时间后，变为
     */
    Created,
    /**
     * 待执行
     */
    Waiting ,


    /**
     * 已下发指令
     * 以下发和执行中是一个级别的，执行中某些是跳过直接完成的
     */
    Send,
    /**
     * 执行中
     */
    Executing,
    /**
     * 执行完成
     */
    Finished
}
