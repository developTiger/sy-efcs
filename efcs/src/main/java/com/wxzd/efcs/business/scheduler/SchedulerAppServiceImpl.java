package com.wxzd.efcs.business.scheduler;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.application.service.BatteryAppService;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.application.workProcedures.TestProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.log.FileLogFactory;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.policy.locationAllot.AllotOutResult;
import com.wxzd.policy.locationAllot.AllotPolicyType;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.policy.locationAllot.LocationAllotService;
import com.wxzd.policy.locationAllot.impl.DefaultAllotPolicyParam;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
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
 * Created by zhouzh on 2017/5/16.
 */
@Service
public class SchedulerAppServiceImpl implements SchedulerAppService {

    @Autowired
    ProcedureAppService procedureAppService;

    @Autowired
    FcsSchedulerService schedulerService;

    @Autowired
    LocationAllotQyery locationAllotQyery;

    @Autowired
    @Qualifier("emptyPalletAllotPolicyService")
    LocationAllotService locationAllotService;

    @Autowired
    @Qualifier("testEmptyPalletAllotPolicyService")
    LocationAllotService testLocationAllotService;

    @Autowired
    MemoryInstructionAppService instructionAppService;

    @Autowired
    PalletDispatchService palletDispatchService;
    @Autowired
    StorageService storageService;

    @Autowired
    StorageLocationService storageLocationService;

