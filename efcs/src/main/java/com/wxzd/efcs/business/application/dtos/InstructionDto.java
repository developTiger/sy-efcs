package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.enums.InstructionMovePolicy;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.InstructionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;

import java.util.Date;
import java.util.UUID;

/**
 * 指令表
 */
public class InstructionDto {

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
     * 起点位置
     */
    private String from_pos;
    /**
     * 终点位置
     */
    private String to_pos;
    /**
     * 指令优先级，数值越大优先级越高
     */
    private int instr_level = 0;
    /**
     * 移动策略
     */
    private InstructionMovePolicy move_policy;
    /**
     * 执行状态
     */
    private InstructionStatus instr_status;
    /**
     * 备注
     */
    private String remark;

    /**
     * 异常代码
     */
    public EfcsErrorCode error_code;
    /**
     * 异常信息
     */
    public String error_desc;


    private Date create_datetime;


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

    public String getFrom_pos() {
        return from_pos;
    }

    public void setFrom_pos(String from_pos) {
        this.from_pos = from_pos;
    }

    public String getTo_pos() {
        return to_pos;
    }

    public void setTo_pos(String to_pos) {
        this.to_pos = to_pos;
    }

    public int getInstr_level() {
        return instr_level;
    }

    public void setInstr_level(int instr_level) {
        this.instr_level = instr_level;
    }

    public InstructionMovePolicy getMove_policy() {
        return move_policy;
    }

    public void setMove_policy(InstructionMovePolicy move_policy) {
        this.move_policy = move_policy;
    }

    public InstructionStatus getInstr_status() {
        return instr_status;
    }

    public void setInstr_status(InstructionStatus instr_status) {
        this.instr_status = instr_status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public EfcsErrorCode getError_code() {
        return error_code;
    }

    public void setError_code(EfcsErrorCode error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }
}