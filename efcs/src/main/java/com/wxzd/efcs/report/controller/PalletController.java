package com.wxzd.efcs.report.controller;

import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import com.wxzd.efcs.business.application.dtos.PalletDispatchDto;
import com.wxzd.efcs.business.application.dtos.PalletMoveDetailDto;
import com.wxzd.efcs.business.application.queryService.PalletDetailExQueryService;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
import com.wxzd.efcs.business.application.service.ManualProcedureAppService;
import com.wxzd.efcs.business.application.service.PalletDispatchAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.business.scheduler.ChargeResult;
import com.wxzd.efcs.business.scheduler.MesChargeNumber;
import com.wxzd.efcs.report.ModuleEfcsReport;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 托盘查询
 * Created by jade on 2017/4/18.
 */
@Module(ModuleEfcsReport.name)
@Controller
@RequestMapping
public class PalletController {

    @Resource
    private StorageDetailAppService storageDetailAppService;

    @Resource
    private PalletDetailExQueryService palletDetailExQueryService;


    @Autowired
    private PalletDispatchAppService palletDispatchAppService;

    @Autowired
    private ManualProcedureAppService manualProcedureAppService;

    @Autowired
    private ProcedureAppService procedureAppService;

    @Autowired
    private PalletDispatchService palletDispatchService;


    @Autowired
    private FmProcedureService fmProcedureService;


    @Autowired
    private FcsSchedulerService fcsSchedulerService;

    /**
     * 展示页
     */
    @RequestMapping
    public void index_view() {

    }

    /**
     * 数据
     */
    @RequestMapping
    @ResponseBody
    public PageResult<PalletDispatchDto> grid(PalletDispatchExQuery exQuery) {
        return storageDetailAppService.getPalletDispatchPaged(exQuery);
    }

    /**
     * 托盘详情
     */
    @RequestMapping
    public void detail_view(UUID id, String type, Model model) throws Exception {
        if (id == null) {
            throw new Exception("id必须");
        }
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        model.addAttribute("palletDispatchDto", storageDetailAppService.getPalletDispatch(id));
    }

    /**
     * 托盘移动记录查询
     */
    @RequestMapping
    @ResponseBody
    public PageResult<PalletMoveDetailDto> grid_moveDetail(PalletDispatchExQuery exQuery) {
        return storageDetailAppService.getPalletMoveDetailPaged(exQuery);
    }

    /**
     * 电池列表
     */
    @RequestMapping
    @ResponseBody
    public PageResult<PalletDetailDto> grid_battery(PalletBatteryExQuery exQuery) {
//        return storageDetailAppService.getPalletBatteryPaged(exQuery);
        return palletDetailExQueryService.getInnerDetailPaged(exQuery);
    }


