package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.efcs.business.domain.enums.TaskType;
import com.wxzd.wms.ddd.ExQuery;

/**
 * Created by LYK on 2017/5/18
 */
public class ToolSchedulerExQuery extends ExQuery {

    /**
     * 库号
     */
    private String house_no;

    /**
     * 计划类型
     */
    private SchedulerType scheduler_type;

    /**
     * 设备号
     */
    private String device_no;

    /**
     * 任务类型
     */
    private SchedulerType schedulerType;

    /**
     * 计划状态
     */
    private SchedulerStatus scheduler_status;


    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public SchedulerType getScheduler_type() {
        return scheduler_type;
    }

    public void setScheduler_type(SchedulerType scheduler_type) {
        this.scheduler_type = scheduler_type;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public SchedulerType getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(SchedulerType schedulerType) {
        this.schedulerType = schedulerType;
    }

    public SchedulerStatus getScheduler_status() {
        return scheduler_status;
    }

    public void setScheduler_status(SchedulerStatus scheduler_status) {
        this.scheduler_status = scheduler_status;
    }
}
