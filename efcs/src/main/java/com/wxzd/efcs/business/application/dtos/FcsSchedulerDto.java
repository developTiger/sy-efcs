package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.*;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/5/18
 */
public class FcsSchedulerDto {

    private UUID id;
    /**
     * 库ID
     */
    private UUID house_id;

    /**
     * 库号
     */
    private String house_no;
    /**
     * 计划类型
     */
    private SchedulerType scheduler_type;

    /**
     * 开始时间
     */
    private Date scheduler_start_time;

    /**
     * 设备号
     */
    private String device_no;
    /**
     * 任务类型
     */
    private TaskType task_type;

    /**
     * 工序信息
     */
    public WorkProcedure workProcedure;

    /**
     * 计划状态
     */
    private SchedulerStatus scheduler_status;
    /**
     * 计划请求体(json)
     */
    private String scheduler_obj;
    /**
     * 请求次数
     * /请求成功，但返回结果为业务失败，再次请求则加1，
     */
    public int run_times = 0;
    /**
     * 异常次数，调用失败，链接中断，或者抛异常
     */
    private int error_times = 0;
    /**
     * 最后请求时间
     */
    private Date last_run_time;
    /**
     * 间隔时间
     */
    private int time_interval = 0;
    /**
     * 间隔时间单位
     */
    private TimeIntervalUnit time_interval_unit;

    /**
     * 下次执行时间
     */
    private Date next_run_time;
    /**
     * 计划结束时间
     */
    private Date scheduler_end_time;

    /**
     * 异常代码
     */
    public int error_code = 0;
    /**
     * 异常信息
     */
    private String error_msg;

    private Boolean is_step1_success;

    private Boolean is_step2_success;

    private Boolean is_step3_success;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
    }

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

    public Date getScheduler_start_time() {
        return scheduler_start_time;
    }

    public void setScheduler_start_time(Date scheduler_start_time) {
        this.scheduler_start_time = scheduler_start_time;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public TaskType getTask_type() {
        return task_type;
    }

    public void setTask_type(TaskType task_type) {
        this.task_type = task_type;
    }

    public WorkProcedure getWorkProcedure() {
        return workProcedure;
    }

    public void setWorkProcedure(WorkProcedure workProcedure) {
        this.workProcedure = workProcedure;
    }

    public SchedulerStatus getScheduler_status() {
        return scheduler_status;
    }

    public void setScheduler_status(SchedulerStatus scheduler_status) {
        this.scheduler_status = scheduler_status;
    }

    public String getScheduler_obj() {
        return scheduler_obj;
    }

    public void setScheduler_obj(String scheduler_obj) {
        this.scheduler_obj = scheduler_obj;
    }

    public int getRun_times() {
        return run_times;
    }

    public void setRun_times(int run_times) {
        this.run_times = run_times;
    }

    public int getError_times() {
        return error_times;
    }

    public void setError_times(int error_times) {
        this.error_times = error_times;
    }

    public Date getLast_run_time() {
        return last_run_time;
    }

    public void setLast_run_time(Date last_run_time) {
        this.last_run_time = last_run_time;
    }

    public int getTime_interval() {
        return time_interval;
    }

    public void setTime_interval(int time_interval) {
        this.time_interval = time_interval;
    }

    public TimeIntervalUnit getTime_interval_unit() {
        return time_interval_unit;
    }

    public void setTime_interval_unit(TimeIntervalUnit time_interval_unit) {
        this.time_interval_unit = time_interval_unit;
    }

    public Date getNext_run_time() {
        return next_run_time;
    }

    public void setNext_run_time(Date next_run_time) {
        this.next_run_time = next_run_time;
    }

    public Date getScheduler_end_time() {
        return scheduler_end_time;
    }

    public void setScheduler_end_time(Date scheduler_end_time) {
        this.scheduler_end_time = scheduler_end_time;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public Boolean getIs_step1_success() {
        return is_step1_success;
    }

    public void setIs_step1_success(Boolean is_step1_success) {
        this.is_step1_success = is_step1_success;
    }

    public Boolean getIs_step2_success() {
        return is_step2_success;
    }

    public void setIs_step2_success(Boolean is_step2_success) {
        this.is_step2_success = is_step2_success;
    }

    public Boolean getIs_step3_success() {
        return is_step3_success;
    }

    public void setIs_step3_success(Boolean is_step3_success) {
        this.is_step3_success = is_step3_success;
    }
}
