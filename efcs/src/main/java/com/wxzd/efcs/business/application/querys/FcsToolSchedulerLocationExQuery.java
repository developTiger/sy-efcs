package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.wms.ddd.ExQuery;

import java.util.Date;
import java.util.UUID;

/**
 * 工装查询条件对象封装
 * Created by zhaowy on 2017/6/16
 */
public class FcsToolSchedulerLocationExQuery extends ExQuery {

    /**
     * 计划编号-模糊
     */
    private String sc_no;
    /**
     * 单据类型-精确
     */
    private String tool_type;
    /**
     * 工装编号-模糊
     */
    private String tool_no;
    /**
     * 计划id-精确
     */
    private UUID scheduler_id;
    /**
     * 库位No-模糊
     */
    private String location_no;
    /**
     * 工单状态-精确
     */
    private AppointmentStatus appointment_status;
    /**
     * 预约开始时间-区间
     */
    private Date appointment_time_from;
    /**
     * 预约截止时间-区间
     */
    private Date appointment_time_end;
    /**
     * 完成开始时间-区间
     */
    private Date complete_time_from;
    /**
     * 完成截止时间-区间
     */
    private Date complete_time_end;

    /**
     * 工单优先级-精确
     */
    private Integer priority;

    /**
     * 计划编号-模糊
     */
    public String getSc_no() {
        return sc_no;
    }

    /**
     * 计划编号-模糊
     */
    public void setSc_no(String sc_no) {
        this.sc_no = sc_no;
    }

    /**
     * 单据类型-精确
     */
    public String getTool_type() {
        return tool_type;
    }

    /**
     * 单据类型-精确
     */
    public void setTool_type(String tool_type) {
        this.tool_type = tool_type;
    }
    /**
     * 工装编号-模糊
     */
    public String getTool_no() {
        return tool_no;
    }
    /**
     * 工装编号-模糊
     */
    public void setTool_no(String tool_no) {
        this.tool_no = tool_no;
    }
    /**
     * 计划id-精确
     */
    public UUID getScheduler_id() {
        return scheduler_id;
    }
    /**
     * 计划id-精确
     */
    public void setScheduler_id(UUID scheduler_id) {
        this.scheduler_id = scheduler_id;
    }
    /**
     * 库位No-模糊
     */
    public String getLocation_no() {
        return location_no;
    }
    /**
     * 库位No-模糊
     */
    public void setLocation_no(String location_no) {
        this.location_no = location_no;
    }
    /**
     * 工单状态-精确
     */
    public AppointmentStatus getAppointment_status() {
        return appointment_status;
    }
    /**
     * 工单状态-精确
     */
    public void setAppointment_status(AppointmentStatus appointment_status) {
        this.appointment_status = appointment_status;
    }

    /**
     * 预约开始时间-区间
     */
    public Date getAppointment_time_from() {
        return appointment_time_from;
    }

    /**
     * 预约开始时间-区间
     */
    public void setAppointment_time_from(Date appointment_time_from) {
        this.appointment_time_from = appointment_time_from;
    }

    /**
     * 预约截止时间-区间
     */
    public Date getAppointment_time_end() {
        return appointment_time_end;
    }

    /**
     * 预约截止时间-区间
     */
    public void setAppointment_time_end(Date appointment_time_end) {
        this.appointment_time_end = appointment_time_end;
    }

    /**
     * 完成开始时间-区间
     */
    public Date getComplete_time_from() {
        return complete_time_from;
    }

    /**
     * 完成开始时间-区间
     */
    public void setComplete_time_from(Date complete_time_from) {
        this.complete_time_from = complete_time_from;
    }

    /**
     * 完成截止时间-区间
     */
    public Date getComplete_time_end() {
        return complete_time_end;
    }

    /**
     * 完成截止时间-区间
     */
    public void setComplete_time_end(Date complete_time_end) {
        this.complete_time_end = complete_time_end;
    }

    /**
     * 工单优先级-精确
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 工单优先级-精确
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}
