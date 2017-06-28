package test.wxzd.efcs.application.instruction;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.efcs.business.domain.service.InstructionComparator;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.gaia.common.base.core.date.DateUtl;
import com.wxzd.gaia.common.base.core.map.MapWrap;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.core.SerialNoGenerator;

import test.wxzd.efcs.application.mold.SpringDemoBase;

/**
 * 模拟数据
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
public class InstructionDemoData extends SpringDemoBase {

	private String houseNo = "29";

	@Autowired
	InstructionService instructionService;

	@Autowired
	DemoService demoService;

	@Test
	public void demoTransaction() {
		demoService.demo();
	}

	@Test
	public void initial_create() throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 10);
		Calendar c2 = Calendar.getInstance();
		c2.add(Calendar.DATE, 1);
		int i = 1;
		String perfix = "xx";
		String queueId = "q1";
		//10min后调度
		Instruction inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setScheduler_time(c.getTime());
		//		inst.setSend_type(SendType.Scheduler);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_status(InstructionStatus.Waiting);
		i++;
		instructionService.saveInstr(inst);
		//1天后调度
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setScheduler_time(c2.getTime());
		//		inst.setSend_type(SendType.Scheduler);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_status(InstructionStatus.Waiting);
		i++;
		instructionService.saveInstr(inst);
		//队列
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setQueue_no(queueId);
		inst.setSend_type(SendType.QueuesDirect);
		inst.setInstr_status(InstructionStatus.Waiting);
		i++;
		instructionService.saveInstr(inst);
		//队列
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setQueue_no(queueId);
		inst.setSend_type(SendType.QueuesDirect);
		inst.setInstr_status(InstructionStatus.Waiting);
		i++;
		instructionService.saveInstr(inst);
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setQueue_no(queueId);
		inst.setScheduler_time(c.getTime());
		inst.setSend_type(SendType.QueuesDirect);
		//		inst.setSend_type(SendType.QueuesScheduler);
		inst.setInstr_status(InstructionStatus.Waiting);
		i++;
		instructionService.saveInstr(inst);
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_status(InstructionStatus.Waiting);
		i++;
		instructionService.saveInstr(inst);
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_status(InstructionStatus.Created);
		i++;
		instructionService.saveInstr(inst);
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		inst.setSend_type(SendType.Direct);
		inst.setInstr_status(InstructionStatus.Finished);
		i++;
		instructionService.saveInstr(inst);
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		i++;
		instructionService.saveInstr(inst);
		inst = new Instruction(houseNo);
		inst.setInstr_no(perfix + i);
		i++;
		instructionService.saveInstr(inst);
	}

	@Test
	public void initial_change_state() {

	}

	@Test
	public void demo() {
		try {
			ListResult<Instruction> liveInstruction = instructionService.getLiveInstruction();
			List<Instruction> list = liveInstruction.getItems();
			for (Instruction ins : list) {
				System.out.println(ins);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	select * from FCS_INSTRUCTION where  TO_CHAR(CREATE_DATETIME,'yyyy-mm-dd HH24:MI:SS') >= '2017-05-02 21:04:29';
	//
	//	select * from FCS_INSTRUCTION where  CREATE_DATETIME >= TO_DATE('2017-05-02 21:04:29', 'yyyy-mm-dd HH24:MI:SS');
	@Test
	public void demoData() {
		String sql = "select * from FCS_INSTRUCTION where CREATE_DATETIME >= :createDate";
		List<Map<String, Object>> queryList = DatabaseExecuter.queryList(sql, new MapWrap<String, Object>().put("createDate", DateUtl.parseDateTime("2017-05-02 21:04:29")).getMap());
		//		String sql = "select * from FCS_INSTRUCTION where CREATE_DATETIME >= :createDate";
		//		List<Map<String, Object>> queryList = DatabaseExecuter.queryList(sql, new MapWrap<String, Object>().put("createDate", DateUtl.parseDateTime("2017-05-02 21:04:29")).getMap());
		System.out.println(queryList.size());
		for (Map map : queryList) {
			System.out.println(map);
		}

	}

}
