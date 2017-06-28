package com.wxzd.efcs.business.application.workProcedures.formationPalletizeRework;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.ProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FmPalletizeService;
import com.wxzd.efcs.business.domain.service.InstructionService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.SerialNoGenerator;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("formationPalletizeReworkExecutorService")
public class FormationPalletizeReworkExecutorServiceImpl implements FormationPalletizeReworkExecutorService<DefaultProcedure, GaiaResult> {


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


    private String cuttentProcedureRoute = ProcedureConfig.Formation_Palletize_rework;
    private WorkProcedure currentWorkProcedure = WorkProcedure.Formation_Rework_Palletize;
    private WorkProcedure nextProcedure = WorkProcedure.Formation_Rework;

    @Override
    public GaiaResult doWorkProcedure(DefaultProcedure procedureInfo) {
        PalletizePrecedureInfo palletizePrecedureInfo = new PalletizePrecedureInfo(procedureInfo.getHouseNo(), procedureInfo.getPallet_no(), procedureInfo.getFmCreateMode());
        try {
            palletizePrecedureInfo = (PalletizePrecedureInfo) procedureInfo;
        } catch (Exception ex) {
            BeanUtl.copyProperties(procedureInfo, palletizePrecedureInfo);
        }
        if (!StringUtl.isEmpty(palletizePrecedureInfo.getFrom_pallet_no()))
            palletizePrecedureInfo.setPallet_no(palletizePrecedureInfo.getFrom_pallet_no());
        //默认一个工序 会经历 3个库位。
        //1、待入
        //2、入组盘位
        //3、出组盘位

        //TODO dispatch 可能不对，待测
        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
        if (procedureInfo.getPalletDispatch() == null) {
            procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(palletizePrecedureInfo.getPallet_no()));
            if (procedureInfo.getPalletDispatch() == null) {
                throw new RuntimeException("托盘调度异常,调度信息不存在！");
            }
        }

