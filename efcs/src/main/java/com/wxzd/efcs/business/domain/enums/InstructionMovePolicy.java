package com.wxzd.efcs.business.domain.enums;

/**
 * 指令的移动策略。在某些情况下，电气会根据实际的情况临时调整移动的目标位置。通过移动策略，可以强制要求电气必须按照下发指令要求将货物移动指定位置。
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum InstructionMovePolicy {

    /**
     * 必须按照下发的指定移动，目标位置不能发生变化
     */
    Static,
    /**
     * 可以根据实际情况微调，目标位置可能发生变化，位置的选择由电气决定
     */
    Dynamic
}
