package com.wxzd.efcs.business.instruction;

import java.util.List;

import com.wxzd.efcs.business.domain.entities.Instruction;

/**
 * InstructionMemoryServiceImpl 会用到的策略，不知道该放在哪个包
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
public interface QueueInstructionPolicyService {

	/**
	 * 对队列指令进行排序
	 * create 状态的和计划任务时间没到的不显示出来
	 */
	List<Instruction> policyExecute(List<Instruction> list);
	
	/**
	 * 排序
	 * 如果需要的话根据策略把
	 * create和计划任务一起加入排序的结果
	 */
	List<Instruction> policy(List<Instruction> list);
	
}
