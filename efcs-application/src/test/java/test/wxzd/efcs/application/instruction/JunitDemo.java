package test.wxzd.efcs.application.instruction;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.efcs.business.domain.service.InstructionComparator;
import com.wxzd.gaia.common.base.core.date.DateUtl;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-23
 */
public class JunitDemo {

	private String houseNo = "29";

	@Test
	public void sort() {
		List<Instruction> list = new LinkedList<>();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 10);
		Calendar c2 = Calendar.getInstance();
		c2.add(Calendar.DATE, 1);
		String perfix = "xx";
		String queueId = "q1";
		Instruction inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 1);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_level(1);
		inst.setInstr_status(InstructionStatus.Waiting);
		list.add(inst);
		
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 3);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_level(3);
		inst.setInstr_status(InstructionStatus.Waiting);
		list.add(inst);
		
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 2);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_level(2);
		inst.setInstr_status(InstructionStatus.Waiting);
		list.add(inst);
		
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 4);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_level(2);
		inst.setInstr_status(InstructionStatus.Send);
		list.add(inst);
		

		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 5);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_level(2);
		inst.setInstr_status(InstructionStatus.Executing);
		list.add(inst);
		
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 6);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_level(2);
		inst.setInstr_status(InstructionStatus.Executing);
		list.add(inst);
	
		Calendar c8 = Calendar.getInstance();
		c8.add(Calendar.DAY_OF_YEAR, -1);
		System.out.println(DateUtl.formatDateTime(c8.getTime()));
		
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 8);
		inst.setSend_type(SendType.QueuesScheduler);
		inst.setInstr_level(8);
		inst.setScheduler_time(c8.getTime());
		inst.setInstr_status(InstructionStatus.Waiting);
		list.add(inst);
		
		Calendar c6 = Calendar.getInstance();
		c6.add(Calendar.DAY_OF_YEAR, -1);
		System.out.println(DateUtl.formatDateTime(c6.getTime()));
		
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 7);
		inst.setSend_type(SendType.Scheduler);
		inst.setInstr_level(1);
		inst.setScheduler_time(c6.getTime());
		inst.setInstr_status(InstructionStatus.Waiting);
		list.add(inst);
		
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + 9);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_level(8);
		inst.setInstr_status(InstructionStatus.Created);
		list.add(inst);

		Collections.sort(list, new InstructionComparator());
		for (Instruction in : list) {
			System.out.println(in.getInstr_level() + in.getInstr_no());
		}
	}
}
