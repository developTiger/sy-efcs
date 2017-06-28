package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.*;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/5/18
 */
public class FcsToolSchedulerDto {

    public void FcsToolSchedulerDto(){}

    /**
     * 创建新工装
     * @param scName
     * @param originalLocationNo
     * @param scStartTime
     * @param scEndTime
     * @param interval
     * @param description
     */
    public void FcsToolSchedulerDto(String scName,String originalLocationNo,Date scStartTime,Date scEndTime,Integer interval,String description)
    {
        this.setSc_name(scName);
        this.setSc_name(scName);
        this.setOriginal_location_no(originalLocationNo);
        this.setScheduler_start_time(scStartTime);
        this.setScheduler_end_time(scEndTime);
        this.setTime_interval(interval);
        this.setDescription(description);
    }

    /**
     * 创建修改工装对象
     * @param scId
     * @param scName
     * @param originalLocationNo
     * @param scStartTime
     * @param scEndTime
     * @param interval
     * @param description
     */
    public void FcsToolSchedulerDto(UUID scId,String scName,String originalLocationNo,Date scStartTime,Date scEndTime,Integer interval,String description)
    {
        this.setId(scId);
        this.setSc_name(scName);
        this.setOriginal_location_no(originalLocationNo);
        this.setScheduler_start_time(scStartTime);
        this.setScheduler_end_time(scEndTime);
        this.setTime_interval(interval);
        this.setDescription(description);
    }

    /**
     * 标识
     */
    private UUID id;
    /**
     * 计划编号
     */
    private String sc_no;

    /**
     * 计划任务名称
     */
    private  String sc_name;

    /**
     * 计划任务发起者
     */
    private String sc_luncher;
    /**
     * 库号
     * 工装设备的冗余
     */
    private String house_no;
    /**
     * 库id
     * 工装设备的冗余
     */
    private String house_id;

    /**
     * 计划类型
     */
    private SchedulerType scheduler_type;

    /**
     * 任务类型
     */
    private String tool_type;
    /**
     * 设备号（托盘编号）
     */
    private String tool_no;

    /**
     * 当前工装所在库位No
     */
    private String current_location_no;

    /**
     * 工装原始所在库位No
     */
    private String original_location_no;

    /**
     * 计划开始时间
     */
    private Date scheduler_start_time;
    /**
     * 计划状态
     */
    private SchedulerStatus scheduler_status;
    /**
     * 计划请求体(json)
     */
    private String scheduler_obj;
    /**
     * 完成数量
     */
    private int complete_loc_count = 0;
    /**
     * 作业库位总数量
     */
    private int total_loc_count = 0;
    /**
     * 最后请求时间
     */
    private Date last_run_time;
    /**
     * 时间间隔单位
     */
    private TimeIntervalUnit time_interval_unit;
    /**
     * 间隔时间
     */
    private int time_interval = 0;
    /**
     * 下次执行时间
     */
    private Date next_run_time;

    /**
     * 计划结束时间
     */
    private Date scheduler_end_time;

    /**
     * 运行次数
     */
    private Integer run_times;

    /**
     * 描述
     */
    private String description;
    /**
     * attr1
     */
    private String attr1;
    /**
     * attr2
     */
    private String attr2;
    /**
     * attr3
     */
    private String attr3;
    /**
     * attr4
     */
    private String attr4;
    /**
     * attr5
     */
    private String attr5;
    /**
     * 异常代码
     */
    private String error_code;
    /**
     * 异常信息
     */
    private String error_msg;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSc_no() {
        return sc_no;
    }

    public void setSc_no(String sc_no) {
        this.sc_no = sc_no;
    }

    public String getSc_name() {
        return sc_name;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getHouse_id() {
        return house_id;
    }

    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    public SchedulerType getScheduler_type() {
        return scheduler_type;
    }

    public void setScheduler_type(SchedulerType scheduler_type) {
        this.scheduler_type = scheduler_type;
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

    public String getCurrent_location_no() {
        return current_location_no;
    }

    public void setCurrent_location_no(String current_location_no) {
        this.current_location_no = current_location_no;
    }

    public Date getScheduler_start_time() {
        return scheduler_start_time;
    }

    public void setScheduler_start_time(Date scheduler_start_time) {
        this.scheduler_start_time = scheduler_start_time;
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

    public int getComplete_loc_count() {
        return complete_loc_count;
    }

    public void setComplete_loc_count(int complete_loc_count) {
        this.complete_loc_count = complete_loc_count;
    }

    public int getTotal_loc_count() {
        return total_loc_count;
    }

    public void setTotal_loc_count(int total_loc_count) {
        this.total_loc_count = total_loc_count;
    }

    public Date getLast_run_time() {
        return last_run_time;
    }

    public void setLast_run_time(Date last_run_time) {
        this.last_run_time = last_run_time;
    }

    public TimeIntervalUnit getTime_interval_unit() {
        return time_interval_unit;
    }

    public void setTime_interval_unit(TimeIntervalUnit time_interval_unit) {
        this.time_interval_unit = time_interval_unit;
    }

    public int getTime_interval() {
        return time_interval;
    }

    public void setTime_interval(int time_interval) {
        this.time_interval = time_interval;
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

    public Integer getRun_times() {
        return run_times;
    }

    public void setRun_times(Integer run_times) {
        this.run_times = run_times;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getOriginal_location_no() {
        return original_location_no;
    }

    public void setOriginal_location_no(String original_location_no) {
        this.original_location_no = original_location_no;
    }

    public String getSc_luncher() {
        return sc_luncher;
    }

    public void setSc_luncher(String sc_luncher) {
        this.sc_luncher = sc_luncher;
    }
}
