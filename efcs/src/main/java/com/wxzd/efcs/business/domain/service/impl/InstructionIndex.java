package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.enums.SendType;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
public class InstructionIndex {

	private String instr_no;

	private SendType send_type;

	private String queue_no;

	public String getInstr_no() {
		return instr_no;
	}

	public void setInstr_no(String instr_no) {
		this.instr_no = instr_no;
	}

	public SendType getSend_type() {
		return send_type;
	}

	public void setSend_type(SendType send_type) {
		this.send_type = send_type;
	}

	public String getQueue_no() {
		return queue_no;
	}

	public void setQueue_no(String queue_no) {
		this.queue_no = queue_no;
	}

}
