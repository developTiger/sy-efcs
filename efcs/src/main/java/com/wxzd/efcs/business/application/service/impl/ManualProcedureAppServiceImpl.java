package com.wxzd.efcs.business.application.service.impl;

import com.atlmes.ws.celltest.SfcRemovalResponse;
import com.atlmes.ws.machineintegration.MiCommonResponse;
import com.wxzd.catl.CatlWebServiceFunction;
import com.wxzd.catl.base.CatlWebServiceConfig;
import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.efcs.alarm.domain.events.SystemErrorEvent;
import com.wxzd.efcs.business.application.dtos.PalletizeDto;
import com.wxzd.efcs.business.application.exception.DataNotFoundException;
import com.wxzd.efcs.business.application.service.ManualProcedureAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.application.workProcedures.IProcedure;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureService;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryIn;
import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.*;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.event.exception.EventException;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.protocol.wcs.domain.enums.PositionType;
import com.wxzd.protocol.wcs.domain.exception.DeviceNoIncorrectException;
import com.wxzd.wms.core.SerialNoGenerator;
import com.wxzd.wms.core.application.dtos.ContainerDto;
import com.wxzd.wms.core.domain.entities.*;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.core.domain.service.ContainerService;
import com.wxzd.wms.core.domain.service.ContainerTypeService;
import com.wxzd.wms.core.domain.service.StorageService;
import com.wxzd.wms.core.domain.service.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/11.
 */
@Service
public class ManualProcedureAppServiceImpl implements ManualProcedureAppService {
    @Autowired
    WareHouseService wareHouseService;

    @Autowired
    private FmPalletizeService fmPalletizeService;

    @Autowired
    private FmPalletSplitService fmPalletSplitService;

    @Autowired
    private FmProcedureService fmProcedureService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private LocationAllotQyery locationAllotQyery;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PalletDispatchService palletDispatchService;

    @Autowired
    private ContainerTypeService containerTypeService;

    @Autowired
    private FmBatteryInService fmBatteryInService;

    @Autowired

    private BatteryInfoService batteryInfoService;

    @Autowired
    private WorkProcedureService workProcedureService;

    @Override
    public GaiaResult clearCatchLocationStorage(String houseNo, String location, List<String> containerNos) {
        if (containerNos == null || containerNos.size() == 0) {
            return GaiaResultFactory.getError("请选择要移出缓存位的托盘");
        }
        if (location.equals("1010") || location.equals("3011") || location.equals("3121")) {
            Warehouse warehouse = wareHouseService.getByNo(houseNo);
            List<Storage> storages = locationAllotQyery.getEquipLocationStorage(warehouse.getId(), location);
            List<StorageLocation> locations = locationAllotQyery.getEquipLocation(warehouse.getId(), DispatcherConfig.manual_temp_location);
            for (Storage storage : storages) {
                if (storage.getSto_type() == StorageType.container) {
                    if (containerNos.contains(storage.getPallet_no())) {
                        storageService.storageTransfer(warehouse.getId(), locations.get(0).getId(), storage.getPallet_no(), "", "");
                    }
                }
            }
            return GaiaResultFactory.getSuccess();

        } else {
            return GaiaResultFactory.getError("只可以操作空托缓存位");
        }

    }

    @Override
    public synchronized GaiaResult checkProcedure(String houseNo, String palletNo, WorkProcedure workProcedure) {

        Container pallet = containerService.getContainerByBarcode(palletNo);
        //        if(StringUtl.isEmpty(houseNo)){
        //            Warehouse warehouse =wareHouseService.getById()
        //        }
        if (pallet == null) {
            return GaiaResultFactory.getError("托盘信息不存在，请先添加托盘！");
        }

        switch (workProcedure) {
            case Formation_Palletize:
            case Test_Palletize:
            case Formation_Rework_Palletize:
                return GaiaResultFactory.getError("请直接选择组完盘后要执行的工序");
            case FORMATION_ERROR_EXPORT:
            case TEST_ERROR_EXPORT:
            case Test_PalletMove:
            case Formation_PalletMove:
                return GaiaResultFactory.getError("该工序无法做组盘操作");
            case High_Temperature:
            case Formation:
            case Formation_Rework:
            case Normal_Temperature_1:
            case Test_OCV_1:
            case Normal_Temperature_2:
            case Formation_Split:
            case Test_OCV_2:
                //                List<PalletDetail> details = palletDispatchService.getPalletInnerDetail(palletNo);
                //                if (details.size() == DispatcherConfig.pallet_battery_count)
                //                    return GaiaResultFactory.getError("托盘为满盘托盘，无需手工操作！");
                return GaiaResultFactory.getSuccess();

            case Test_Pallet_Split:
                return GaiaResultFactory.getSuccess();

            default:
                return GaiaResultFactory.getError("此工序不允许人工操作！");

        }
        //        if (workProcedure == WorkProcedure.Formation_Palletize
        //                || workProcedure == WorkProcedure.Test_Palletize
        //                || workProcedure == WorkProcedure.Formation_Rework_Palletize) {
        //            if (details.size() < DispatcherConfig.pallet_battery_count) {
        //                return GaiaResultFactory.getSuccess();
        //            }
        //            if (details.size() > DispatcherConfig.pallet_battery_count) {
        //                return GaiaResultFactory.getError("电池数量异常，大于最大容量！请联系系统管理员");
        //            } else {
        //                return GaiaResultFactory.getError("托盘电池已满，无法进行组盘操作，如需入线请选择工序变更！");
        //            }
        //        }
        //        if (workProcedure == WorkProcedure.Formation_Split || workProcedure == WorkProcedure.Test_Pallet_Split) {
        //            if (details.size() == 0)
        //                return GaiaResultFactory.getError("托盘为空，无法进行拆盘操作！");
        //            else
        //                return GaiaResultFactory.getSuccess();
        //        }
        //        else {
        //            Boolean isOk = details.size() == DispatcherConfig.pallet_battery_count;
        //            if (isOk) {
        //                return GaiaResultFactory.getSuccess();
        //            } else {
        //                return GaiaResultFactory.getError("托盘不满盘，无法转换工序！");
        //            }
        //        }

    }

