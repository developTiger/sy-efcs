package com.wxzd.efcs.business.application.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.atlmes.ws.celltest.SfcRemovalResponse;

import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlmes.ws.celltestintegration.CheckProcessLotResponse;
import com.atlmes.ws.machineintegration.MiCommonResponse;
import com.wxzd.catl.CatlWebServiceFunction;
import com.wxzd.catl.base.CatlWebServiceConfig;
import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.alarm.domain.events.BatteryNgOverRangeEvent;
import com.wxzd.efcs.alarm.domain.events.SystemErrorEvent;
import com.wxzd.efcs.business.application.exception.DataNotFoundException;
import com.wxzd.efcs.business.application.exception.ProcedureException;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.application.workProcedures.IProcedure;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureService;
import com.wxzd.efcs.business.application.workProcedures.dto.BatteryInPrecedureInfo;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.business.webservice.MESAppWebservice;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.event.exception.EventException;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.protocol.wcs.battery.GrabType;
import com.wxzd.protocol.wcs.battery.feedback.BatteryCheckFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFinishedFeedbackEvent;
import com.wxzd.protocol.wcs.domain.exception.DeviceNoIncorrectException;
import com.wxzd.protocol.wcs.transport.feedback.CommandFeedbackEvent;
import com.wxzd.protocol.wcs.transport.feedback.PalletArriveFeedbackEvent;
import com.wxzd.wms.core.application.dtos.ContainerDto;
import com.wxzd.wms.core.domain.entities.Container;
import com.wxzd.wms.core.domain.entities.ContainerType;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.Warehouse;
import com.wxzd.wms.core.domain.service.ContainerService;
import com.wxzd.wms.core.domain.service.ContainerTypeService;
import com.wxzd.wms.core.domain.service.StorageService;
import com.wxzd.wms.core.domain.service.WareHouseService;

/**
 * Created by zhouzh on 2017/4/20.
 */
@Service
public class ProcedureAppServiceImpl implements ProcedureAppService {

    @Autowired
    private WorkProcedureService workProcedureService;

    @Autowired
    private LocationAllotQyery locationAllotQyery;

    @Autowired
    private WareHouseService wareHouseService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PalletDispatchService palletDispatchService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ContainerTypeService containerTypeService;

    @Autowired
    private BatteryInfoService batteryInfoService;

    private static HashMap<String, Integer> batteryNgCount = new HashMap<>();

    @Override
    public BatteryCheckFeedbackEvent batteryIn(BatteryCheckFeedbackEvent event) throws Exception {
        // 校验设备是否是组盘机械手
        // 明确当前工序是化成组盘还是测试组盘
        // 根据工序要求确定向MES请求电池状态接口的参数
        String deviceNo = event.getDevice_no();
        WorkProcedure workProcedure = null;
        String mesOperation = null;

        if (deviceNo.equals(DispatcherConfig.formation_palletize_device_no)) {
            workProcedure = WorkProcedure.Formation_In;
            mesOperation = CatlWebServiceConfig.getHighTempOpertion();
        } else if (deviceNo.equals(DispatcherConfig.test_palletize_device_no)) {
            workProcedure = WorkProcedure.Test_In;
            mesOperation = CatlWebServiceConfig.getNormalTempOpertion();
        } else {
            throw new DeviceNoIncorrectException("Device is not a palletize device.");
        }

        List<BatteryInContainer> batteries = event.getBatteries();
        List<BatteryInContainer> needcheck = new ArrayList<>();
        for (BatteryInContainer b : batteries) {
            if (StringUtl.isEmptyContainTrim(b.getBattery_no())) {
                b.setBattery_status(DispatcherConfig.battery_default_ng);
                b.setInfo("barcode is null.");
                addNgCount(event.getHouse_no());
            } else if (!b.getBattery_no().startsWith(DispatcherConfig.batteryProfixCheck.get(event.getHouse_no()))) {
                b.setBattery_status(DispatcherConfig.battery_default_ng);
                b.setInfo("barcode profix check error.");
                addNgCount(event.getHouse_no());
                needcheck.add(b);
            } else if (DispatcherConfig.wcs_battery_barcode_no_read.equals(b.getBattery_no())) {
                b.setBattery_status(DispatcherConfig.battery_default_ng);
                b.setInfo("barcode can not read.");
                addNgCount(event.getHouse_no());
            } else {
                // 排除非空和未读到条码的
                needcheck.add(b);

                // TODO 调用MES的电芯校验方法
                try {
                    MiCommonResponse response = null;
                    if (CatlWebServiceConfig.getHighTempOpertion().equals(mesOperation)) {
                        response = CatlWebServiceFunction.miCheckSFCStatusEx_highTemp(mesOperation, b.getBattery_no());
                    } else if (CatlWebServiceConfig.getNormalTempOpertion().equals(mesOperation)) {
                        response = CatlWebServiceFunction.miCheckSFCStatusEx_normalTemp(mesOperation, b.getBattery_no());
                    }
//                    MiCommonResponse response = CatlWebServiceFunction.miCheckSFCStatusEx(mesOperation, b.getBattery_no());
                    b.setBattery_status(response.getCode().toString());
                    if (response != null && response.getCode() == 0) {
                        resetNgCount(event.getHouse_no());
                    } else {
                        b.setBattery_status(DispatcherConfig.battery_default_ng);
                        b.setInfo("MES校验电池不通过，校验返回代码：" + response.getCode() + "。返回内容：" + response.getMessage());
                        addNgCount(event.getHouse_no());
                    }

                    // Test start
//                    if (b.getBattery_no().equals("15G702387733")
//                            || b.getBattery_no().equals("15G702592524")
//                            || b.getBattery_no().equals("15G702489285")
//                            || b.getBattery_no().equals("15G702388380")) {
//                        b.setBattery_status(DispatcherConfig.battery_default_ng);
//                    } else {
//                        b.setBattery_status(DispatcherConfig.battery_default_ok);
//                    }
//                     Test end

                } catch (Exception ex) {
                    ex.printStackTrace();
                    b.setBattery_status(DispatcherConfig.battery_default_nc);
                    b.setInfo("调用MES校验电池信息时发生错误，自动将电池标识为NC状态。错误信息：" + ex.getMessage());
                    addNgCount(event.getHouse_no());
                }
            }

            checkNgCount(event.getHouse_no(), b.getPos_no());
        }


        // 执行电池入库操作
        // 电池的校验以及结果返回是一条一校验还是多条一校验更合适？？
        BatteryInPrecedureInfo precedureInfo = new BatteryInPrecedureInfo(
                event.getHouse_no(),
                event.getDevice_no(),
                FmCreateMode.System);
        precedureInfo.setListBattery(needcheck);

        GaiaResult result = doWork(workProcedure, precedureInfo);
//        if (!result.isSuccess()) {
//            event = new BatteryCheckFeedbackEvent();
//            throw new ProcedureException("电池入库操作失败");
//        }

        // 映射机械手需要的电池条码信息
        batteries = event.getBatteries();
        for (BatteryInContainer info : batteries) {
            info.setBattery_status(DispatcherConfig.getRobotBatteryStatus(info.getBattery_status()));
        }
        if (!result.isSuccess()) {
            event.setError(result.getMessage());
        }
        return event;
    }

