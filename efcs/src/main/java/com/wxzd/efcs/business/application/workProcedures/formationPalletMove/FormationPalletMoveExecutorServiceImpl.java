package com.wxzd.efcs.business.application.workProcedures.formationPalletMove;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.SchedulerConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.ProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
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
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
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
@Service("formationPalletMoveExecutorService")
public class FormationPalletMoveExecutorServiceImpl implements FormationPalletMoveExecutorService<DefaultProcedure, ObjectResult<Instruction>> {

    @Autowired
    FmProcedureService fmProcedureService;
    @Autowired
    @Qualifier("emptyPalletAllotPolicyService")
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

    private WorkProcedure currentWorkProcedure = WorkProcedure.Formation_PalletMove;
    private WorkProcedure nextProcedure = WorkProcedure.Formation_Palletize;

    @Override
    public ObjectResult<Instruction> doWorkProcedure(DefaultProcedure procedureInfo) {
        //默认一个工序 会经历 3个库位。
        //1、待入
        //2、入库
        //3、出库
        Instruction instruction = new Instruction(procedureInfo.getHouseNo());

        String route = "";
        SendType sendType = SendType.Direct;
        String stockNo = "";
        InstructionType instructionType = InstructionType.Transport;
        if (procedureInfo.getPalletDispatch() != null) {
            procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));
            if (procedureInfo.getPalletDispatch() == null) {
                throw new RuntimeException("托盘调度异常,调度信息不存在！");
            }
        }
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.high_temperature_waitin) || procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_waitin_location)) {
            procedureInfo.getPalletDispatch().setIs_empty(true);

            //清除遗留库存
            List<Storage> storages = locationAllotQyery.getStorageByPallet(procedureInfo.getPallet_no());
            if (storages.size() > 1) {
                List<Storage> outList = new ArrayList<>();
                for (Storage st : storages) {
                    if (st.getSto_type() == StorageType.sku) {
                        outList.add(st);
                    }
                }
                Storage fromContainer = new Storage(storages.get(0).getLocation_id(), "", StorageType.container);
                fromContainer.setHouse_id(procedureInfo.getWarehouse().getId());
                storageService.storageOut(fromContainer, StorageType.sku, outList, WorkProcedure.Formation_Out.toString(), null, null);
            }
            storageService.setPalletIsEmpty(procedureInfo.getPallet_no(), true);

            try {
                //分配库位
                //            route = procedureInfo.getPalletDispatch().getProcedure_route();

                //            if (StringUtl.isEmpty(route) || (ProcedureRouteMatch.isRouteEnd(route, procedureInfo.getCurrentPos()))) {
                //                if (StringUtl.isEmpty(procedureInfo.getPalletDispatch().getStorage_location())) {
                ObjectResult<AllotStorageLocation> result;
                if(StringUtl.isEmpty(procedureInfo.getPalletDispatch().getStorage_location())) {
                    result = locationAllotService.allotStorageLocationIn(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getWarehouse().getId(),
                            procedureInfo.getHouseNo(), procedureInfo.getCurrentPos(), AllotPolicyType.FromBigToSmall, AllotPolicyType.FromBigToSmall);
                }else{
                    List<StorageLocation> storageLocations=locationAllotQyery.getEquipLocation(procedureInfo.getHouseNo(),procedureInfo.getPalletDispatch().getStorage_location());
                    if(storageLocations==null||storageLocations.size()==0){
                        result= new ObjectResult<>(false,"库位分配失败，未知库位号："+procedureInfo.getPalletDispatch().getStorage_location()+";托盘自动入1010");
                    }else {
                        AllotStorageLocation alloc = new AllotStorageLocation();
                        alloc.setHouseNo(procedureInfo.getHouseNo());
                        alloc.setStorageLocation(storageLocations.get(0));
                        alloc.setLocationCode(storageLocations.get(0).getLoc_no());
                        result = GaiaResultFactory.getObject(alloc);
                    }
                }
                if (result.isSuccess()) {
                    StorageLocation storageLocation = result.getObject().getStorageLocation();
                    if (storageLocation.getLoc_type() == LocationType.cubic) {

                        //如果是从1300 进入的空托，则直接去往目标库位
                        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.high_temperature_waitin)) {
                            route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), storageLocation.getX_pos().toString());
                            route = ProcedureRouteMatch.replaceEndPosition(route, result.getObject().getStorageLocation().getLoc_no());
                            instructionType = InstructionType.Stock_In;
                            procedureInfo.getPalletDispatch().setStorage_location(storageLocation.getLoc_no());
                        } else { //从2260 过来的托盘

//                            List<Storage> tempStorage = locationAllotQyery.getEquipLocationStorage(procedureInfo.getWarehouse().getId(), DispatcherConfig.formation_empty_pallet_temp_cubic_location);
                            //需要经过中转库位
                            procedureInfo.getPalletDispatch().setStorage_location(storageLocation.getLoc_no());

                            dispatchService.newPalletDispatch(procedureInfo.getPalletDispatch());
                            instructionType = InstructionType.Stock_In;
                            instruction.newInstruction(procedureInfo.getHouseNo(),
                                    currentWorkProcedure,
                                    procedureInfo.getPallet_no(),
                                    instructionType,
                                    DispatcherConfig.formation_stocker_2,//是否设置为堆垛机
                                    procedureInfo.getCurrentPos(),
                                    DispatcherConfig.formation_empty_pallet_temp_cubic_location,//入库库位
                                    InstructionMovePolicy.Static,
                                    sendType);

                            instruction.setInstr_level(7);
                            instruction.setQueue_no(null);
                            instruction.setSend_type(SendType.Direct);
//                            if(tempStorage.size()>0){
                            instruction.setInstr_status(InstructionStatus.Created);
//                            }
                            instructionService.createInstr(instruction);
                            return GaiaResultFactory.getObject(instruction);
                        }


                    } else {

                        route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_scaner_no);
                        instructionType = InstructionType.Stock_Move;
                    }
                    procedureInfo.getPalletDispatch().setProcedure_route(route);
                } else {
                    //原为分配库位任务，但空托分配会卡盘，所以直接去1010 不管满不满

                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_scaner_no);
                    instructionType = InstructionType.Stock_Move;