    /**
     * 电池列表
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult charge_number(String palletNo) {
        PalletDispatch currentPalletDiapatch = palletDispatchService.getByPalletNo(palletNo);
        if (currentPalletDiapatch != null && currentPalletDiapatch.getWork_procedure() != null) {

            WorkProcedure errorWP = null;
            ChargeResult chargeResult = null;
            switch (currentPalletDiapatch.getWork_procedure()) {

                case High_Temperature:
                case Normal_Temperature_1:
                case Normal_Temperature_2:
                    // 查询电芯条码信息
                    List<PalletDetail> batteries = palletDispatchService.getPalletInnerDetail(palletNo);
                    FmProcedure procedureByPallet = fmProcedureService.getProcedureByPallet(palletNo, currentPalletDiapatch.getWork_procedure());
                    Long timeSecond = (new Date().getTime() - procedureByPallet.getIn_time().getTime()) / 1000;

                    List<String> sfcBarcodes = new ArrayList<>();
                    for (PalletDetail item : batteries) {
                        sfcBarcodes.add(item.getBattery_barcode());
                    }
                    DefaultProcedure procedure = new DefaultProcedure("29", currentPalletDiapatch.getCurrent_pos(), FmCreateMode.Manual);
                    procedure.setPallet_no(palletNo);
                    chargeResult = MesChargeNumber.chargeNumber(currentPalletDiapatch.getWork_procedure(), procedure, sfcBarcodes, timeSecond.intValue());
                    break;
                default:
                    return GaiaResultFactory.getError("当前工序不允许继续收数" + currentPalletDiapatch.getWork_procedure().name());
            }
            if (chargeResult != null) {
                if (chargeResult.getCode() == 0) {
                    return GaiaResultFactory.getSuccess();
                } else {
                    return GaiaResultFactory.getError("code:" + chargeResult.getCode() + "--msg:" + chargeResult.getErrorMessage());
                }
            }


            return GaiaResultFactory.getError("收数失败");

        } else {
            return GaiaResultFactory.getError("托盘或工序信息不存在");
        }


    }

    @RequestMapping
    @ResponseBody
    public GaiaResult error_export(String palletNo) throws Exception {
        PalletDispatchDto currentPalletDiapatch = palletDispatchAppService.getCurrentPalletDiapatch(palletNo);

        if (currentPalletDiapatch != null && currentPalletDiapatch.getWork_procedure() != null) {

            WorkProcedure errorWP = null;

            switch (currentPalletDiapatch.getWork_procedure()) {
                case Formation:
                case Formation_Rework:
                case Formation_Split:
                case Palletize_Cache:
                case Formation_Rework_Palletize:
                case Formation_Palletize:
                case Formation_PalletMove:
                case High_Temperature:
                    errorWP = WorkProcedure.FORMATION_ERROR_EXPORT;
                    break;
                case Test_OCV_1:
                case Test_OCV_2:
                case Test_Palletize:
                case Test_PalletMove:
                case Normal_Temperature_1:
                case Normal_Temperature_2:
                case Test_Pallet_Split:
                    errorWP = WorkProcedure.TEST_ERROR_EXPORT;
                    break;
                default:
                    return GaiaResultFactory.getError("当前工序不允许转异常工序");
            }
            GaiaResult result1 = manualProcedureAppService.setPalletToManual(currentPalletDiapatch.getHouse_no(), palletNo, errorWP);
            GaiaResult result = procedureAppService.procedureArrive(currentPalletDiapatch.getHouse_no(), currentPalletDiapatch.getCurrent_pos(), palletNo);
            return result;
//            }

        } else {
            return GaiaResultFactory.getError("托盘或工序信息不存在");
        }

    }

    @RequestMapping
    @ResponseBody
    public GaiaResult step_next(String palletNo) throws Exception {
        PalletDispatchDto currentPalletDiapatch = palletDispatchAppService.getCurrentPalletDiapatch(palletNo);

        if (currentPalletDiapatch != null && currentPalletDiapatch.getWork_procedure() != null) {

            WorkProcedure errorWP = null;
            DefaultProcedure procedure = new DefaultProcedure(currentPalletDiapatch.getHouse_no(), currentPalletDiapatch.getCurrent_pos(), FmCreateMode.Manual);
            procedure.setPallet_no(palletNo);
            String nextPos = "";
            switch (currentPalletDiapatch.getWork_procedure()) {

                case High_Temperature:
                case Normal_Temperature_1:
                case Normal_Temperature_2:
                    GaiaResult result =  procedureAppService.chargeNumber(currentPalletDiapatch.getWork_procedure(), procedure);
                    if(result.isSuccess()){
                        fcsSchedulerService.closeALlSchedulerByDevice(currentPalletDiapatch.getHouse_no(),currentPalletDiapatch.getCurrent_pos());
                    }

                    return result;
                case Formation:
                case Formation_Rework:
                    return procedureAppService.procedureFinished(currentPalletDiapatch.getHouse_no(), currentPalletDiapatch.getCurrent_pos(), palletNo);
                default:
                    return GaiaResultFactory.getError("当前工序不能自动执行下一步工序");
            }


        } else {
            return GaiaResultFactory.getError("托盘或工序信息不存在");
        }

    }

    @RequestMapping
    @ResponseBody
    public GaiaResult clearLocationStatus(String locationId) throws Exception {
        return palletDispatchService.clearLocationStatus(UUID.fromString(locationId));
    }
}