    @Override
    public BatteryGrabFeedbackEvent batteryGrab(BatteryGrabFeedbackEvent event) throws Exception {
        String deviceNo = event.getDevice_no();
        //如果是拆盘 补上 电池条码
        if (StringUtl.isEmpty(event.getBattery_no())) {
            if (!StringUtl.isEmpty(event.getFrom_pallet_no()) && event.getFrom_pos_channel_no() > 0) {
                PalletDetail detail = palletDispatchService.getPalletInnerDetail(event.getFrom_pallet_no(), event.getFrom_pos_channel_no());
                if (detail != null) {
                    event.setBattery_no(detail.getBattery_barcode());
                }else{
                    event.setError("电池信息不存在");
                    return event;
                }
            }
        }

        // 如果在调用MES时发生异常，则需要将当前托盘转换为异常托盘
        if (deviceNo.equals(DispatcherConfig.formation_palletize_device_no)) {
            // 化成组盘
            if (event.getTo_pos_no().equals(DispatcherConfig.formation_palletize_pos_ng)) {
                // 如果放货位置为NG拉带线，调用出库工序
                batteryMove(WorkProcedure.Formation_Out, event);
            } else if (event.getTo_pos_no().equals(DispatcherConfig.formation_palletize_pos_cache)) {
                // 如果放货位置为Cache位置，则不予理会
            } else {
                // 否则调用组盘工序
                // 调用MES电池绑定托盘
                // TODO add mes exception check
                mesBindBatteryIntoPallet(event.getHouse_no(), event.getTo_pallet_no(), event.getBattery_no(), event.getTo_pos_channel_no(), WorkProcedure.Formation_Palletize);
                // 电池组盘移动记录
                batteryMove(WorkProcedure.Formation_Palletize, event);
            }

        } else if (deviceNo.equals(DispatcherConfig.test_palletize_device_no)) {
            // 测试组盘
            if (event.getTo_pos_no().equals(DispatcherConfig.test_palletize_pos_ng)) {
                // 如果放货位置为NG拉带线，调用出库工序
                batteryMove(WorkProcedure.Test_Out, event);
            } else if (event.getTo_pos_no().equals(DispatcherConfig.test_palletize_pos_cache)) {
                // 如果放货位置为Cache位置，则不予理会
            } else {
                // 调用MES电池绑定托盘
                mesBindBatteryIntoPallet(event.getHouse_no(), event.getTo_pallet_no(), event.getBattery_no(), event.getTo_pos_channel_no(), WorkProcedure.Test_Palletize);
                // 电池组盘移动记录
                batteryMove(WorkProcedure.Test_Palletize, event);
            }
        } else if (deviceNo.equals(DispatcherConfig.formation_split_device_no)) {
            // 化成拆盘，需要考虑到缓存位的移动
            // Remark from Leon Regulus 
            // TODO 下面的注释需要修改
            // 化成拆盘机械手拆盘逻辑（比较复杂），主要看取货位置来判定工序，除Cache操作外，都是用Mave方法
            // 1、如果放货位置和取货位置是同一个，则调用重排接口，不涉及工序操作，不涉及帐的变化； MES 先拆 在组
            // 2、如果放货位置是Cache拉带线 ，则使用拆盘工序（不记账），再使用Cach方法调拆盘工序（记账）；MES 拆盘
            // 3、如果取货位置是Cache拉带线，则使用出库工序（记账）；
            // 4、如果放货位置是Rework托盘，则使用拆盘工序（不记账），再使用Rework工（记账）； MES 先拆盘 在组盘
            // 5、其他，则使用拆盘工序（不记账），再使用出库工序（记账）；MES 拆盘
            if (event.getFrom_pos_no().equals(event.getTo_pos_no())) {
//                CatlWebServiceFunction.releaseCell(event.getFrom_pallet_no(), event.getBattery_no());
                palletBatteryResort(WorkProcedure.Formation_Split, event);
                mesBindBatteryIntoPallet(event.getHouse_no(), event.getTo_pallet_no(), event.getBattery_no(), event.getTo_pos_channel_no(), WorkProcedure.Formation_Rework_Palletize);

            } else if (DispatcherConfig.formation_split_pos_rework.equals(event.getTo_pos_no())) {
//                CatlWebServiceFunction.releaseCell(event.getFrom_pallet_no(), event.getBattery_no());
                batteryMove(WorkProcedure.Formation_Split, event);
                mesBindBatteryIntoPallet(event.getHouse_no(), event.getTo_pallet_no(), event.getBattery_no(), event.getTo_pos_channel_no(), WorkProcedure.Formation_Rework_Palletize);
                batteryMove(WorkProcedure.Formation_Rework_Palletize, event);

            } else {
//                CatlWebServiceFunction.releaseCell(event.getFrom_pallet_no(), event.getBattery_no());
                batteryMove(WorkProcedure.Formation_Split, event);
                batteryMove(WorkProcedure.Formation_Out, event);

            }

        } else if (deviceNo.equals(DispatcherConfig.test_split_device_no)) {
            // Remark from Leon Regulus
            // TODO 下面的注释需要修改
            // 测试拆盘机械手拆盘逻辑
            // 测试拆盘，需要考虑到缓存位的移动
            // 1、如果放货位置是Cache拉带线 ，则使用拆盘工序（不记账），再使用Cach方法调拆盘工序（记账）；MES 拆盘
            // 2、如果取货位置是Cache拉带线，则使用出库工序（记账）；
            // 3、其他，则使用拆盘工序（不记账），再使用出库工序（记账）；MES 拆盘


            // 检查电池的状态，如果不是OK电池，则需要调用MES的转换工序方法
            BatteryInfo batteryInfo = batteryInfoService.getById(event.getBattery_no());
            if (batteryInfo!=null&&!batteryInfo.getBattery_status().equals(DispatcherConfig.battery_default_ok)) {
                // MES 转换工序
                //TODO @py 此处的operation应该是获取测试结果里面的operation
                MESAppWebservice.miStepToOtherOperation(event.getHouse_no(), batteryInfo.getBattery_barcode(), CatlWebServiceConfig.getOcvNextNextOperation());
            }

//            CatlWebServiceFunction.releaseCell(event.getFrom_pallet_no(), event.getBattery_no());
            batteryMove(WorkProcedure.Test_Pallet_Split, event);
            batteryMove(WorkProcedure.Test_Out, event);

        } else {
            throw new DeviceNoIncorrectException("Device is not a palletize device.");
        }

        return event;
    }