//					if (procedureInfo.getPalletOperate() != PalletOperate.AllotStorageLocation) {
//						dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
//								PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
//						String defaultProcedureJson = JsonUtl.parse(ConvertFactory.convertToCleanProcedure(procedureInfo));
//						FcsScheduler scheduler = new FcsScheduler(SchedulerType.ExecutionLoop, TaskType.AllotLocation, new Date(), currentWorkProcedure, defaultProcedureJson,
//								SchedulerConfig.allotLocation_interval, SchedulerConfig.allotLocation_interval_unit);
//						scheduler.setDevice_no(procedureInfo.getCurrentPos());
//						scheduler.setHouse_id(procedureInfo.getWarehouse().getId());
//						scheduler.setHouse_no(procedureInfo.getHouseNo());
//						fcsSchedulerService.saveScheduler(scheduler);
//					}
                    return new ObjectResult<>(false, result.getMessage());
                }

            } catch (Exception ex) {
                route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_scaner_no);

            }
//qu xiao shangmian de zhushi yao shanchu ciju
//            route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_scaner_no);

            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
                    PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");

            String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
            if (ProcedureRouteMatch.isStocker(nextPos)) {
                stockNo = nextPos;
                nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                sendType = SendType.QueuesDirect;
            }
            instruction.newInstruction(procedureInfo.getHouseNo(),
                    currentWorkProcedure,
                    procedureInfo.getPallet_no(),
                    instructionType,
                    stockNo,//是否设置为堆垛机
                    procedureInfo.getCurrentPos(),
                    nextPos,//入库库位
                    InstructionMovePolicy.Static,
                    sendType);

            instruction.setInstr_level(7);
            instruction.setQueue_no(null);
            instruction.setSend_type(SendType.Direct);

            //TODO to stop 1010 send command
            if (!DispatcherConfig.noNeedSendNextPosition.contains(procedureInfo.getCurrentPos())) {
                instructionService.createInstr(instruction);
            }
