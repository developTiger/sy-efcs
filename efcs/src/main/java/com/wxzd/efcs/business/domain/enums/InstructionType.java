package com.wxzd.efcs.business.domain.enums;

/**
 * 指令类型，用以判定该指令是什么目的下发的。
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum InstructionType {

    /**
     * 上架（堆垛机指令）
     */
    Stock_In,
    /**
     * 下架（堆垛机指令）
     */
    Stock_Out,
    /**
     * 摆渡（堆垛机指令）
     */
    Stock_Move,
    /**
     * 移库（堆垛机指令）
     */
    Stock_Change,
    /**
     * 移动（输送线指令）
     */
    Transport
}