    /**
     * 组盘/拆盘确操作
     *
     * @param houseNo
     * @param palletNo
     * @param workProcedure
     * @return
     */
    @Transaction
    @Override
    public GaiaResult palletFinish(String houseNo, String palletNo, WorkProcedure workProcedure) {
        List<PalletDetail> details = palletDispatchService.getActivePalletDetailByPalletWithStatus(palletNo);

        if (workProcedure == WorkProcedure.Formation_Palletize || workProcedure == WorkProcedure.Test_Palletize || workProcedure == WorkProcedure.Formation_Rework_Palletize) {
            if (details.size() < DispatcherConfig.pallet_battery_count) {
                return GaiaResultFactory.getSuccess("需要新增假电池");
            }
            if (details.size() > DispatcherConfig.pallet_battery_count) {
                return GaiaResultFactory.getError("电池数量异常，大于最大容量！请联系系统管理员");
            } else {
                return GaiaResultFactory.getSuccess();
            }
        }
        if (workProcedure == WorkProcedure.Formation_Split || workProcedure == WorkProcedure.Test_Pallet_Split) {
            if (details.size() == 0)
                return GaiaResultFactory.getError("托盘为空，无法进行拆盘操作！");
            else {

                String msg = "";
                for (PalletDetail detail : details) {
                    if (StringUtl.isEmpty(detail.getTo_pos_no())) {
                        GaiaResult result = this.removePalletizeBattery(detail, workProcedure);
                        if (result.isSuccess()) {
                            detail.setTo_pos_no("人工");
                            detail.setTo_pos_channel_no("0");
                            detail.setTo_pos_type(com.wxzd.efcs.business.domain.enums.PositionType.Manual);
                            palletDispatchService.savePalletInnerDetail(detail);
                        } else {
                            msg = msg + result.getMessage() + ";";
                            //                        return GaiaResultFactory.getError("拆盘未完成：" + detail.getBattery_barcode() + result.getMessage());
                        }
                    }
                }

                if (msg.equals("")) {
                    FmPalletSplit procedureByPallet = fmPalletSplitService.getProcedureByPallet(palletNo, workProcedure);
                    procedureByPallet.setFm_status(FmStatus.Finished);
                    procedureByPallet.setCreate_mode(FmCreateMode.Manual);
                    procedureByPallet.setPalletize_status(PalletizeStatus.Finished);
                    procedureByPallet.setProc_complete_time(new Date());
                    return GaiaResultFactory.getSuccess();
                } else {
                    return GaiaResultFactory.getError("拆盘未完成：" + msg);
                }
            }
        } else {
            return GaiaResultFactory.getSuccess();
        }
    }

