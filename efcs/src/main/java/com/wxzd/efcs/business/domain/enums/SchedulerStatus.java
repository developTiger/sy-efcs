package com.wxzd.efcs.business.domain.enums;

/**
 * Created by zhouzh on 2017/5/15.
 */
public enum SchedulerStatus {

    /**
     * 执行中
     */
    Running,

    /**
     * 中止
     */
    Stop,

    /**
     * 结束
     */
    Finish,

    /**
     * 计划中
     */
    OnScheduler,


    /**
     * 异常结束
     */
     ErrorFinished
}
