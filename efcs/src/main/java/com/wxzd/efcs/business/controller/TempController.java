package com.wxzd.efcs.business.controller;

import java.util.Calendar;
import java.util.List;

import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.efcs.alarm.domain.enums.FireAlarmDeviceType;
import com.wxzd.efcs.alarm.domain.enums.FireAlarmEventType;
import com.wxzd.efcs.alarm.domain.events.FireAlarmEvent;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.InstructionType;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.efcs.business.domain.service.MemoryInstructionService;
import com.wxzd.gaia.web.module.Module;

/**
 * 测试用
 * 
 * @version 1
 * @author y
 * @.create 2017-04-23
 */
@Module("core")
@Controller
@RequestMapping
public class TempController {

	@Autowired
	private InstructionService instructionService;

	@Autowired
	private MemoryInstructionService memoryInstructionService;

	@Autowired
	private MemoryInstructionAppService memoryInstructionAppService;

	@RequestMapping
	@ResponseBody
	public void reload() {
		memoryInstructionService.clear();
		List<Instruction> items = instructionService.getLiveInstruction().getItems();
		for (Instruction item : items) {
			memoryInstructionService.createInstr(item);
		}
	}
	
	@RequestMapping
	@ResponseBody
	public void demo() {
		System.out.println("指令查询：");
		List<Instruction> instructions = memoryInstructionAppService.getAllInstruction().getItems();
		for (Instruction instruction : instructions) {
		    System.out.println("...................");
		    System.out.println(instruction.getInstr_no());
		    System.out.println(JsonUtl.parseWithoutException(instruction));
		}
	}

	/**
	 * 临时，用来测试改写内存的一些方法
	 */
	@RequestMapping
	@ResponseBody
	public void executing() {
		//		memoryInstructionAppService.setInstrSatus("1", InstructionStatus.Finished);
		memoryInstructionAppService.setInstrSatus("68", InstructionStatus.Finished);
//		memoryInstructionAppService.setInstrSatus("2", InstructionStatus.Waiting);
//		memoryInstructionAppService.setInstrSatus("3", InstructionStatus.Waiting);
	}

	@RequestMapping
	@ResponseBody
	public void create() {
		//移库
		//		Instruction inst = new Instruction(houseNo);
		//		inst.setHouse_no(houseNo);
		//		inst.setPallet_no("托盘编号1");
		//		inst.setInstr_type(InstructionType.Transport);
		//		inst.setEquip_no("1170");
		//		inst.setFrom_pos("1170");
		//		inst.setTo_pos("1220");
		//		inst.setSend_type(SendType.Direct);
		//		inst.setInstr_status(InstructionStatus.Created);
		//		inst.setInstr_level(1);
		//		memoryInstructionAppService.createInstr(inst);
		Instruction inst = new Instruction("29");
		inst.setInstr_no("1");
		inst.setHouse_no("29");
		inst.setQueue_no("29110");
		inst.setPallet_no("托盘编号1");
		inst.setInstr_type(InstructionType.Stock_Out);
		inst.setEquip_no("110");
		inst.setFrom_pos("LA020202");
		inst.setTo_pos("1320");
		inst.setSend_type(SendType.QueuesDirect);
		inst.setInstr_status(InstructionStatus.Created);
		inst.setInstr_level(1);
		memoryInstructionAppService.createInstr(inst);

		inst = new Instruction("29");
		inst.setInstr_no("2");
		inst.setHouse_no("29");
		inst.setQueue_no("29110");
		inst.setPallet_no("托盘编号1");
		inst.setInstr_type(InstructionType.Stock_Out);
		inst.setEquip_no("110");
		inst.setFrom_pos("LA020303");
		inst.setTo_pos("1320");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 2);
		inst.setScheduler_time(c.getTime());
		inst.setSend_type(SendType.QueuesScheduler);
		inst.setInstr_status(InstructionStatus.Created);
		inst.setInstr_level(3);
		memoryInstructionAppService.createInstr(inst);

		inst = new Instruction("29");
		inst.setInstr_no("3");
		inst.setHouse_no("29");
		inst.setQueue_no("29110");
		inst.setPallet_no("托盘编号1");
		inst.setInstr_type(InstructionType.Stock_Out);
		inst.setEquip_no("110");
		inst.setFrom_pos("LA020404");
		inst.setTo_pos("1320");
		inst.setSend_type(SendType.QueuesDirect);
		inst.setInstr_status(InstructionStatus.Created);
		inst.setInstr_level(2);
		memoryInstructionAppService.createInstr(inst);
	}


	@RequestMapping
    @ResponseBody
	public void fire() {
	    try {
            FireAlarmEvent alarmEvent = new FireAlarmEvent();
            alarmEvent.setHouseNo("29");
            alarmEvent.setDeviceNo("测试");
            alarmEvent.setFireAlarmDeviceType(FireAlarmDeviceType.Smoke_Sensor);
            alarmEvent.setEventType(FireAlarmEventType.warn);
            alarmEvent.setLocation("LA030201");
            alarmEvent.setTray_no("L29B0001");
            alarmEvent.setContent("测试报警");
            alarmEvent.setSource(this);

            // 发布事件
            ApplicationEventPublisher.trigger(alarmEvent);

        } catch (Exception e) {
        }
    }
}
