package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.*;

import java.util.Date;
import java.util.UUID;

/**
 * Created by zhaowy on 2017/6/16
 */
public class FcsToolSchedulerLocationDto {

    /**
     * 标识
     */
    private UUID id;

    /**
     * 计划编号
     */
    private String sc_no;
    /**
     * 单据类型
     */
    private String tool_type;
    /**
     * 工装编号
     */
    private String tool_no;
    /**
     * 计划id
     */
    private UUID scheduler_id;
    /**
     * 库位id
     */
    private UUID location_id;
    /**
     * 库位No
     */
    private String location_no;
    /**
     * 工单状态
     */
    private AppointmentStatus appointment_status;

    /**
     * 工单执行描述
     */
    private String description;

    /**
     * 预约时间
     */
    private Date appointment_time;
    /**
     * 完成时间
     */
    private Date complete_time;

    /**
     * 工单优先级
     */
    private Integer priority;

    public String getSc_no() {

        return sc_no;
    }

    public void setSc_no(String sc_no) {
        this.sc_no = sc_no;
    }

    public String getTool_type() {
        return tool_type;
    }

    public void setTool_type(String tool_type) {
        this.tool_type = tool_type;
    }

    public String getTool_no() {
        return tool_no;
    }

    public void setTool_no(String tool_no) {
        this.tool_no = tool_no;
    }

    public UUID getScheduler_id() {
        return scheduler_id;
    }

    public void setScheduler_id(UUID scheduler_id) {
        this.scheduler_id = scheduler_id;
    }

    public UUID getLocation_id() {
        return location_id;
    }

    public void setLocation_id(UUID location_id) {
        this.location_id = location_id;
    }

    public String getLocation_no() {
        return location_no;
    }

    public void setLocation_no(String location_no) {
        this.location_no = location_no;
    }

    public AppointmentStatus getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(AppointmentStatus appointment_status) {
        if(appointment_status.equals(AppointmentStatus.Finished)
                || appointment_status.equals(AppointmentStatus.ErrorFinished))
            this.setComplete_time(new Date());
        this.appointment_status = appointment_status;
    }

    public Date getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(Date appointment_time) {
        this.appointment_time = appointment_time;
    }

    public Date getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(Date complete_time) {
        this.complete_time = complete_time;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
