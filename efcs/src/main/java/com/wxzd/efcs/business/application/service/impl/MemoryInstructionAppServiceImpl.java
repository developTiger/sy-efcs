package com.wxzd.efcs.business.application.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wxzd.configration.catlConfig.InstructionConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.efcs.business.domain.service.MemoryInstructionService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.gaia.jdbc.core.annotation.TransactionType;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.wms.core.SerialNoGenerator;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
@Service
public class MemoryInstructionAppServiceImpl implements MemoryInstructionAppService {

	private static final Logger log = LoggerFactory.getLogger(MemoryInstructionAppServiceImpl.class);

	@Autowired
	private MemoryInstructionService memoryInstructionService;

	@Autowired
	private InstructionService instructionService;

	@Override
	public ObjectResult<Instruction> getByInstrNo(String instr) {
		ObjectResult<Instruction> result = memoryInstructionService.getByInstrNo(instr);
		return result;
	}

	@Transaction(TransactionType.NOT_SUPPORTED)
	@Override
	public GaiaResult createInstr(Instruction instruction) {
		//		DatabaseExecuter.execute(new SqlExecuter(){
		//			@Override
		//			public Object deal(Connection connection, DatabaseType databaseType) {
		//				System.out.println(connection);
		//				try {
		//					System.out.println(connection.getAutoCommit());
		//				} catch (SQLException e) {
		//					e.printStackTrace();
		//				}
		//				return null;
		//			}});
		if (StringUtl.isEmpty(instruction.getInstr_no())) {
			instruction.setInstr_no(SerialNoGenerator.getSerialNo("INS"));
		}
		GaiaResult insresult = memoryInstructionService.createInstr(instruction);
		if (insresult.isSuccess()) {
			GaiaResult insresult2 = instructionService.saveInstr(instruction);//会补全uuid和数据
			if (!insresult2.isSuccess()) {
				memoryInstructionService.remove(instruction.getInstr_no());
				return insresult2;
			}
		}
		return insresult;
	}

	//
	//	@Override
	//	public GaiaResult executerInstr(Instruction instruction) {
	//		memoryInstructionService.addInstr(instruction);
	//		instructionService.setInstrSatus(instruction, InstructionStatus.Watting);
	//		return GaiaResultFactory.getSuccess();
	//	}

	@Transaction(TransactionType.NOT_SUPPORTED)
	@Override
	public GaiaResult setInstrSatus(String instrNo, InstructionStatus instructionStatus) {
		ObjectResult<Instruction> byInstrNo = memoryInstructionService.getByInstrNo(instrNo);
		if (byInstrNo.isSuccess()) {
			GaiaResult setInstrSatus = instructionService.setInstrSatus(instrNo, instructionStatus);
			if (setInstrSatus.isSuccess()) {
				memoryInstructionService.setInstrSatus(byInstrNo.getObject(), instructionStatus);
			}
			return setInstrSatus;
		} else {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令不存在"));
		}
	}

	@Transaction(TransactionType.NOT_SUPPORTED)
	public GaiaResult setInstrSatus(Instruction instruction, InstructionStatus instructionStatus) {
		GaiaResult setInstrSatus = instructionService.setInstrSatus(instruction, instructionStatus);
		if (setInstrSatus.isSuccess()) {
			memoryInstructionService.setInstrSatus(instruction, instructionStatus);
		}
		return setInstrSatus;
	}

	@Transaction(TransactionType.NOT_SUPPORTED)
	@Override
	public GaiaResult abnormalFinish(String instrNo, EfcsErrorCode error_code, String error_message) {
		ObjectResult<Instruction> byInstrNo = memoryInstructionService.getByInstrNo(instrNo);
		if (byInstrNo.isSuccess()) {
			GaiaResult abnormalFinish = instructionService.abnormalFinish(instrNo, error_code, error_message);
			if (abnormalFinish.isSuccess()) {
				memoryInstructionService.abnormalFinish(byInstrNo.getObject(), error_code, error_message);
			}
			return abnormalFinish;
		} else {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令不存在"));
		}
	}

	@Override
	public GaiaResult setInstrLevel(String instrNo, Integer instrLevel) {
		GaiaResult setInstrLevel = memoryInstructionService.setInstrLevel(instrNo, instrLevel);
		return setInstrLevel;
	}

	/**
	 * 查询都从数据库中查
	 */
	@Override
	public ListResult<Instruction> getAllInstruction() {
		return memoryInstructionService.getAllInstruction();
	}

	@Override
	public ListResult<Instruction> getInstructionByQueueNo(String houseNo, String deviceNo) {
		return memoryInstructionService.getInstructionByQueueNo(houseNo, deviceNo);
	}

	@Override
	public ListResult<Instruction> getInstructionByHouseNo(String houseNo) {
		List<Instruction> instructions = getAllInstruction().getItems();
		ListResult<Instruction> result = new ListResult<>(true, null);
		List<Instruction> resultList = new LinkedList<>();
		for (Instruction instruction : instructions) {
			if (StringUtl.isEqual(houseNo, instruction.getHouse_no())) {
				resultList.add(instruction);
			}
		}
		result.setItems(resultList);
		return result;
	}

	@Override
	public GaiaResult addSentInstruction(Instruction instruction) {
		//		GaiaResult result = memoryInstructionService.createInstr(instruction);
		//		if (result.isSuccess()) {
		//			instructionService.saveById(instruction);
		//		}
		return createInstr(instruction);
	}

	/**
	 * 指令重发
	 * waiting不做任何处理
	 * finish需要从数据库中查询出来并写到内存中
	 * 其他变更指令状态
	 */
	public void resendCommand(UUID uuid) {
		ObjectResult<Instruction> insObj = instructionService.getById(uuid);
		if (insObj.getObject() == null) {
			log.warn("指令不存在");
			return;
		}
		Instruction ins = insObj.getObject();
		if (InstructionStatus.Finished.equals(ins.getInstr_status())) {
			ins.setInstr_status(InstructionStatus.Waiting);
			ins.setInstr_level(InstructionConfig.resend_command_level);
			ins.setSend_time(null);
			ins.setError_code(null);
			ins.setError_desc(null);
			createInstr(ins);
		} else {
			ObjectResult<Instruction> byInstrNo = memoryInstructionService.getByInstrNo(ins.getInstr_no());
			if (byInstrNo != null && byInstrNo.getObject() != null) {
				Instruction ins2 = byInstrNo.getObject();
				if(InstructionStatus.Waiting.equals(ins2.getInstr_status())){
					return;
				}else{
					ins2.setInstr_status(InstructionStatus.Waiting);
					ins2.setInstr_level(InstructionConfig.resend_command_level);
					ins2.setSend_time(null);
					ins2.setError_code(null);
					ins2.setError_desc(null);
					instructionService.saveInstr(ins2);
				}
			}
		}

	}

}
