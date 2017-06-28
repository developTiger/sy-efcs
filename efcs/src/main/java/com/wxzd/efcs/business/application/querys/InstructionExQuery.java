package com.wxzd.efcs.business.application.querys;

import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.wms.ddd.ExQuery;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/26
 */
public class InstructionExQuery extends ExQuery {

    private UUID id;
    /**
     * 指令号
     */
    private String instr_no;
    /**
     * 指令类型
     */
    private InstructionType instr_type;

    /**
     * 下发策略
     * <p>
     * 只有设定了下发策略，scheduler_time和queue_no才生效
     */
    private SendType send_type;

    /**
     * 队列号
     */
    private String queue_no;
    /**
     * 库号
     */
    private String house_no;
    /**
     * 设备号
     */
    private String equip_no;
    /**
     * 托盘号
     */
    private String pallet_no;
    /**
     * 单据号
     */
    private String form_no;
    /**
     * 工序类型
     */
    private WorkProcedure work_procedure;

    /**
     * 实际下发时间
     */
    private Date send_time;

    /**
     * 执行状态
     */
    private InstructionStatus instr_status;


    /**
     * 用户自定义执行状态(暂时用于仓库执行指令统计)
     */
    private UserDefinedStatus userDefinedStatus;


    private Date start_time;

    private Date complete_time;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInstr_no() {
        return instr_no;
    }

    public void setInstr_no(String instr_no) {
        this.instr_no = instr_no;
    }

    public InstructionType getInstr_type() {
        return instr_type;
    }

    public void setInstr_type(InstructionType instr_type) {
        this.instr_type = instr_type;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getEquip_no() {
        return equip_no;
    }

    public void setEquip_no(String equip_no) {
        this.equip_no = equip_no;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(Date complete_time) {
        this.complete_time = complete_time;
    }

    public String getQueue_no() {
        return queue_no;
    }

    public void setQueue_no(String queue_no) {
        this.queue_no = queue_no;
    }

    public SendType getSend_type() {
        return send_type;
    }

    public void setSend_type(SendType send_type) {
        this.send_type = send_type;
    }

    public Date getSend_time() {
        return send_time;
    }

    public void setSend_time(Date send_time) {
        this.send_time = send_time;
    }

    public UserDefinedStatus getUserDefinedStatus() {
        return userDefinedStatus;
    }

    public void setUserDefinedStatus(UserDefinedStatus userDefinedStatus) {
        this.userDefinedStatus = userDefinedStatus;
    }

    public InstructionStatus getInstr_status() {
        return instr_status;
    }

    public void setInstr_status(InstructionStatus instr_status) {
        this.instr_status = instr_status;
    }
}
