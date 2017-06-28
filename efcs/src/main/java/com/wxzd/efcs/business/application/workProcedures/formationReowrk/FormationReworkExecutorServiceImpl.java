package com.wxzd.efcs.business.application.workProcedures.formationReowrk;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.configration.catlConfig.SchedulerConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.ProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.AllotPolicyType;
import com.wxzd.policy.locationAllot.AllotStorageLocation;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.policy.locationAllot.LocationAllotService;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("formationReworkExecutorService")
public class FormationReworkExecutorServiceImpl implements FormationReworkExecutorService<DefaultProcedure, ObjectResult<Instruction>> {


    @Autowired
    FmProcedureService fmProcedureService;
    @Autowired
    @Qualifier("detaultLocationAllotService")
    LocationAllotService locationAllotService;
    @Autowired
    StorageService storageService;
    @Autowired
    MemoryInstructionAppService instructionService;

    @Autowired
    PalletDispatchService dispatchService;

    @Autowired
    StorageLocationService storageLocationService;

    @Autowired
    LocationAllotQyery locationAllotQyery;


    @Autowired
    FcsSchedulerService fcsSchedulerService;

    private String cuttentProcedureRoute = ProcedureConfig.Formation_Rework;
    private WorkProcedure currentWorkProcedure = WorkProcedure.Formation_Rework;
    private WorkProcedure nextProcedure = WorkProcedure.Formation_Split;

    @Override
    public ObjectResult<Instruction> doWorkProcedure(DefaultProcedure procedureInfo) {
        //默认一个工序 会经历 3个库位。
        //1、待入
        //2、入库
        //3、出库
        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
        /**
         * 高温完成 化成开始
         */
        String route = "";
        SendType sendType = SendType.Direct;
        String stockNo = "";
        InstructionType instructionType = InstructionType.Transport;
        if (procedureInfo.getPalletDispatch() == null) {
            procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));
            if (procedureInfo.getPalletDispatch() == null) {
                throw new RuntimeException("托盘调度异常,调度信息不存在！");
            }
        }
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_pallet_allot_location)) {

            Integer count = dispatchService.getInnerDetailCountByPalletDispatchId(procedureInfo.getPalletDispatch().getId());
            if (count < DispatcherConfig.pallet_battery_count) {
                throw new RuntimeException("电池数量不足，无法进化成，请补充假电池！");
            }
            FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedure == null) {
                //化成表单初始化
                procedure = new FmProcedure();
                procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
                procedure.setHouse_id(procedureInfo.getWarehouse().getId());
                procedure.setHouse_no(procedureInfo.getHouseNo());
                procedure.setPallet_no(procedureInfo.getPallet_no());
                fmProcedureService.saveFmProcedure(procedure);

            }
            instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                    procedureInfo.getPallet_no(),
                    instructionType,
                    stockNo,//是否设置为堆垛机
                    procedureInfo.getCurrentPos(),
                    DispatcherConfig.formation_rework_waitin_location,//入库库位
                    InstructionMovePolicy.Static,
                    sendType);

//            instruction.setInstr_level(6);
            instructionService.createInstr(instruction);
