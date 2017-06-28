package com.wxzd.efcs.business.instruction;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.gaia.event.EventObject;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-27
 */
public class QueueInstructionFinishEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	//完成的指令
	private Instruction finishInstruction;

	public Instruction getFinishInstruction() {
		return finishInstruction;
	}

	public void setFinishInstruction(Instruction finishInstruction) {
		this.finishInstruction = finishInstruction;
	}

}
