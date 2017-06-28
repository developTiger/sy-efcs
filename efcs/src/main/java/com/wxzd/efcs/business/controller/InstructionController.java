package com.wxzd.efcs.business.controller;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.InstructionDto;
import com.wxzd.efcs.business.application.querys.InstructionExQuery;
import com.wxzd.efcs.business.application.service.InstructionAppService;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.module.Module;

/**
 * 指令查询
 * Created by jade on 2017/4/27.
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class InstructionController {

	@Resource
	private InstructionAppService instructionAppService;

	@Resource
	private MemoryInstructionAppService memoryInstructionAppService;

	@RequestMapping
	public void index_view() {

	}

	@RequestMapping
	@ResponseBody
	public PageResult<InstructionDto> grid(InstructionExQuery exQuery) {
		return instructionAppService.getInstructionPaged(exQuery);
	}

	/**
	 * 指令重发
	 *
	 * @param id
	 */
	@RequestMapping
	@ResponseBody
	public void resend(UUID id) {
		memoryInstructionAppService.resendCommand(id);
	}

	/**
	 * 作废指令
	 */
	@RequestMapping
	@ResponseBody
	public void deprecated(String instrNo) {
		if (!StringUtl.isEmpty(instrNo)) {
			memoryInstructionAppService.abnormalFinish(instrNo, EfcsErrorCode.instruction_scrapped, "人工作废");
		}
	}
}
