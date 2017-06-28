package com.wxzd.efcs.business.domain.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wxzd.configration.catlConfig.InstructionConfig;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.InstructionType;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.efcs.business.domain.service.MemoryInstructionService;
import com.wxzd.efcs.business.instruction.QueueInstructionFinishEvent;
import com.wxzd.efcs.business.instruction.QueueInstructionPolicyService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.event.exception.EventException;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.gaia.web.i18n.I18nContext;

/**
 * 把内存repository和service写在一起了
 * 考虑重构并拆分
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
@Service
public class MemoryInstructionServiceImpl implements MemoryInstructionService {

	private List<String> index = Collections.synchronizedList(new LinkedList<String>());

	/**
	 * 1 立即执行
	 * 2 计划下发
	 * 4 队列下发
	 * 
	 */
	private List<InstructionIndex> indexContent = Collections.synchronizedList(new LinkedList<InstructionIndex>());

	/**
	 * 立即执行和定时执行的任务
	 * 
	 */
	//	private ConcurrentMap<String, Instruction> executes = new ConcurrentHashMap<>();
	private List<Instruction> executes = Collections.synchronizedList(new LinkedList<Instruction>());

	/**
	 * 那些需要队列执行的任务
	 * listeners = Collections.synchronizedList(new
	 * LinkedList<EventListener>());
	 */
	private ConcurrentMap<String, List<Instruction>> queues = new ConcurrentHashMap<>();

	@Autowired
	private QueueInstructionPolicyService queueInstructionPolicyService;

	public GaiaResult createInstr(Instruction instruction) {
		if (instruction == null) {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令号不存在"));
		}
		if (index.contains(instruction.getInstr_no())) {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令号已存在"));
		}
		if (InstructionStatus.Finished.equals(instruction.getInstr_status())) {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令号不允许为完成状态"));
		}
		if (isQueue(instruction.getSend_type())) {
			if (StringUtl.isEmpty(instruction.getQueue_no())) {
				instruction.setQueue_no(instruction.getHouse_no() + instruction.getEquip_no());
			}
			addQueueInstruction(instruction.getQueue_no(), instruction);
		} else {
			executes.add(instruction);
		}
		index.add(instruction.getInstr_no());
		InstructionIndex ii = new InstructionIndex();
		ii.setInstr_no(instruction.getInstr_no());
		ii.setQueue_no(instruction.getQueue_no());
		ii.setSend_type(instruction.getSend_type());
		indexContent.add(ii);
		return GaiaResultFactory.getSuccess();
	}

	public Object createListLock = new Object();

	private void addQueueInstruction(String queueNo, Instruction instruction) {
		List<Instruction> list = queues.get(queueNo);
		if (list == null) {
			synchronized (createListLock) {
				if (list == null) {
					queues.put(queueNo, Collections.synchronizedList(new LinkedList<Instruction>()));
				}
				list = queues.get(queueNo);
			}
		}
		list.add(instruction);
	}

	@Override
	public ObjectResult<Instruction> getByInstrNo(String instr) {
		ObjectResult<Instruction> result = new ObjectResult<>(true, null);
		if (!index.contains(instr)) {
			result.setSuccess(false);
			result.setMessage(I18nContext.getMessage("指令不存在"));
			return result;
		}
		int indexOf = index.indexOf(instr);
		InstructionIndex ii = indexContent.get(indexOf);
		List<Instruction> list = new LinkedList<>();
		String qNo = null;
		//		if (ii.getSend_type() != null && (4 & ii.getSend_type().getValue()) == 4) {
		if (isQueue(ii.getSend_type())) {
			//从队列中获取
			qNo = ii.getQueue_no();
			list = queues.get(qNo);
		} else {
			list = executes;
		}
		for (int i = 0, j = list.size(); i < j; i++) {
			Instruction ins = list.get(i);
			if (ins.getInstr_no().equals(instr)) {
				result.setObject(ins);
				break;
			}
		}
		//强校验不写，因为先进行了索引判断，所以此处可以考虑做个强校验来判断此处不应该不存在指令
		return result;
	}

	public void remove(String instr) {
		if (!index.contains(instr)) {
			return;
		}
		int indexOf = index.indexOf(instr);
		InstructionIndex ii = indexContent.get(indexOf);

		index.remove(indexOf);
		indexContent.remove(ii);

		List<Instruction> list = new LinkedList<>();
		String qNo = null;
		if (isQueue(ii.getSend_type())) {
			//从队列中获取
			qNo = ii.getQueue_no();
			list = queues.get(qNo);
		} else {
			list = executes;
		}
		for (int i = 0, j = list.size(); i < j; i++) {
			Instruction ins = list.get(i);
			if (ins.getInstr_no().equals(instr)) {
				list.remove(i);
				break;
			}
		}
		//如果队列为空则清空队列数据
		if (isQueue(ii.getSend_type())) {
			if (list.size() == 0) {
				queues.remove(qNo);
			}
		}
	}

	private boolean isQueue(SendType type) {
		if (type != null && (4 & type.getValue()) == 4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计划任务是否需要执行
	 * true 请执行
	 * false 未到执行时间
	 */
	private boolean isSchedulerExecuter(Date date) {
		if (date != null && date.getTime() - new Date().getTime() > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public GaiaResult setInstrSatus(String instrNo, InstructionStatus instructionStatus) {
		Instruction instruction = getByInstrNo(instrNo).getObject();
		return setInstrSatus(instruction, instructionStatus);
	}

	private static final Logger log = LoggerFactory.getLogger(MemoryInstructionServiceImpl.class);

	@Override
	public GaiaResult setInstrSatus(Instruction instruction, InstructionStatus instructionStatus) {
		if (instruction != null) {
			instruction.setInstr_status(instructionStatus);
			//完成了需要移除
			if (InstructionStatus.Finished.equals(instructionStatus)) {
				//移除数据
				remove(instruction.getInstr_no());
				if (isQueue(instruction.getSend_type())) {//如果是队列的
					QueueInstructionFinishEvent event = new QueueInstructionFinishEvent();
					event.setFinishInstruction(instruction);
					try {
						ApplicationEventPublisher.trigger(event);
					} catch (EventException e) {
						log.warn("", e);
					}
				}
			}
		} else {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令不存在"));
		}
		return GaiaResultFactory.getSuccess();
	}

	@Override
	public GaiaResult setInstrLevel(String instrNo, Integer instrLevel) {
		Instruction ins = getByInstrNo(instrNo).getObject();
		if (ins != null) {
			ins.setInstr_level(instrLevel);
		}
		return GaiaResultFactory.getSuccess();
	}

	@Override
	public GaiaResult abnormalFinish(String instrNo, EfcsErrorCode error_code, String error_message) {
		Instruction ins = getByInstrNo(instrNo).getObject();
		return abnormalFinish(ins, error_code, error_message);
	}

	@Override
	public GaiaResult abnormalFinish(Instruction ins, EfcsErrorCode error_code, String error_message) {
		if (ins != null) {
			ins.setInstr_status(InstructionStatus.Finished);
			ins.setError_code(error_code);
			ins.setError_desc(error_message);
			remove(ins.getInstr_no());
		} else {
			return GaiaResultFactory.getError(I18nContext.getMessage("指令不存在"));
		}
		return GaiaResultFactory.getSuccess();
	}

	@Override
	public ListResult<Instruction> getAllInstruction() {
		//		System.out.println("memory getAllInstruction");
		//		System.out.println(index.size());
		//		System.out.println(indexContent.size());
		ListResult<Instruction> result = new ListResult<>(true, null);
		List<Instruction> all = new LinkedList<>();
		for (int i = 0, j = executes.size(); i < j; i++) {
			Instruction inst = executes.get(i);
			all.add(inst);
		}
		Set<String> keySet = queues.keySet();
		for (String key : keySet) {
			List<Instruction> listTemp = queues.get(key);
			for (int i = 0, j = listTemp.size(); i < j; i++) {
				Instruction inst = listTemp.get(i);
				all.add(inst);
			}
		}
		all = queueInstructionPolicyService.policy(all);
		return result.setItems(all);
	}

	@Override
	public ListResult<Instruction> getInstructionByQueueNo(String queueNO) {
		ListResult<Instruction> result = new ListResult<>(true, null);
		List<Instruction> list = new LinkedList<>();
		if (queues.containsKey(queueNO)) {
			list = queues.get(queueNO);
		}
		list = queueInstructionPolicyService.policy(list);
		return result.setItems(list);
	}

	public ListResult<Instruction> getInstructionByQueueNo(String houseNo, String deviceNo) {
		return getInstructionByQueueNo(houseNo + deviceNo);
	}

	@Override
	public ListResult<Instruction> getExecuteInstruction() {
		ListResult<Instruction> result = new ListResult<>(true, null);
		List<Instruction> all = new LinkedList<>();
		for (int i = 0, j = executes.size(); i < j; i++) {
			Instruction inst = executes.get(i);
			InstructionStatus status = inst.getInstr_status();
			switch (status) {
			case Waiting:
				SendType instr_type = inst.getSend_type();
				if (SendType.Scheduler.equals(instr_type)) {
					if (isSchedulerExecuter(inst.getScheduler_time())) {
						all.add(inst);
					}
				} else {
					all.add(inst);
				}
				break;
			default:
				break;
			}
		}

		Set<String> keySet = queues.keySet();
		for (String key : keySet) {
			List<Instruction> listTemp = queues.get(key);
			listTemp = queueInstructionPolicyService.policyExecute(listTemp);
			if (listTemp != null && listTemp.size() > 0) {
				Instruction inst = listTemp.get(0);
				if (InstructionStatus.Waiting.equals(inst.getInstr_status())) {
					all.add(inst);
				} else if (InstructionStatus.Send.equals(inst.getInstr_status()) || InstructionStatus.Executing.equals(inst.getInstr_status())) {
					/*
					 * |-------------------------------|
					 * |- y sign: created or verified -|
					 * |-------------------------------|
					 * TODO 指令下发策略需要调整
					 */
					/* 1.追加超时执行其他任务
					 *  * 如果是上架则默认一致
					 *  * 如果是其他的则根据目标地址判断延迟
					 */
					//找到超时的指令，并且所有的指令超时的话
					List<String> to_locations = new ArrayList<>();
					boolean hasStockIn = false;//有入库
					boolean allTime_out = true;//全部超时才下发别的指令
					if (isTimeOut(inst.getSend_time())) {
						for (Instruction ins : listTemp) {
							if (InstructionStatus.Send.equals(ins.getInstr_status()) || InstructionStatus.Executing.equals(ins.getInstr_status())) {
								if (isTimeOut(ins.getSend_time())) {
									if (InstructionType.Stock_In.equals(ins.getInstr_type())) {
										if (hasStockIn == false) {
											hasStockIn = true;
										}
									} else {
										if (!to_locations.contains(ins.getTo_pos())) {
											to_locations.add(ins.getTo_pos());
										}
									}
								} else {
									allTime_out = false;
									break;
								}
							}
						}
						if (allTime_out) {
							//如果全部超时，那么寻找可以派发的指令并下发
							for (Instruction ins : listTemp) {
								if (InstructionStatus.Waiting.equals(ins.getInstr_status())) {
									if (InstructionType.Stock_In.equals(ins.getInstr_type())) {
										if (!hasStockIn) {
											all.add(ins);
											break;
										}
									} else {
										if (!to_locations.contains(ins.getTo_pos())) {
											all.add(ins);
											break;
										}
									}
								}
							}
						}
					}
					//追加完毕
				}
			}
		}
		all = queueInstructionPolicyService.policy(all);

		return result.setItems(all);
	}

	/**
	 * 检查是否超时
	 */
	private boolean isTimeOut(Date date) {
		if (date == null) {
			return false;
		}
		return new Date().getTime() - date.getTime() > InstructionConfig.queue_time_out;
	}

	public void clear() {
		index = Collections.synchronizedList(new LinkedList<String>());
		indexContent = Collections.synchronizedList(new LinkedList<InstructionIndex>());
		executes = Collections.synchronizedList(new LinkedList<Instruction>());
		queues = new ConcurrentHashMap<>();
	}

}
