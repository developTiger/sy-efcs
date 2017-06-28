package com.wxzd.efcs.business.instruction.impl;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.efcs.business.domain.service.InstructionComparator;
import com.wxzd.efcs.business.instruction.QueueInstructionPolicyService;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
@Service
public class QueueInstructionPolicyServiceImpl implements QueueInstructionPolicyService {

	@Override
	public List<Instruction> policyExecute(List<Instruction> list) {
		List<Instruction> all = new LinkedList<>();
		for (int i = 0, j = list.size(); i < j; i++) {
			Instruction inst = list.get(i);
			InstructionStatus status = inst.getInstr_status();
			switch (status) {
			case Send:
			case Executing:
				all.add(inst);
				break;
			case Waiting:
				SendType instr_type = inst.getSend_type();
				if (SendType.QueuesScheduler.equals(instr_type)) {
					if (isSchedulerExecuter(inst.getScheduler_time())) {
						all.add(inst);
					}
				} else {
					all.add(inst);
				}
				break;
			default:
				break;
			}
		}
		Collections.sort(all, new InstructionComparator());
		return all;
	}

	/**
	 * 计划任务是否需要执行
	 * true 请执行
	 * false 未到执行时间
	 */
	private boolean isSchedulerExecuter(Date date) {
		if (date != null && date.getTime() - new Date().getTime() > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<Instruction> policy(List<Instruction> list) {
		List<Instruction> all = new LinkedList<>();
		for (Instruction instruction : list) {
			all.add(instruction);
		}
		Collections.sort(all, new InstructionComparator());
		return all;
	}

}
