package com.wxzd.efcs.business.instruction;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wxzd.configration.catlConfig.InstructionConfig;
import com.wxzd.efcs.alarm.domain.events.InstructionAlarmEvent;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.efcs.business.domain.service.MemoryInstructionService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.thread.ThreadUtl;
import com.wxzd.gaia.common.base.core.type.TypeUtl;
import com.wxzd.gaia.common.base.spring.core.Web;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.protocol.ProtocolException;
import com.wxzd.protocol.wcs.domain.enums.CommandType;
import com.wxzd.protocol.wcs.domain.enums.MovePolicy;
import com.wxzd.protocol.wcs.transport.command.ExecutingCommandEvent;

/**
 * 指令自动下发服务
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
@Web
@Component
@Lazy(false)
public class InstructionAutoExecute implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(InstructionAutoExecute.class);

	@Autowired
	private InstructionService instructionService;

	@Autowired
	private MemoryInstructionService memoryInstructionService;

	@Autowired
	private MemoryInstructionAppService memoryInstructionAppService;

	@Resource
	DefaultWebSecurityManager securityManager;

	@Autowired
	private QueueInstructionPolicyService queueInstructionPolicyService;

	/**
	 * 加载数据库数据
	 * 初始化内存质量
	 */
	public void initial() {
		List<Instruction> items = instructionService.getLiveInstruction().getItems();
		for (Instruction item : items) {
			memoryInstructionService.createInstr(item);
		}
	}

	/**
	 * 定时调度执行指令
	 */
	public void execute() {
		new Thread() {
			@Override
			public void run() {
				//TODO 9 临时的为了解决数据库存储的时候异常
				SecurityUtils.setSecurityManager(securityManager);
				for (;;) {
					try {
						ListResult<Instruction> executeInstruction = memoryInstructionService.getExecuteInstruction();
						if(executeInstruction.getItems().size()!=0){
							log.trace("发送指令数：{}", executeInstruction.getItems().size());
						}
//						log.trace("发送指令数：{}", executeInstruction.getItems().size());
						for (Instruction instruction : executeInstruction.getItems()) {
							try {
								ExecutingCommandEvent command = new ExecutingCommandEvent();
								command.setCom_no(instruction.getInstr_no());
								if (instruction.getInstr_type() != null) {
									command.setCom_type(TypeUtl.parse(instruction.getInstr_type().name(), CommandType.class));
								}
								command.setDevice_no(instruction.getEquip_no());
								command.setHouse_no(instruction.getHouse_no());
								if (instruction.getMove_policy() != null) {
									command.setMove_policy(TypeUtl.parse(instruction.getMove_policy().name(), MovePolicy.class));
								}
								command.setPallet_no(instruction.getPallet_no());
//								command.setPriority(instruction.getInstr_level());
								command.setPriority(3);//下发的指令优先级为统一的
								command.setSource(this);
								command.setSource_location(instruction.getFrom_pos());
								command.setTarget_location(instruction.getTo_pos());
								
								//TODO 3 临时解决rework的方案
								//如果soruce是1300 target是1310的时候设定优先级为2
								if("1300".equals(instruction.getFrom_pos())&&"1310".equals(instruction.getTo_pos())){
									command.setPriority(2);
								}
								
								try {
//									if (DevContext.isCommunicateFake()) {
//										System.out.println("模拟发送命令：" + JsonUtl.parseWithoutException(command));
//									} else {
										ApplicationEventPublisher.trigger(command);
//									}
									//变更状态
									instruction.setSend_time(new Date());
									instruction.setInstr_status(InstructionStatus.Send);
									instructionService.saveInstr(instruction);
									//								memoryInstructionAppService.setInstrSatus(instruction.getInstr_no(), InstructionStatus.Sent);
									//如果是队列的话那么队列中活动的内容都增加1
									if (isQueue(instruction.getSend_type())) {
										String queue = instruction.getQueue_no();
										List<Instruction> instructionByQueueNo = memoryInstructionService.getInstructionByQueueNo(queue).getItems();
										if (instructionByQueueNo != null) {
											instructionByQueueNo = queueInstructionPolicyService.policyExecute(instructionByQueueNo);
											for (Instruction ins : instructionByQueueNo) {
												if (InstructionStatus.Waiting.equals(ins.getInstr_status())) {
													ins.setInstr_level(ins.getInstr_level() + 1);
												}
											}
										}
									}
								} catch (Exception e) {
									
//									log.warn("", e);
									ProtocolException type = getType(e);
									if (type != null) {
										String code = type.getCode()==null?"":type.getCode().name();
										String message = type.getMessage()==null?"":type.getMessage();
										memoryInstructionAppService.abnormalFinish(instruction.getInstr_no(), EfcsErrorCode.instruction_exception, code + message);
										try {
											InstructionAlarmEvent alarm = new InstructionAlarmEvent();
											alarm.setHouseNo(instruction.getHouse_no());
											alarm.setDeviceNo(instruction.getEquip_no());
											alarm.setLocation(instruction.getEquip_no());
											alarm.setContent(e.getMessage());
											ApplicationEventPublisher.trigger(alarm);
										} catch (Exception e1) {
											log.warn("", e1);
										}
									} else {
										log.warn("", e);
									}
//									if (e instanceof ProtocolException || (e.getCause() != null && e.getCause() instanceof ProtocolException)) {
//										//										ProtocolException e2 = (ProtocolException) e;
//										memoryInstructionAppService.abnormalFinish(instruction.getInstr_no(), EfcsErrorCode.instruction_exception, e.getMessage());
//										try {
//											InstructionAlarmEvent alarm = new InstructionAlarmEvent();
//											alarm.setHouseNo(instruction.getHouse_no());
//											alarm.setDeviceNo(instruction.getEquip_no());
//											alarm.setLocation(instruction.getEquip_no());
//											alarm.setContent(e.getMessage());
//											ApplicationEventPublisher.trigger(alarm);
//										} catch (Exception e1) {
//											log.warn("", e1);
//										}
//									} else {
//										log.warn("", e);
//									}
//									//										else {
//									//											memoryInstructionAppService.abnormalFinish(instruction.getInstr_no(), EfcsErrorCode.Unknown, e.getMessage());
//									//										}
								}

							} catch (Exception e) {
								log.warn("", e);
							}
						}
					} catch (Exception e) {
						log.warn("", e);
					}
					ThreadUtl.sleep(InstructionConfig.getExecuter_interval());
				}
			}
		}.start();
	}

	public static ProtocolException getType(Exception exception) {
		if (exception == null) {
			return null;
		}
		Throwable throwable = exception;
		for (; throwable.getCause() != null;) {
			throwable = throwable.getCause();
			if (throwable instanceof ProtocolException) {
				return (ProtocolException) throwable;
			}
		}
		return null;
	}
	
	@Autowired
	private MemoryInstructionFairy memoryInstructionFairy;

	private boolean isQueue(SendType type) {
		return memoryInstructionFairy.isQueue(type);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initial();
		execute();
	}

}
