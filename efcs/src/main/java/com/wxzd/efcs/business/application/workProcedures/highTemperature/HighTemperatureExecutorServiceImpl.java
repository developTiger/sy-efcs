package com.wxzd.efcs.business.application.workProcedures.highTemperature;

import com.atlmes.ws.celltest.StandbyDurationResponse;
import com.wxzd.catl.CatlWebServiceFunction;
import com.wxzd.catl.base.CatlWebServiceConfig;
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
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.business.webservice.MESAppWebservice;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.json.JsonUtl;
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
@Service("highTemperatureExcutorService")
public class HighTemperatureExecutorServiceImpl implements HighTemperatureExecutorService<DefaultProcedure, GaiaResult> {


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


    private String cuttentProcedureRoute = ProcedureConfig.High_Temperature;

    private WorkProcedure nextProcedure = WorkProcedure.Formation;
    private WorkProcedure currentWorkProcedure = WorkProcedure.High_Temperature;

    @Override
    public GaiaResult doWorkProcedure(DefaultProcedure procedureInfo) {
        //默认一个工序 会经历 3个库位。
        //1、待入
        //2、入库
        //3、出库
        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
        /**
         * 组盘完成  高温开始
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
        if (DispatcherConfig.formation_palletize_end.equals(procedureInfo.getCurrentPos())) {
            FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedure == null) {
                //化成表单初始化
                procedure = new FmProcedure();
                procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos());
            }
            //初始化 高温工序单
            procedure.setHouse_id(procedureInfo.getWarehouse().getId());
            procedure.setHouse_no(procedureInfo.getHouseNo());
            fmProcedureService.saveFmProcedure(procedure);

            //创建指令 返回
            instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                    procedureInfo.getPallet_no(),
                    instructionType,
                    "",
                    procedureInfo.getCurrentPos(),
                    DispatcherConfig.high_temperature_waitin,
                    InstructionMovePolicy.Static,
                    sendType);
            instructionService.createInstr(instruction);
            //更新托盘状态
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
            return GaiaResultFactory.getObject(instruction);
        } else {
            FmProcedure procedure = fmProcedureService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedureInfo.getPalletDispatch().getWork_procedure() != currentWorkProcedure) {
                throw new RuntimeException("托盘调度异常,工序不是高温工序！");
            }
            if (procedure == null) {
                throw new RuntimeException("工序单不存在！");
            }
            //高温分配库位
            if (procedureInfo.getCurrentPos().equals(DispatcherConfig.high_temperature_waitin)) {

                Integer count = dispatchService.getInnerDetailCountByPalletDispatchId(procedureInfo.getPalletDispatch().getId());
                if (count < DispatcherConfig.pallet_battery_count) {
                    throw new RuntimeException("电池数量不足24个，请检查托盘！");
               }
                if (StringUtl.isEmpty(procedure.getIn_loc_no())) {
                	
                    ObjectResult<AllotStorageLocation> result = locationAllotService.allotStorageLocationIn(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getWarehouse().getId(),
                            procedureInfo.getHouseNo(), procedureInfo.getCurrentPos(), AllotPolicyType.FromSmallToBig, AllotPolicyType.FromSmallToBig);
                    if (result.isSuccess()) {
                        //设置分配库位信息
                        procedure.setIn_loc_no(result.getObject().getStorageLocation().getLoc_no());
                        procedure.setLoc_assign_time(new Date());
                        procedure.setPallet_status(PalletStatus.In_Waiting);
                        procedure.setFm_status(FmStatus.Executing);
                        fmProcedureService.saveFmProcedure(procedure);
                        fcsSchedulerService.closeALlSchedulerByDevice(procedure.getHouse_id(),procedureInfo.getCurrentPos());

                    } else { //请求库位失败
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
                                scheduler.setHouse_id(procedureInfo.getWarehouse().getId());
                                scheduler.setPallet_no(procedureInfo.getPallet_no());
                                scheduler.setHouse_no(procedureInfo.getHouseNo());
                                scheduler.setDevice_no(procedureInfo.getCurrentPos());
                                fcsSchedulerService.saveScheduler(scheduler);
                            }
                        }
                        return GaiaResultFactory.getError(result.getMessage());

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

                //更新托盘状态啊
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                //生成指令
                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        instructionType,
                        stockNo,//是否设置为堆垛机
                        procedureInfo.getCurrentPos(),
                        procedure.getIn_loc_no(),//入库库位
                        InstructionMovePolicy.Static,
                        sendType);
                instructionService.createInstr(instruction);
                return GaiaResultFactory.getSuccess();

            }
            if (procedureInfo.getPalletOperate() == PalletOperate.ChargeNumberSuccess) {
                route = ProcedureRouteMatch.getRouteByEquipNo(ProcedureRouteMatch.getCubicXPos(procedure.getIn_loc_no()), DispatcherConfig.formation_allot_location);

                String nextPos = ProcedureRouteMatch.getNextPosition(route, ProcedureRouteMatch.getCubicXPos(procedure.getIn_loc_no()));
                if (ProcedureRouteMatch.isStocker(nextPos)) {
                    stockNo = nextPos;
                    nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                    instructionType = InstructionType.Stock_Out;
                    sendType = SendType.QueuesDirect;
                }
                //生成出库指令，计划指令，待mes 下发可出库时生效
                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        instructionType,
                        stockNo,//是否设置为堆垛机
                        procedureInfo.getCurrentPos(),
                        nextPos,
                        InstructionMovePolicy.Static,
                        sendType);

                instructionService.createInstr(instruction);
                return GaiaResultFactory.getSuccess();
            }
            //高温库位 入库完成
            //入到高温立库了
            if (procedureInfo.getStorageLocation() != null
                    && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic &&
                    procedureInfo.getPalletOperate() == PalletOperate.CommandFinished) {
                procedure.setIn_time(new Date());
                procedure.setPallet_status(PalletStatus.In_Finished);
                if(procedure.getIn_loc_no()!=null) {
                    if (procedure.getIn_loc_no().equals(procedureInfo.getStorageLocation().getLoc_no())) {
                        storageLocationService.setHasGoodAndAndReduceAllotLocationInlock(procedureInfo.getStorageLocation());
                    } else {
                        storageLocationService.setIsHasGood(procedureInfo.getStorageLocation(), true);
                    }
                }else{
                    storageLocationService.setIsHasGood(procedureInfo.getStorageLocation(), true);
                }
                procedure.setIn_loc_no(procedureInfo.getStorageLocation().getLoc_no());

                procedure.setIs_auto_out(0);
                fmProcedureService.saveFmProcedure(procedure);

                try {
                    // ------------------------------
                    // @author Leon Regulus
                    // 修改为通过配置设定静置时长
                    //-------------------------------
//                    StandbyDurationResponse standbyDuration = CatlWebServiceFunction.getStandbyDuration(CatlWebServiceConfig.getHighTempOpertion(), procedureInfo.getPallet_no(), CatlWebServiceConfig.getHighTempResource());

                    int configTime = DispatcherConfig.mes_hightemprature_time.get(procedureInfo.getHouseNo());
                    int stayTime = configTime * 60;
                    procedureInfo.setStayTime(stayTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (procedureInfo.getStayTime() != null && procedureInfo.getStayTime() > 0) {
                    procedure.setOut_plan_time(new Date(new Date().getTime() + procedureInfo.getStayTime() * 1000));//根据时长计算
                }
                procedureInfo.getPalletDispatch().setCurrent_procedure_time(new Date());
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                // storageTransfer(UUID houseId, UUID toLocationId, String palletNo,String formNo, String businessType)

                //库存从 上架货位 移动到高温库位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        procedureInfo.getStorageLocation().getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                String defaultProcedureJson = JsonUtl.parse(ConvertFactory.convertToCleanProcedure(procedureInfo));
                FcsScheduler scheduler = new FcsScheduler(SchedulerType.ExecutionLoop, TaskType.MESChargeNumber, procedure.getOut_plan_time(), currentWorkProcedure, defaultProcedureJson,SchedulerConfig.normalTemperatureStay_interval, SchedulerConfig.allotLocation_interval_unit);
                scheduler.setDevice_no(procedureInfo.getCurrentPos());
                scheduler.setHouse_id(procedureInfo.getWarehouse().getId());
                scheduler.setPallet_no(procedureInfo.getPallet_no());
                scheduler.setHouse_no(procedureInfo.getHouseNo());
                fcsSchedulerService.saveScheduler(scheduler);


                return GaiaResultFactory.getSuccess();
            } else if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_allot_location)) {
                if (procedure.getIn_time() == null) {
                    return new ObjectResult<>(false, "未入高温！");
                }

                procedure.setOut_loc_no(procedureInfo.getStorageLocation().getLoc_no());
                procedure.setOut_time(new Date());
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);
                fmProcedureService.saveFmProcedure(procedure);
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                // storageTransfer(UUID houseId, UUID toLocationId, String palletNo,String formNo, String businessType)
                //库存从 高温库 移动到 下架库位
                //调用MES接口上传高温时间
//                Long timeSecond = (procedure.getOut_time().getTime() - procedure.getIn_time().getTime()) / 1000;
//                // CatlWebServiceFunction.dataCollectForProcessLotEx();
//                try {
//                    MESAppWebservice.hightDataCollectForProcessLotExCore(procedureInfo.getHouse_no(), procedureInfo.getPallet_no(), timeSecond.intValue());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), procedure.getIn_loc_no());

                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        procedureInfo.getStorageLocation().getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                if (!StringUtl.isEmpty(procedure.getIn_loc_no())) {
                    storageLocationService.setIsHasGood(procedure.getHouse_id(), procedure.getIn_loc_no(), false);
                }

                return GaiaResultFactory.getSuccess();
            } else {
                //都不是上面的情况
                //启动自动寻径

                if (procedureInfo.getStorageLocation() != null
                        && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic && procedureInfo.getPalletOperate() == PalletOperate.ChangeProcedure) {
                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getStorageLocation().getX_pos().toString(), DispatcherConfig.high_temperature_waitin);
                } else {
                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.high_temperature_waitin);
                }
                procedureInfo.getPalletDispatch().setProcedure_route(route);


                String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
                if (ProcedureRouteMatch.isStocker(nextPos)) {
                    stockNo = nextPos;
                    nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                    instructionType = InstructionType.Stock_Out;
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
    public Boolean needActiveNextProcedure(WorkProcedure workProcedure, PalletStatus palletStatus, String
            currentPos) {
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
    public GaiaResult errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
//        currentWorkProcedure=workProcedure;

        FmProcedure procedure = fmProcedureService.getProcedureByPallet(palletNo, currentWorkProcedure);
        if(procedure!=null){
            procedure.setFm_status(FmStatus.ErrorFinished);
            procedure.setError_code(EfcsErrorCode.ErrorFinish);
            procedure.setError_desc(remark);

            fmProcedureService.saveFmProcedure(procedure);
            return GaiaResultFactory.getSuccess();
        }

        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
        dispatch.setError_desc(remark);
        dispatch.setPallet_status(PalletStatus.In_Waiting);

        dispatchService.saveDispatchWithErrorMoveDetail(dispatch, EfcsErrorCode.ErrorFinish, remark);

        return GaiaResultFactory.getSuccess();

    }

    /**
     * 非正常的开始一个新工序
     *
     * @param workProcedure
     * @param procedureInfo
     * @return
     */
    @Override
    public GaiaResult initProcedureCurrenPosImproper(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {
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

        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());

//
//
//        if(procedureInfo.getStorageLocation()!=null){
//            if(procedureInfo.getStorageLocation().getLoc_type()==LocationType.cubic){
//
//            }
//
//
//        }


        return GaiaResultFactory.getSuccess();
    }


}
