package com.wxzd.efcs.business.domain.entities;

import java.util.Date;

/***********************************************************************
 * Module:  fcs_instruction.java
 * Author:  zhouzh
 * Purpose: Defines the Class fcs_instruction
 ***********************************************************************/
import com.wxzd.efcs.business.domain.enums.InstructionMovePolicy;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.InstructionType;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.entities.EfcsErrorEntity;
import com.wxzd.gaia.common.base.bean.AliasName;

/**
 * 指令表
 */
@AliasName("fcs_instruction")
public class Instruction extends EfcsErrorEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Instruction(){
		
	}
	
	public Instruction(String houseNo) {
		this.house_no = houseNo;
		this.instr_level = 1;
		this.instr_status = InstructionStatus.Created;
	}

	/**
	 * 指令号
	 * 
	 * 当前 非完成状态 的指令是唯一
	 */
	private String instr_no;
	/**
	 * 下发策略
	 * 
	 * 只有设定了下发策略，scheduler_time和queue_no才生效
	 */
	private SendType send_type;
	/**
	 * 队列编号
	 * 
	 * 默认：堆垛机的队列编号为库号+设备号
	 */
	private String queue_no;
	/**
	 * 计划下发时间
	 */
	private Date scheduler_time;
	/**
	 * 实际下发时间
	 */
	private Date send_time;
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

	public void newInstruction(String house_no,WorkProcedure workProcedure, String palletNo, InstructionType instrType, String equipNo, String fromPos, String toPos, InstructionMovePolicy instructionMovePolicy,
			SendType sendType) {
		this.house_no = house_no;
		this.work_procedure = workProcedure;
		this.instr_type = instrType;
		this.equip_no = equipNo;
		this.from_pos = fromPos;
		this.to_pos = toPos;
		this.move_policy = instructionMovePolicy;
		this.pallet_no = palletNo;
		this.send_type = sendType;
		this.instr_status=InstructionStatus.Waiting;

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

	public String getQueue_no() {
		return queue_no;
	}

	public void setQueue_no(String queue_no) {
		this.queue_no = queue_no;
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

	public Date getScheduler_time() {
		return scheduler_time;
	}

	public void setScheduler_time(Date scheduler_time) {
		this.scheduler_time = scheduler_time;
	}
}