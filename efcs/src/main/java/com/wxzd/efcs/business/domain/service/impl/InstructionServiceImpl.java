package com.wxzd.efcs.business.domain.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.efcs.business.repositorys.InstructionRepository;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.map.MapWrap;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.wms.core.SerialNoGenerator;

/**
 * Created by zhouzh on 2017/4/18.
 */
@Service
public class InstructionServiceImpl implements InstructionService {

	private static final Logger log = LoggerFactory.getLogger(InstructionServiceImpl.class);

	@Autowired
	InstructionRepository instructionRepository;

	/**
	 * 根据id获取 命令信息
	 *
	 * @param id
	 *        主键id
	 * @return ObjectResult<FmProcedure>
	 */
	@Override
	public ObjectResult<Instruction> getById(UUID id) {
		Instruction instruction = instructionRepository.getById(id);
		return GaiaResultFactory.getObject(instruction);
	}

	/**
	 * 更具指令号获取命令信息
	 *
	 * @param instr
	 *        令号
	 * @return ObjectResult<FmProcedure>
	 */
	@Override
	public ObjectResult<Instruction> getByInstrNo(String instr) {

		Instruction instruction = instructionRepository.getByNo(instr);
		return GaiaResultFactory.getObject(instruction);
	}

	public GaiaResult saveInstr(Instruction instruction) {
		if (StringUtl.isEmpty(instruction.getInstr_no())) {
			instruction.setInstr_no(SerialNoGenerator.getSerialNo("INS"));
		}
		try {
			instructionRepository.saveById(instruction);
		} catch (Exception e) {
			log.warn("", e);
			return GaiaResultFactory.getError(e.getMessage());
		}
		return GaiaResultFactory.getSuccess();
	}

	//	@Override
	//	public void saveById(Instruction instruction) {
	//		instructionRepository.saveById(instruction);
	//	}

	/**
	 * 设置指令优先级
	 *
	 * @param instruction
	 * @param instrLevel
	 * @return
	 */
	@Override
	public GaiaResult setInstrLevel(Instruction instruction, Integer instrLevel) {
		instruction.setInstr_level(instrLevel);
		instructionRepository.saveById(instruction);
		return GaiaResultFactory.getSuccess();
	}

	@Override
	public GaiaResult setInstrLevel(String instrNo, Integer instrLevel) {
		Instruction instruction = instructionRepository.getByNo(instrNo);
		if (instruction != null) {
			instruction.setInstr_level(instrLevel);
			instructionRepository.saveById(instruction);
		}
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 设置指令状态
	 *
	 * @param instrNo
	 * @param instructionStatus
	 * @return
	 */
	@Override
	public GaiaResult setInstrSatus(String instrNo, InstructionStatus instructionStatus) {
		Instruction instruction = instructionRepository.getByNo(instrNo);
		return setInstrSatus(instruction, instructionStatus);
	}

	@Override
	public GaiaResult setInstrSatus(Instruction instruction, InstructionStatus instructionStatus) {
		if (instruction != null) {
			try {
				instruction.setInstr_status(instructionStatus);
				instructionRepository.saveById(instruction);
				return GaiaResultFactory.getSuccess();
			} catch (Exception e) {
				log.warn("", e);
				return GaiaResultFactory.getError(I18nContext.getMessage("指令存储异常"));
			}
		} else {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令不存在"));
		}
	}

	@Override
	public ListResult<Instruction> getLiveInstruction() {
		ListResult<Instruction> result = new ListResult<>(true, null);
		String sql = "select $columns from $table where $status = :status1 or $status = :status2 or $status = :status3 or $status = :status4 order by instr_level desc, create_datetime desc, id asc";
		List<Instruction> list = DatabaseExecuter.queryBeanList(sql,//
				new MapWrap<String, Object>()//
						.put("columns", SqlUtl.getColumns(Instruction.class))//
						.put("table", SqlUtl.getTable(Instruction.class))//
						.put("status", SqlUtl.getColumn(Instruction.class, "instr_status"))//
						.put("status1", InstructionStatus.Waiting)//
						.put("status2", InstructionStatus.Send)//
						.put("status3", InstructionStatus.Executing)//
						.put("status4", InstructionStatus.Created)//
						.getMap(),//
				Instruction.class);
		return result.setItems(list);
	}

	@Override
	public GaiaResult abnormalFinish(String instrNo, EfcsErrorCode error_code, String error_message) {
		Instruction instruction = instructionRepository.getByNo(instrNo);
		if (instruction != null) {
			try {
				instruction.setInstr_status(InstructionStatus.Finished);
				instruction.setError_code(error_code);
				instruction.setError_desc(error_message);
				instructionRepository.saveById(instruction);
				return GaiaResultFactory.getSuccess();
			} catch (Exception e) {
				log.warn("", e);
				return GaiaResultFactory.getError(I18nContext.getMessage("指令存储异常"));
			}
		} else {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令不存在"));
		}
	}

}