    @Override
    public BatteryGrabFinishedFeedbackEvent batteryGrabFinished(BatteryGrabFinishedFeedbackEvent event) throws Exception {
        String deviceNo = event.getDevice_no();
        // 化成组盘和测试组盘完成时检查托盘内的电池情况，如果存在NG的，需要将

        if (deviceNo.equals(DispatcherConfig.formation_palletize_device_no)) {
            palletizeFinished(WorkProcedure.Formation_Palletize, event);
            checkPalletBatteryAllOk(event.getHouse_no(), event.getPallet_no(), event.getDevice_no());

        } else if (deviceNo.equals(DispatcherConfig.formation_split_device_no)) {
            if (GrabType.Palletize.equals(event.getGrab_type())) {
                // 化成拆盘机的组盘操作即是化成Rework组盘工序
                palletizeFinished(WorkProcedure.Formation_Rework_Palletize, event);
                checkPalletBatteryAllRework(event.getHouse_no(), event.getPallet_no(), event.getDevice_no());

            } else {
                // 否则都是拆盘工序
                splitFinished(WorkProcedure.Formation_Split, event);

            }
        } else if (deviceNo.equals(DispatcherConfig.test_palletize_device_no)) {
            palletizeFinished(WorkProcedure.Test_Palletize, event);
//            checkPalletBatteryAllOk(event.getHouse_no(), event.getPallet_no(), event.getDevice_no());

        } else if (deviceNo.equals(DispatcherConfig.test_split_device_no)) {
            splitFinished(WorkProcedure.Test_Pallet_Split, event);

        } else {
            throw new DeviceNoIncorrectException("该设备位置不是组盘位.");
        }

        return event;
    }

