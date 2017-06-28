package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

/**
 * 会操作内存中数据并同时持久化数据库
 * 未完成的指令都会在内存中保存
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
public interface MemoryInstructionService {

	/**
	 * 内存中添加指令
	 */
	GaiaResult createInstr(Instruction instruction);

	/**
	 * 根据指令号查询
	 */
	ObjectResult<Instruction> getByInstrNo(String instr);

	/**
	 * 状态变更
	 * 执行：wait
	 * 执行中：
	 * 完成：finish
	 * 变更状态，并移内存中的指令
	 */
	GaiaResult setInstrSatus(String instrNo, InstructionStatus instructionStatus);

	/**
	 * 状态变更
	 * 执行：wait
	 * 执行中：
	 * 完成：finish
	 * 变更状态，并移内存中的指令
	 */
	GaiaResult setInstrSatus(Instruction instruction, InstructionStatus instructionStatus);

	/**
	 * 异常
	 * 状态变更为finish
	 * 
	 * 作废
	 * 强制完成
	 * 为了解决无回复的指令
	 */
	GaiaResult abnormalFinish(String instrNo, EfcsErrorCode error_code, String error_message);

	/**
	 * 更改优先级
	 */
	GaiaResult setInstrLevel(String instrNo, Integer instrLevel);

	/**
	 * 查询所有指令
	 * 非排序，按照存入顺序的
	 * 等策略具体实现了可以考虑把数据根据策略排序
	 */
	ListResult<Instruction> getAllInstruction();

	/**
	 * 查询某队列指令列表
	 * 非排序，按照存入顺序的
	 * 等策略具体实现了可以考虑把数据根据策略排序
	 */
	ListResult<Instruction> getInstructionByQueueNo(String queueNO);

	ListResult<Instruction> getInstructionByQueueNo(String houseNo, String deviceNo);

	/**
	 * 数据库同步
	 * 暂不实现，后期考虑
	 * 目前只需要启动的时候同步
	 */

	//提供给指令下发的接口

	/**
	 * 获取立即执行列表
	 * 包含立即执行和计划执行的和队列的可执行的指令列表
	 * 返回的是引用，方便直接进行操作
	 */
	ListResult<Instruction> getExecuteInstruction();

	/**
	 * 移除当前指令号
	 */
	void remove(String instr);

	void clear();

	GaiaResult abnormalFinish(Instruction ins, EfcsErrorCode error_code, String error_message);

	/**
	 * 当前队列的运行状态的优先级增加一步
	 */
}