//			instructionService.createInstr(instruction);

            //更新托盘状态
            return GaiaResultFactory.getObject(instruction);

        } else {
            if (procedureInfo.getStorageLocation()!=null&&procedureInfo.getStorageLocation().getLoc_no().equals(DispatcherConfig.formation_empty_pallet_temp_cubic_location)) {
                storageService.storageTransfer(procedureInfo.getWarehouse().getId(), procedureInfo.getStorageLocation().getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());

                instructionType = InstructionType.Stock_Change;

                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        instructionType,
                        DispatcherConfig.formation_stocker_1,//是否设置为堆垛机
                        procedureInfo.getCurrentPos(),
                        procedureInfo.getPalletDispatch().getStorage_location(),//入库库位
                        InstructionMovePolicy.Static,
                        sendType);
                instruction.setInstr_level(7);
                instruction.setQueue_no(null);
                instruction.setSend_type(SendType.Direct);
                instructionService.createInstr(instruction);
                return GaiaResultFactory.getObject(instruction);


            }
            //空托 入库完成

            //入到立库了
            if (procedureInfo.getStorageLocation() != null && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic) {
                //库存 移动到下架位
                storageService.storageTransfer(procedureInfo.getWarehouse().getId(), procedureInfo.getStorageLocation().getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());
                storageLocationService.setHasGoodAndAndReduceAllotLocationInlock(procedureInfo.getStorageLocation());
                procedureInfo.getPalletDispatch().setStorage_location(procedureInfo.getStorageLocation().getLoc_no());
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching,
                        PositionType.Storage_Location, procedureInfo.getCurrentPos(), "");
                //
                //                    String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
                //                    if (ProcedureRouteMatch.isStocker(nextPos)) {
                //                        stockNo = nextPos;
                //                        nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                //                        instructionType = InstructionType.Stock_Out;
                //                        sendType = SendType.QueuesDirect;
                //                    }
                //                    //生成出库指令，计划指令，待mes 下发可出库时生效
                //                    instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                //                            procedureInfo.getPallet_no(),
                //                            instructionType,
                //                            stockNo,//是否设置为堆垛机
                //                            procedureInfo.getCurrentPos(),
                //                            nextPos,//入库库位
                //                            InstructionMovePolicy.Static,
                //                            sendType);
                //
                //                    instruction.setHouse_no(procedureInfo.getHouseNo());
                //                    instructionService.createInstr(instruction);
                return GaiaResultFactory.getObject(instruction);

