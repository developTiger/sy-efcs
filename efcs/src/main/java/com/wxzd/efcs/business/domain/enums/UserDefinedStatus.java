package com.wxzd.efcs.business.domain.enums;

/**
 * Created by LYK on 2017/4/28
 * 区别于InstructionStatus,用于仓库执行中指令统计，查询
 */
public enum UserDefinedStatus {


    /**
     * 执行中 (包括Created,Waiting,Send,Executing)
     */
    Executing,
    /**
     * 执行完成
     */
    Finished

}
