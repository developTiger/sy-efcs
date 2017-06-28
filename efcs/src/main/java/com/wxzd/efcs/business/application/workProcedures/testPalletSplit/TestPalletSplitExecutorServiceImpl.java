package com.wxzd.efcs.business.application.workProcedures.testPalletSplit;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.TestProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FmPalletSplitService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.log.FileLogUnity;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.policy.locationAllot.LocationAllotService;
import com.wxzd.wms.core.domain.entities.StorageLocation;
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
@Service("testPalletSplitExecutorService")
public class TestPalletSplitExecutorServiceImpl implements TestPalletSplitExecutorService<DefaultProcedure, GaiaResult> {


    @Autowired
    FmPalletSplitService fmPalletSplitService;
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
    @Qualifier("palletSplitAllotPolicyService")
    LocationAllotService locationAllotService;


    private WorkProcedure currentWorkProcedure = WorkProcedure.Test_Pallet_Split;
    private WorkProcedure nextProcedure = WorkProcedure.Test_PalletMove;


    @Override
    public GaiaResult doWorkProcedure(DefaultProcedure procedureInfo) {
        //默认一个工序 会经历 3个库位。
        //1、待入
        //2、入组盘位
        //3、出组盘位
        //机械手位置 --- 下面1号位 上面2号位  REWORK 3号位         OK4号位  NG 5号位  缓存6号位
        PalletizePrecedureInfo palletizePrecedureInfo = new PalletizePrecedureInfo(procedureInfo.getHouseNo(), procedureInfo.getPallet_no(), procedureInfo.getFmCreateMode());
        try {
            palletizePrecedureInfo = (PalletizePrecedureInfo) procedureInfo;
        } catch (Exception ex) {
            BeanUtl.copyProperties(procedureInfo, palletizePrecedureInfo);
        }
        if (!StringUtl.isEmpty(palletizePrecedureInfo.getFrom_pallet_no()))
            palletizePrecedureInfo.setPallet_no(palletizePrecedureInfo.getFrom_pallet_no());
        String route = "";
        SendType sendType = SendType.Direct;
        String stockNo = "";
        InstructionType instructionType = InstructionType.Transport;
        Instruction instruction = new Instruction(procedureInfo.getHouseNo());
        if (procedureInfo.getPalletDispatch() == null) {
            procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(palletizePrecedureInfo.getPallet_no()));
            if (procedureInfo.getPalletDispatch() == null) {
                throw new RuntimeException("托盘调度异常,调度信息不存在！");
            }
        }
        //初始化  创建组盘单
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv1_out) || procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv2_out)) {
            FmPalletSplit procedure = fmPalletSplitService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedure == null) {
                procedure = new FmPalletSplit();
                procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos(), ProcedureConfig.PalletChannel_Policy);
            }
            procedure.setHouse_id(procedureInfo.getWarehouse().getId());
            procedure.setHouse_no(procedureInfo.getHouseNo());
            procedure.setPallet_no(procedureInfo.getPallet_no());
            procedureInfo.getPalletDispatch().setChannel_policy(PalletChannelPolicy.N);
            procedureInfo.getPalletDispatch().setSplit_policy(PalletSplitPolicy.SplitAll);

            //初始化 拆盘工序单
            fmPalletSplitService.saveFmPalletSplit(procedure);

            String nextPos = "";
//            if (currentWorkProcedure == WorkProcedure.Test_OCV_1) {
//                if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv1_out)) {
//                    nextPos = DispatcherConfig.test_normal_temperature2_up1;
//                } else {
//                    nextPos = DispatcherConfig.test_normal_temperature2_up2;
//
//                }
//            } else {
            if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_ocv1_out)) {
                nextPos = DispatcherConfig.test_split_equip_1;
            } else {
                nextPos = DispatcherConfig.test_split_equip_2;
            }
