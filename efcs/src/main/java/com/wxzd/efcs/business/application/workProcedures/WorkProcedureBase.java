//package com.wxzd.efcs.business.application.workProcedures;
//
//import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
//import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
//import com.wxzd.efcs.business.domain.entities.Instruction;
//import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
//import com.wxzd.efcs.business.domain.enums.*;
//import com.wxzd.efcs.business.domain.service.FmProcedureService;
//import com.wxzd.efcs.business.domain.service.InstructionService;
//import com.wxzd.efcs.business.domain.service.PalletDispatchService;
//import com.wxzd.gaia.common.base.core.result.GaiaResult;
//import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
//import com.wxzd.gaia.common.base.core.result.ObjectResult;
//import com.wxzd.policy.locationAllot.AllotPolicyType;
//import com.wxzd.policy.locationAllot.LocationAllotService;
//import com.wxzd.wms.core.domain.entities.StorageLocation;
//import com.wxzd.wms.core.domain.service.StorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import java.util.Date;
//
///**
// * Created by zhouzh on 2017/4/21.
// */
//public abstract class WorkProcedureBase {
//
//
//    @Autowired
//    FmProcedureService fmProcedureService;
//    @Autowired
//    @Qualifier("detaultLocationAllotService")
//    LocationAllotService locationAllotService;
//    @Autowired
//    StorageService storageService;
//    @Autowired
//    InstructionService instructionService;
//
//    @Autowired
//    PalletDispatchService dispatchService;
//
//    protected GaiaResult workProcedureCreate(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {
//        //初始化 高温工序单
//        FmProcedure procedure = new FmProcedure();
//        procedure.newProcedure(workProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
//        fmProcedureService.saveFmProcedure(procedure);
//
//        //创建指令 返回
//        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
//        instruction.newInstruction(workProcedure,
//                procedureInfo.getPallet_no(),
//                InstructionType.Stock_Move,
//                procedureInfo.getCurrentPos(),
//                procedureInfo.getCurrentPos(),
//                ProcedureRouteMatch.getNextPosition(getCurrentRout(), procedureInfo.getCurrentPos()),
//                InstructionMovePolicy.Static,
//                SendType.Direct);
//        instruction.setEquip_no(procedureInfo.getCurrentPos());
//        instruction.setInstr_type(InstructionType.Stock_Move);
//        instructionService.createInstr(instruction);
//        //更新托盘状态
//        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, workProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//        return GaiaResultFactory.getObject(instruction);
//    }
//
//
//    protected GaiaResult workProcedureExecuting(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {
//        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
//        FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no());
//        procedure.setIn_time(new Date());
//        procedure.setPallet_status(PalletStatus.In_Finished);
//        if( procedureInfo.getStayTime()!=null&&procedureInfo.getStayTime() >0) {
//            procedure.setOut_plan_time(new Date(new Date().getTime() + procedureInfo.getStayTime() * 60 * 1000));//根据时长计算
//            procedure.setIs_auto_out(0);
//        }
//        else{
//            procedure.setIs_auto_out(1);
//        }
//
//        fmProcedureService.saveFmProcedure(procedure);
//
//        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, workProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//        // storageTransfer(UUID houseId, UUID toLocationId, String palletNo,String formNo, String businessType)
//        //库存从 上架货位 移动到高温库位
//        storageService.storageTransfer(
//                procedureInfo.getWarehouse().getId(),
//                procedureInfo.getStorageLocation().getId(),
//                procedureInfo.getPallet_no(),
//                procedure.getForm_no(),
//                workProcedure.toString()
//        );
//
//        //生成出库指令，计划指令，待mes 下发可出库时生效
//        instruction.newInstruction(workProcedure,
//                procedureInfo.getPallet_no(),
//                InstructionType.Stock_Out,
//                procedureInfo.getCurrentPos(),//是否设置为堆垛机
//                procedureInfo.getCurrentPos(),
//                ProcedureRouteMatch.getNextPosition(getCurrentRout(), procedureInfo.getCurrentPos()),
//                InstructionMovePolicy.Static,
//                SendType.Scheduler);
//
//        instructionService.createInstr(instruction);
//        return GaiaResultFactory.getSuccess();
//    }
//
//
//    protected GaiaResult workProcedureAllotLocation(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {
//        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
//        FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no());
//        procedure.newProcedure(workProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
//        ObjectResult<StorageLocation> result = locationAllotService.allotStorageLocationIn(procedureInfo.getWarehouse().getId(),
//                procedureInfo.getHouseNo(), procedureInfo.getCurrentPos(), AllotPolicyType.FromBigToSmall, AllotPolicyType.FromSmallToBig);
//        if (result.isSuccess()) {
//            //设置分配库位信息
//            procedure.setIn_loc_no(result.getObject().getLoc_no());
//            procedure.setStay_time(procedureInfo.getStayTime());
//            procedure.setLoc_assign_time(new Date());
//            procedure.setPallet_status(PalletStatus.In_Waiting);
//            procedure.setFm_status(FmStatus.Executing);
//
//
//            fmProcedureService.saveFmProcedure(procedure);
//            //更新托盘状态啊
//            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, workProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//            //生成指令
//            instruction.newInstruction(WorkProcedure.High_Temperature,
//                    procedureInfo.getPallet_no(),
//                    InstructionType.Stock_In,
//                    procedureInfo.getCurrentPos(),//是否设置为堆垛机
//                    procedureInfo.getCurrentPos(),
//                    procedure.getIn_loc_no(),//入库库位
//                    InstructionMovePolicy.Static,
//                    SendType.Direct);
//
//            instructionService.createInstr(instruction);
//            return GaiaResultFactory.getSuccess();
//
//        } else
//            return GaiaResultFactory.getError("");
//    }
//
//
//    protected GaiaResult workProcedureEnd(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {
//        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
//        FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no());
//        procedure.setOut_loc_no(procedureInfo.getStorageLocation().getLoc_no());
//        procedure.setOut_time(new Date());
//        procedure.setPallet_status(PalletStatus.Out_Finished);
//        procedure.setFm_status(FmStatus.Finished);
//        fmProcedureService.saveFmProcedure(procedure);
//        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, workProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//        // storageTransfer(UUID houseId, UUID toLocationId, String palletNo,String formNo, String businessType)
//        //库存从 上架货位 移动到高温库位
//        storageService.storageTransfer(
//                procedureInfo.getWarehouse().getId(),
//                procedureInfo.getStorageLocation().getId(),
//                procedureInfo.getPallet_no(),
//                procedure.getForm_no(),
//                WorkProcedure.High_Temperature.toString()
//        );
//
//        //生成出库指令，计划指令，待mes 下发可出库时生效
//        instruction.newInstruction(WorkProcedure.High_Temperature,
//                procedureInfo.getPallet_no(),
//                InstructionType.Stock_Out,
//                procedureInfo.getCurrentPos(),//是否设置为堆垛机
//                procedureInfo.getCurrentPos(),
//                ProcedureRouteMatch.getNextPosition(getCurrentRout(), procedureInfo.getCurrentPos()),
//                InstructionMovePolicy.Static,
//                SendType.Scheduler);
//
//        instructionService.createInstr(instruction);
//        return GaiaResultFactory.getObject(instruction);
//
//    }
//
//
//    protected abstract WorkProcedure getCurrentWorkProcedure();
//
//
//    protected abstract String getCurrentRout();
//
//
//}
