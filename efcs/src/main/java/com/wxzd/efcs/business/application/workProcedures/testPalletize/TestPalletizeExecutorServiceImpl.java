package com.wxzd.efcs.business.application.workProcedures.testPalletize;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.TestProcedureRouteMatch;
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
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.SerialNoGenerator;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testPalletizeExecutorService")
public class TestPalletizeExecutorServiceImpl implements TestPalletizeExecutorService<DefaultProcedure, GaiaResult> {


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


    private WorkProcedure currentWorkProcedure = WorkProcedure.Test_Palletize;
    private WorkProcedure nextProcedure = WorkProcedure.Normal_Temperature_1;

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
        PalletDispatch palletDispatch;
        FmPalletize procedure;
        if (procedureInfo.getPalletDispatch() == null) {
            palletizePrecedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));

        }
        //初始化  创建组盘单
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_palletize_scaner_no)) {
            //不管什么情况 重新规划路线
            route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_palletize_device_no);
            //入库口入进来的组盘托盘
            if (procedureInfo.getPalletDispatch() != null && procedureInfo.getPalletDispatch().getWork_procedure().equals(currentWorkProcedure)) {
                procedure = fmPalletizeService.getProcedureByPallet(procedureInfo.getPallet_no());
                palletDispatch = procedureInfo.getPalletDispatch();
                procedure.setPallet_cargo_id(palletDispatch.getId());
                palletDispatch.setProcedure_route(route);
            } else { //空托盘转 待组盘工序
                procedure = new FmPalletize();
                String formNo = SerialNoGenerator.getSerialNo("PT");
                procedure.setForm_no(formNo);
                palletDispatch = procedureInfo.getPalletDispatch();

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
                    DispatcherConfig.test_palletize_device_no,
                    InstructionMovePolicy.Static,
                    sendType);

            instructionService.createInstr(instruction);
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());

            return GaiaResultFactory.getObject(instruction);
        } else {
            procedure = fmPalletizeService.getProcedureByPallet(palletizePrecedureInfo.getPallet_no());
            //托盘到达
            if ((DispatcherConfig.test_palletize_equip_1.equals(procedureInfo.getCurrentPos())
                    || DispatcherConfig.test_palletize_equip_2.equals(procedureInfo.getCurrentPos()))
                //&&procedureInfo.getPalletOperate()==PalletOperate.CommandFinished
                    ) {
                //不是组盘完成
                //执行空托盘移库入位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        palletizePrecedureInfo.getStorageLocation().getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),

                        currentWorkProcedure.toString()
                );
                //更新托盘状态
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                return GaiaResultFactory.getObject(instruction);
            }
            //电池组盘
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.MoveItem //不是组盘完成
                    ) {
                //第一次入盘 设置开始时间

                procedureInfo.getPalletDispatch().setIs_empty(false);
                procedure.setFm_status(FmStatus.Executing);
                //初始化 组盘工序单
                //库存移动到高温上架位
                storageService.storageTransferToPallet(
                        procedureInfo.getWarehouse().getId(),
                        palletizePrecedureInfo.getTo_pallet_no(),
                        palletizePrecedureInfo.getBattery_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                PalletDetail detail = new PalletDetail(ProcedureConfig.getHouseSkuId(procedureInfo.getHouseNo())
                        , procedureInfo.getPalletDispatch().getId()
                        , String.valueOf(palletizePrecedureInfo.getTo_pos_channel_no())
                        , procedure.getForm_no(), palletizePrecedureInfo.getBattery_no()
                        , DispatcherConfig.battery_default_ok
                        , palletizePrecedureInfo.getPallet_no());
                detail.setFromPos(palletizePrecedureInfo.getFrom_pos_no(), palletizePrecedureInfo.getClamp_no(), palletizePrecedureInfo.getFrom_pos_no(), String.valueOf(palletizePrecedureInfo.getFrom_pos_channel_no()), PositionType.Line);
                if (procedure.getProc_start_time() == null) {
                    procedure.setProc_start_time(new Date());
                }
                storageService.setPalletIsEmpty(procedureInfo.getPallet_no(), false);

                fmPalletizeService.saveFmPalletize(procedure);

                dispatchService.savePalletInnerDetail(detail);
                dispatchService.newPalletDispatch(procedureInfo.getPalletDispatch());


                return GaiaResultFactory.getObject(instruction);
            }
            //组盘完成
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.OperateComplete //组盘完成
                    ) {
                //完成 组盘工序单
                procedure.setProc_complete_time(new Date());
                procedure.setPallet_status(PalletStatus.Out_Waiting);
                fmPalletizeService.saveFmPalletize(procedure);

                String endPos = "";
                if (procedureInfo.getPalletDispatch().getCurrent_pos().equals(DispatcherConfig.test_palletize_equip_1)) {
                    endPos = DispatcherConfig.test_palletize_end_1;
                } else {
                    endPos = DispatcherConfig.test_palletize_end_2;
                }

                route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getPalletDispatch().getCurrent_pos(), endPos);
                procedureInfo.getPalletDispatch().setProcedure_route(route);
                //仅更新托盘状态,托盘未移动
                dispatchService.setPalletStatus(procedureInfo.getPalletDispatch(), PalletStatus.Out_Waiting);
                return GaiaResultFactory.getObject(instruction);
            }
            if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_palletize_end_1) || procedureInfo.getCurrentPos().equals(DispatcherConfig.test_palletize_end_2)) {
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);
                fmPalletizeService.saveFmPalletize(procedure);
                //获取上架库位
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(palletizePrecedureInfo.getWarehouse().getId(), procedureInfo.getCurrentPos());
                if (locations.size() == 0) {
                    throw new RuntimeException(I18nContext.getMessage("上架设备库位未设置"));
                }
                //库存移动到上架位
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        locations.get(0).getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                procedureInfo.getPalletDispatch().setProcedure_route("");
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());

                return GaiaResultFactory.getSuccess();
            } else {
                //都不是上面的情况
                //启动自动寻径
                try {
                    if (procedureInfo.getPalletDispatch() != null && procedureInfo.getPalletDispatch().getWork_procedure().equals(currentWorkProcedure)) {
                        procedure = fmPalletizeService.getProcedureByPallet(procedureInfo.getPallet_no());
                        palletDispatch = procedureInfo.getPalletDispatch();
                        procedure.setPallet_cargo_id(palletDispatch.getId());
                        palletDispatch.setProcedure_route(route);
                    } else { //空托盘转 待组盘工序
                        procedure = new FmPalletize();
                        String formNo = SerialNoGenerator.getSerialNo("PT");
                        procedure.setForm_no(formNo);
                        palletDispatch = procedureInfo.getPalletDispatch();

                        palletDispatch.Reset(currentWorkProcedure);
                        palletDispatch.setCurrent_form_no(formNo);
                        palletDispatch.setProcedure_route(route);
                        UUID id = dispatchService.newPalletDispatchWithMoveDetail(palletDispatch, false);
                        procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), id, procedureInfo.getCurrentPos(), ProcedureConfig.PalletChannel_Policy);
                        procedure.setHouse_id(procedureInfo.getWarehouse().getId());
                        procedure.setHouse_no(procedureInfo.getHouseNo());
                        fmPalletizeService.saveFmPalletize(procedure);
                    }
                    route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_palletize_device_no);
                    procedureInfo.getPalletDispatch().setProcedure_route(route);

                    String nextPos = TestProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
                    if (TestProcedureRouteMatch.isStocker(nextPos)) {
                        stockNo = nextPos;
                        nextPos = TestProcedureRouteMatch.getNextPosition(route, stockNo);
                        instructionType = InstructionType.Stock_Move;
                        sendType = SendType.QueuesDirect;
                    }

                    if (nextPos.equals(procedureInfo.getCurrentPos())) {
                        return GaiaResultFactory.getObject(instruction);

                    }

                    dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(),
                            PalletStatus.In_Waiting,
                            currentWorkProcedure,
                            PalletDispatchStatus.Dispatching,
                            PositionType.Transport_Location,
                            procedureInfo.getCurrentPos(),
                            procedure.getForm_no());
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

                    return GaiaResultFactory.getObject(instruction);
                } catch (Exception ex) {
                    return new ObjectResult<>(false, "未识别的设备号" + procedureInfo.getCurrentPos());
                }
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
        if (currentPos.equals(DispatcherConfig.test_palletize_end_1) || currentPos.equals(DispatcherConfig.test_palletize_end_2)) {
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
        if (procedureInfo.getPalletDispatch() == null) {
            procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));
            if (procedureInfo.getPalletDispatch() == null) {
                throw new RuntimeException("工序切换错误，托盘信息状态异常");
            }
        }
        if (procedureInfo.getPalletDispatch().getWork_procedure() != workProcedure) {
            procedureInfo.getPalletDispatch().setWork_procedure(currentWorkProcedure);
        }
        FmPalletize procedure = fmPalletizeService.getProcedureByPallet(procedureInfo.getPallet_no());

        if (procedure == null) {
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
