package com.wxzd.efcs.business.application.workProcedures.formationPalletize;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.ProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FmPalletizeService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.SerialNoGenerator;
import com.wxzd.wms.core.domain.entities.Container;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("formationPalletizeExcutorService")
public class FormationPalletizeExecutorServiceImpl implements FormationPalletizeExecutorService<DefaultProcedure, GaiaResult> {

    private static final Logger log = LoggerFactory.getLogger(FormationPalletizeExecutorServiceImpl.class);

    @Autowired
    FmPalletizeService fmPalletizeService;
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


    private String cuttentProcedureRoute = ProcedureConfig.Formation_Palletize;
    private WorkProcedure currentWorkProcedure = WorkProcedure.Formation_Palletize;
    private WorkProcedure nextProcedure = WorkProcedure.High_Temperature;

    @Override
    public GaiaResult doWorkProcedure(DefaultProcedure procedureInfo) {


        PalletizePrecedureInfo palletizePrecedureInfo = new PalletizePrecedureInfo(procedureInfo.getHouseNo(), procedureInfo.getPallet_no(), procedureInfo.getFmCreateMode());
        try {
            palletizePrecedureInfo = (PalletizePrecedureInfo) procedureInfo;
        } catch (Exception ex) {
            BeanUtl.copyProperties(procedureInfo, palletizePrecedureInfo);
        }
        if (!StringUtl.isEmpty(palletizePrecedureInfo.getTo_pallet_no()))
            palletizePrecedureInfo.setPallet_no(palletizePrecedureInfo.getTo_pallet_no());


//        if (palletizePrecedureInfo.getCurrentPos().equals(DispatcherConfig.formation_palletize_device_no))
//            return GaiaResultFactory.getSuccess();
        //默认一个工序 会经历 3个库位。
        //1、待入
        //2、入组盘位
        //3、出组盘位
        Instruction instruction = new Instruction(procedureInfo.getHouseNo());

        String route = "";
        SendType sendType = SendType.Direct;
        String stockNo = "";
        InstructionType instructionType = InstructionType.Transport;
        //初始化  创建组盘单
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_palletize_scaner_no)) {
            PalletDispatch palletDispatch;
            FmPalletize procedure;
            //不管什么情况 重新规划路线
            route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_device_no);
            //入库口入进来的组盘托盘
            if (palletizePrecedureInfo.getPalletDispatch() != null && palletizePrecedureInfo.getPalletDispatch().getWork_procedure().equals(currentWorkProcedure)) {
                procedure = fmPalletizeService.getProcedureByPallet(procedureInfo.getPallet_no());
                palletDispatch = palletizePrecedureInfo.getPalletDispatch();
                procedure.setPallet_cargo_id(palletDispatch.getId());
                palletDispatch.setProcedure_route(route);
            } else { //空托盘转 待组盘工序
                procedure = new FmPalletize();
                String formNo = SerialNoGenerator.getSerialNo("PT");
                procedure.setForm_no(formNo);
                palletDispatch = palletizePrecedureInfo.getPalletDispatch();

                palletDispatch.Reset(currentWorkProcedure);
                palletDispatch.setCurrent_form_no(formNo);
                palletDispatch.setProcedure_route(route);
                UUID id = dispatchService.newPalletDispatchWithMoveDetail(palletDispatch, false);
                procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), id, procedureInfo.getCurrentPos(), ProcedureConfig.PalletChannel_Policy);
                procedure.setHouse_id(procedureInfo.getWarehouse().getId());
                procedure.setHouse_no(procedureInfo.getHouseNo());
                fmPalletizeService.saveFmPalletize(procedure);
            }
            //创建指令
            instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                    procedureInfo.getPallet_no(),
                    instructionType,
                    "",
                    procedureInfo.getCurrentPos(),
                    ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos()),
                    InstructionMovePolicy.Static,
                    sendType);

            instructionService.createInstr(instruction);
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());

            return GaiaResultFactory.getObject(instruction);
        } else {
            PalletDispatch dispatch = dispatchService.getByPalletNo(palletizePrecedureInfo.getPallet_no());
            FmPalletize procedure = fmPalletizeService.getProcedureByPallet(palletizePrecedureInfo.getPallet_no());

            if (dispatch == null) {
                throw new RuntimeException("托盘调度异常,调度信息不存在！");
            }
            if (dispatch.getWork_procedure() != currentWorkProcedure) {
                throw new RuntimeException("托盘调度异常,工序不是组盘工序！");
            }
            if (procedure == null) {
                throw new RuntimeException("工序单不存在！");
            }
//            if (DispatcherConfig.formation_palletize_scaner_no.equals(procedureInfo.getCurrentPos())) {
//                //创建指令
//                instruction.newInstruction(procedureInfo.getHouse_no(),currentWorkProcedure,
//                        procedureInfo.getPallet_no(),
//                        instructionType,
//                        procedureInfo.getCurrentPos(),
//                        procedureInfo.getCurrentPos(),
//                        ProcedureRouteMatch.getNextPosition(palletizePrecedureInfo.getPalletDispatch().getProcedure_route(), procedureInfo.getCurrentPos()),
//                        InstructionMovePolicy.Dynamic,
//                        sendType);
//                instructionService.createInstr(instruction);
//                //更新托盘状态
//                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//                return GaiaResultFactory.getObject(instruction);
//            }
            //托盘到达
            if ((DispatcherConfig.formation_palletize_equip_1.equals(procedureInfo.getCurrentPos())
                    || DispatcherConfig.formation_palletize_equip_2.equals(procedureInfo.getCurrentPos()))
//                    && palletizePrecedureInfo.getPalletOperate() == PalletOperate.CommandFinished //不是组盘完成
                    ) {

                //执行空托盘移库入位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        palletizePrecedureInfo.getStorageLocation().getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                //更新托盘状态
                dispatchService.updatePalletStatusAndPos(dispatch, PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                return GaiaResultFactory.getObject(instruction);
            }
            //电池组盘
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.MoveItem //不是组盘完成
                    ) {
                try {
                    dispatch.setIs_empty(false);
                    procedure.setFm_status(FmStatus.Executing);
                    procedure.setPalletize_status(PalletizeStatus.Executing);
                    log.trace("通道号:{},电池到位:{},起点位置{},目标托盘{}", palletizePrecedureInfo.getTo_pos_channel_no(), palletizePrecedureInfo.getBattery_no(), palletizePrecedureInfo.getFrom_pos_no(), palletizePrecedureInfo.getTo_pallet_no());

                    //库存移动到高温上架位
                    storageService.storageTransferToPallet(
                            procedureInfo.getWarehouse().getId(),
                            palletizePrecedureInfo.getTo_pallet_no(),
                            palletizePrecedureInfo.getBattery_no(),
                            procedure.getForm_no(),
                            currentWorkProcedure.toString()
                    );
                    PalletDetail detail = new PalletDetail(ProcedureConfig.getHouseSkuId(procedureInfo.getHouseNo()), dispatch.getId(), String.valueOf(palletizePrecedureInfo.getTo_pos_channel_no()), procedure.getForm_no(), palletizePrecedureInfo.getBattery_no(), "OK", palletizePrecedureInfo.getPallet_no());
                    detail.setFromPos(palletizePrecedureInfo.getFrom_pos_no(), palletizePrecedureInfo.getClamp_no(), palletizePrecedureInfo.getFrom_pos_no(), String.valueOf(palletizePrecedureInfo.getFrom_pos_channel_no()), PositionType.Line);
                    dispatchService.savePalletInnerDetail(detail);
                    dispatchService.newPalletDispatch(dispatch);

                    //第一次入盘 设置开始时间
                    if (procedure.getProc_start_time() == null) {
                        log.trace("组盘开始...");
                        procedure.setProc_start_time(new Date());


                    }
                    storageService.setPalletIsEmpty(palletizePrecedureInfo.getPallet_no(), false);

                    //初始化 组盘工序单
                    fmPalletizeService.saveFmPalletize(procedure);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return GaiaResultFactory.getObject(instruction);
            }
            //组盘完成
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.OperateComplete //组盘完成
                    ) {
                //完成 组盘工序单
                procedure.setProc_complete_time(new Date());
                procedure.setPallet_status(PalletStatus.Out_Waiting);
                procedure.setPalletize_status(PalletizeStatus.Finished);
                fmPalletizeService.saveFmPalletize(procedure);

                route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_end);
                dispatch.setProcedure_route(route);
                //仅更新托盘状态,托盘未移动
                dispatchService.setPalletStatus(dispatch, PalletStatus.Out_Waiting);
                return GaiaResultFactory.getObject(instruction);
            }
            if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_palletize_end)) {
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);
                fmPalletizeService.saveFmPalletize(procedure);
                //获取高温上架库位
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(palletizePrecedureInfo.getWarehouse().getId(), DispatcherConfig.high_temperature_waitin);
                if (locations.size() == 0) {
                    throw new RuntimeException(I18nContext.getMessage("高温上架设备库位未设置"));
                }
                //库存移动到高温上架位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        locations.get(0).getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                dispatch.setProcedure_route("");
                procedureInfo.setPalletDispatch(dispatch);
                dispatchService.updatePalletStatusAndPos(dispatch, PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//                instruction.newInstruction(procedureInfo.getHouse_no(), currentWorkProcedure,
//                        procedureInfo.getPallet_no(),
//                        instructionType,
//                        stockNo,
//                        procedureInfo.getCurrentPos(),
//                        DispatcherConfig.high_temperature_waitin,
//                        InstructionMovePolicy.Static,
//                        sendType);
//                instructionService.createInstr(instruction);
                return GaiaResultFactory.getSuccess();
            } else {
                //都不是上面的情况
                //启动自动寻径

                if (procedureInfo.getStorageLocation() != null
                        && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic && procedureInfo.getPalletOperate() == PalletOperate.ChangeProcedure) {
                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getStorageLocation().getX_pos().toString(), DispatcherConfig.formation_palletize_device_no);
                } else {
                    route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.formation_palletize_device_no);
                }

                palletizePrecedureInfo.getPalletDispatch().setProcedure_route(route);

                String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
                if (ProcedureRouteMatch.isStocker(nextPos)) {
                    stockNo = nextPos;
                    nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
                    instructionType = InstructionType.Stock_Move;
                    sendType = SendType.QueuesDirect;
                }
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
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(),
                        PalletStatus.In_Waiting,
                        currentWorkProcedure,
                        PalletDispatchStatus.Dispatching,
                        PositionType.Transport_Location,
                        procedureInfo.getCurrentPos(),
                        procedure.getForm_no());
                return GaiaResultFactory.getObject(instruction);

            }
        }

        //return GaiaResultFactory.getError(I18nContext.getMessage("工序异常"));
    }

    /**
     * 是否需要创建下一道工序
     *
     * @param workProcedure
     * @param currentPos
     * @return
     */
    @Override
    public Boolean needActiveNextProcedure(WorkProcedure workProcedure, PalletStatus palletStatus, String currentPos) {
        if (currentPos.equals(DispatcherConfig.formation_palletize_end)) {
            return true;
        }
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

        FmPalletize procedure = fmPalletizeService.getProcedureByPallet(palletNo);
        if(procedure!=null){
            procedure.setPallet_status(PalletStatus.Error_Finished);
            procedure.setFm_status(FmStatus.ErrorFinished);

            procedure.setError_code(EfcsErrorCode.ErrorFinish);
            procedure.setError_desc(remark);
            fmPalletizeService.saveFmPalletize(procedure);
        }


        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
        dispatch.setPallet_status(PalletStatus.Error_Finished);
        dispatch.setProcedure_route("");
        dispatchService.saveDispatchWithErrorMoveDetail(dispatch, EfcsErrorCode.ErrorFinish, remark);
        return  GaiaResultFactory.getSuccess();

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
        if(procedureInfo.getPalletDispatch()==null){
            procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));
            if(procedureInfo.getPalletDispatch()==null){
                throw new RuntimeException("工序切换错误，托盘信息状态异常");
            }
        }
        if( procedureInfo.getPalletDispatch().getWork_procedure()!=workProcedure){
            procedureInfo.getPalletDispatch().setWork_procedure(currentWorkProcedure);
        }
        FmPalletize procedure = fmPalletizeService.getProcedureByPallet(procedureInfo.getPallet_no());

        if(procedure==null) {
            procedure = new FmPalletize();
            String formNo = SerialNoGenerator.getSerialNo("PT");
            procedure.setForm_no(formNo);
            procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos(), ProcedureConfig.PalletChannel_Policy);
            procedure.setHouse_id(procedureInfo.getWarehouse().getId());
        }
        fmPalletizeService.saveFmPalletize(procedure);
        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getPalletDispatch().getCurrent_pos(), procedure.getForm_no());

        return GaiaResultFactory.getSuccess();
    }


}
