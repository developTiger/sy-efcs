package com.wxzd.efcs.business.domain.service;

import java.util.UUID;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

/**
 * 新增指令
 * 作废指令
 * 指令异常
 *
 * 指令分为
 *
 * Created by zhouzh on 2017/4/18.
 */
public interface InstructionService {

	/**
	 * 根据id获取 命令信息
	 * 
	 * @param id
	 *        主键id
	 * @return ObjectResult<FmProcedure>
	 */
	ObjectResult<Instruction> getById(UUID id);

	/**
	 * 更具指令号获取命令信息
	 * 
	 * @param instr
	 *        令号
	 * @return ObjectResult<FmProcedure>
	 */
	ObjectResult<Instruction> getByInstrNo(String instr);

	/**
	 * 新增指令
	 * 
	 * @param instruction
	 * @return
	 */
	GaiaResult saveInstr(Instruction instruction);
	
	/**
	 * 设置指令优先级
	 * 
	 * @param instruction
	 * @param instrLevel
	 * @return
	 */
	GaiaResult setInstrLevel(Instruction instruction,Integer instrLevel );

	/**
	 * 设置指令优先级
	 * 
	 * @param instruction
	 * @param instrLevel
	 * @return
	 */
	GaiaResult setInstrLevel(String instrNo, Integer instrLevel);

	/**
	 * 设置指令状态
	 * 
	 * @param instrNo
	 * @param instructionStatus
	 * @return
	 */
	GaiaResult setInstrSatus(String instrNo, InstructionStatus instructionStatus);
	
	/**
	 * 设置指令状态
	 * 
	 * @param instrNo
	 * @param instructionStatus
	 * @return
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
	 * 获取活动状态的指令
	 * 即非关闭状态的和创建状态
	 */
	ListResult<Instruction> getLiveInstruction();
	
//	/**
//	 * 存档
//	 */
//	void saveById(Instruction instruction);
}