        //临时修改，更改为空托盘
        if (palletizePrecedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_waitin_location)) {
            FmPalletize procedure = fmPalletizeService.getProcedureByPallet(procedureInfo.getPallet_no());
            if (procedure != null) {
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);
                fmPalletizeService.saveFmPalletize(procedure);
            }
            //保存 dispatch
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Error_Finished, currentWorkProcedure, PalletDispatchStatus.Finished, PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
            PalletDispatch dis = ConvertFactory.convertOldDispatchToNewOne(palletizePrecedureInfo.getPalletDispatch());
            dis.setDispatch_status(PalletDispatchStatus.Dispatching);
            dis.setWork_procedure(WorkProcedure.Formation_PalletMove);
            dispatchService.newPalletDispatch(dis);
            procedureInfo.setPalletDispatch(dis);

            instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                    procedureInfo.getPallet_no(),
                    InstructionType.Stock_Move,
                    "",
                    procedureInfo.getCurrentPos(),
                    DispatcherConfig.formation_palletize_catch_location,
                    InstructionMovePolicy.Static,
                    SendType.Direct);
            instructionService.createInstr(instruction);
            return GaiaResultFactory.getObject(instruction);
        }
        //初始化  创建组盘单
        if (palletizePrecedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_pallet_allot_location)) {

            palletizePrecedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));
            String nextPos = DispatcherConfig.formation_split_equip_rework;
            if (StringUtl.isEmpty(palletizePrecedureInfo.getPalletDispatch().getProcedure_route())) {
//            if(nextPos.equals(DispatcherConfig.formation_split_equip_rework)) {
                List<Storage> storages = locationAllotQyery.getEquipLocationStorage(procedureInfo.getWarehouse().getId(), DispatcherConfig.formation_split_equip_rework);
                if (storages.size() == 0 
//                		|| (procedureInfo.getPalletDispatch().getIs_empty() && storages.size() == 0) 
//                		|| !procedureInfo.getPalletDispatch().getIs_empty()
                		) {
                    FmPalletize procedure = new FmPalletize();
                    String formNo = SerialNoGenerator.getSerialNo("RPT");
                    procedure.setForm_no(formNo);
                    procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos(), ProcedureConfig.PalletChannel_Policy);
                    procedure.setHouse_id(procedureInfo.getWarehouse().getId());
                    procedure.setHouse_no(procedureInfo.getHouseNo());
                    fmPalletizeService.saveFmPalletize(procedure);
                } else {
                    procedureInfo.getPalletDispatch().setWork_procedure(WorkProcedure.Formation_PalletMove);
                    dispatchService.setPalletStatus(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting);
                    instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                            procedureInfo.getPallet_no(),
                            InstructionType.Transport,
                            "",
                            procedureInfo.getCurrentPos(),
                            DispatcherConfig.formation_rework_waitin_location,
                            InstructionMovePolicy.Static,
                            SendType.Direct);
                    instructionService.createInstr(instruction);
                    return GaiaResultFactory.getObject(instruction);
                }

                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        InstructionType.Transport,
                        "",
                        procedureInfo.getCurrentPos(),
                        nextPos,
                        InstructionMovePolicy.Static,
                        SendType.Direct);
                instruction.setInstr_level(4);

                instructionService.createInstr(instruction);
                return GaiaResultFactory.getObject(instruction);
            } else {

//                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        InstructionType.Transport,
                        "",
                        procedureInfo.getCurrentPos(),
                        DispatcherConfig.formation_rework_waitin_location,
                        InstructionMovePolicy.Static,
                        SendType.Direct);
                instructionService.createInstr(instruction);
                return GaiaResultFactory.getObject(instruction);
            }
        } else {
            PalletDispatch dispatch = procedureInfo.getPalletDispatch();
            FmPalletize procedure = fmPalletizeService.getProcedureByPallet(procedureInfo.getPallet_no());

            if (procedureInfo.getPalletDispatch().getWork_procedure() != currentWorkProcedure) {
                throw new RuntimeException("托盘调度异常,工序不是rework组盘工序！");
            }
            if (procedure == null) {
                throw new RuntimeException("工序单不存在！");
            }
            //rework组盘 托盘到位
            if (DispatcherConfig.formation_split_equip_rework.equals(palletizePrecedureInfo.getCurrentPos())) {
                //库存移动到高温上架位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        procedureInfo.getStorageLocation().getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                //更新托盘状态
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                return GaiaResultFactory.getObject(instruction);
            }
            //电池组盘
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.MoveItem) {

                try {
                    PalletDetail oldDetail = dispatchService.getPalletInnerDetail(palletizePrecedureInfo.getFrom_pallet_no(), palletizePrecedureInfo.getFrom_pos_channel_no());
                    palletizePrecedureInfo.setBattery_no(oldDetail.getBattery_barcode());
                    procedure.setFm_status(FmStatus.Executing);
                    procedure.setPalletize_status(PalletizeStatus.Executing);
                    //库存移动到高温上架位
                    storageService.storageTransferToPallet(
                            procedureInfo.getWarehouse().getId(),
                            palletizePrecedureInfo.getTo_pallet_no(),
                            palletizePrecedureInfo.getBattery_no(),
                            procedure.getForm_no(),
                            currentWorkProcedure.toString()
                    );
                    //初始化 组盘工序单

                    PalletDetail detail = new PalletDetail(ProcedureConfig.getHouseSkuId(procedureInfo.getHouseNo()), dispatch.getId(), String.valueOf(palletizePrecedureInfo.getTo_pos_channel_no()), procedure.getForm_no(), palletizePrecedureInfo.getBattery_no(), "OK", palletizePrecedureInfo.getPallet_no());
                    detail.setFromPos(palletizePrecedureInfo.getFrom_pos_no(), palletizePrecedureInfo.getClamp_no(), palletizePrecedureInfo.getFrom_pos_no(), String.valueOf(palletizePrecedureInfo.getFrom_pos_channel_no()), PositionType.Line);
                    //第一次入盘 设置开始时间
                    if (procedure.getProc_start_time() == null) {
                        procedure.setProc_start_time(new Date());
                        procedureInfo.getPalletDispatch().setIs_empty(false);

                    }
                    dispatchService.savePalletInnerDetail(detail);
                    fmPalletizeService.saveFmPalletize(procedure);
                    storageService.setPalletIsEmpty(palletizePrecedureInfo.getPallet_no(), false);

                }catch (Exception ex ){
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
                fmPalletizeService.saveFmPalletize(procedure);
                //获取高温上架库位
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(palletizePrecedureInfo.getWarehouse().getId(), DispatcherConfig.formation_rework_waitin_location);

                if (locations.size() == 0) {
                    throw new RuntimeException(I18nContext.getMessage("设备库位未设置"));
                }

                //库存移动到高温上架位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        locations.get(0).getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );


//              String route = ProcedureRouteMatch.getRouteByEquipNo(DispatcherConfig.formation_split_equip_rework, DispatcherConfig.formation_rework_waitin_location);
                dispatch.setProcedure_route("2150-2260");
                //仅更新托盘状态,托盘未移动
                dispatchService.setPalletStatus(dispatch, PalletStatus.Out_Waiting);
                return GaiaResultFactory.getObject(instruction);
            }

        }
        return GaiaResultFactory.getError(I18nContext.getMessage("工序异常"));
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
     * 是否需要创建下一道工序
     *
     * @param workProcedure
     * @param currentPos
     * @return
     */
    public Boolean needActiveNextProcedure(WorkProcedure workProcedure, String currentPos, PalletStatus palletStatus) {
        //todo 取消route
        if (currentPos.equals(DispatcherConfig.formation_rework_pallet_allot_location) && palletStatus == PalletStatus.Out_Finished) {
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
        if (procedure != null) {
            procedure.setPallet_status(PalletStatus.Out_Finished);
            procedure.setFm_status(FmStatus.Finished);
            procedure.setError_code(EfcsErrorCode.ErrorFinish);
            procedure.setError_desc(remark);
            fmPalletizeService.saveFmPalletize(procedure);

        }
        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
        dispatch.setProcedure_route("");
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