//                }
            //更新托盘状态
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
            return GaiaResultFactory.getObject(instruction);


        }
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_allot_location) || procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_waitin_location)) {

            Integer count = dispatchService.getInnerDetailCountByPalletDispatchId(procedureInfo.getPalletDispatch().getId());
            if (count < DispatcherConfig.pallet_battery_count) {
                throw new RuntimeException("电池数量不组，无法进化成，请补充假电池！");
            }
            FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedure == null) {
                //化成表单初始化
                procedure = new FmProcedure();
                procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
            }
            if (StringUtl.isEmpty(procedure.getIn_loc_no())) {
                ObjectResult<AllotStorageLocation> result = locationAllotService.allotStorageLocationIn(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getWarehouse().getId(),
                        procedureInfo.getHouseNo(), procedureInfo.getCurrentPos(), AllotPolicyType.FromBigToSmall, AllotPolicyType.FromSmallToBig);
                if (result.isSuccess()) {
                    //设置分配库位信息
                    procedure.setIn_loc_no(result.getObject().getStorageLocation().getLoc_no());
                    // procedure.setStay_time(procedureInfo.getStayTime());
                    procedure.setLoc_assign_time(new Date());
                    procedure.setPallet_status(PalletStatus.In_Waiting);
                    procedure.setFm_status(FmStatus.Executing);
                    procedure.setHouse_id(procedureInfo.getWarehouse().getId());
                    procedure.setHouse_no(procedureInfo.getHouseNo());
                    procedure.setPallet_no(procedureInfo.getPallet_no());
                    fmProcedureService.saveFmProcedure(procedure);
                    fcsSchedulerService.closeALlSchedulerByDevice(procedure.getHouse_id(),procedureInfo.getCurrentPos());

                } else {
                    //只执行一次，后面的就不做操作了
                    if (procedureInfo.getPalletOperate() != PalletOperate.AllotStorageLocation) {
                        if(procedure.getPallet_status()!=PalletStatus.AllotLocation) {
                            procedure.setPallet_status(PalletStatus.AllotLocation);
                            procedure.setFm_status(FmStatus.Executing);
                            fmProcedureService.saveFmProcedure(procedure);
                            procedureInfo.getPalletDispatch().setError_desc(result.getMessage());
                            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                            String defaultProcedureJson = JsonUtl.parse(ConvertFactory.convertToCleanProcedure(procedureInfo));
                            FcsScheduler scheduler = new FcsScheduler(SchedulerType.ExecutionLoop, TaskType.AllotLocation, new Date(), currentWorkProcedure, defaultProcedureJson, SchedulerConfig.allotLocation_interval, SchedulerConfig.allotLocation_interval_unit);
                            scheduler.setDevice_no(procedureInfo.getCurrentPos());
                            scheduler.setHouse_id(procedureInfo.getWarehouse().getId());
                            scheduler.setHouse_no(procedureInfo.getHouseNo());
                            scheduler.setPallet_no(procedureInfo.getPallet_no());
                            fcsSchedulerService.saveScheduler(scheduler);
                        }
                    }
                    return new ObjectResult<>(false, result.getMessage());

                }
            }
            route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), ProcedureRouteMatch.getCubicXPos(procedure.getIn_loc_no()));
            route = ProcedureRouteMatch.replaceEndPosition(route, procedure.getIn_loc_no());

            String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
            if (ProcedureRouteMatch.isStocker(nextPos)) {
                stockNo = nextPos;
                nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                instructionType = InstructionType.Stock_In;
                sendType = SendType.QueuesDirect;
            }
            procedureInfo.getPalletDispatch().setProcedure_route(route);

            //生成指令
            instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                    procedureInfo.getPallet_no(),
                    instructionType,
                    stockNo,//是否设置为堆垛机
                    procedureInfo.getCurrentPos(),
                    nextPos,//入库库位
                    InstructionMovePolicy.Static,
                    sendType);

            instruction.setInstr_level(6);
            instruction.setQueue_no(instruction.getHouse_no() + instruction.getEquip_no() + "In");

            instructionService.createInstr(instruction);
