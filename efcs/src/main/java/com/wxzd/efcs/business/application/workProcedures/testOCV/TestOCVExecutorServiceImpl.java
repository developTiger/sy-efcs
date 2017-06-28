package com.wxzd.efcs.business.application.workProcedures.testOCV;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.TestProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
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

import java.util.Date;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testOCVExecutorServiceImpl")
public class TestOCVExecutorServiceImpl implements TestOCVExecutorService<DefaultProcedure, ObjectResult<Instruction>> {


    @Autowired
    FmProcedureService fmProcedureService;

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


//    private WorkProcedure currentWorkProcedure;

    @Override
    public ObjectResult<Instruction> doWorkProcedure(DefaultProcedure procedureInfo) {
    	WorkProcedure currentWorkProcedure = procedureInfo.getWorkProcedure();
        //默认一个工序 会经历 3个库位。
        //1、待入
        //2、入库
        //3、出库
        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
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
        /**
         * OCV开始
         */
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_normal_temperature_down_1)
                ||
                procedureInfo.getCurrentPos().equals(DispatcherConfig.test_normal_temperature_down_2)) {
            FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedure == null) {
                //化成表单初始化
                procedure = new FmProcedure();
                procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
            }

            // procedure.setStay_time(procedureInfo.getStayTime());
            procedure.setLoc_assign_time(new Date());
            procedure.setPallet_status(PalletStatus.In_Waiting);
            procedure.setFm_status(FmStatus.Executing);
            procedure.setHouse_id(procedureInfo.getWarehouse().getId());
            procedure.setHouse_no(procedureInfo.getHouseNo());
            procedure.setPallet_no(procedureInfo.getPallet_no());
            fmProcedureService.saveFmProcedure(procedure);

            procedureInfo.getPalletDispatch().setProcedure_route(route);
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());


            return GaiaResultFactory.getObject(instruction);

        } else {
            FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedureInfo.getPalletDispatch().getWork_procedure() != currentWorkProcedure) {
                throw new RuntimeException("托盘调度异常,工序不是OCV工序！");
            }
            if (procedure == null) {
                throw new RuntimeException("工序单不存在！");
            }
            //入到OCV
            if ((procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv1_pos) || procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv2_pos)) && procedureInfo.getPalletOperate() == PalletOperate.Arrived) {
                procedure.setIn_time(new Date());
                procedure.setPallet_status(PalletStatus.In_Finished);

                procedure.setIn_loc_no(procedureInfo.getCurrentPos());

                procedure.setIs_auto_out(0);
                fmProcedureService.saveFmProcedure(procedure);
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                //库存从 化成位 移动到下架位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        procedureInfo.getStorageLocation().getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );

                return GaiaResultFactory.getObject(instruction);
            } else if (procedureInfo.getPalletOperate() == PalletOperate.OperateComplete //化成完成
                    ) {
                procedure.setOut_plan_time(new Date());
                //完成 组盘工序单
                procedure.setPallet_status(PalletStatus.Out_Waiting);
                fmProcedureService.saveFmProcedure(procedure);


                procedureInfo.getPalletDispatch().setProcedure_route(route);
                dispatchService.setPalletStatus(procedureInfo.getPalletDispatch(), PalletStatus.Out_Waiting);
//                //生成出库指令，计划指令，待mes 下发可出库时生效
//                instruction.newInstruction(procedureInfo.getHouse_no(), currentWorkProcedure,
//                        procedureInfo.getPallet_no(),
//                        instructionType,
//                        stockNo,//是否设置为堆垛机
//                        procedureInfo.getCurrentPos(),
//                        nextPos,
//                        InstructionMovePolicy.Static,
//                        sendType);
//                instruction.setInstr_status(InstructionStatus.Created);
//                instruction.setInstr_level(DispatcherConfig.formation_instruction_level);
//                instructionService.createInstr(instruction);
//                //仅更新托盘状态,托盘未移动

                return GaiaResultFactory.getObject(instruction);
            } else if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv1_out) || procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv2_out)) {
                //到达下架位 工序结束

                procedure.setOut_time(new Date());
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);
//
//                String nextPos = "";
//                if(currentWorkProcedure==WorkProcedure.Test_OCV_1){
//                    if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv1_out)){
//                        nextPos = DispatcherConfig.test_normal_temperature2_up1;
//                    }else{
//                        nextPos = DispatcherConfig.test_normal_temperature2_up2;
//
//                    }
//                }else{
//                    if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv1_out)){
//                        nextPos = DispatcherConfig.test_split_equip_1;
//                    }else{
//                        nextPos = DispatcherConfig.test_split_equip_2;
//                    }
//                }
//
//                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), nextPos);
//                if (locations.size() == 0) {
//                    throw new RuntimeException(I18nContext.getMessage("拆盘缓存设备库位未设置"));
//                }
//                procedure.setOut_loc_no(nextPos);
                fmProcedureService.saveFmProcedure(procedure);
                //库存从 上架货位 移动到高温库位

                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//                storageLocationService.setInFobiden(procedureInfo.getStorageLocation(), LockDirection.inLock, false);
                return GaiaResultFactory.getObject(instruction);
            } else {
                //都不是上面的情况
                //启动自动寻径
                route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_split_scan_no);
                procedureInfo.getPalletDispatch().setProcedure_route(route);

                String nextPos = TestProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
                if (TestProcedureRouteMatch.isStocker(nextPos)) {
                    stockNo = nextPos;
                    nextPos = TestProcedureRouteMatch.getNextPosition(route, stockNo);
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
        if (workProcedure == WorkProcedure.Test_OCV_1)

            return WorkProcedure.Normal_Temperature_2;
        else
            return WorkProcedure.Test_Pallet_Split;
    }

    /**
     * 异常结束工序
     *
     * @param workProcedure
     * @param palletNo
     * @param remark        @return
     */
    @Override
    public ObjectResult errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
    	WorkProcedure  currentWorkProcedure = workProcedure;
        FmProcedure procedure = fmProcedureService.getProcedureByPallet(palletNo, currentWorkProcedure);
        if (procedure != null) {
            procedure.setFm_status(FmStatus.ErrorFinished);
            procedure.setPallet_status(PalletStatus.Error_Finished);
            procedure.setError_code(EfcsErrorCode.ErrorFinish);
            procedure.setError_desc(remark);

            fmProcedureService.saveFmProcedure(procedure);
        }

        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
        dispatch.setPallet_status(PalletStatus.In_Waiting);
        dispatch.setError_desc(remark);

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
    public ObjectResult initProcedureCurrenPosImproper(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {
    	WorkProcedure  currentWorkProcedure = workProcedure;
        FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
        if (procedure == null) {
            //化成表单初始化
            procedure = new FmProcedure();
            procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
        }
        //初始化 高温工序单
        procedure.setHouse_id(procedureInfo.getWarehouse().getId());
        procedure.setHouse_no(procedureInfo.getHouseNo());
        //TODO 异常清空库位
//        if(procedureInfo.getStorageLocation().getLoc_type()==LocationType.cubic){
//            procedureInfo.getPalletDispatch().setImproperStartOutLocation(procedureInfo.getCurrentPos());
//        }
        fmProcedureService.saveFmProcedure(procedure);
        PalletDispatch dispatch = dispatchService.getByPalletNo(procedureInfo.getPallet_no());
        dispatchService.updatePalletStatusAndPos(dispatch, PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//
//        if(procedureInfo.getStorageLocation()!=null){
//            if(procedureInfo.getStorageLocation().getLoc_type()==LocationType.cubic){
//
//            }
//
//
//        }


        return new ObjectResult<>(true, "");
    }


}