    /**
     * 工序转为人工工序
     *
     * @param houseNo
     * @param palletNo
     * @param workProcedure
     * @return
     * @throws EventException
     */
    @Transaction
    @Override
    public GaiaResult setPalletToManual(String houseNo, String palletNo, WorkProcedure workProcedure) throws EventException {

        if (workProcedure == null) {
            return GaiaResultFactory.getError("工序信息异常");
        }

        // TODO 暂时不加校验，库里可能有使用非本拉托盘，用了工序调整不了。过段时间 可放开
//        if (!palletNo.startsWith(DispatcherConfig.pallet_no_profix)) {
//            // 不是当前拉线的托盘号，序号异常排出
//            String errorMsg = "托盘条码前缀不符合校验标准，托盘不是当前拉线允许使用托盘。托盘号码：" + palletNo;
//            return GaiaResultFactory.getError(errorMsg);
//        }

        Container pallet = containerService.getContainerByBarcode(palletNo);
        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        List<PalletDetail> details = palletDispatchService.getPalletInnerDetail(palletNo);

        if (dispatch != null && dispatch.getWork_procedure() == workProcedure) {
            switch (workProcedure) {
                case Formation_Palletize:
                case Test_Palletize:
                case Formation_Rework_Palletize:
                    //                    return GaiaResultFactory.getError("请直接选择组完盘后要执行的工序");
                case FORMATION_ERROR_EXPORT:
                case TEST_ERROR_EXPORT:
                case Test_PalletMove:
                case Formation_PalletMove:
                    return GaiaResultFactory.getSuccess();
                //                    return GaiaResultFactory.getError("该工序无法转换工序");
                case High_Temperature:
                case Formation:
                case Formation_Rework:
                case Normal_Temperature_1:
                case Test_OCV_1:
                case Normal_Temperature_2:
                case Test_OCV_2:
                    //                    if (details.size() == DispatcherConfig.pallet_battery_count)
                    //                        return GaiaResultFactory.getError("托盘为满盘托盘，无需手工操作！");
                    FmProcedure procedure = fmProcedureService.getProcedureByPallet(palletNo, workProcedure);
                    if (procedure != null) {
                        procedure.setCreate_mode(FmCreateMode.Manual);
                        fmProcedureService.saveFmProcedure(procedure);
                    } else {
                        DefaultProcedure procedure2 = new DefaultProcedure(houseNo, dispatch.getCurrent_pos(), FmCreateMode.Manual);
                        procedure2.setPallet_no(palletNo);
                        procedure2.setPalletDispatch(dispatch);
//				List<Storage> storages = locationAllotQyery.getStorageByPallet(palletNo);
//				if(storages.size()>0)//
//					storageService.setPalletIsEmpty(palletNo, false);
//                }
                        this.startNewProcedure(workProcedure, procedure2, "人工切换工序");
                    }
                    return GaiaResultFactory.getSuccess();

                case Formation_Split:
                    //                    if (details.size() == DispatcherConfig.pallet_battery_count)
                    //                        return GaiaResultFactory.getError("托盘为满盘托盘，无需手工操作！");
                    return GaiaResultFactory.getSuccess();
                case Test_Pallet_Split:
                    return GaiaResultFactory.getSuccess();
                default:
                    return GaiaResultFactory.getError("此工序不允许人工操作！");
            }
        } else {
            Warehouse warehouse = wareHouseService.getByNo(houseNo);
            List<StorageLocation> equipLocation = new ArrayList<>();
            if (workProcedure == WorkProcedure.Formation_PalletMove) {
                equipLocation = locationAllotQyery.getEquipLocation(warehouse.getId(), DispatcherConfig.formation_empty_pallet_storage_location_no);
            } else if (workProcedure == WorkProcedure.Test_PalletMove) {
                equipLocation = locationAllotQyery.getEquipLocation(warehouse.getId(), DispatcherConfig.test_empty_pallet_storage_location_no);

            } else {
                equipLocation = locationAllotQyery.getEquipLocation(warehouse.getId(), DispatcherConfig.manual_temp_location);

            }

            if (equipLocation.size() > 0) {
                if (pallet == null) {
                    ContainerType palletType = containerTypeService.getByName(DispatcherConfig.new_pallet_type_names.get(houseNo));

                    if (palletType == null) {
                        SystemErrorEvent noPalletTypeEvent = new SystemErrorEvent();
                        noPalletTypeEvent.setHouseNo(houseNo);
                        noPalletTypeEvent.setDeviceNo(palletNo);
                        noPalletTypeEvent.setLocation("人工操作");
                        noPalletTypeEvent.setPalletNo(palletNo);
                        noPalletTypeEvent.setContent("无默认的托盘类型，请联系管理员！");
                        noPalletTypeEvent.setSource(this);

                        ApplicationEventPublisher.trigger(noPalletTypeEvent);
                        throw new DataNotFoundException("无默认托盘类型配置参数");
                    }

                    // 新增加一个托盘，按照系统配置的托盘参数添加
                    ContainerDto p = new ContainerDto();
                    p.setContainer_barcode(palletNo);
                    p.setType_id(palletType.getId());
                    p.setType_name(palletType.getContainer_name());
                    GaiaResult result = containerService.addOrUpdateContainer(p);

                    if (!result.isSuccess()) {
                        throw new DataNotFoundException("无法创建新托盘");
                    }
                    // 将托盘放入到人工专用库位
                    //  (UUID houseId,UUID locationId, String palletNo,String businessType, String businessNo, String processId)

                    // 重新获取托盘信息
                    pallet = containerService.getContainerByBarcode(palletNo);
                }
                List<Storage> storage = locationAllotQyery.getStorageByPallet(palletNo);
                if (storage.size() > 0) {
                    storageService.storageTransfer(warehouse.getId(), equipLocation.get(0).getId(), pallet.getContainer_barcode(), "", workProcedure.toString());
                } else {
                    storageService.palletInStorage(warehouse.getId(), equipLocation.get(0).getId(), pallet.getContainer_barcode(), workProcedure.toString(), "", "");
                }

                if (dispatch == null) {
                    dispatch = new PalletDispatch();
                    dispatch.setHouse_id(warehouse.getId());
                    dispatch.setPallet_status(PalletStatus.In_Waiting);
                    dispatch.setDispatch_status(PalletDispatchStatus.Dispatching);
                    dispatch.setContainer_id(pallet.getId().toString());
                    dispatch.setContainer_no(pallet.getContainer_barcode());
                    dispatch.setIs_empty(true);
                    dispatch.setWork_procedure(workProcedure);
                    palletDispatchService.newPalletDispatch(dispatch);
                }
                // 如果当前托盘没有工序，则将托盘标识为异常，执行异常工序
                if (dispatch.getWork_procedure() != null && dispatch.getWork_procedure() != workProcedure) {
                    //结束掉之前的工序
                    workProcedureService.errorFinishProcedure(dispatch.getWork_procedure(), palletNo, "工序人工调整");
                    if (workProcedure == WorkProcedure.Formation_PalletMove || workProcedure == WorkProcedure.Test_PalletMove) {

                        if (dispatch.getWork_procedure() == WorkProcedure.TEST_ERROR_EXPORT || dispatch.getWork_procedure() == WorkProcedure.FORMATION_ERROR_EXPORT
                                || dispatch.getWork_procedure() == WorkProcedure.Formation_Split || dispatch.getWork_procedure() == WorkProcedure.Test_Pallet_Split
                                || dispatch.getWork_procedure() == WorkProcedure.Formation_PalletMove || dispatch.getWork_procedure() == WorkProcedure.Test_PalletMove) {

                            dispatch.setDispatch_status(PalletDispatchStatus.Finished);
                            palletDispatchService.newPalletDispatch(dispatch);

                            dispatch = new PalletDispatch();
                            dispatch.setHouse_id(warehouse.getId());
                            dispatch.setPallet_status(PalletStatus.In_Waiting);
                            dispatch.setDispatch_status(PalletDispatchStatus.Dispatching);
                            dispatch.setContainer_id(pallet.getId().toString());
                            dispatch.setContainer_no(pallet.getContainer_barcode());
                            dispatch.setIs_empty(true);
                            dispatch.setWork_procedure(workProcedure);
//						dispatch.setCurrent_pos("1230");
                            palletDispatchService.newPalletDispatch(dispatch);
                            //清除遗留库存
                            List<Storage> storages = locationAllotQyery.getStorageByPallet(palletNo);
                            if (storages.size() > 1) {
                                List<Storage> outList = new ArrayList<>();
                                for (Storage st : storages) {
                                    if (st.getSto_type() == StorageType.sku) {
                                        outList.add(st);
                                    }
                                }
                                Storage fromContainer = new Storage(storages.get(0).getLocation_id(), "", StorageType.container);
                                fromContainer.setHouse_id(warehouse.getId());
                                storageService.storageOut(fromContainer, StorageType.sku, outList, WorkProcedure.Formation_Out.toString(), null, null);
                            }
                            storageService.setPalletIsEmpty(palletNo, true);
                        } else {
                            return GaiaResultFactory.getError("业务单据不允许执行空托注册，请认真核实托盘号");

                        }
                    } else {
                        List<Storage> storages = locationAllotQyery.getStorageByPallet(palletNo);
                        if (storages.size() > 1)//
                            storageService.setPalletIsEmpty(palletNo, false);

                    }
                }
                DefaultProcedure procedure = new DefaultProcedure(houseNo, dispatch.getCurrent_pos(), FmCreateMode.Manual);
                procedure.setPallet_no(palletNo);
                procedure.setWarehouse(warehouse);
                procedure.setStorageLocation(equipLocation.get(0));
                procedure.setPalletDispatch(dispatch);

//				List<Storage> storages = locationAllotQyery.getStorageByPallet(palletNo);
//				if(storages.size()>0)// 
//					storageService.setPalletIsEmpty(palletNo, false);
//                }
                this.startNewProcedure(workProcedure, procedure, "人工切换工序");

                return GaiaResultFactory.getSuccess();
            } else {
                return GaiaResultFactory.getError("虚拟库位未设置无法执行工序切换");
            }

        }
    }

