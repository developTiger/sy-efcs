package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.InstructionDto;
import com.wxzd.efcs.business.application.querys.InstructionExQuery;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.protocol.wcs.domain.enums.ExecuteStatus;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/20.
 */
public interface InstructionAppService {

	/**
	 * 指令执行情况
	 * 
	 * @param com_no
	 * @param executeStatus
	 * @return
	 */
	GaiaResult instructionStatus(String com_no, ExecuteStatus executeStatus);


	/**
	 * 根据ID获取指令
	 * @param id
	 * @return
     */
	InstructionDto getById(UUID id);


	/**
	 * 分页查询
	 * @param exQuery
	 * @return
     */
	PageResult<InstructionDto> getInstructionPaged(InstructionExQuery exQuery);


    /**
	 * 不分页查询
	 * @param exQuery
	 * @return
     */
	List<InstructionDto>  getInstructionList(InstructionExQuery exQuery);

	/**
	 * 指令重发
	 * @param uuid
	 */
	void resendCommand(UUID uuid);


}