    @Override
    public PalletArriveFeedbackEvent palletArrive(PalletArriveFeedbackEvent event) throws Exception {
        // 如果当前设备号在托盘到位不触发业务配置中不存在，才会触发业务
        for (String sDivice : DispatcherConfig.noDealPalletArrivedLocation) {
            if (sDivice.equals(event.getDevice_no())) {
                return event;
            }
        }

        String deviceNo = event.getDevice_no();
        String palletBarcode = event.getPallet_no();
        Container pallet = containerService.getContainerByBarcode(palletBarcode);

        // 托盘入线时，对托盘信息进行检查，如果托盘信息在系统中不存在，则添加一条新的托盘信息
        if (pallet == null) {
            ContainerType palletType = containerTypeService.getByName(DispatcherConfig.new_pallet_type_names.get(event.getHouse_no()));

            if (palletType == null) {
                SystemErrorEvent noPalletTypeEvent = new SystemErrorEvent();
                noPalletTypeEvent.setHouseNo(event.getHouse_no());
                noPalletTypeEvent.setDeviceNo(event.getDevice_no());
                noPalletTypeEvent.setLocation(event.getDevice_no());
                noPalletTypeEvent.setPalletNo(event.getPallet_no());
                noPalletTypeEvent.setContent("无默认的托盘类型，请联系管理员！");
                noPalletTypeEvent.setSource(this);

                ApplicationEventPublisher.trigger(noPalletTypeEvent);
                throw new DataNotFoundException("无默认托盘类型配置参数");
            }

            // 新增加一个托盘，按照系统配置的托盘参数添加
            ContainerDto p = new ContainerDto();
            p.setContainer_barcode(palletBarcode);
            p.setType_id(palletType.getId());
            p.setType_name(palletType.getContainer_name());
            GaiaResult result = containerService.addOrUpdateContainer(p);

            if (!result.isSuccess()) {

                throw new DataNotFoundException("无法创建新托盘");
            }

            // 重新获取托盘信息
            pallet = containerService.getContainerByBarcode(palletBarcode);
        }


        DefaultProcedure procedure = new DefaultProcedure(event.getHouse_no(), event.getDevice_no(), FmCreateMode.System);
        procedure.setPallet_no(event.getPallet_no());
        PalletDispatch dispatch = palletDispatchService.getByPalletNo(event.getPallet_no());

        // 如果当前托盘没有工序，则将托盘标识为异常，执行异常工序
        if (dispatch == null) {
            boolean isEmptyRegistDevice = false;
            // 只有在空托盘注册点，才能创建空托盘流转工序
            for (String registDevice : DispatcherConfig.empty_pallet_regist_device) {
                if (deviceNo.equals(registDevice)) {
                    // 初始化空托盘的工序
                    isEmptyRegistDevice = true;
                    break;
                }
            }

            if (isEmptyRegistDevice) {
                // 将托盘和库绑定
                dispatch = new PalletDispatch();
                Warehouse warehouse = wareHouseService.getByNo(event.getHouse_no());
                dispatch.setHouse_id(warehouse.getId());
                dispatch.setPallet_status(PalletStatus.In_Waiting);
                dispatch.setDispatch_status(PalletDispatchStatus.Dispatching);
                dispatch.setContainer_id(pallet.getId().toString());
                dispatch.setContainer_no(pallet.getContainer_barcode());
                dispatch.setIs_empty(true);

                dispatch.setWork_procedure(DispatcherConfig.getPalletMoveProcedure(deviceNo));
                palletDispatchService.newPalletDispatch(dispatch);

                // 将托盘放入到空托盘专用库位
                //  (UUID houseId,UUID locationId, String palletNo,String businessType, String businessNo, String processId)
                List<StorageLocation> equipLocation = locationAllotQyery.getEquipLocation(warehouse.getId(), DispatcherConfig.getEmptyLocationNo(deviceNo));
                if (equipLocation.size() > 0) {
                    storageService.palletInStorage(
                            warehouse.getId(),
                            equipLocation.get(0).getId(),
                            pallet.getContainer_barcode(),
                            DispatcherConfig.getPalletMoveProcedure(deviceNo).toString(),
                            "",
                            "");
                }
            } else {
                // 将托盘和异常库位绑定
                dispatch = new PalletDispatch();
                Warehouse warehouse = wareHouseService.getByNo(event.getHouse_no());
                dispatch.setHouse_id(warehouse.getId());
                dispatch.setPallet_status(PalletStatus.Out_Waiting);
                dispatch.setDispatch_status(PalletDispatchStatus.Dispatching);
                dispatch.setContainer_id(pallet.getId().toString());
                dispatch.setContainer_no(pallet.getContainer_barcode());
                dispatch.setIs_empty(true);

                dispatch.setWork_procedure(DispatcherConfig.getPalletErrorProcedure(deviceNo));
                palletDispatchService.newPalletDispatch(dispatch);

                // 将托盘放入到异常托盘专用库位
                //  (UUID houseId,UUID locationId, String palletNo,String businessType, String businessNo, String processId)
                List<StorageLocation> equipLocation = locationAllotQyery.getEquipLocation(warehouse.getId(), DispatcherConfig.getErrorLocationNo(deviceNo));
                if (equipLocation.size() > 0) {
                    storageService.palletInStorage(
                            warehouse.getId(),
                            equipLocation.get(0).getId(),
                            pallet.getContainer_barcode(),
                            DispatcherConfig.getPalletErrorProcedure(deviceNo).toString(),
                            "",
                            "");
                }

                WorkProcedure errorProcedure = DispatcherConfig.getPalletErrorProcedure(deviceNo);
                procedure.setWorkProcedure(errorProcedure);
                procedure.setPalletDispatch(dispatch);
                doWork(errorProcedure, procedure);
                return event;
            }
        }


        // 判定当前的设备号是化成段还是测试段
        boolean isFormation;
        if (Integer.parseInt(deviceNo) < 3000) {
            isFormation = true;
        } else {
            isFormation = false;
        }

        // 托盘号前缀校验
        for (String palletNoCheckDevice : DispatcherConfig.pallet_profix_check_device) {
            if (deviceNo.equals(palletNoCheckDevice)) {
                // 校验托盘编号前缀
                if (!palletBarcode.startsWith(DispatcherConfig.pallet_no_profix)) {
                    // 不是当前拉线的托盘号，序号异常排出
                    String errorMsg = "托盘条码前缀不符合校验标准，托盘不是当前拉线允许使用托盘。托盘号码：" + palletBarcode;
                    event.setError(errorMsg);
                    changeToErrorProcedure(event.getHouse_no(), deviceNo, event.getPallet_no(), errorMsg);
                    return event;
                }
            }
        }

        // 托盘号可用范围校验，在1170和1230两个扫码处校验
        if (deviceNo.equals(DispatcherConfig.Formation_enter_exit) || deviceNo.equals(DispatcherConfig.formation_palletize_scaner_no)) {
            // 校验托盘号是否允许在化成段使用
            if (DispatcherConfig.convertPalletNoToInt(palletBarcode) > DispatcherConfig.foramtion_pallet_check_no) {
                // 超出允许值，托盘转异常并且排出
                String errorMsg = "托盘条码大于化成段允许使用范围。托盘号码：" + palletBarcode;
                event.setError(errorMsg);
                changeToErrorProcedure(event.getHouse_no(), deviceNo, event.getPallet_no(), errorMsg);
                return event;
            }
        }

        // 在需要 MES 校验托盘的位置，对托盘进行检查
        for (String checkDevice : DispatcherConfig.mes_pallet_check_device) {
            if (deviceNo.equals(checkDevice)) {

//                CheckProcessLotResponse checkProcessLotResponse = CatlWebServiceFunction.miCheckProcessLot(palletBarcode);
                CheckProcessLotResponse checkProcessLotResponse;
                if (isFormation) {
                    checkProcessLotResponse = CatlWebServiceFunction.miCheckProcessLot_highTemp(palletBarcode);
                } else {
                    checkProcessLotResponse = CatlWebServiceFunction.miCheckProcessLot_normalTemp(palletBarcode);
                }

                if (!checkProcessLotResponse.getCode().equals("0")) {
                    // 如果托盘异常，则将托盘标识为异常
                    String errorMsg = "MES反馈托盘校验异常。托盘号码：" + palletBarcode;
                    changeToErrorProcedure(event.getHouse_no(), deviceNo, event.getPallet_no(), errorMsg);
                    return event;
                }

                // Test start
//            if (event.getPallet_no().equals("L26B0004")) {
//                 模拟标识为异常
//                changeToErrorProcedure(event.getHouse_no(), deviceNo, event.getPallet_no(), "MES 托盘校验异常");
//            }
                // Test end
            }
        }

        // 如果当前托盘工序是拆盘工序，并且当前位置需要调用MES的拆盘接口，则调用MES的拆盘接口
        boolean isNeedSplitPallet = false;
        for (WorkProcedure wp : DispatcherConfig.mes_pallet_split_workprocedure) {
            if (wp.equals(dispatch.getWork_procedure())) {
                for (String d : DispatcherConfig.mes_pallet_split_device) {
                    if (d.equals(deviceNo)) {
                        isNeedSplitPallet = true;
                        break;
                    }
                }
            }
            if (isNeedSplitPallet) {
                break;
            }
        }
        if (isNeedSplitPallet) {
            // 循环解除电池绑定关系。如果接触绑定报错，则将该电池状态更新为OPEN NC
            List<PalletDetail> batteries = palletDispatchService.getActivePalletDetailByPalletWithStatus(palletBarcode);
            for (PalletDetail b : batteries) {
                try {
//                    SfcRemovalResponse res = CatlWebServiceFunction.releaseCell(palletBarcode, b.getBattery_barcode());
                    SfcRemovalResponse res;
                    if (isFormation) {
                        res = CatlWebServiceFunction.releaseCell_high(palletBarcode, b.getBattery_barcode());
                    } else {
                        res = CatlWebServiceFunction.releaseCell_normal(palletBarcode, b.getBattery_barcode());
                    }

                    if (res.getCode() != 0) {
                        String errorMsg = "调用MES接触电池绑定关系发生错误。托盘号：" + palletBarcode + "，电池条码：" + b.getBattery_barcode();
                        event.setError(errorMsg);
                        batteryInfoService.updateBatteryStatus(b.getBattery_barcode(), DispatcherConfig.battery_default_ng, errorMsg);
                    }
                } catch (Exception e) {
                    String errorMsg = "调用MES接触电池绑定关系发生异常。托盘号：" + palletBarcode + "，电池条码：" + b.getBattery_barcode();
                    event.setError(errorMsg);
                    batteryInfoService.updateBatteryStatus(b.getBattery_barcode(), DispatcherConfig.battery_default_nc, errorMsg);
                }
            }
        }


        // 执行托盘工序
//        if (dispatch.getCurrent_pos() == null || !dispatch.getCurrent_pos().equals(event.getDevice_no())) {
        procedure.setPalletDispatch(dispatch);
        GaiaResult result = doWork(dispatch.getWork_procedure(), procedure);
//        }
        if (!result.isSuccess()) {
            event.setError(result.getMessage());
        }
        return event;
    }