//                else {
//                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_scaner_no);
//
//                    String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
//                    if (ProcedureRouteMatch.isStocker(nextPos)) {
//                        stockNo = nextPos;
//                        nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
//                        sendType = SendType.QueuesDirect;
//                    }
//                    instruction.newInstruction(procedureInfo.getHouseNo(),
//                            currentWorkProcedure,
//                            procedureInfo.getPallet_no(),
//                            instructionType,
//                            stockNo,//是否设置为堆垛机
//                            procedureInfo.getCurrentPos(),
//                            nextPos,//入库库位
//                            InstructionMovePolicy.Static,
//                            sendType);
//
//                    instruction.setInstr_level(7);
//                    instruction.setQueue_no(null);
//                    instruction.setSend_type(SendType.Direct);
//
//
//                    instructionService.createInstr(instruction);
//
//                    //更新托盘状态
//                    return GaiaResultFactory.getObject(instruction);
//                }

            } else if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_pallet_to_hczpjxs)
                    || procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_palletize_catch_location)) {

                //获取空托缓存位
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), DispatcherConfig.formation_palletize_catch_location);
                if (locations.size() == 0) {
                    throw new RuntimeException(I18nContext.getMessage("空托缓存设备库位未设置"));
                }
                if (!StringUtl.isEmpty(procedureInfo.getPalletDispatch().getStorage_location())) {
                    //解锁空托货位
                    List<StorageLocation> storageLocation = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), procedureInfo.getPalletDispatch().getStorage_location());
                    storageLocationService.setIsHasGood(storageLocation.get(0), false);
                    storageLocationService.setInFobiden(storageLocation.get(0), LockDirection.outLock, false);

                    procedureInfo.getPalletDispatch().setStorage_location("");
                }

                //库存从 上架货位 移动到缓存位
                storageService.storageTransfer(procedureInfo.getWarehouse().getId(), locations.get(0).getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());
                procedureInfo.getPalletDispatch().setProcedure_route("");
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching,
                        PositionType.Storage_Location, procedureInfo.getCurrentPos(), "");
                //                instruction.newInstruction(procedureInfo.getHouse_no(), currentWorkProcedure,
                //                        procedureInfo.getPallet_no(),
                //                        instructionType,
                //                        stockNo,
                //                        procedureInfo.getCurrentPos(),
                //
                //                        DispatcherConfig.formation_palletize_scaner_no,
                //                        InstructionMovePolicy.Static,
                //                        SendType.Direct);
                //                instructionService.createInstr(instruction);
                //                return GaiaResultFactory.getObject(instruction);
                return GaiaResultFactory.getObject(null);
                //创建指令

            } else if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_palletize_scaner_no)) {
                // 工序结束
                procedureInfo.getPalletDispatch().setProcedure_route("");
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Finished,
                        PositionType.Storage_Location, procedureInfo.getCurrentPos(), "");

                return GaiaResultFactory.getObject(instruction);
            } else {
                //都不是上面的情况
                //启动自动寻径
                if (!DispatcherConfig.noNeedSendNextPosition.contains(procedureInfo.getCurrentPos())) {
                    if (procedureInfo.getStorageLocation() != null && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic
                            && procedureInfo.getPalletOperate() == PalletOperate.ChangeProcedure) {
                        route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getStorageLocation().getX_pos().toString(), DispatcherConfig.formation_palletize_scaner_no);
                        route = ProcedureRouteMatch.replaceStartPosition(route, procedureInfo.getStorageLocation().getLoc_no());

                    } else {
                        route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_scaner_no);
                    }
                    procedureInfo.getPalletDispatch().setProcedure_route(route);

                    String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
                    if (ProcedureRouteMatch.isStocker(nextPos)) {
                        stockNo = nextPos;
                        nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                        instructionType = InstructionType.Stock_Move;
                        sendType = SendType.QueuesDirect;
                    }

                    //创建指令
                    instruction.newInstruction(procedureInfo.getHouseNo(),
                            currentWorkProcedure,
                            procedureInfo.getPallet_no(),
                            instructionType,
                            stockNo,
                            procedureInfo.getCurrentPos(),
                            nextPos,
                            InstructionMovePolicy.Static,
                            sendType);

                    instruction.setInstr_level(7);
                    instruction.setQueue_no(null);
                    instruction.setSend_type(SendType.Direct);


                    instructionService.createInstr(instruction);
                }
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
                        PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");

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
     * @param remark
     * @return
     */
    @Override
    public ObjectResult<Instruction> errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
        dispatch.setPallet_status(PalletStatus.Error_Finished);
        dispatch.setProcedure_route("");
        dispatchService.saveDispatchWithErrorMoveDetail(dispatch, EfcsErrorCode.ErrorFinish, remark);
        return GaiaResultFactory.getObject(null);
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

        procedureInfo.getPalletDispatch().setWork_procedure(procedureInfo.getWorkProcedure());
        procedureInfo.getPalletDispatch().setPallet_status(PalletStatus.In_Waiting);
        procedureInfo.getPalletDispatch().setProcedure_route("");
        if (procedureInfo.getStorageLocation() == null) {

            List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getHouseNo(), procedureInfo.getCurrentPos());
            if (locations.size() > 0) {
                procedureInfo.setStorageLocation(locations.get(0));

            }
        }
        if (procedureInfo.getStorageLocation() != null && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic) {
            procedureInfo.getPalletDispatch().setStorage_location(procedureInfo.getStorageLocation().getLoc_no());
        }
        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, workProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location,
                procedureInfo.getCurrentPos(), "");
        //清除遗留库存
        List<Storage> storages = locationAllotQyery.getStorageByPallet(procedureInfo.getPallet_no());
        if (storages.size() > 1) {
            List<Storage> outList = new ArrayList<>();
            for (Storage st : storages) {
                if (st.getSto_type() == StorageType.sku) {
                    outList.add(st);
                }
            }
            Storage fromContainer = new Storage(storages.get(0).getLocation_id(), "", StorageType.container);
            fromContainer.setHouse_id(procedureInfo.getWarehouse().getId());
            storageService.storageOut(fromContainer, StorageType.sku, outList, WorkProcedure.Formation_Out.toString(), null, null);
        }
        storageService.setPalletIsEmpty(procedureInfo.getPallet_no(), true);

        return GaiaResultFactory.getObject(null);

    }

}
