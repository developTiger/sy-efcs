package com.wxzd.efcs.business.listener.transport;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wxzd.protocol.ProtocolError;
import org.apache.commons.collections.list.SynchronizedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.gaia.common.base.core.object.ObjectUtl;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.wcs.transport.feedback.PalletArriveFeedbackEvent;

/**
 * @author Leon Regulus on 2017/4/20.
 * @version 1.0
 * @since 1.0
 */
@Component
public class PalletArriveFeedbackEventListener extends AbstractEventListener<PalletArriveFeedbackEvent> {

	@Autowired
	private ProcedureAppService procedureAppService;

	@Autowired
	private MemoryInstructionAppService memoryInstructionAppService;

	private static ConcurrentHashMap<String, String> deviceLockObject = new ConcurrentHashMap<>();

	@Override
	public void onEvent(PalletArriveFeedbackEvent event) throws Exception {

	    // 增加和校验同步锁
//	    if (deviceLockObject.get(event.getDevice_no()) == null) {
//	        deviceLockObject.put(event.getDevice_no(), "PalletArriveFeedbackEvent");
//        } else {
//	        event.setCode(ProtocolError.Communication);
//	        event.setError("未接受到上一次到位反馈前，不处理下一次到位操作");
//	        return;
//        }
//
	    try {
	        /**
	         * 0.如果指令异常则警告
	         * 1.如果指令已存在，直接下发
	         * 2.如果指令不存在走业务创建指令
	         */
	        String pallet_no = event.getPallet_no();
	        String house_no = event.getHouse_no();
	        String device_no = event.getDevice_no();
	        if (StringUtl.isEmpty(pallet_no) || StringUtl.isEmpty(house_no) || StringUtl.isEmpty(device_no)) {
	            //TODO 3 @py 返回异常指令无法走下去
	        } else {
				/*
				 * wcs 会等到此次请求完成才会再次发送同一个地点的到位
				 * 所以创建同一指令不会并发
				 */
	            List<Instruction> instructions = memoryInstructionAppService.getAllInstruction().getItems();
	            Instruction target = null;
	            for (Instruction instruction : instructions) {
	                if (pallet_no.equals(instruction.getPallet_no())
	                        && house_no.equals(instruction.getHouse_no())
	                        && device_no.equals(instruction.getFrom_pos())) {
	                    target = instruction;
	                    break;
	                }
	            }
	            if (target != null) {//如果指令已存在并且不是wait状态则下发指令
					/*
					 * 1.如果是wait则不动作
					 * 2.如果是send则变成wait继续发送
					 */
	                //此指令不应该会是create状态的指令，但是为了避免指令挂掉此处算在内
	                if (!ObjectUtl.equals(target.getInstr_status(), InstructionStatus.Waiting)) {
	                    memoryInstructionAppService.setInstrSatus(target, InstructionStatus.Waiting);
	                }
	            } else {//否则走业务并下发指令
	            	
            		procedureAppService.palletArrive(event);
	            	
	            }
	        }
        } catch (Exception e) {
		 	event.setError(e.getMessage());
		}finally {
			// 去掉同步锁
//        deviceLockObject.remove(event.getDevice_no());
		}

	}
}