    /**
     * 人工组盘
     *
     * @param workProcedure
     * @param palletizeDto
     * @return
     */
    @Transaction
    @Override
    public GaiaResult manualPalletize(WorkProcedure workProcedure, PalletizeDto palletizeDto) throws DeviceNoIncorrectException {
        if (!palletizeDto.getPalletNo().startsWith(DispatcherConfig.pallet_no_profix)) {
            // 不是当前拉线的托盘号，序号异常排出
            String errorMsg = "托盘条码前缀不符合校验标准，托盘不是当前拉线允许使用托盘。托盘号码：" + palletizeDto.getPalletNo();
            return GaiaResultFactory.getError(errorMsg);
        }
        if (!palletizeDto.getBattery_barcode().startsWith(DispatcherConfig.batteryProfixCheck.get(palletizeDto.getHouseNo()))){
            return GaiaResultFactory.getError("电池绑定托盘失败,电池类型不匹配，要求类型:"+DispatcherConfig.batteryProfixCheck.get(palletizeDto.getHouseNo()));
        }
         WorkProcedure palletizeProcedure = null;
        String formNo = getProcedureFormNo(workProcedure, palletizeDto.getPalletNo());
        switch (workProcedure) {
            case Formation_Palletize:
            case Test_Palletize:
            case Formation_Rework_Palletize:
                return GaiaResultFactory.getError("请直接选择组盘后要执行的工序！");
            case High_Temperature:
            case Formation:
                palletizeProcedure = WorkProcedure.Formation_Palletize;

                break;
            case Formation_Rework:
                palletizeProcedure = WorkProcedure.Formation_Rework_Palletize;
                break;
            case Normal_Temperature_1:
            case Test_OCV_1:
            case Normal_Temperature_2:
            case Test_OCV_2:
                palletizeProcedure = WorkProcedure.Test_Palletize;
                break;
            case FORMATION_ERROR_EXPORT:
            case TEST_ERROR_EXPORT:
            case Formation_Split:

                palletizeProcedure = WorkProcedure.Formation_Palletize;
                break;
            case Test_Pallet_Split:
            case Test_PalletMove:
            case Formation_PalletMove:
                return GaiaResultFactory.getError("此工序不允许人工操作！");

            default:
                return GaiaResultFactory.getError("此工序不允许人工操作！");
        }

        String locationNo = DispatcherConfig.manual_temp_location;
        Warehouse warehouse = wareHouseService.getByNo(palletizeDto.getHouseNo());
        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletizeDto.getPalletNo());
        //        Warehouse warehouse = wareHouseService.getByType(palletizeDto.getHouseNo());
        List<StorageLocation> location = locationAllotQyery.getEquipLocation(warehouse.getId(), locationNo);
        if (location.size() == 0) {
            return GaiaResultFactory.getError(locationNo + "虚拟库位未配置");
        }
        //TODO 临时这样做，后面页面完善后 可以去掉该逻辑
        //begin
        PalletDetail checkDetail = palletDispatchService.getPalletInnerDetail(palletizeDto.getPalletNo(), palletizeDto.getChannel_no());
        if (checkDetail != null) {
            removePalletizeBattery(palletizeDto.getPalletNo(), checkDetail.getBattery_barcode());
        }

