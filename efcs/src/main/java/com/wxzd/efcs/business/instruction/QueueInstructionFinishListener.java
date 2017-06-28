package com.wxzd.efcs.business.instruction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.service.MemoryInstructionService;
import com.wxzd.gaia.event.listener.AbstractEventListener;

/**
 * 堆垛机某处指令完成的一个优化算法
 * 
 * @version 1
 * @author y
 * @.create 2017-04-27
 */
@Component
public class QueueInstructionFinishListener extends AbstractEventListener<QueueInstructionFinishEvent> {

	@Autowired
	private MemoryInstructionService memoryInstructionService;

	@Override
	public void onEvent(QueueInstructionFinishEvent eventObj) throws Exception {
		Instruction instruction = eventObj.getFinishInstruction();
		if (DispatcherConfig.formation_down_location.equals(instruction.getTo_pos())) {//如果是到2010去的，优先设定2260的
			String fromPosition = DispatcherConfig.formation_rework_waitin_location;
			List<Instruction> list = memoryInstructionService.getInstructionByQueueNo(instruction.getQueue_no()).getItems();
			for (Instruction instruction2 : list) {
				if (InstructionStatus.Waiting.equals(instruction2.getInstr_status())) {
					if (fromPosition.equals(instruction2.getFrom_pos())) {
						instruction2.setInstr_level(instruction2.getInstr_level() + DispatcherConfig.formation_instruction_level + 1);
						break;
					}
				}
			}
		}

	}

}