    /**
     * 收数
     *
     * @param fcsScheduler
     * @return
     */
    @Override
    public GaiaResult chargeNumberAndOut(FcsScheduler fcsScheduler) {
        fcsScheduler.setError_msg("");
        //		if (true)
        //			return GaiaResultFactory.getSuccess();
        fcsScheduler.setError_msg("");

        DefaultProcedure procedure = JsonUtl.format(fcsScheduler.getScheduler_obj(), DefaultProcedure.class);
        fcsScheduler.setLast_run_time(new Date());
        Boolean isHaveDown = false;
        String schedulerStocker = "";
        if (fcsScheduler.getDevice_no().startsWith("LB")) {
            String xpos = TestProcedureRouteMatch.getCubicXPos(fcsScheduler.getDevice_no());
            if (xpos.equals("5") || xpos.equals("6")) {
                schedulerStocker = DispatcherConfig.test_stocker_1;
            } else {
                schedulerStocker = DispatcherConfig.test_stocker_2;
            }
        } else {
            schedulerStocker = DispatcherConfig.formation_stocker_1;
        }
        //如果堆垛机尚有未完成的 空托指令，则不去做出库操作，
        //否则托盘不到位，会无限下架空托
        ListResult<Instruction> instructions = instructionAppService.getInstructionByQueueNo(fcsScheduler.getHouse_no(), schedulerStocker);
        if (instructions.isSuccess() && instructions.getItems().size() > 0) {
            for (Instruction instruction : instructions.getItems()) {
                if (instruction.getHouse_no().equals(fcsScheduler.getHouse_no()) && instruction.getWork_procedure() == fcsScheduler.getWorkProcedure()
                        && instruction.getInstr_type() == InstructionType.Stock_Out && (instruction.getInstr_status() == InstructionStatus.Executing
                        || instruction.getInstr_status() == InstructionStatus.Send || instruction.getInstr_status() == InstructionStatus.Waiting)) {

                    isHaveDown = true;
                    fcsScheduler.setError_code(303);
                    fcsScheduler.executeNextTime();
                    fcsScheduler.setError_msg("已有托盘下架，需等待执行完成，指令号：" + instruction.getInstr_no());

                }
                if (isHaveDown)
                    break;
            }
        }
        if (!isHaveDown) {
            try {
                fcsScheduler.setLast_run_time(new Date());
                if (!fcsScheduler.getIs_step1_success()) {
                    Long timeSecond = (new Date().getTime() - fcsScheduler.getCreate_datetime().getTime()) / 1000;

                    // 查询电芯条码信息
                    List<PalletDetail> batteries = palletDispatchService.getPalletInnerDetail(procedure.getPallet_no());
                    List<String> sfcBarcodes = new ArrayList<>();
                    for (PalletDetail item : batteries) {
                        sfcBarcodes.add(item.getBattery_barcode());
                    }

                    ChargeResult chargeResult = MesChargeNumber.chargeNumber(fcsScheduler.getWorkProcedure(), procedure, sfcBarcodes, timeSecond.intValue());
                    if (chargeResult.getCode() == 0) {
                        fcsScheduler.setIs_step1_success(true);
                        fcsScheduler.setError_times(0);
                    } else
                        fcsScheduler.addScheduleErrorTime(chargeResult.getCode(), chargeResult.getErrorMessage());
                    if (fcsScheduler.getError_times() > 3) {
                        fcsScheduler.setSchedulerFinish();
                        try {
                            StorageLocation storageLocation = storageLocationService.getLocationByLocationNo(fcsScheduler.getHouse_no(), fcsScheduler.getDevice_no());
                            storageLocation.setLogError(true, "库位收数异常！");
                            storageLocation.setForbid_in(storageLocation.getForbid_in()+1);
                            storageLocationService.addOrUpdateLocation(storageLocation);
                            //                            procedureAppService.changeToErrorProcedure(procedure.getHouseNo(), procedure.getCurrentPos(), procedure.getPallet_no(), "収数失败排出！");

                        } catch (Exception e) {
                            e.printStackTrace();
                            //                        fcsScheduler.executeNextTime();
                        }
                    } else {
                        fcsScheduler.executeNextTime();
                    }

                }
                if (fcsScheduler.getIs_step1_success() && !fcsScheduler.getIs_step2_success()) {
                    GaiaResult result = procedureAppService.chargeNumber(fcsScheduler.getWorkProcedure(), procedure);
                    if (result.isSuccess()) {
                        fcsScheduler.setIs_step2_success(true);
                        fcsScheduler.setSchedulerFinish();
                    } else {
                        fcsScheduler.addScheduleErrorTime(500, result.getMessage());
                        if (fcsScheduler.getError_times() > 3) {
                            fcsScheduler.setSchedulerFinish();
                            try {
                                StorageLocation storageLocation = storageLocationService.getLocationByLocationNo(fcsScheduler.getHouse_no(), fcsScheduler.getDevice_no());
                                storageLocation.setLogError(true, "库位收数异常！");
                                storageLocation.setForbid_in(storageLocation.getForbid_in()+1);

                                storageLocationService.addOrUpdateLocation(storageLocation);
                                //
                                //                                procedureAppService.changeToErrorProcedure(procedure.getHouseNo(), procedure.getCurrentPos(), procedure.getPallet_no(), "収数失败排出！");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else
                            fcsScheduler.executeNextTime();
                    }
                }
            } catch (Exception ex) {
                fcsScheduler.addScheduleErrorTime(500, ex.getMessage());
                if (fcsScheduler.getError_times() > 3) {
                    fcsScheduler.setSchedulerFinish();

                } else {
                    fcsScheduler.executeNextTime();
                }
            }
        }
        schedulerService.saveScheduler(fcsScheduler);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 空托出库
     *
     * @param fcsScheduler
     * @return
     */
    @Override
    public GaiaResult emptyPalletOut(FcsScheduler fcsScheduler) {
        fcsScheduler.setLast_run_time(new Date());
        Boolean isHaveDown = false;
        String stocker = "";
        //判断是哪个堆垛机执行下架
        if (fcsScheduler.getDevice_no().equals(DispatcherConfig.formation_pallet_to_hczpjxs)) {
            stocker = DispatcherConfig.formation_stocker_1;
        } else if (fcsScheduler.getDevice_no().equals(DispatcherConfig.test_palletize_cache_cwzpjxs2)) {
            stocker = DispatcherConfig.test_stocker_2;
        } else {
            stocker = DispatcherConfig.test_stocker_1;
        }
        //如果堆垛机尚有未完成的 空托指令，则不去做出库操作，
        //否则托盘不到位，会无限下架空托
        ListResult<Instruction> instructions = instructionAppService.getInstructionByQueueNo(fcsScheduler.getHouse_no(), stocker);
        if (instructions.isSuccess() && instructions.getItems().size() > 0) {
            for (Instruction instruction : instructions.getItems()) {
                if (instruction.getHouse_no().equals(fcsScheduler.getHouse_no()) && instruction.getWork_procedure() == fcsScheduler.getWorkProcedure()
                        && (instruction.getInstr_status() == InstructionStatus.Executing || instruction.getInstr_status() == InstructionStatus.Send
                        || instruction.getInstr_status() == InstructionStatus.Waiting)) {

                    isHaveDown = true;
                    fcsScheduler.setError_code(303);
                    fcsScheduler.executeNextTime();
                    fcsScheduler.setError_msg("已有托盘下架，需等待执行完成:" + instruction.getInstr_no());

                }
            }
        }
        if (!isHaveDown) {

            //            fcsScheduler.setError_msg("");
            if (fcsScheduler.getWorkProcedure() == WorkProcedure.Formation_PalletMove) {

                Integer formationCatchCount = locationAllotQyery.getEquipLocationEmptyPalletCount(fcsScheduler.getHouse_id(), DispatcherConfig.formation_palletize_catch_location);
                if (formationCatchCount < DispatcherConfig.formation_palletize_catch_location_min_count) {
                    DefaultAllotPolicyParam param = new DefaultAllotPolicyParam();
                    param.setHouseId(fcsScheduler.getHouse_id());
                    param.setHouseNo(fcsScheduler.getHouse_no());
                    param.setOutStorageType(StorageType.container);
                    param.setIs_EmptyContainer(true);
                    param.setColAllotPolicyType(AllotPolicyType.FromSmallToBig);
                    param.setRowAllotPolicyType(AllotPolicyType.FromBigToSmall);

                    ListResult<AllotOutResult> result = locationAllotService.allotStorageOut(param);

                    if (result.isSuccess()) {
                        AllotOutResult allotOutResult = result.getItems().get(0);
                        List<Storage> st = locationAllotQyery.getEquipLocationStorage(fcsScheduler.getHouse_id(), result.getItems().get(0).getLoc_no());
                        if (st.size() > 1) {
                            DefaultProcedure procedure = JsonUtl.format(fcsScheduler.getScheduler_obj(), DefaultProcedure.class);

                            storageService.setPalletIsEmpty(procedure.getPallet_no(), false);

                            StorageLocation storageLocation = storageLocationService.getLocationByLocationNo(fcsScheduler.getHouse_no(), fcsScheduler.getDevice_no());
                            storageLocation.setLogError(true, "找到的托盘并非空托！");
                            storageLocationService.addOrUpdateLocation(storageLocation);
                        } else {

                            String stockNo = "";
                            //                    if (allotOutResult.getX_pos() == 1) {
                            stockNo = DispatcherConfig.formation_stocker_1;
                            //                    } else
                            //                        stockNo = DispatcherConfig.formation_stocker_2;
                            Instruction instruction = new Instruction(fcsScheduler.getHouse_no());
                            instruction.newInstruction(fcsScheduler.getHouse_no(), fcsScheduler.getWorkProcedure(), result.getItems().get(0).getPallet_no(), InstructionType.Stock_Out, stockNo,//是否设置为堆垛机
                                    result.getItems().get(0).getLoc_no(), DispatcherConfig.formation_pallet_to_hczpjxs,//入库库位
                                    InstructionMovePolicy.Static, SendType.QueuesDirect);
                            instructionAppService.createInstr(instruction);
                        }
                    } else {
                        fcsScheduler.addScheduleErrorTime(1, result.getMessage());
                    }
                    fcsScheduler.executeNextTime();
                }
            } else {//测试段
                Integer formationCatchCount = locationAllotQyery.getEquipLocationEmptyPalletCount(fcsScheduler.getHouse_id(), DispatcherConfig.test_palletize_cache_cwzpjxs1);
                if (formationCatchCount < DispatcherConfig.formation_palletize_catch_location_min_count) {
                    DefaultAllotPolicyParam param = new DefaultAllotPolicyParam();
                    param.setHouseId(fcsScheduler.getHouse_id());
                    param.setHouseNo(fcsScheduler.getHouse_no());
                    param.setOutStorageType(StorageType.container);
                    param.setEquipNo(fcsScheduler.getDevice_no());
                    param.setIs_EmptyContainer(true);
                    param.setColAllotPolicyType(AllotPolicyType.FromSmallToBig);
                    param.setRowAllotPolicyType(AllotPolicyType.FromBigToSmall);

                    ListResult<AllotOutResult> result = testLocationAllotService.allotStorageOut(param);
                    if (result.isSuccess()) {
                        AllotOutResult allotOutResult = result.getItems().get(0);
                        List<Storage> st = locationAllotQyery.getEquipLocationStorage(fcsScheduler.getHouse_id(), result.getItems().get(0).getLoc_no());
                        if (st.size() > 1) {
                            DefaultProcedure procedure = JsonUtl.format(fcsScheduler.getScheduler_obj(), DefaultProcedure.class);

                            storageService.setPalletIsEmpty(procedure.getPallet_no(), false);
                            StorageLocation storageLocation = storageLocationService.getLocationByLocationNo(fcsScheduler.getHouse_no(), fcsScheduler.getDevice_no());
                            storageLocation.setLogError(true, "找到的托盘并非空托！");
                            storageLocationService.addOrUpdateLocation(storageLocation);

                        } else {
                            String stockNo = "";
                            if (allotOutResult.getX_pos() == 5 || allotOutResult.getX_pos() == 6) {
                                stockNo = DispatcherConfig.test_stocker_1;
                            } else
                                stockNo = DispatcherConfig.test_stocker_2;
                            Instruction instruction = new Instruction(fcsScheduler.getHouse_no());
                            instruction.newInstruction(fcsScheduler.getHouse_no(), fcsScheduler.getWorkProcedure(), result.getItems().get(0).getPallet_no(), InstructionType.Stock_Out, stockNo,//是否设置为堆垛机
                                    result.getItems().get(0).getLoc_no(), fcsScheduler.getDevice_no(),//入库库位
                                    InstructionMovePolicy.Static, SendType.QueuesDirect);
                            instructionAppService.createInstr(instruction);
                        }
                    } else {
                        fcsScheduler.addScheduleErrorTime(1, result.getMessage());
                    }
                    fcsScheduler.executeNextTime();
                }
            }
        }
        schedulerService.saveScheduler(fcsScheduler);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 高温和常温静置出库
     *
     * @param fcsScheduler
     * @return
     */
    @Override
    public GaiaResult storageSchedulerOut(FcsScheduler fcsScheduler) {
        fcsScheduler.setLast_run_time(new Date());
        return null;
    }

    /**
     * 分配库位
     *
     * @param fcsScheduler
     * @return
     */
    @Override
    public GaiaResult allotLocation(FcsScheduler fcsScheduler) {
        DefaultProcedure procedure = JsonUtl.format(fcsScheduler.getScheduler_obj(), DefaultProcedure.class);
        try {
            GaiaResult result = procedureAppService.procedureAllotLocation(procedure.getHouseNo(), procedure.getCurrentPos(), procedure.getPallet_no());
            if (result.isSuccess()) {
                fcsScheduler.setIs_step1_success(true);
                fcsScheduler.setSchedulerFinish();
            } else {
                fcsScheduler.addScheduleErrorTime(1, result.getMessage());
                fcsScheduler.executeNextTime();
            }
        } catch (Exception e) {
            fcsScheduler.addScheduleErrorTime(1, e.getMessage());
            fcsScheduler.executeNextTime();
            e.printStackTrace();
        }

        schedulerService.saveScheduler(fcsScheduler);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 空托上架策略
     *
     * @return
     */
    @Override
    public GaiaResult palletMoveUp(FcsScheduler fcsScheduler) {
        List<Storage> tempStorage = locationAllotQyery.getEquipLocationStorage(fcsScheduler.getHouse_id(), DispatcherConfig.formation_empty_pallet_temp_cubic_location);
        Boolean needSend = false;
        Instruction instruction = null;
        FileLogFactory.infoCustomer("/scheduler/message", "palletMoveUp", "","计划任务执行开始；判断moveUp 库存数量，数量："+tempStorage.size());
         if (tempStorage.size() == 0) {
            ListResult<Instruction> instructions = instructionAppService.getInstructionByHouseNo(fcsScheduler.getHouse_no());

            if(instructions.getItems()!=null) {
                FileLogFactory.infoCustomer("/scheduler/message", "palletMoveUp", "", "开始moveUp，获取指令现有 ，指令数量：" + instructions.getItems().size());

                if (instructions.getItems().size() > 0)
                //region
                {

                    for (Instruction ins : instructions.getItems()) {
                        if (ins.getWork_procedure() == WorkProcedure.Formation_PalletMove
                                && (ins.getInstr_status() == InstructionStatus.Waiting
                                || ins.getInstr_status() == InstructionStatus.Send
                                || ins.getInstr_status() == InstructionStatus.Executing)
                                && ins.getTo_pos().equals(DispatcherConfig.formation_empty_pallet_temp_cubic_location)
                                ) {
                            needSend = false;
                            FileLogFactory.infoCustomer("/scheduler/message", "palletMoveUp", "", "有待执行的020201入库指令：" + ins.getInstr_no());

                            return GaiaResultFactory.getSuccess();

                        }
                        if (ins.getWork_procedure() == WorkProcedure.Formation_PalletMove
                                && (ins.getInstr_status() == InstructionStatus.Waiting
                                || ins.getInstr_status() == InstructionStatus.Send
                                || ins.getInstr_status() == InstructionStatus.Executing)
                                && ins.getFrom_pos().equals(DispatcherConfig.formation_empty_pallet_temp_cubic_location)
                                ) {
                            needSend = false;
                            FileLogFactory.infoCustomer("/scheduler/message", "palletMoveUp", "", "有待执行的020201出库指令：" + ins.getInstr_no());

                            return GaiaResultFactory.getSuccess();

                        }
                        if (ins.getWork_procedure() == WorkProcedure.Formation_PalletMove
                                && ins.getInstr_status() == InstructionStatus.Created
                                && ins.getTo_pos().equals(DispatcherConfig.formation_empty_pallet_temp_cubic_location)) {
                            FileLogFactory.infoCustomer("/scheduler/message", "palletMoveUp", "", "校验正常，可下发指令：" + ins.getInstr_no());


                            needSend = true;
                            instruction = ins;
                        }
                    }
                }
            }
            //endregion
        }
        if (needSend && null!= instruction) {
            FileLogFactory.infoCustomer("/scheduler/message", "palletMoveUp", "", "success:更新指令状态为待下发：" + instruction.getInstr_no());

            instructionAppService.setInstrSatus(instruction, InstructionStatus.Waiting);
            return GaiaResultFactory.getSuccess();


        }

        FileLogFactory.infoCustomer("/scheduler/message", "palletMoveUp", "", "本次执行结束无可下发指令！" );


        return GaiaResultFactory.getSuccess();

    }
}
