package com.wxzd.efcs.business.application.workProcedures.palletSplit;

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
import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FmPalletSplitService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.AllotStorageLocation;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.policy.locationAllot.LocationAllotService;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("formationPalletSplitExecutorService")
public class FormationPalletSplitExecutorServiceImpl implements FormationPalletSplitExecutorService<DefaultProcedure, GaiaResult> {


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


    private WorkProcedure currentWorkProcedure = WorkProcedure.Formation_Split;
    private WorkProcedure nextProcedure = WorkProcedure.Formation_Rework_Palletize;


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
        if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_pallet_split_catch_location)) {

			FmPalletSplit procedure = fmPalletSplitService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
			if (procedure == null) {
				procedure = new FmPalletSplit();
				procedure.newProcedure(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getPalletDispatch().getId(), procedureInfo.getCurrentPos(),
						ProcedureConfig.PalletChannel_Policy);
			}
			procedure.setHouse_id(procedureInfo.getWarehouse().getId());
			procedure.setHouse_no(procedureInfo.getHouseNo());
			procedure.setPallet_no(procedureInfo.getPallet_no());
			procedureInfo.getPalletDispatch().setChannel_policy(ProcedureConfig.PalletChannel_Policy);
			//初始化 拆盘工序单
			//            fmPalletSplitService.saveFmPalletSplit(procedure);
			//            dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
			//            ObjectResult<AllotStorageLocation> result = locationAllotService.allotStorageLocationIn(currentWorkProcedure, palletizePrecedureInfo.getPallet_no(), procedureInfo.getWarehouse().getId(),
			//                    procedureInfo.getHouseNo(), palletizePrecedureInfo.getCurrentPos(), null, null);
			//            //执托盘移库入位
			//            //执行空托盘移库入位
			//            storageService.storageTransfer(
			//                    procedureInfo.getWarehouse().getId(),
			//                    result.getObject().getStorageLocation().getId(),
			//                    procedureInfo.getPallet_no(),
			//                    procedure.getForm_no(),
			//                    currentWorkProcedure.toString()
			//            );
			//            if (result.getObject().getSplitAll()) {
			procedure.setSplit_policy(PalletSplitPolicy.SplitAll.name());
			procedureInfo.getPalletDispatch().setSplit_policy(PalletSplitPolicy.SplitAll);
			//            } else {
			//                procedure.setSplit_policy(PalletSplitPolicy.NotSplitALl.name());
			//                procedureInfo.getPalletDispatch().setSplit_policy(PalletSplitPolicy.NotSplitALl);
			//            }
			fmPalletSplitService.saveFmPalletSplit(procedure);
			//更新托盘状态
			dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
					PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
			//创建指令
			instruction.newInstruction(palletizePrecedureInfo.getHouseNo(), currentWorkProcedure, palletizePrecedureInfo.getPallet_no(), instructionType, "", palletizePrecedureInfo.getCurrentPos(),
					DispatcherConfig.formation_split_device_no, InstructionMovePolicy.Static, sendType);

			instructionService.createInstr(instruction);
			return GaiaResultFactory.getObject(instruction);
			//            return GaiaResultFactory.getObject(instruction);

        	
        } else {
            PalletDispatch dispatch = dispatchService.getByPalletNo(palletizePrecedureInfo.getPallet_no());
            FmPalletSplit procedure = fmPalletSplitService.getProcedureByPallet(procedureInfo.getPallet_no(), currentWorkProcedure);
            if (procedureInfo.getPalletDispatch().getWork_procedure() != currentWorkProcedure) {
                throw new RuntimeException("托盘调度异常,工序不是拆盘工序！");
            }
            if (procedure == null) {
                throw new RuntimeException("工序单不存在！");
            }
            //组盘前置扫描
//            if (DispatcherConfig.formation_pallet_split_catch_location.equals(procedureInfo.getCurrentPos())) {
//                //分配库位
//                ObjectResult<AllotStorageLocation> result = locationAllotService.allotStorageLocationIn(currentWorkProcedure, palletizePrecedureInfo.getPallet_no(), procedureInfo.getWarehouse().getId(),
//                        procedureInfo.getHouseNo(), palletizePrecedureInfo.getCurrentPos(), null, null);
//                //执托盘移库入位
//                //执行空托盘移库入位
//                storageService.storageTransfer(
//                        procedureInfo.getWarehouse().getId(),
//                        result.getObject().getStorageLocation().getId(),
//                        procedureInfo.getPallet_no(),
//                        procedure.getForm_no(),
//                        currentWorkProcedure.toString()
//                );
//                if (result.getObject().getSplitAll()) {
//                    procedure.setSplit_policy(PalletSplitPolicy.SplitAll.name());
//                    procedureInfo.getPalletDispatch().setSplit_policy(PalletSplitPolicy.SplitAll);
//                } else {
//                    procedure.setSplit_policy(PalletSplitPolicy.NotSplitALl.name());
//                    procedureInfo.getPalletDispatch().setSplit_policy(PalletSplitPolicy.NotSplitALl);
//                }
//                fmPalletSplitService.saveFmPalletSplit(procedure);
//                //更新托盘状态
//                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
//                //创建指令
//                instruction.newInstruction(palletizePrecedureInfo.getHouseNo(), currentWorkProcedure,
//                        palletizePrecedureInfo.getPallet_no(),
//                        instructionType,
//                        "",
//                        palletizePrecedureInfo.getCurrentPos(),
//                        result.getObject().getStorageLocation().getLoc_no(),
//                        InstructionMovePolicy.Static,
//                        sendType);
//
//                instructionService.createInstr(instruction);
//                return GaiaResultFactory.getObject(instruction);
//            }
            //托盘到达 是机械手 （（1号位 或者 2号位 ）并且　 PalletOperate.Arrived）
            if (DispatcherConfig.formation_split_equip_1.equals(palletizePrecedureInfo.getCurrentPos()) || DispatcherConfig.formation_split_equip_2.equals(palletizePrecedureInfo.getCurrentPos())
//                    && palletizePrecedureInfo.getPalletOperate() == PalletOperate.CommandFinished
                    ) {

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

                    PalletDetail detail = dispatchService.getPalletInnerDetail(palletizePrecedureInfo.getFrom_pallet_no(), palletizePrecedureInfo.getFrom_pos_channel_no());
//                detail.setChannel_no(String.valueOf(palletizePrecedureInfo.getTo_pos_channel_no()));

                    if (detail == null)
                        throw new RuntimeException("通过托盘及通道获取电池信息失败");
                    PositionType positionType = PositionType.Pallet;
                    if (palletizePrecedureInfo.getTo_pos_no().equals("4") || palletizePrecedureInfo.getTo_pos_no().equals("5") || palletizePrecedureInfo.getTo_pos_no().equals("7"))
                        positionType = PositionType.Line;

                    detail.setToPos(palletizePrecedureInfo.getTo_pallet_no(), palletizePrecedureInfo.getClamp_no(), palletizePrecedureInfo.getTo_pos_no(), String.valueOf(palletizePrecedureInfo.getTo_pos_channel_no()), positionType);
                    dispatchService.savePalletInnerDetail(detail);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return GaiaResultFactory.getObject(instruction);
            }
            //拆盘完成
            if (palletizePrecedureInfo.getPalletOperate() == PalletOperate.OperateComplete) {
                //完成 组盘工序单
                procedure.setProc_complete_time(new Date());
                procedure.setPallet_status(PalletStatus.Out_Waiting);
                fmPalletSplitService.saveFmPalletSplit(procedure);

                //判断要移动到的位置
                String nextPos = "";
                if (procedureInfo.getPalletDispatch().getCurrent_pos().equals(DispatcherConfig.formation_split_equip_1)) { //1号位 只能到缓存位
                    nextPos = DispatcherConfig.formation_rework_pallet_catch_location;
                    dispatchService.setPalletStatus(dispatch, PalletStatus.Out_Waiting);
                    //完成 组盘工序单
                    procedure.setProc_complete_time(new Date());
                    procedure.setPallet_status(PalletStatus.Out_Waiting);
                    fmPalletSplitService.saveFmPalletSplit(procedure);

                } else {
                    procedure.setPallet_status(PalletStatus.Out_Finished);
                    procedure.setFm_status(FmStatus.Finished);
                    fmPalletSplitService.saveFmPalletSplit(procedure);
                    nextPos = DispatcherConfig.formation_rework_waitin_location;
                    dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Finished, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                    PalletDispatch dis = ConvertFactory.convertOldDispatchToNewOne(palletizePrecedureInfo.getPalletDispatch());
                    dis.setDispatch_status(PalletDispatchStatus.Dispatching);
                    dis.setWork_procedure(WorkProcedure.Formation_PalletMove);
                    dispatchService.newPalletDispatch(dis);

                }
                List<StorageLocation> locations = locationAllotQyery.getEquipLocation(palletizePrecedureInfo.getWarehouse().getId(), nextPos);

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
                if(!DispatcherConfig.noNeedSendNextPosition.contains(procedureInfo.getPalletDispatch().getCurrent_pos())) {
                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        InstructionType.Transport,
                        "",
                        dispatch.getCurrent_pos(),
                        nextPos,
                        InstructionMovePolicy.Static,
                        SendType.Direct);
                instructionService.createInstr(instruction);
                }
                //仅更新托盘状态,托盘未移动
                return GaiaResultFactory.getObject(instruction);
            }
            // rowork托盘到位
            if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_pallet_catch_location)
