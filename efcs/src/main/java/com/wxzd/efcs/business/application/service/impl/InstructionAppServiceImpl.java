package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.InstructionDto;
import com.wxzd.efcs.business.application.queryService.InstructionExQueryService;
import com.wxzd.efcs.business.application.querys.InstructionExQuery;
import com.wxzd.efcs.business.application.service.InstructionAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.protocol.wcs.domain.enums.ExecuteStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27
 */
@Service
public class InstructionAppServiceImpl implements InstructionAppService {

	private static final Logger log = LoggerFactory.getLogger(InstructionAppServiceImpl.class);
	
    @Autowired
    private InstructionExQueryService instructionExQueryService;

    @Autowired
    private InstructionService instructionService;

    /**
     * 指令执行情况
     *
     * @param com_no
     * @param executeStatus
     * @return
     */
    @Override
    public GaiaResult instructionStatus(String com_no, ExecuteStatus executeStatus) {
        return null;
    }

    /**
     * 根据ID获取指令
     *
     * @param id
     * @return
     */
    @Override
    public InstructionDto getById(UUID id) {
        if (id != null) {
            Instruction instruction = instructionService.getById(id).getObject();
            if (instruction != null)
                return BeanUtl.copyProperties(instruction, InstructionDto.class);
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<InstructionDto> getInstructionPaged(InstructionExQuery exQuery) {
        return instructionExQueryService.getInstructionPaged(exQuery);
    }

    /**
     * 不分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<InstructionDto> getInstructionList(InstructionExQuery exQuery) {
        return instructionExQueryService.getInstructionList(exQuery);
    }
    
    /**
     * 指令重发
     * waiting不做任何处理
     * finish需要从数据库中查询出来并写到内存中
     * 其他变更指令状态
     */
    public void resendCommand(UUID uuid){
    	ObjectResult<Instruction> insObj = instructionService.getById(uuid);
    	if(insObj.getObject()==null){
    		log.warn("指令不存在");
    		return;
    	}
    	Instruction ins = insObj.getObject();
    	
    }
}
