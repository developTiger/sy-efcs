package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.efcs.business.domain.enums.TimeIntervalUnit;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/29.
 */
public class ToolSchedulerDto {

    private UUID id;

    /**
     * 库号
     */
    private String house_no;
    /**
     * 库id
     */
    private UUID house_id;
    /**
     * 计划类型
     */
    private SchedulerType scheduler_type;
    /**
     * 任务类型
     */
    private String tool_type;
    /**
     * 设备号
     */
    private String tool_no;


    private String tool_location;
    /**
     * 计划开始时间
     */
    private Date scheduler_start_time; 
    /**
     * 时间间隔单位
     */
    private TimeIntervalUnit time_interval_unit;
    /**
     * 间隔时间
     */
    private int time_interval = 0; 
    /**
     * 计划结束时间
     */
    private Date scheduler_end_time;
 
    /**
     * 描述
     */
    private String desc;
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
    
    
    private Boolean is_all;
    
    private List<UUID> locationIds;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
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

    public String getTool_location() {
        return tool_location;
    }

    public void setTool_location(String tool_location) {
        this.tool_location = tool_location;
    }

    public Date getScheduler_start_time() {
        return scheduler_start_time;
    }

    public void setScheduler_start_time(Date scheduler_start_time) {
        this.scheduler_start_time = scheduler_start_time;
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

    public Date getScheduler_end_time() {
        return scheduler_end_time;
    }

    public void setScheduler_end_time(Date scheduler_end_time) {
        this.scheduler_end_time = scheduler_end_time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public Boolean getIs_all() {
        return is_all;
    }

    public void setIs_all(Boolean is_all) {
        this.is_all = is_all;
    }

    public List<UUID> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<UUID> locationIds) {
        this.locationIds = locationIds;
    }
}
