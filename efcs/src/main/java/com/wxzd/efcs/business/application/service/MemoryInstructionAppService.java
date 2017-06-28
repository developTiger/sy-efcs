package com.wxzd.efcs.business.application.service;

import java.util.UUID;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

/**
 * 查询直接对内存操作
 * 但是持久化会同时变更数据库的状态
 * Created by zhouzh on 2017/4/20.
 */
public interface MemoryInstructionAppService {

    /**
     * 根据指令号查询
     * 不包含历史记录
     */
    ObjectResult<Instruction> getByInstrNo(String instr);

    /**
     * 创建
     */
    GaiaResult createInstr(Instruction instruction);

    //	/**
    //	 * 开始执行
    //	 * 状态变为待执行
    //	 * 内存中加入数据
    //	 */
    //	GaiaResult executerInstr(Instruction instruction);

    /**
     * 变更状态
     * 执行：wait
     * 执行中
     * 完成:finish
     */
    GaiaResult setInstrSatus(String instrNo, InstructionStatus instructionStatus);

    /**
     * 异常
     * 作废
     * 强制完成
     * 手动：为了解决无回复的指令
     * 状态变更为finish
     */
    GaiaResult abnormalFinish(String instrNo, EfcsErrorCode error_code, String error_message);

    /**
     * 更改优先级
     * 只会更改数据库中的优先级
     */
    GaiaResult setInstrLevel(String instrNo, Integer instrLevel);

    /**
     * 查询所有内存中指令
     * 状态为wait和send的
     * 非排序，按照存入顺序的
     * 等策略具体实现了可以考虑把数据根据策略排序
     */
    ListResult<Instruction> getAllInstruction();

    /**
     * 查询内存中某队列指令列表
     * 非排序，按照存入顺序的
     * 等策略具体实现了可以考虑把数据根据策略排序
     */
    ListResult<Instruction> getInstructionByQueueNo(String houseNo, String deviceNo);
//	ListResult<Instruction> getInstructionByQueueNo(String queueNO);


    /**
     * 所有非finished状态的指令
     */
    ListResult<Instruction> getInstructionByHouseNo(String houseNo);


    /**
     * 记录一条为sent状态的指令
     * <p>
     * ~~记录一条指令，将其设置为Sent的状态~~
     * <p>
     * 会存放到内存中，并存放到数据库中
     * <p>
     * 如果内存已存在则不会做任何处理
     */
    GaiaResult addSentInstruction(Instruction instruction);

    GaiaResult setInstrSatus(Instruction instruction, InstructionStatus instructionStatus);

	void resendCommand(UUID uuid);


}
