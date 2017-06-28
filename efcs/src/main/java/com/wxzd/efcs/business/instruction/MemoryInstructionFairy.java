package com.wxzd.efcs.business.instruction;

import org.springframework.stereotype.Component;

import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.gaia.common.base.spring.core.NonAop;

/**
 * Instruction的一些简单的
 * 基于spring的
 * 工具类
 * 小精灵
 * 
 * @version 1
 * @author y
 * @.create 2017-04-25
 */
@NonAop
@Component
public class MemoryInstructionFairy {
	
	public boolean isQueue(SendType type) {
		if (type != null && (4 & type.getValue()) == 4) {
			return true;
		} else {
			return false;
		}
	}
}