        //end
        //        Storage storage = storageService.getStorageByBarcode(palletizeDto.getBattery_barcode());
        //        if (storage != null) {
        //            return GaiaResultFactory.getError("电池已绑定到其他位置，请先解绑在做操作");
        //        } else {
//		try {

        //MiCommonResponse response = null;
        String mesOperation;
        if (WorkProcedure.Formation_Palletize == palletizeProcedure || WorkProcedure.Formation_Rework_Palletize == palletizeProcedure) {
            mesOperation = CatlWebServiceConfig.getHighTempOpertion();
            //response = CatlWebServiceFunction.miCheckSFCStatusEx_highTemp(mesOperation, palletizeDto.getBattery_barcode());
        } else if (WorkProcedure.Test_Palletize == palletizeProcedure) {
            mesOperation = CatlWebServiceConfig.getNormalTempOpertion();
            //response = CatlWebServiceFunction.miCheckSFCStatusEx_normalTemp(mesOperation, palletizeDto.getBattery_barcode());
        }

        //if (response != null && response.getCode() == 0) {
        try {
            MiCommonResponse miCommonResponse = null;
            if (WorkProcedure.Formation_Palletize == palletizeProcedure || WorkProcedure.Formation_Rework_Palletize == palletizeProcedure) {
                miCommonResponse = CatlWebServiceFunction.miBindSFCintoTray_highTemp(palletizeDto.getPalletNo(), palletizeDto.getBattery_barcode(), palletizeDto.getChannel_no());
            } else if (WorkProcedure.Test_Palletize == palletizeProcedure) {
                miCommonResponse = CatlWebServiceFunction.miBindSFCintoTray_normalTemp(palletizeDto.getPalletNo(), palletizeDto.getBattery_barcode(), palletizeDto.getChannel_no());
            }

            if (miCommonResponse != null && miCommonResponse.getCode() == 0) {

                BatteryInContainer batteryInContainer = new BatteryInContainer();
                batteryInContainer.setBattery_status(DispatcherConfig.battery_default_ok);
                batteryInContainer.setPos_channel_no(palletizeDto.getChannel_no());
                batteryInContainer.setPos_no("人工");
                batteryInContainer.setPos_no("Manual");
                batteryInContainer.setBattery_no(palletizeDto.getBattery_barcode());
                batteryInContainer.setPos_type(PositionType.Pallet);
                addBatteryToBallet(warehouse.getId(), palletizeDto.getHouseNo(), location.get(0).getId(), palletizeDto.getPalletNo(), batteryInContainer);
                PalletDetail detail = new PalletDetail(ProcedureConfig.getHouseSkuId(palletizeDto.getHouseNo()), palletDispatch.getId(), palletizeDto.getChannel_no().toString(), formNo,
                        palletizeDto.getBattery_barcode(), DispatcherConfig.battery_default_ok, palletizeDto.getPalletNo());
                detail.setFromPos("", "", "", palletizeDto.getChannel_no().toString(), com.wxzd.efcs.business.domain.enums.PositionType.Pallet);
                palletDispatchService.savePalletInnerDetail(detail);
                storageService.setPalletIsEmpty(palletizeDto.getPalletNo(), false);
                palletDispatch.setIs_empty(false);
                palletDispatchService.newPalletDispatch(palletDispatch);
                return GaiaResultFactory.getSuccess();
            } else {
                DatabaseExecuter.setRollbackOnly();
                return GaiaResultFactory.getError("电池绑定托盘失败，错误信息:(MES)" + miCommonResponse.getMessage());
            }
        } catch (Exception ex) {
            DatabaseExecuter.setRollbackOnly();
            return GaiaResultFactory.getError("电池绑定托盘失败！");
        }
//			} else {
//				return GaiaResultFactory.getError("电池校验失败，错误信息:(MES)" + response.getMessage());
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return GaiaResultFactory.getError("电池信息MES校验失败");
//		}
        //        }

    }

    /**
     * 新增假电池
     *
     * @param workProcedure
     * @param palletizeDto
     * @return
     */
    public GaiaResult addFeekBatteryToBallet(WorkProcedure workProcedure, PalletizeDto palletizeDto) {
        String locationNo = DispatcherConfig.manual_temp_location;
        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletizeDto.getPalletNo());

        if (palletizeDto.getHouseId() == null) {
            Warehouse warehouse = wareHouseService.getByNo(palletizeDto.getHouseNo());
            palletizeDto.setHouseId(warehouse.getId());
        }
        PalletDetail detail = new PalletDetail(ProcedureConfig.getHouseSkuId(palletizeDto.getHouseNo()), palletDispatch.getId(), palletizeDto.getChannel_no().toString(), "",
                palletizeDto.getBattery_barcode(), DispatcherConfig.battery_default_ok, palletizeDto.getPalletNo());
        detail.setFromPos("", "", "", palletizeDto.getChannel_no().toString(), com.wxzd.efcs.business.domain.enums.PositionType.Pallet);
        palletDispatchService.savePalletInnerDetail(detail);
        Storage storage = storageService.getStorageByBarcode(palletizeDto.getBattery_barcode());
        List<StorageLocation> location = locationAllotQyery.getEquipLocation(palletizeDto.getHouseId(), locationNo);
        if (location.size() == 0) {
            return GaiaResultFactory.getError(locationNo + "虚拟库位未配置");
        }
        if (storage != null) {
            return GaiaResultFactory.getError("电池已绑定到其他位置，请先解绑在做操作");
        } else {

            List<BatteryInContainer> batteryInContainers = new ArrayList<>();
            BatteryInContainer batteryInContainer = new BatteryInContainer();
            batteryInContainer.setBattery_status(DispatcherConfig.battery_default_fake);
            batteryInContainer.setPos_channel_no(palletizeDto.getChannel_no());
            batteryInContainer.setPos_no("人工");
            batteryInContainer.setPos_no("Manual");
            batteryInContainer.setBattery_no(palletizeDto.getBattery_barcode());
            batteryInContainer.setPos_type(PositionType.Pallet);
            batteryInContainers.add(batteryInContainer);
            return addBatteryToBallet(palletizeDto.getHouseId(), palletizeDto.getHouseNo(), location.get(0).getId(), palletizeDto.getPalletNo(), batteryInContainers, true);
        }
    }

    /**
     * 设定工序
     *
     * @param houseNo
     * @param palletNo
     * @param newWorkProcedure
     * @return
     */
    @Transaction
    @Override
    public GaiaResult setProcedure(String houseNo, String palletNo, WorkProcedure newWorkProcedure) {
        //        Warehouse warehouse = wareHouseService.getByType(houseNo);
        //        if (warehouse == null) {
        //            return GaiaResultFactory.getError(houseNo + "--库编号不存在");
        //        }
        //        Container pallet = containerService.getContainerByBarcode(palletNo);
        //
        //
        //        if (newWorkProcedure == WorkProcedure.Formation_Palletize
        //                || newWorkProcedure == WorkProcedure.Test_Palletize
        //                || newWorkProcedure == WorkProcedure.Formation_Rework_Palletize) {
        //            return GaiaResultFactory.getError("组盘工序请切换为空托流转，投入口投入!");
        //        }
        //        if (newWorkProcedure == WorkProcedure.Formation_In || newWorkProcedure == WorkProcedure.Formation_Out ||
        //                newWorkProcedure == WorkProcedure.Test_In || newWorkProcedure == WorkProcedure.Test_Out
        //                ) {
        //            return GaiaResultFactory.getError("不能将工序切换为出/入库工序");
        //        }
        //        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        //        if (dispatch != null && dispatch.getWork_procedure() != newWorkProcedure) {
        //            if (!(dispatch.getWork_procedure() == WorkProcedure.Manual ||
        //                    dispatch.getWork_procedure() == WorkProcedure.TEST_ERROR_EXPORT
        //                    || dispatch.getWork_procedure() == WorkProcedure.FORMATION_ERROR_EXPORT)) {
        //                return GaiaResultFactory.getError("不允许直接切换工序，请先排出托盘，确认后再更改入线");
        //            }
        //        }
        //        DefaultProcedure procedure = new DefaultProcedure(houseNo, "人工组盘", FmCreateMode.System);
        //        procedure.setPallet_no(palletNo);
        //        procedure.setPalletDispatch(dispatch);
        //        procedure.setWarehouse(warehouse);
        //        workProcedureService.initProcedureCurrenPosImproper(newWorkProcedure, procedure);
        //
        //        PalletDispatch newDispatch = new PalletDispatch();
        //        newDispatch.setHouse_id(warehouse.getId());
        //        newDispatch.setPallet_status(PalletStatus.In_Waiting);
        //        newDispatch.setDispatch_status(PalletDispatchStatus.Dispatching);
        //        newDispatch.setContainer_id(pallet.getId().toString());
        //        newDispatch.setChannel_policy(PalletChannelPolicy.N);
        //        newDispatch.setContainer_no(pallet.getContainer_barcode());
        //        newDispatch.setIs_empty(true);
        //
        //        newDispatch.setWork_procedure(newWorkProcedure);
        //        UUID disId = palletDispatchService.newPalletDispatch(newDispatch);
        //
        //
        //        if (newWorkProcedure == WorkProcedure.FORMATION_ERROR_EXPORT || newWorkProcedure == WorkProcedure.TEST_ERROR_EXPORT) {
        //            return GaiaResultFactory.getSuccess();
        //        }
        //        if (newWorkProcedure == WorkProcedure.Test_Pallet_Split || newWorkProcedure == WorkProcedure.Formation_Split) {
        //            FmPalletSplit procedure = fmPalletSplitService.getProcedureByPallet(palletNo, newWorkProcedure);
        //            if (procedure == null) {
        //                procedure = new FmPalletSplit();
        //                procedure.newProcedure(newWorkProcedure, palletNo, disId, "人工组盘", ProcedureConfig.PalletChannel_Policy);
        //            }
        //            procedure.setHouse_id(warehouse.getId());
        //            procedure.setHouse_no(warehouse.getHouse_no());
        //            procedure.setPallet_no(palletNo);
        //            //初始化 拆盘工序单
        //            fmPalletSplitService.saveFmPalletSplit(procedure);
        //            dispatch.setCurrent_form_no(procedure.getForm_no());
        //            palletDispatchService.newPalletDispatch(newDispatch);
        //        } else {
        //            FmProcedure procedure = fmProcedureService.getProcedureByPallet(palletNo, newWorkProcedure);
        //            if (procedure == null) {
        //                //化成表单初始化
        //                procedure = new FmProcedure();
        //                procedure.newProcedure(newWorkProcedure, palletNo, disId, "手工单");
        //            }
        //            //初始化 高温工序单
        //            procedure.setHouse_id(warehouse.getId());
        //            procedure.setHouse_no(warehouse.getHouse_no());
        //            fmProcedureService.saveFmProcedure(procedure);
        //            dispatch.setCurrent_form_no(procedure.getForm_no());
        //            palletDispatchService.newPalletDispatch(newDispatch);
        //        }
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 移除组盘电池
     *
     * @param palletNo
     * @param batteryNo
     * @return
     */
    @Transaction
    @Override
    public GaiaResult removePalletizeBattery(String palletNo, String batteryNo) {
        PalletDetail detail = palletDispatchService.getActivePalletDetailByPalletWithStatus(palletNo, batteryNo);
        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        GaiaResult result = removePalletizeBattery(detail, dispatch.getWork_procedure());

        if (result.isSuccess()) {
            palletDispatchService.deleteDetailHard(detail.getId());
            //            return GaiaResultFactory.getSuccess();
        }
        return result;
    }

    /**
     * 移除组盘电池
     *
     * @return
     */
    private GaiaResult removePalletizeBattery(PalletDetail palletDetail, WorkProcedure workProcedure) {
        try {
            Storage storage;
            if (!StringUtl.isEmpty(palletDetail.getBattery_barcode())) {
                storage = storageService.getStorageByBarcode(palletDetail.getBattery_barcode());
            } else {
                return GaiaResultFactory.getError("条码:" + palletDetail.getBattery_barcode() + " 不存在！");
            }
            if (storage != null) {
                if (storage.getPallet_no().equals(palletDetail.getPallet_no())) {

                    //            throw new RuntimeException("条码:" + palletDetail.getBattery_barcode() + " 库存不存在！");
                    List<Storage> storages = new ArrayList<>();
                    storages.add(storage);
                    Storage fromContainer = new Storage(storage.getLocation_id(), "", StorageType.container);
                    fromContainer.setHouse_id(storage.getHouse_id());
                    storageService.storageOut(fromContainer, StorageType.sku, storages, WorkProcedure.Formation_Out.toString(), null, null);
                }
            }

            if (palletDetail.getBattery_status() != "-2") {

                WorkProcedure mesSplitWrokprocedure = null;
                switch (workProcedure) {
                    case Formation:
                    case Formation_Rework:
                    case Formation_Split:
                    case Palletize_Cache:
                    case Formation_Rework_Palletize:
                    case Formation_Palletize:
                    case Formation_PalletMove:
                    case High_Temperature:
                        mesSplitWrokprocedure = WorkProcedure.Formation_Split;
                        break;
                    case Test_OCV_1:
                    case Test_OCV_2:
                    case Test_Palletize:
                    case Test_PalletMove:
                    case Normal_Temperature_1:
                    case Normal_Temperature_2:
                    case Test_Pallet_Split:
                        mesSplitWrokprocedure = WorkProcedure.Test_Pallet_Split;
                        break;
                }

                if (mesSplitWrokprocedure == null) {
                    return GaiaResultFactory.getError("");
                }
                try {
                    SfcRemovalResponse sfcRemovalResponse = null;
                    if (WorkProcedure.Formation_Split == mesSplitWrokprocedure) {
                        sfcRemovalResponse = CatlWebServiceFunction.releaseCell_high(palletDetail.getPallet_no(), palletDetail.getBattery_barcode());
                    } else if (WorkProcedure.Test_Pallet_Split == mesSplitWrokprocedure) {
                        sfcRemovalResponse = CatlWebServiceFunction.releaseCell_normal(palletDetail.getPallet_no(), palletDetail.getBattery_barcode());
                    }
                    if (sfcRemovalResponse.getCode().equals(0) || sfcRemovalResponse.getCode().equals(20943)) {
                        return GaiaResultFactory.getSuccess();
                    } else {

//					DatabaseExecuter.setRollbackOnly();
                        return GaiaResultFactory.getError("MES 电池解绑异常:" + sfcRemovalResponse.getMessage());
                    }
                } catch (Exception e) {
                    return GaiaResultFactory.getError("MES 电池解绑异常:" + e.getMessage());
                }
            }
        } catch (Exception ex) {
            return GaiaResultFactory.getError("MES 电池解绑异常:" + ex.getMessage());
        }
        //        palletDispatchService.deleteDetailHard(palletDetail.getId());
        return GaiaResultFactory.getSuccess();

        //        return GaiaResultFactory.getSuccess();
    }

    /**
     * 托盘上新增电池
     *
     * @param houseId
     * @param houseNo
     * @param locationId
     * @param palletNo
     * @param batteryInContainer
     * @return
     */
    private GaiaResult addBatteryToBallet(UUID houseId, String houseNo, UUID locationId, String palletNo, BatteryInContainer batteryInContainer) {
        List<BatteryInContainer> batteryInContainers = new ArrayList<>();
        batteryInContainers.add(batteryInContainer);
        return addBatteryToBallet(houseId, houseNo, locationId, palletNo, batteryInContainers, false);
    }

    /**
     * 新增电池
     *
     * @param houseId
     * @param houseNo
     * @param locationId
     * @param palletNo
     * @param batteryInContainers
     * @param isFeek
     * @return
     */
    private GaiaResult addBatteryToBallet(UUID houseId, String houseNo, UUID locationId, String palletNo, List<BatteryInContainer> batteryInContainers, Boolean isFeek) {
        List<Storage> needAdd = ConvertFactory.convertToStorage(houseId, locationId, palletNo, "", houseNo, batteryInContainers);

        //TODO  出库 测试代码  测试完成删除  important
        //——————————————————测试代码——————————————————————————
        for (BatteryInContainer s : batteryInContainers) {
            if (s.getBattery_no() != null) {
                try {
                    Storage storage = storageService.getStorageByBarcode(s.getBattery_no());
                    if (storage != null) {
                        List<Storage> storages = new ArrayList<>();
                        storages.add(storage);
                        Storage fromContainer = new Storage(storage.getLocation_id(), "", StorageType.container);
                        fromContainer.setHouse_id(houseId);
                        storageService.storageOut(fromContainer, StorageType.sku, storages, WorkProcedure.Formation_Out.toString(), null, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }
        //---------------------------------------------------------------------------------------------

        //获取设备库位信息
        Storage toContainer = new Storage(locationId, palletNo, StorageType.container);
        storageService.storageIn(toContainer, StorageType.sku, needAdd, WorkProcedure.Manual.toString(), null, null);

        //获取设备库位信息
        //        Storage toContainer = new Storage(locationId, palletNo, StorageType.container);
        //        storageService.storageIn(toContainer, StorageType.sku, needAdd, WorkProcedure.Manual.toString(), null, null);
        List<FmBatteryIn> fmBatteryInList = new ArrayList<>();
        for (BatteryInContainer battery : batteryInContainers) {
            FmBatteryIn fmBatteryIn = new FmBatteryIn();
            fmBatteryIn.setSku_id(ProcedureConfig.getHouseSkuId(houseNo));
            fmBatteryIn.setWork_procedure(WorkProcedure.Manual);
            fmBatteryIn.setBattery_barcode(battery.getBattery_no());
            //TODO linePos
            fmBatteryIn.setLine_pos("Manual");
            fmBatteryIn.setLine_channel_no(String.valueOf(battery.getPos_channel_no()));
            fmBatteryIn.setCreate_mode(FmCreateMode.System);
            fmBatteryIn.setOperate_datetime(new Date());
            fmBatteryIn.setEquip_no("人工");
            fmBatteryIn.setHouse_id(houseId);
            fmBatteryIn.setHouse_no(houseNo);
            fmBatteryIn.setFm_status(FmStatus.Finished);

            fmBatteryInList.add(fmBatteryIn);
        }
        //生成入库单
        fmBatteryInService.save(fmBatteryInList);
        //        if (!isFeek) {
        //保存电池信息
        List<BatteryInfo> batteryInfos = ConvertFactory.ConvertToBatteryInfo(batteryInContainers, houseNo);
        batteryInfoService.saveBatteryInfo(batteryInfos);
        //        }
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 完善IProcedure 库信息 和 库位信息并执行工艺
     *
     * @param defaultProcedure
     * @return
     */
    private GaiaResult startNewProcedure(WorkProcedure workProcedure, DefaultProcedure defaultProcedure, String remark) {
        //根据houseNO 获取houseId
        Warehouse house = wareHouseService.getByNo(defaultProcedure.getHouseNo());
        defaultProcedure.setWarehouse(house);
        List<StorageLocation> locations = locationAllotQyery.getEquipLocation(house.getId(), defaultProcedure.getCurrentPos());
        if (locations.size() > 0) {
            defaultProcedure.setStorageLocation(locations.get(0));
        }
        workProcedureService.initProcedureCurrenPosImproper(workProcedure, defaultProcedure);

        return GaiaResultFactory.getSuccess();
    }

    private String getProcedureFormNo(WorkProcedure workProcedure, String palletNo) {
        String formNo = "";
        switch (workProcedure) {
            case Formation_Palletize:
            case Test_Palletize:
            case Formation_Rework_Palletize:
                FmPalletize fmPalletize = fmPalletizeService.getProcedureByPallet(palletNo);
                if (fmPalletize.getWork_procedure() == workProcedure) {
                    formNo = fmPalletize.getForm_no();
                } else {
                    throw new RuntimeException(palletNo + "存在多个在执行的拆盘单。");
                }
                break;
            case Formation_Rework:
            case Normal_Temperature_1:
            case Test_OCV_1:
            case Normal_Temperature_2:
            case Test_OCV_2:
            case High_Temperature:
            case Formation:
                FmProcedure procedure = fmProcedureService.getProcedureByPallet(palletNo, workProcedure);
                if (procedure == null) {
                    throw new RuntimeException(palletNo + "工序单不存在！（" + workProcedure.toString() + "）");
                }
                formNo = procedure.getForm_no();
                break;

            case FORMATION_ERROR_EXPORT:
            case TEST_ERROR_EXPORT:
            case Test_PalletMove:
                formNo = "";
                break;
            case Test_Pallet_Split:
            case Formation_Split:
                FmPalletSplit procedureByPallet = fmPalletSplitService.getProcedureByPallet(palletNo, workProcedure);

                if (procedureByPallet == null) {
                    throw new RuntimeException(palletNo + "工序单不存在！（" + workProcedure.toString() + "）");
                }
                formNo = procedureByPallet.getForm_no();
                break;
            default:
                formNo = "";
        }
        return formNo;

    }

}
