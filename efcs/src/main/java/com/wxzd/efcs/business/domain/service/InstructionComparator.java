package com.wxzd.efcs.business.domain.service;

import java.util.Comparator;
import java.util.Date;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.SendType;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-23
 */
public class InstructionComparator implements Comparator<Instruction> {

	/**
	 * 默认机制：根据第一个参数小于、等于或大于第二个参数
	 * 分别返回负整数、零或正整数。
	 * 从小到大
	 */
	/**
	 * 顺序
	 * 
	 * 执行中
	 * 已发送
	 * 待发送
	 * 创建
	 * 等价与null
	 */
	@Override
	public int compare(Instruction o1, Instruction o2) {
		InstructionStatus s1 = o1.getInstr_status();
		InstructionStatus s2 = o2.getInstr_status();
		if (s1 == null) {
			s1 = InstructionStatus.Created;
		}
		if (s2 == null) {
			s2 = InstructionStatus.Created;
		}
		SendType t1 = o1.getSend_type();
		if (isScheduler(t1)) {
			if (!isSchedulerExecuter(o1.getScheduler_time())) {//没到时间的认为是create
				if (InstructionStatus.Waiting.equals(s1)) {
					s1 = InstructionStatus.Created;
				}
			}
		}
		SendType t2 = o2.getSend_type();
		if (isScheduler(t2)) {
			if (!isSchedulerExecuter(o2.getScheduler_time())) {//没到时间的认为是create
				if (InstructionStatus.Waiting.equals(s2)) {
					s2 = InstructionStatus.Created;
				}
			}
		}
		if (s1.equals(s2)) {
			//如果下发状态一优先级大的优先
			int r1 = o2.getInstr_level() - o1.getInstr_level();
			if (r1 == 0) {
				Date d1 = o1.getCreate_datetime();
				Date d2 = o2.getCreate_datetime();
				if (d1 == null) {
					return 1;
				}
				if (d2 == null) {
					return -1;
				}
				long l1 = d1.getTime();
				long l2 = d2.getTime();
				if (l2 > l1) {
					return -1;
				} else if (l2 < l1) {
					return 1;
				} else {
					return 0;
				}
			} else {
				return r1;
			}
		} else {
			//不考虑finish
			switch (s1) {
			case Executing:
				return -1;
			case Send:
				switch (s2) {
				case Executing:
					return 1;
				default:
					return -1;
				}
			case Waiting:
				switch (s2) {
				case Executing:
				case Send:
					return 1;
				default:
					return -1;
				}
			case Created:
			default://其他等价create
				return 1;
			}
		}
	}

	private boolean isScheduler(SendType type) {
		if (type != null && (2 & type.getValue()) == 2) {
			return true;
		} else {
			return false;
		}
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

}