//                }
            //更新托盘状态
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
            return GaiaResultFactory.getObject(instruction);

        } else {
            FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedureInfo.getPalletDispatch().getWork_procedure() != currentWorkProcedure) {
                throw new RuntimeException("托盘调度异常,工序不是化成工序！");
            }

            if (procedureInfo.getPalletOperate() == PalletOperate.ChangeLocation) {
                try {
                    if (StringUtl.isEmpty(procedure.getOut_loc_no())) {
                        Integer row = Integer.parseInt(ProcedureRouteMatch.getCubicXPos(procedure.getIn_loc_no()));
                        List<Integer> rows = new ArrayList<>();
                        rows.add(row);
                        ObjectResult<StorageLocation> result = locationAllotQyery.getStorageLocation(currentWorkProcedure, procedureInfo.getWarehouse().getId(), rows, AllotPolicyType.FromBigToSmall, AllotPolicyType.FromBigToSmall);
                        if (result.isSuccess()) {
                            //设置分配库位信息
                            procedure.setOut_loc_no(result.getObject().getLoc_no());
                            // procedure.setStay_time(procedureInfo.getStayTime());
                            procedure.setLoc_assign_time(new Date());
                            procedure.setPallet_status(PalletStatus.In_Waiting);
                            procedure.setFm_status(FmStatus.Executing);
                            procedure.setHouse_id(procedureInfo.getWarehouse().getId());
                            procedure.setHouse_no(procedureInfo.getHouseNo());
                            procedure.setPallet_no(procedureInfo.getPallet_no());
                            procedure.setRemark(PalletOperate.ChangeLocation.name());
                            fmProcedureService.saveFmProcedure(procedure);
                            storageLocationService.setInFobiden(result.getObject(), LockDirection.inLock, true);

                            String nextPos = "";

                            stockNo = DispatcherConfig.formation_stocker_2;
                            sendType = SendType.QueuesDirect;
                            instructionType = instructionType.Stock_Change;
                            nextPos = result.getObject().getLoc_no();

                            //生成指令
                            instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                                    procedureInfo.getPallet_no(),
                                    instructionType,
                                    stockNo,//是否设置为堆垛机
                                    procedureInfo.getCurrentPos(),
                                    nextPos,//入库库位
                                    InstructionMovePolicy.Static,
                                    sendType);

                            instruction.setInstr_status(InstructionStatus.Created);
                            instructionService.createInstr(instruction);
//                }
                            //更新托盘状态
                            return GaiaResultFactory.getObject(instruction);
                        } else
                            return new ObjectResult<>(false, result.getMessage());
                    } else
                        return new ObjectResult<>(false, "库位已分配等待取货中");
                } catch (Exception ex) {
                    return new ObjectResult<>(false, "库位更换失败！");

                }
            }
            //化成分库位 入库完成
            if (procedureInfo.getStorageLocation() != null && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic
                    && procedureInfo.getPalletOperate() == PalletOperate.CommandFinished) {
                procedure.setIn_time(new Date());
                procedure.setPallet_status(PalletStatus.In_Finished);
                if (procedure.getIn_loc_no() != null) {
                    if (procedure.getIn_loc_no().equals(procedureInfo.getStorageLocation().getLoc_no())) {
                        storageLocationService.setHasGoodAndAndReduceAllotLocationInlock(procedureInfo.getStorageLocation());
                    } else {
                        //更换库位
                        if(procedureInfo.getStorageLocation().getLoc_no().equals(procedure.getOut_loc_no())){
                            List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), procedure.getIn_loc_no());
                            storageLocationService.setIsHasGood(locations.get(0), false);
                            storageLocationService.setHasGoodAndAndReduceAllotLocationInlock(procedureInfo.getStorageLocation());
                            procedure.setOut_loc_no("");
                        }else{//满入 或其他特殊情况 换库位
                            storageLocationService.setIsHasGood(procedureInfo.getStorageLocation(), false);
                        }

                    }
                }else{
                    storageLocationService.setIsHasGood(procedureInfo.getStorageLocation(),true);
                }
                procedure.setIn_loc_no(procedureInfo.getStorageLocation().getLoc_no());

                procedure.setIs_auto_out(0);
                fmProcedureService.saveFmProcedure(procedure);
                procedureInfo.getPalletDispatch().setCurrent_procedure_time(new Date());
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                //库存从 移动到库里
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        procedureInfo.getStorageLocation().getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                storageLocationService.setHasGoodAndAndReduceAllotLocationInlock(procedureInfo.getStorageLocation());

                return GaiaResultFactory.getObject(instruction);


            } else if (procedureInfo.getPalletOperate() == PalletOperate.OperateComplete //化成完成
                    ) {
                procedure.setOut_plan_time(new Date());
                //完成 组盘工序单
                procedure.setPallet_status(PalletStatus.Out_Waiting);
                fmProcedureService.saveFmProcedure(procedure);

                route = ProcedureRouteMatch.getRouteByEquipNo(ProcedureRouteMatch.getCubicXPos(procedure.getIn_loc_no()), DispatcherConfig.formation_down_location);

                String nextPos = ProcedureRouteMatch.getNextPosition(route, ProcedureRouteMatch.getCubicXPos(procedure.getIn_loc_no()));
                if (ProcedureRouteMatch.isStocker(nextPos)) {
                    stockNo = nextPos;
                    nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                    instructionType = InstructionType.Stock_Out;
                    sendType = SendType.QueuesDirect;
                }
//                procedureInfo.getPalletDispatch().setProcedure_route(route);
                //仅更新托盘状态,托盘未移动
                dispatchService.setPalletStatus(procedureInfo.getPalletDispatch(), PalletStatus.Out_Waiting);
                //生成出库指令，计划指令，待mes 下发可出库时生效
                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        instructionType,
                        stockNo,//是否设置为堆垛机
                        procedureInfo.getCurrentPos(),
                        DispatcherConfig.formation_pallet_split_catch_location,
                        InstructionMovePolicy.Static,
                        sendType);
                instruction.setInstr_status(InstructionStatus.Created);
                instruction.setInstr_level(DispatcherConfig.formation_instruction_level);
                instructionService.createInstr(instruction);

                return GaiaResultFactory.getObject(instruction);
            } else if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_pallet_split_catch_location)) {
                //到达下架位 工序结束
                procedure.setOut_time(new Date());
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);

                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), DispatcherConfig.formation_pallet_split_catch_location);
                if (locations.size() == 0) {
                    throw new RuntimeException(I18nContext.getMessage("拆盘缓存设备库位未设置"));
                }
                procedure.setOut_loc_no(DispatcherConfig.formation_pallet_split_catch_location);
                fmProcedureService.saveFmProcedure(procedure);
                //库存从 上架货位 移动到高温库位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        locations.get(0).getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                if (!StringUtl.isEmpty(procedure.getIn_loc_no())) {
                    storageLocationService.setIsHasGood(procedure.getHouse_id(), procedure.getIn_loc_no(), false);

                }
                //生成出库指令，计划指令，待mes 下发可出库时生效
