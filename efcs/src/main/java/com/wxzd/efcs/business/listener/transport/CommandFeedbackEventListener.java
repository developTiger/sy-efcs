package com.wxzd.efcs.business.listener.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wxzd.efcs.alarm.domain.events.InstructionAlarmEvent;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.service.PalletDispatchAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.core.type.TypeUtl;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.protocol.ProtocolError;
import com.wxzd.protocol.wcs.domain.enums.ExecuteStatus;
import com.wxzd.protocol.wcs.transport.feedback.CommandFeedbackEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Leon Regulus on 2017/4/20.
 * @version 1.0
 * @since 1.0
 */
@Component
public class CommandFeedbackEventListener extends AbstractEventListener<CommandFeedbackEvent> {

	private static final Logger log = LoggerFactory.getLogger(CommandFeedbackEventListener.class);

	@Autowired
	private ProcedureAppService procedureAppService;

	@Autowired
	private PalletDispatchAppService palletDispatchAppService;

	@Autowired
	private MemoryInstructionAppService memoryInstructionAppService;

    private static ConcurrentHashMap<String, String> deviceLockObject = new ConcurrentHashMap<>();


    @Override
	public void onEvent(CommandFeedbackEvent event) throws Exception {

    	// 定点作业指令完成
    	if (!StringUtl.isEmpty(event.getCom_no()) && Integer.parseInt(event.getCom_no()) < 0 ) {
			if(event.getPallet_no()!=null) {
				procedureAppService.commandFinished(event);
			}
    		return;
    	}
    	
        // 增加和校验同步锁
//        if (deviceLockObject.get(event.getDevice_no()) == null) {
//            deviceLockObject.put(event.getDevice_no(), "CommandFeedbackEvent");
//        } else {
//            event.setCode(ProtocolError.Communication);
//            event.setError("未处理完到上一次完成反馈前，不处理下一次完成反馈操作");
//            return;
//        }
//
//        try {
	        if (ExecuteStatus.Exception.equals(event.getCom_status())) {
	            String exceptionMessage = "CommandFeedbackEvent 触发异常";
	            // 将指令标识为异常指令
	            GaiaResult abnormalFinish = memoryInstructionAppService.abnormalFinish(event.getCom_no(), EfcsErrorCode.instruction_exception, exceptionMessage);
	            if (!abnormalFinish.isSuccess()) {
	                log.warn("指令异常：{}", JsonUtl.parseWithoutException(event));
	                event.setCode(ProtocolError.Unknown);
	                event.setError(abnormalFinish.getMessage());
	                return;
	            }
	            // 发布一个异常的事件出来
	            try {
	                InstructionAlarmEvent alarm = new InstructionAlarmEvent();
	                alarm.setHouseNo(event.getHouse_no());
	                alarm.setDeviceNo(event.getDevice_no());
	                alarm.setLocation(event.getLocation());
	                alarm.setContent(exceptionMessage);
	                ApplicationEventPublisher.trigger(alarm);
	            } catch (Exception e1) {
	                log.warn("", e1);
	            }
	        } else if (ExecuteStatus.Scrapped.equals(event.getCom_status())) {
	            String exceptionMessage = "CommandFeedbackEvent 触发作废";
	            // 将指令标识为作废指令
	            GaiaResult abnormalFinish = memoryInstructionAppService.abnormalFinish(event.getCom_no(), EfcsErrorCode.instruction_scrapped, exceptionMessage);
	            if (!abnormalFinish.isSuccess()) {
	                log.warn("指令作废：{}", JsonUtl.parseWithoutException(event));
	                event.setCode(ProtocolError.Unknown);
	                event.setError(abnormalFinish.getMessage());
	                return;
	            }
	        } else {
	            InstructionStatus status = TypeUtl.parse(event.getCom_status().name(), InstructionStatus.class);
	            GaiaResult setInstrSatus = memoryInstructionAppService.setInstrSatus(event.getCom_no(), status);
	            if (!setInstrSatus.isSuccess()) {
	                log.warn("指令异常：{}", JsonUtl.parseWithoutException(event));
	                event.setCode(ProtocolError.Unknown);
	                event.setError(setInstrSatus.getMessage());
	                return;
	            }
	            // 触发工序执行
	            
            	procedureAppService.commandFinished(event);
	        }
	        
//        } catch (Exception e) {
//			e.printStackTrace();
//		}
        // 去掉同步锁
//        deviceLockObject.remove(event.getDevice_no());
	}
}
