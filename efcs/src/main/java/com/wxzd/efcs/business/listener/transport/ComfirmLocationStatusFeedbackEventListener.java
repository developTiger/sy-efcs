package com.wxzd.efcs.business.listener.transport;

import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.event.listener.AbstractEventListener;
import com.wxzd.protocol.ProtocolError;
import com.wxzd.protocol.wcs.transport.feedback.ComfirmLocationStatusFeedbackEvent;
import com.wxzd.wms.core.application.queryService.StorageLocationQueryService;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.StorageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 堆垛机取放货前确认事件监听，如果当前库位没有异常，则允许取放货
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
@Component
public class ComfirmLocationStatusFeedbackEventListener extends AbstractEventListener<ComfirmLocationStatusFeedbackEvent> {

    @Autowired
    private MemoryInstructionAppService memoryInstructionAppService;

    @Autowired
    private StorageLocationQueryService storageLocationQueryService;

    @Autowired
    private ProcedureAppService procedureAppService;

    @Override
    public void onEvent(ComfirmLocationStatusFeedbackEvent event) throws Exception {
        // 根据指令号查到库存位置
        String com_no = event.getCom_no();
        ObjectResult<Instruction> result = memoryInstructionAppService.getByInstrNo(com_no);
        if (!result.isSuccess()) {
            event.setCode(ProtocolError.Parameter_Incorrect);
            event.setError("指令号不存在");
            return;
        }
        Instruction ins = result.getObject();
        if (ins == null) {
            event.setCode(ProtocolError.Unknown);
            event.setError("指令对象异常");
            return;
        }

        boolean hasError = false;
        String ErrorMessage = "目标存在异常";
        try {
            StorageLocation storageLocation = storageLocationQueryService.queryLocationByNo(event.getHouse_no(), event.getLocation_no());
            switch (event.getMoveType()) {
                case Put_In:
                    // 取货时，库位中应该有货
                    hasError = (storageLocation.getLoc_is_error() || storageLocation.getStorage_status() == StorageStatus.empty);
                    ErrorMessage = "目标存在异常或库位无货";
                    break;
                case Get_Out:
                    // 放货时，库位中应该无货
                    hasError = (storageLocation.getLoc_is_error() || storageLocation.getStorage_status() == StorageStatus.haveGoods);
                    ErrorMessage = "目标存在异常或库位有货";
                    // 获取新库位位置
                    Instruction instruction = procedureAppService.redistributeStorageLocation(event.getHouse_no(), event.getDevice_no(), event.getPalletNo());
                    String newLocation = instruction.getTo_pos();
                    event.setNew_location_no(newLocation);
                    // 保存到数据库，Sent状态
                    memoryInstructionAppService.addSentInstruction(instruction);
                    break;
            }
        } catch (Exception ex) {
            ErrorMessage = ex.getMessage();
        }

        if (hasError) {
            event.setCode(ProtocolError.Unknown);
            event.setError(ErrorMessage);
        } else {
            event.setCode(ProtocolError.None);
            event.setError("");
        }
    }

}