    @Override
    public CommandFeedbackEvent commandFinished(CommandFeedbackEvent event) throws Exception {
        // 如果当前设备号在指令完成不触发业务配置中不存在，才会触发业务
        for (String sDivice : DispatcherConfig.noDealCommandFinishedLocation) {
            if (sDivice.equals(event.getDevice_no())) {
                return event;
            }
        }

        DefaultProcedure procedure = new DefaultProcedure(event.getHouse_no(), event.getLocation(), FmCreateMode.System);
        procedure.setPallet_no(event.getPallet_no());
        procedure.setPalletOperate(PalletOperate.CommandFinished);
        PalletDispatch dispatch = palletDispatchService.getByPalletNo(event.getPallet_no());

        // 如果当前托盘没有工序，则将托盘标识为异常，执行异常工序
        if (dispatch == null) {
            WorkProcedure errorProcedure = DispatcherConfig.getPalletErrorProcedure(event.getDevice_no());
            procedure.setWorkProcedure(errorProcedure);
            doWork(errorProcedure, procedure);
            return event;
        }

        // 执行托盘工序
//        if (dispatch.getCurrent_pos() == null || !dispatch.getCurrent_pos().equals(event.getDevice_no())) {
        procedure.setPalletDispatch(dispatch);
        doWork(dispatch.getWork_procedure(), procedure);
//        }

        return event;
    }

    /**
     * 获取托盘的当前工序
     *
     * @param houseNo
     * @param palletNo
     * @return
     */
    @Override
    public WorkProcedure getCurrentWorkProcedure(String houseNo, String palletNo) {
        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletNo);
        return palletDispatch.getWork_procedure();
    }

    /**
     * 进站
     *
     * @param houseNo
     * @param positionNo
     * @param palletNo
     * @throws Exception
     */
    @Override
    public GaiaResult procedureArrive(String houseNo, String positionNo, String palletNo) throws Exception {
        DefaultProcedure procedure = new DefaultProcedure(houseNo, positionNo, FmCreateMode.System);
        procedure.setPallet_no(palletNo);
        procedure.setPalletOperate(PalletOperate.Arrived);

        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        if (dispatch == null) {
            throw new DataNotFoundException();
        }
        procedure.setPalletDispatch(dispatch);
        return doWork(dispatch.getWork_procedure(), procedure);
    }

    /**
     * 申请重新分配库位
     *
     * @param houseNo
     * @param positionNo
     * @param palletNo
     * @return
     * @throws Exception
     */
    @Override
    public GaiaResult procedureAllotLocation(String houseNo, String positionNo, String palletNo) throws Exception {
        DefaultProcedure procedure = new DefaultProcedure(houseNo, positionNo, FmCreateMode.System);
        Warehouse house = wareHouseService.getByNo(procedure.getHouseNo());

        List<StorageLocation> locations = locationAllotQyery.getEquipLocation(house.getId(), procedure.getCurrentPos());
        if (locations.size() > 0) {
            procedure.setStorageLocation(locations.get(0));
        }
        procedure.setPalletOperate(PalletOperate.AllotStorageLocation);
        procedure.setWarehouse(house);
        procedure.setPallet_no(palletNo);

        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        if (dispatch == null) {
            throw new DataNotFoundException();
        }
        procedure.setPalletDispatch(dispatch);
        return tryDoWork(dispatch.getWork_procedure(), procedure);
    }

    /**
     * 出站
     *
     * @param houseNo
     * @param positionNo
     * @param palletNo
     * @throws Exception
     */
    @Override
    public GaiaResult procedureFinished(String houseNo, String positionNo, String palletNo) throws Exception {
        DefaultProcedure procedure = new DefaultProcedure(houseNo, positionNo, FmCreateMode.System);
        procedure.setPallet_no(palletNo);
        procedure.setPalletOperate(PalletOperate.OperateComplete);

        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        if (dispatch == null) {
            throw new DataNotFoundException();
        }
        procedure.setPalletDispatch(dispatch);
        return doWork(dispatch.getWork_procedure(), procedure);
    }

    /**
     * 库位更换
     *
     * @param houseNo  库号
     * @param deviceNo 设备号
     * @param palletNo 托盘号
     * @return
     * @throws Exception
     */
    @Override
    public Instruction redistributeStorageLocation(String houseNo, String deviceNo, String palletNo) throws Exception {
        DefaultProcedure procedure = new DefaultProcedure(houseNo, deviceNo, FmCreateMode.System);
        procedure.setPallet_no(palletNo);
        procedure.setPalletOperate(PalletOperate.RedistributeLocation);

        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        if (dispatch == null) {
            throw new DataNotFoundException("找不到载货托盘信息");
        }
        procedure.setPalletDispatch(dispatch);

        // 申请重新分配库位
        ObjectResult<Instruction> result = (ObjectResult<Instruction>) doWork(dispatch.getWork_procedure(), procedure);
        Instruction instruction = result.getObject();
        if (!result.isSuccess() || instruction == null) {
            throw new ProcedureException("工序执行错误，无法分配新的库位");
        }

        return instruction;
    }

    //