//                    || palletizePrecedureInfo.getCurrentPos() == DispatcherConfig.formation_rework_pallet_allot_location
                    || procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_waitin_location)
                    ) {
                procedure.setPallet_status(PalletStatus.Out_Finished);
                procedure.setFm_status(FmStatus.Finished);
                fmPalletSplitService.saveFmPalletSplit(procedure);
                //保存 dispatch
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Finished, PositionType.Transport_Location, procedureInfo.getCurrentPos(), procedure.getForm_no());
                if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_rework_pallet_catch_location)) {
                    //获取托盘中重排信息
                    //新增一条dispatch记录
                    PalletDispatch dis = ConvertFactory.convertOldDispatchToNewOne(palletizePrecedureInfo.getPalletDispatch());
                    dis.setDispatch_status(PalletDispatchStatus.Dispatching);
                    dis.setWork_procedure(WorkProcedure.Formation_Rework_Palletize);
                    if (dispatch.getSplit_policy() == PalletSplitPolicy.NotSplitALl) {
                        dis.setIs_empty(false);

                    } else {
                        dispatch.setIs_empty(true);
                        storageService.setPalletIsEmpty(procedureInfo.getPallet_no(), true);
                    }
                    UUID id = dispatchService.newPalletDispatch(dis);
                    try {
                        if (dispatch.getSplit_policy() == PalletSplitPolicy.NotSplitALl) {
//                procedureInfo.setPalletDispatch(dis);
                            List<PalletDetail> newDetailList = new ArrayList<>();
                            List<PalletDetail> details = dispatchService.getPalletInnerDetailResort(procedureInfo.getPalletDispatch().getId());
                            //如果托盘中有东西，则将电池 加入新的dispatch中
                            if (details.size() > 0) {
                                for (PalletDetail detail : details) {
                                    PalletDetail newdetail = new PalletDetail(ProcedureConfig.getHouseSkuId(procedureInfo.getHouseNo()), id, detail.getTo_pos_channel_no(), "", detail.getBattery_barcode(), detail.getBattery_status(), detail.getPallet_no());
                                    newdetail.setFromPos(detail.getTo_equip_no(), detail.getTo_clamp_no(), detail.getTo_pos_no(), detail.getTo_pos_channel_no(), detail.getTo_pos_type());
                                    newDetailList.add(newdetail);
                                }
                                dispatchService.savePalletInnerDetail(details);
                            }
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    procedureInfo.setPalletDispatch(dis);
                }
                return GaiaResultFactory.getSuccess();
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
                instructionService.createInstr(instruction);
                dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
                return GaiaResultFactory.getObject(instruction);

            }
        }
//        return GaiaResultFactory.getError(I18nContext.getMessage("工序异常"));
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
        if (currentPos.equals(DispatcherConfig.formation_rework_pallet_catch_location))
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
        if(procedure!=null){
            procedure.setFm_status(FmStatus.ErrorFinished);
            procedure.setError_code(EfcsErrorCode.ErrorFinish);
            procedure.setError_desc(remark);
//        dispatch.setError_desc(remark);
            fmPalletSplitService.saveFmPalletSplit(procedure);
        }
        PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
        dispatch.setPallet_status(PalletStatus.In_Waiting);

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

        return GaiaResultFactory.getObject(null);
    }
}