//            }

            List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), nextPos);
            if (locations.size() == 0) {
                throw new RuntimeException(I18nContext.getMessage("拆盘缓存设备库位未设置"));
            }
            storageService.storageTransfer(
                    procedureInfo.getWarehouse().getId(),
                    locations.get(0).getId(),
                    procedureInfo.getPallet_no(),
                    procedure.getForm_no(),
                    currentWorkProcedure.toString()
            );
            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
            instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                    procedureInfo.getPallet_no(),
                    instructionType,
                    stockNo,//是否设置为堆垛机
                    procedureInfo.getCurrentPos(),
                    nextPos,
                    InstructionMovePolicy.Static,
                    sendType);
            instructionService.createInstr(instruction);
            return GaiaResultFactory.getObject(instruction);
        } else {
            FmPalletSplit procedure = fmPalletSplitService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
//            //组盘前置扫描
//            if (DispatcherConfig.test_split_scan_no.equals(procedureInfo.getCurrentPos())) {
//                //更新托盘状态
//                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//                //创建指令
//                instruction.newInstruction(procedureInfo.getHouse_no(), currentWorkProcedure,
//                        procedureInfo.getPallet_no(),
//                        instructionType,
//                        "",
//                        procedureInfo.getCurrentPos(),
//                        "",//TODO  weizhi
//                        InstructionMovePolicy.Static,
//                        sendType);
//
//                instructionService.createInstr(instruction);
//                return GaiaResultFactory.getObject(instruction);
//            }
            //托盘到达 是机械手 （（1号位 或者 2号位 ）并且　 PalletOperate.Arrived）
            if ((DispatcherConfig.test_split_equip_1.equals(palletizePrecedureInfo.getCurrentPos()) || DispatcherConfig.test_split_equip_2.equals(palletizePrecedureInfo.getCurrentPos()))
                // && palletizePrecedureInfo.getPalletOperate() == PalletOperate.CommandFinished
                    ) {

                procedureInfo.getPalletDispatch().setChannel_policy(PalletChannelPolicy.N);
                procedureInfo.getPalletDispatch().setSplit_policy(PalletSplitPolicy.SplitAll);

                //执托盘移库入位
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
            //电池拆盘  PalletOperate.MoveItem）
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.MoveItem) {
                try {
                    //第一次拆盘 设置开始时间
                    if (procedure.getProc_start_time() == null) {
                        procedure.setProc_start_time(new Date());
                        //更新 组盘工序单
                        fmPalletSplitService.saveFmPalletSplit(procedure);
                    }

                    PalletDetail detail = dispatchService.getPalletInnerDetail(palletizePrecedureInfo.getTo_pallet_no(), palletizePrecedureInfo.getFrom_pos_channel_no());
//                detail.setChannel_no(String.valueOf(palletizePrecedureInfo.getTo_pos_channel_no()));

                    PositionType positionType = PositionType.Pallet;
                    if (palletizePrecedureInfo.getTo_pos_no().equals("4") || palletizePrecedureInfo.getTo_pos_no().equals("5"))
                        positionType = PositionType.Line;

                    detail.setToPos(palletizePrecedureInfo.getTo_pallet_no(), palletizePrecedureInfo.getClamp_no(), palletizePrecedureInfo.getTo_pos_no(), String.valueOf(palletizePrecedureInfo.getTo_pos_channel_no()), positionType);
                    dispatchService.savePalletInnerDetail(detail);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return GaiaResultFactory.getObject(instruction);
            }
            //拆盘完成
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.OperateComplete) {
                //初始化 拆盘工序单
                procedure.setProc_complete_time(new Date());
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);
                fmPalletSplitService.saveFmPalletSplit(procedure);
                procedureInfo.setCurrentPos(procedureInfo.getPalletDispatch().getCurrent_pos());

                String nextPos = "";
                if (procedureInfo.getPalletDispatch().getCurrent_pos().equals(DispatcherConfig.test_split_equip_2)) {
                    nextPos = DispatcherConfig.test_normal_temperature2_up2;
                } else {
                    nextPos = DispatcherConfig.test_normal_temperature2_up1;
                }
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(palletizePrecedureInfo.getWarehouse().getId(), nextPos);//TODO  next pos

                if (locations.size() == 0) {
                    throw new RuntimeException(I18nContext.getMessage("设备库位未设置"));
                }
                storageService.storageTransfer(
                        procedureInfo.getWarehouse().getId(),
                        locations.get(0).getId(),
                        procedureInfo.getPallet_no(),
                        procedure.getForm_no(),
                        currentWorkProcedure.toString()
                );
                PalletDispatch dis = ConvertFactory.convertOldDispatchToNewOne(palletizePrecedureInfo.getPalletDispatch());
                dis.setDispatch_status(PalletDispatchStatus.Dispatching);
                dis.setWork_procedure(WorkProcedure.Test_PalletMove);
                dispatchService.newPalletDispatch(dis);
                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        InstructionType.Transport,
                        "",
                        procedureInfo.getPalletDispatch().getCurrent_pos(),
                        nextPos,//TODO  next pos
                        InstructionMovePolicy.Static,
                        SendType.Direct);
                //仅更新托盘状态,托盘未移动
                instructionService.createInstr(instruction);
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Finished, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                procedureInfo.setPalletDispatch(dis);

                return GaiaResultFactory.getObject(instruction);
            } else {
                //都不是上面的情况
                //启动自动寻径
                try {
                    route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_normal_temperature2_up2);
                } catch (Exception e) {
                    route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_normal_temperature2_up1);

                }
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
     * @param currentPos    @return
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
    public GaiaResult errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {

        FmPalletSplit procedure = fmPalletSplitService.getProcedureByPallet(palletNo, currentWorkProcedure);

        if (procedure != null) {
            procedure.setFm_status(FmStatus.ErrorFinished);
            procedure.setError_code(EfcsErrorCode.ErrorFinish);
            procedure.setError_desc(remark);
//        dispatch.setError_desc(remark);
            fmPalletSplitService.saveFmPalletSplit(procedure);
        }
        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
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
        currentWorkProcedure = workProcedure;
        FmPalletSplit procedure = fmPalletSplitService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
        procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));
        if (procedure == null) {
            procedure = new FmPalletSplit();
            procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos(), ProcedureConfig.PalletChannel_Policy);
        }
        procedure.setHouse_id(procedureInfo.getWarehouse().getId());
        procedure.setHouse_no(procedureInfo.getHouseNo());
        procedure.setPallet_no(procedureInfo.getPallet_no());
        procedureInfo.getPalletDispatch().setChannel_policy(ProcedureConfig.PalletChannel_Policy);
        //初始化 拆盘工序单
        fmPalletSplitService.saveFmPalletSplit(procedure);
        dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());

        return GaiaResultFactory.getSuccess();

    }
}