//    @Override
//    public GaiaResult changeStorageLocation(String houseNo, String location, String palletNo) throws Exception {
//        DefaultProcedure procedure = new DefaultProcedure(houseNo, location, FmCreateMode.System);
//        procedure.setPallet_no(palletNo);
//        procedure.setPalletOperate(PalletOperate.ChangeLocation);
//
//        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
//        procedure.setPalletDispatch(dispatch);
//        if (dispatch == null) {
//            throw new DataNotFoundException("无载货托盘数据");
//        }
//
//        return doWork(dispatch.getWork_procedure(), procedure);
//    }
    @Transaction
    @Override
    public GaiaResult changeProcedure(String houseNo, String palletNo, String currentPos, WorkProcedure newProcedure, boolean isNeedChangeLocation, String remark) throws Exception {
//        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletNo);
//        if (palletDispatch == null) {
//            throw new DataNotFoundException("无载货托盘数据");
//        }
//
//        workProcedureService.errorFinishProcedure(palletDispatch.getWork_procedure(), palletNo, remark);
//
//        palletDispatch.setWork_procedure(newProcedure);
//        workProcedureService.doWork()
////        palletDispatchService.newPalletDispatchWithMoveDetail(palletDispatch, true);
//
//        return new GaiaResult(true, "");

        //TODO  代码优化
        PalletDispatch dispatch = errorFinishProcedure(palletNo, remark);

        if (StringUtl.isEmpty(currentPos))
            currentPos = dispatch.getCurrent_pos();
//        WorkProcedure errorProcedure = DispatcherConfig.getPalletErrorProcedure(event.getDevice_no());
        DefaultProcedure procedure = new DefaultProcedure(houseNo, currentPos, FmCreateMode.System);
        procedure.setPallet_no(palletNo);
        Warehouse house = wareHouseService.getByNo(procedure.getHouseNo());
        procedure.setWarehouse(house);
        List<StorageLocation> locations = locationAllotQyery.getEquipLocation(house.getId(), procedure.getCurrentPos());
        if (locations.size() > 0) {
            procedure.setStorageLocation(locations.get(0));
        }
        if (isNeedChangeLocation) {
            procedure.setPalletOperate(PalletOperate.ChangeLocation);
        }

        workProcedureService.initProcedureCurrenPosImproper(newProcedure, procedure);
        return this.doWork(newProcedure, procedure);
    }

    /**
     * 更改工序
     *
     * @param houseNo
     * @param palletNo
     * @param newProcedure
     * @param isNeedChangeLocation
     * @param remark               @throws Exception
     */
    @Override
    public GaiaResult changeProcedure(String houseNo, String palletNo, WorkProcedure newProcedure, boolean isNeedChangeLocation, String remark) throws Exception {
        return changeProcedure(houseNo, palletNo, "", newProcedure, isNeedChangeLocation, remark);
    }

    @Override
    public GaiaResult changeToErrorProcedure(String houseNo, String location, String palletNo, String remark) throws Exception {

        WorkProcedure errorWorkProcedure = DispatcherConfig.getPalletErrorProcedureByLocation(location);
        return changeProcedure(houseNo, palletNo, location, errorWorkProcedure, false, remark);
    }

    /**
     * 收数
     *
     * @param workProcedure
     * @param procedure
     * @return
     */
    @Override
    public GaiaResult chargeNumber(WorkProcedure workProcedure, DefaultProcedure procedure) {
        procedure.setPalletOperate(PalletOperate.ChargeNumberSuccess);
        Warehouse house = wareHouseService.getByNo(procedure.getHouseNo());
        procedure.setWarehouse(house);
        List<StorageLocation> locations = locationAllotQyery.getEquipLocation(house.getId(), procedure.getCurrentPos());
        if (locations.size() > 0) {
            procedure.setStorageLocation(locations.get(0));
        }
        return this.tryDoWork(workProcedure, procedure);
    }

    private PalletDispatch errorFinishProcedure(String palletNo, String remark) throws DataNotFoundException {
        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletNo);
        if (palletDispatch == null) {
            throw new DataNotFoundException("无载货托盘数据");
        }
        workProcedureService.errorFinishProcedure(palletDispatch.getWork_procedure(), palletNo, remark);


        return palletDispatch;
    }

    /**
     * 电池工序移动
     *
     * @param workProcedure 工序
     * @param event         电池移动信息
     * @return GaiaResult
     */
    private GaiaResult batteryMove(WorkProcedure workProcedure, BatteryGrabFeedbackEvent event) throws Exception {

        PalletizePrecedureInfo procedureInfo = new PalletizePrecedureInfo(event.getHouse_no(), event.getDevice_no(), FmCreateMode.System);
        BeanUtl.copyProperties(event, procedureInfo);
        procedureInfo.setPalletOperate(PalletOperate.MoveItem);
        GaiaResult result = doWork(workProcedure, procedureInfo);
        if (!result.isSuccess()) {
            event.setError(result.getMessage());
        }
        return result;
    }

    /**
     * 电池重新排序
     *
     * @param workProcedure 工序
     * @param event         电池移动信息
     * @return GaiaResult
     */
    private GaiaResult palletBatteryResort(WorkProcedure workProcedure, BatteryGrabFeedbackEvent event) throws Exception {
        //重排
        PalletDetail detail = palletDispatchService.getPalletInnerDetail(event.getFrom_pallet_no(), event.getFrom_pos_channel_no());
        detail.setToPos(event.getDevice_no(), event.getClamp_no(), event.getTo_pos_no(), String.valueOf(event.getTo_pos_channel_no()), PositionType.Pallet);
        detail.setIs_resort(true);

        return palletDispatchService.savePalletInnerDetail(detail);
    }