//                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
//                        procedureInfo.getPallet_no(),
//                        instructionType,
//                        stockNo,//是否设置为堆垛机
//                        procedureInfo.getCurrentPos(),
//                        DispatcherConfig.formation_pallet_split_catch_location,
//                        InstructionMovePolicy.Static,
//                        sendType);
////                instruction.setInstr_status(InstructionStatus.Created);
////                instruction.setInstr_level(DispatcherConfig.formation_instruction_level);
//                instructionService.createInstr(instruction);
                return GaiaResultFactory.getObject(instruction);
            } else {
                //都不是上面的情况
                //启动自动寻径
                if (procedureInfo.getStorageLocation() != null
                        && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic && procedureInfo.getPalletOperate() == PalletOperate.ChangeProcedure) {
                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getStorageLocation().getX_pos().toString(), DispatcherConfig.formation_allot_location);
                } else {
                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_allot_location);
                }
                procedureInfo.getPalletDispatch().setProcedure_route(route);

                String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
                if (ProcedureRouteMatch.isStocker(nextPos)) {
                    stockNo = nextPos;
                    nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                    instructionType = InstructionType.Stock_Move;
                    sendType = SendType.QueuesDirect;
                }
                procedureInfo.getPalletDispatch().setProcedure_route(route);
                //创建指令
                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        instructionType,
                        stockNo,
                        procedureInfo.getCurrentPos(),
                        nextPos,
                        InstructionMovePolicy.Static,
                        sendType);
                if (DispatcherConfig.high_temperature_waitin.equals(procedureInfo.getCurrentPos())) {
                    instruction.setInstr_level(10);
                }
                instructionService.createInstr(instruction);
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
                return GaiaResultFactory.getObject(instruction);

            }
        }

    }


    /**
     * 是否需要创建下一道工序
     *
     * @param workProcedure
     * @param palletStatus
     * @param currentPos
     * @return
     */
    @Override
    public Boolean needActiveNextProcedure(WorkProcedure workProcedure, PalletStatus palletStatus, String currentPos) {
        if (palletStatus == PalletStatus.Out_Finished)
            return true;
        return false;
    }

    /**
     * 获取下一道工序
     *
     * @param workProcedure
     * @return
     */
    @Override
    public WorkProcedure getNextProcedure(WorkProcedure workProcedure) {
        return nextProcedure;
    }

    /**
     * 异常结束工序
     *
     * @param workProcedure
     * @param palletNo
     * @param remark        @return
     */
    @Override
    public ObjectResult<Instruction> errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
        FmProcedure procedure = fmProcedureService.getProcedureByPallet(palletNo, currentWorkProcedure);
        if (procedure != null) {
            procedure.setFm_status(FmStatus.ErrorFinished);
            procedure.setError_code(EfcsErrorCode.ErrorFinish);
            procedure.setError_desc(remark);
//        dispatch.setError_desc(remark);
            fmProcedureService.saveFmProcedure(procedure);
        }
        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
        dispatch.setProcedure_route("");
        dispatch.setPallet_status(PalletStatus.Out_Waiting);
        dispatchService.saveDispatchWithErrorMoveDetail(dispatch, EfcsErrorCode.ErrorFinish, remark);

        return new ObjectResult<>(true, "");
    }


    /**
     * 非正常的开始一个新工序
     *
     * @param workProcedure
     * @param procedureInfo
     * @return
     */
    @Override
    public ObjectResult<Instruction> initProcedureCurrenPosImproper(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {


        procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));

        FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
        if (procedure == null) {
            //化成表单初始化
            procedure = new FmProcedure();
            procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
        }
        procedure.setPallet_status(PalletStatus.In_Waiting);
        procedure.setFm_status(FmStatus.Executing);
        procedure.setHouse_id(procedureInfo.getWarehouse().getId());
        procedure.setHouse_no(procedureInfo.getHouseNo());
        procedure.setPallet_no(procedureInfo.getPallet_no());
        fmProcedureService.saveFmProcedure(procedure);
        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());


        return GaiaResultFactory.getObject(null);
    }


}
