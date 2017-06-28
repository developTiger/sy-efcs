package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.wms.ddd.ExQuery;

/**
 * 工装查询条件对象封装
 * Created by zhaowy on 2017/6/16
 */
public class FcsToolSchedulerExQuery extends ExQuery {

    /**
     * 工装计划名称-模糊
     */
    private String sc_name;

    /**
     * 库号-精确
     */
    private String house_no;

    /**
     * 计划类型-精确
     */
    private SchedulerType scheduler_type;

    /**
     * 设备号（托盘编号）-精确
     */
    private String tool_no;

    /**
     * 计划状态-精确
     */
    private SchedulerStatus scheduler_status;


    /**
     * 库号-精确
     */
    public String getHouse_no() {
        return house_no;
    }

    /**
     * 库号-精确
     */
    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    /**
     * 计划类型-精确
     */
    public SchedulerType getScheduler_type() {
        return scheduler_type;
    }

    /**
     * 计划类型-精确
     */
    public void setScheduler_type(SchedulerType scheduler_type) {
        this.scheduler_type = scheduler_type;
    }

    /**
     * 计划状态-精确
     */
    public SchedulerStatus getScheduler_status() {
        return scheduler_status;
    }

    /**
     * 计划状态-精确
     */
    public void setScheduler_status(SchedulerStatus scheduler_status) {
        this.scheduler_status = scheduler_status;
    }

    /**
     * 设备号（托盘编号）-精确
     */
    public String getTool_no() {
        return tool_no;
    }

    /**
     * 设备号（托盘编号）-精确
     */
    public void setTool_no(String tool_no) {
        this.tool_no = tool_no;
    }

    /**
     * 工装计划名称-模糊
     */
    public String getSc_name() {
        return sc_name;
    }

    /**
     * 工装计划名称-模糊
     */
    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }
}