//    /**
//     * 电池放缓存位
//     *
//     * @param workProcedure 工序
//     * @param event         电池移动信息
//     * @return GaiaResult
//     */
//    private GaiaResult batteryCache(WorkProcedure workProcedure, BatteryGrabFeedbackEvent event) {
//        //根据houseNO 获取houseId
//        Warehouse house = wareHouseService.getByNo(event.getHouse_no());
//        List<StorageLocation> locations = locationAllotQyery.getEquipLocation(house.getId(), event.getTo_pos_no());
//        UUID locationId;
//        if (locations.size() > 0) {
//            locationId = locations.get(0).getId();
//        } else {
//            throw new RuntimeException(I18nContext.getMessage("缓存库位不存在:") + event.getTo_pos_no());
//        }
//        Storage storage = storageService.getStorageByBarcode(event.getBattery_no());
//
//        return storageService.storageTransfer(house.getId(), locationId, storage, workProcedure.toString());
//    }

    /**
     * 组盘完成
     *
     * @param event
     * @return
     */
    private GaiaResult palletizeFinished(WorkProcedure workProcedure, BatteryGrabFinishedFeedbackEvent event) {

        // 调用EFCS组盘完成
        PalletizePrecedureInfo procedureInfo = new PalletizePrecedureInfo(event.getHouse_no(), event.getDevice_no(), FmCreateMode.System);
        procedureInfo.setPallet_no(event.getPallet_no());
        procedureInfo.setPalletOperate(PalletOperate.OperateComplete);

        GaiaResult result = doWork(workProcedure, procedureInfo);
        if (!result.isSuccess()) {
            event.setError(result.getMessage());
        }
        return result;
    }

    /**
     * 拆盘完成
     *
     * @param event
     * @return
     */
    private GaiaResult splitFinished(WorkProcedure workProcedure, BatteryGrabFinishedFeedbackEvent event) throws Exception {
        // 调用MES整体拆盘
//        CatlWebServiceFunction.releaseTrayId(event.getPallet_no());
        if (workProcedure == WorkProcedure.Formation_Split) {
            CatlWebServiceFunction.releaseTrayId_high(event.getPallet_no());
        } else if (workProcedure == WorkProcedure.Test_Pallet_Split) {
            CatlWebServiceFunction.releaseTrayId_high(event.getPallet_no());
        }
        // 调用EFCS拆盘完成
        PalletizePrecedureInfo procedureInfo = new PalletizePrecedureInfo(event.getHouse_no(), event.getDevice_no(), FmCreateMode.System);
        procedureInfo.setPallet_no(event.getPallet_no());
        procedureInfo.setPalletOperate(PalletOperate.OperateComplete);

        GaiaResult result = doWork(workProcedure, procedureInfo);
        if (!result.isSuccess()) {
            event.setError(result.getMessage());
        }
        return result;
    }

    /**
     * 完善IProcedure 库信息 和 库位信息并执行工艺
     *
     * @param t
     * @param <T>
     * @return
     */
    private <T extends IProcedure> GaiaResult doWork(WorkProcedure workProcedure, T t) {
        this.checkProcedure(workProcedure, t);
        //根据houseNO 获取houseId
        Warehouse house = wareHouseService.getByNo(t.getHouseNo());
        t.setWarehouse(house);
        List<StorageLocation> locations = locationAllotQyery.getEquipLocation(house.getId(), t.getCurrentPos());
        if (locations.size() > 0) {
            t.setStorageLocation(locations.get(0));
        }

        boolean isNeedChangeToErrorProcedure = false;
        GaiaResult result = GaiaResultFactory.getError("");
        try {
            result = workProcedureService.doWork(workProcedure, t);
        } catch (RuntimeException runtimeException) {
//            throw runtimeException;
            runtimeException.printStackTrace();
            result.setMessage(runtimeException.getMessage());
            isNeedChangeToErrorProcedure = true;
        }

        PalletStatus palletStatus = null;
        if (t.getPalletDispatch() != null) {
            palletStatus = t.getPalletDispatch().getPallet_status();
        }
        if (result.isSuccess()) {
            if (workProcedureService.needActiveNextProcedure(workProcedure, palletStatus, t.getCurrentPos())) {
                WorkProcedure nextProcedure = workProcedureService.getNextProcedure(workProcedure);

                try {
                    result = workProcedureService.doWork(nextProcedure, t);
                } catch (RuntimeException runtimeException) {
                    result.setMessage(runtimeException.getMessage());
                    isNeedChangeToErrorProcedure = true;
                }
            }
        }

        if (isNeedChangeToErrorProcedure && workProcedure != WorkProcedure.FORMATION_ERROR_EXPORT && workProcedure != WorkProcedure.TEST_ERROR_EXPORT) {
            try {
                String palletNo = "";
                if (t.getPalletDispatch() != null)
                    palletNo = t.getPalletDispatch().getContainer_no();
                changeToErrorProcedure(t.getHouseNo(), t.getCurrentPos(), palletNo, result.getMessage());
                result.setMessage(result.getMessage()+";已转为异常盘");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * try catch 执行dowork
     *
     * @param workProcedure
     * @param procedure
     * @return
     */
    private GaiaResult tryDoWork(WorkProcedure workProcedure, IProcedure procedure) {
        GaiaResult result;
        try {
            result = workProcedureService.doWork(workProcedure, procedure);//加tryCatch 把异常全部转到异常工序
        } catch (RuntimeException runtimeException) {
            runtimeException.printStackTrace();
            result = GaiaResultFactory.getError(runtimeException.getMessage());
        }

        return result;
    }

    private <T extends IProcedure> void checkProcedure(WorkProcedure workProcedure, T t) {
        if (StringUtl.isEmpty(t.getCurrentPos())) {
            throw new RuntimeException("位置信息不能为空");
        }
        if (StringUtl.isEmpty(t.getHouseNo())) {
            throw new RuntimeException("库号不能为空");
        }
        if (workProcedure != WorkProcedure.Formation_Out && workProcedure != WorkProcedure.Formation_In
                && workProcedure != WorkProcedure.Test_In && workProcedure != workProcedure.Test_Out) {
            DefaultProcedure procedure = (DefaultProcedure) t;

            if (procedure.getPalletOperate() == PalletOperate.Arrived || procedure.getPalletOperate() == PalletOperate.OperateComplete) {
                if (StringUtl.isEmptyContainTrim(procedure.getPallet_no()))
                    throw new RuntimeException("托盘号不能为空");
            }
            switch (workProcedure) {
                case Formation_Palletize:
                case Test_Palletize:
                case Formation_Rework_Palletize:
                    if (procedure.getPalletOperate() == PalletOperate.MoveItem) {
                        PalletizePrecedureInfo palletizePrecedureInfo = (PalletizePrecedureInfo) t;
                        if (StringUtl.isEmptyContainTrim(palletizePrecedureInfo.getTo_pallet_no()))
                            throw new RuntimeException("组盘目标托盘号不能为空");
                    }
                    break;
                case Formation_Split:
                case Test_Pallet_Split:
                    if (procedure.getPalletOperate() == PalletOperate.MoveItem) {
                        PalletizePrecedureInfo palletizePrecedureInfo = (PalletizePrecedureInfo) t;

                        if (StringUtl.isEmptyContainTrim(palletizePrecedureInfo.getFrom_pallet_no()))
                            throw new RuntimeException("拆盘起始托盘号不能为空");
                    }
                    break;
                case High_Temperature:
                case Formation:
                case Formation_Rework:
                case Normal_Temperature_1:
                case Test_OCV_1:
                case Normal_Temperature_2:
                case Test_OCV_2:
                    break;
                case FORMATION_ERROR_EXPORT:
                case TEST_ERROR_EXPORT:
                    break;
                case Test_PalletMove:
                case Formation_PalletMove:
                    break;
                default:
                    break;
            }
        } else if (workProcedure == WorkProcedure.Formation_In
                || workProcedure == WorkProcedure.Test_In) {
            BatteryInPrecedureInfo inPrecedureInfo = (BatteryInPrecedureInfo) t;
            if (inPrecedureInfo.getListBattery() != null || inPrecedureInfo.getListBattery().size() > 0) {
//                for (BatteryInContainer in : inPrecedureInfo.getListBattery()) {
//                    if (StringUtl.isEmpty(in.getBattery_no()))
//                        throw new RuntimeException("电池条码不能为空");
//                }
            } else {
                PalletizePrecedureInfo palletizePrecedureInfo = (PalletizePrecedureInfo) t;
                if (palletizePrecedureInfo.getFrom_pos_no().equals(DispatcherConfig.formation_split_pos_cache))
                    throw new RuntimeException("缓存位库存已出，无需再次出库！");
            }

        }

    }

    /**
     * 检查连续NG数量
     *
     * @param houseNo
     * @param deviceNo
     */
    private void checkNgCount(String houseNo, String deviceNo) {
        if (batteryNgCount.containsKey(houseNo)) {
            // 如果一直NG多少次，就提醒停线
            if (batteryNgCount.get(houseNo) > DispatcherConfig.max_ng_count) {
                BatteryNgOverRangeEvent ngAlarm = new BatteryNgOverRangeEvent();
                ngAlarm.setHouseNo(houseNo);
                ngAlarm.setDeviceNo(deviceNo);
                ngAlarm.setLocation(deviceNo);
                ngAlarm.setContent("拉带线入库口NG数量超出预设阀值，请现场确认设备状况。");

                try {
                    ApplicationEventPublisher.trigger(ngAlarm);
                } catch (EventException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 增加连续NG数量
     *
     * @param houseNo
     */
    private void addNgCount(String houseNo) {
        Integer houseNg = batteryNgCount.get(houseNo);
        if (houseNg == null) {
            houseNg = new Integer(0);
        }
        batteryNgCount.put(houseNo, (houseNg + 1));
    }

    /**
     * 重置连续NG总数
     *
     * @param houseNo
     */
    private void resetNgCount(String houseNo) {
        if (batteryNgCount.containsKey(houseNo)) {
            batteryNgCount.put(houseNo, new Integer(0));
        }
    }

    /**
     * 如果电池在绑定时调用MES报错，则需要将该电池信息记录为NC，并且将该托盘标识为异常排出工序。
     *
     * @param houseNo
     * @param palletNo
     * @param batteryNo
     * @param channelNo
     */
    private void mesBindBatteryIntoPallet(String houseNo, String palletNo, String batteryNo, int channelNo, WorkProcedure workProcedure) {
        try {
            //此处绑盘失败是不会报错的，是返回非0值
//            MiCommonResponse response = CatlWebServiceFunction.miBindSFCintoTray(palletNo, batteryNo, channelNo);
            MiCommonResponse response = null;
            if (workProcedure == WorkProcedure.Formation_Palletize || workProcedure == WorkProcedure.Formation_Rework_Palletize) {
                response = CatlWebServiceFunction.miBindSFCintoTray_highTemp(palletNo, batteryNo, channelNo);
            } else if (workProcedure == WorkProcedure.Test_Palletize) {
                response = CatlWebServiceFunction.miBindSFCintoTray_normalTemp(palletNo, batteryNo, channelNo);
            }

            if (response != null && response.getCode() != 0) {
                // 记录电池异常信息
                String errorMsg = "MES绑定电池时发生错误。错误代码：" + response.getCode() + "，错误信息：" + response.getMessage();
                batteryInfoService.updateBatteryStatus(batteryNo, DispatcherConfig.battery_default_ng, errorMsg);
            }
        } catch (Exception ex) {
            // 记录电池异常信息
            String errorMsg = "MES绑定电池时发生异常：" + ex.getMessage();
            batteryInfoService.updateBatteryStatus(batteryNo, DispatcherConfig.battery_default_nc, errorMsg);
        }
    }

    /**
     * 检查托盘中是否存在非OK的电池，如果存在，则将电池标识为异常排出
     *
     * @param houseNo
     * @param palletNo
     */
    private void checkPalletBatteryAllOk(String houseNo, String palletNo, String deviceNo) {
        List<PalletDetail> batteries = palletDispatchService.getActivePalletDetailByPalletWithStatus(palletNo);
        boolean isHavaNgBattery = false;
        String errorMsg = "";
        if (batteries.size() != 24) {
            errorMsg = "托盘中电池数量不足24个，需要补假电池。";
            isHavaNgBattery = true;
            //	log.warn(" -----check cell status ----{} : {}",palletNo,errorMsg);
        } else {
            for (PalletDetail detail : batteries) {
                if (!DispatcherConfig.battery_default_ok.equals(detail.getBattery_status())) {
                    errorMsg = "检查托盘中存在非Ok的电池，不可进入下一道工序";
                    isHavaNgBattery = true;
                    break;
                }
            }
        }

        if (isHavaNgBattery) {
            // 将托盘标识为异常托盘
            changePalletToErrorExport(houseNo, palletNo, deviceNo, errorMsg);
//            try {
//                changeToErrorProcedure(houseNo, deviceNo, palletNo, errorMsg);
//            } catch (Exception e) {

//            }
        }
    }

    /**
     * 检查托盘中是否存在非Rework的电池，如果存在，则将电池标识为异常排出
     *
     * @param houseNo
     * @param palletNo
     */
    private void checkPalletBatteryAllRework(String houseNo, String palletNo, String deviceNo) {
        List<PalletDetail> batteries = palletDispatchService.getActivePalletDetailByPalletWithStatus(palletNo);
        boolean isHavaNgBattery = false;
        String errorMsg = "";
        if (batteries.size() != 24) {
            errorMsg = "托盘中电池数量不足24个，需要补假电池。";
            isHavaNgBattery = true;
        } else {
            for (PalletDetail detail : batteries) {
                if (!DispatcherConfig.battery_default_rework.equals(detail.getBattery_status())) {
                    errorMsg = "托盘中存在非Rework的电池，不可进入下一道工序";
                    isHavaNgBattery = true;
                    break;
                }
            }
        }

        if (isHavaNgBattery) {
            changePalletToErrorExport(houseNo, palletNo, deviceNo, errorMsg);
        }
    }

    private void changePalletToErrorExport(String houseNo, String palletNo, String deviceNo, String errorMsg) {
        // 将托盘标识为异常托盘
        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletNo);
        if (palletDispatch.getWork_procedure() == WorkProcedure.Formation_Palletize
                || palletDispatch.getWork_procedure() == WorkProcedure.Formation_Rework_Palletize) {
            try {
                if (palletDispatch.getWork_procedure() != WorkProcedure.FORMATION_ERROR_EXPORT) {
                    changeProcedure(houseNo, palletNo, deviceNo, WorkProcedure.FORMATION_ERROR_EXPORT, false, errorMsg);
                }
            } catch (Exception e) {
                // TODO 打印异常
            }

        } else if (palletDispatch.getWork_procedure() == WorkProcedure.Test_Palletize) {
            try {
                if (palletDispatch.getWork_procedure() != WorkProcedure.TEST_ERROR_EXPORT) {
                    changeProcedure(houseNo, palletNo, deviceNo, WorkProcedure.TEST_ERROR_EXPORT, false, errorMsg);
                }
            } catch (Exception e) {
                // TODO 打印异常
            }
        }
    }


    /**
     * 更换库位
     *
     * @param houseNo
     * @param positionNo
     * @param palletNo
     * @return
     */
    @Override
    public GaiaResult changeLocation(String houseNo, String positionNo, String palletNo, String remark) throws DataNotFoundException {
        DefaultProcedure procedure = new DefaultProcedure(houseNo, positionNo, FmCreateMode.System);
        procedure.setPallet_no(palletNo);
        procedure.setPalletOperate(PalletOperate.ChangeLocation);
        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
        if (dispatch == null) {
            throw new DataNotFoundException();
        }

        procedure.setPalletDispatch(dispatch);
        return doWork(dispatch.getWork_procedure(), procedure);
    }
}
