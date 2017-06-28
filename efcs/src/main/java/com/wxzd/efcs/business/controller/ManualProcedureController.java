package com.wxzd.efcs.business.controller;

import com.wxzd.efcs.alarm.application.queryService.AlarmInfoExQueryService;
import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.PalletDispatchDto;
import com.wxzd.efcs.business.application.dtos.PalletizeDto;
import com.wxzd.efcs.business.application.service.ManualProcedureAppService;
import com.wxzd.efcs.business.application.service.PalletDispatchAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.event.exception.EventException;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.protocol.wcs.domain.exception.DeviceNoIncorrectException;
import com.wxzd.protocol.wcs.transport.feedback.CommandFeedbackEvent;
import com.wxzd.wms.core.application.dtos.WarehouseDto;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 人工组盘
 * Created by jade on 2017/4/24.
 */

@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class ManualProcedureController {

    @Resource
    private PalletDispatchAppService palletDispatchAppService;

    @Resource
    private WareHouseAppService wareHouseAppService;

    @Resource
    private ManualProcedureAppService manualProcedureAppService;

    @Resource
    private AlarmInfoExQueryService alarmInfoExQueryService;

    @Resource
    private ProcedureAppService procedureAppService;

    /**
     * 组盘
     *
     * @param model
     */
    @RequestMapping
    public void index_view(Model model) {
        ListResult<WarehouseDto> wareHouseResult = wareHouseAppService.getAllStorehouse();
        model.addAttribute("houseDto", wareHouseResult.getItems());
    }

    /**
     * 补假电池
     */
    @RequestMapping
    public void fake_view(Model model) {
        ListResult<WarehouseDto> wareHouseResult = wareHouseAppService.getAllStorehouse();
        model.addAttribute("houseDto", wareHouseResult.getItems());
    }

    /**
     * 调整工序
     */
    @RequestMapping
    public void procedure_view() {

    }

    /**
     * 拆盘
     */
    @RequestMapping
    public void split_view() {

    }

    /**
     * 空拖注册
     */
    @RequestMapping
    public void blank_pallet_view(){

    }

    /**
     *  注册空拖
     * @param house_no
     * @param palletNo
     * @param work_procedure
     * @return
     * @throws EventException
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult blank_pallet(String house_no,String palletNo,WorkProcedure work_procedure) throws EventException{
        return manualProcedureAppService.setPalletToManual(house_no, palletNo, work_procedure);
    }

    /**
     * 用于测试工序调整(调试专用)
     */
    @RequestMapping
    public void procedure_test_view(Model model) {
        ListResult<WarehouseDto> wareHouseResult = wareHouseAppService.getAllStorehouse();
        model.addAttribute("houseDto", wareHouseResult.getItems());
    }

    /**
     * 用于测试工序调整(调试专用)
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult procedure_test(String palletNo, WorkProcedure work_procedure,
                                     String house_no, String device_no, String type) throws Exception {

        GaiaResult result1 = manualProcedureAppService.setPalletToManual(house_no, palletNo, work_procedure);

        if (result1.isSuccess()) {
            if (StringUtl.isEqual(type, "1")) {
                CommandFeedbackEvent commandFeedbackEvent = new CommandFeedbackEvent();
                commandFeedbackEvent.setPallet_no(palletNo);
                commandFeedbackEvent.setHouse_no(house_no);
                commandFeedbackEvent.setDevice_no(device_no);
                commandFeedbackEvent.setLocation(device_no);
                CommandFeedbackEvent commandFeedbackEvent1 = procedureAppService.commandFinished(commandFeedbackEvent);
                if (commandFeedbackEvent1 != null &&  !StringUtl.isEmpty(commandFeedbackEvent1.getMessage())) {
                    return  new GaiaResult(false,commandFeedbackEvent1.getMessage());
                }

                return  new GaiaResult(true,"操作成功！");

            } else {
                GaiaResult result = procedureAppService.procedureArrive(house_no, device_no, palletNo);
                return result;
            }
        }

        return result1;

    }

    /**
     * 获取托盘信息
     *
     * @param palletNo
     * @return
     */
    @RequestMapping
    @ResponseBody
    public PalletDispatchDto getPalletInfo(String palletNo) {
        PalletDispatchDto palletDispatchDto = palletDispatchAppService.getCurrentPalletDiapatch(palletNo);

        if (palletDispatchDto == null) {
            return new PalletDispatchDto();
        }
        return palletDispatchDto;
    }

    /**
     * 验证托盘是否可以进行某项操作
     *
     * @param palletNo
     * @return
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult validate(String palletNo, WorkProcedure work_procedure, String house_no) throws EventException {
        GaiaResult result = manualProcedureAppService.checkProcedure(house_no, palletNo, work_procedure);
        if (result != null && result.isSuccess()) {
            GaiaResult result1 = manualProcedureAppService.setPalletToManual(house_no, palletNo, work_procedure);

            return result1;
        }

        return result;
    }

    /**
     * 获取托盘调度信息
     *
     * @param palletNo
     * @param model
     */
    @RequestMapping
    public String form(String palletNo, String work_procedure, String house_no, String type, Model model) {

        PalletDispatchDto palletDispatchDto = palletDispatchAppService.getCurrentPalletDiapatch(palletNo);
        model.addAttribute("palletDispatchDto", palletDispatchDto);
        model.addAttribute("type", type);
        model.addAttribute("work_procedure", work_procedure);
        model.addAttribute("house_no", house_no);
        model.addAttribute("palletNo", palletNo);

        return "/manualProcedure/form";
    }

    /**
     * 组盘
     *
     * @return
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult save(PalletModel palletModel) throws DeviceNoIncorrectException {
        PalletizeDto palletizeDto = new PalletizeDto();
        palletizeDto.setHouseId(palletModel.getHouseId());
        palletizeDto.setHouseNo(palletModel.getHouseNo());
        palletizeDto.setBattery_barcode(palletModel.getBattery_barcode());
        palletizeDto.setPalletNo(palletModel.getPalletNo());
        palletizeDto.setChannel_no(palletModel.getChannel_no());
        palletizeDto.setWork_procedure(palletModel.getWorkProcedure());
        return manualProcedureAppService.manualPalletize(palletModel.getWorkProcedure(), palletizeDto);
    }

    /**
     * 补假电池
     *
     * @param palletModel
     * @return
     * @throws DeviceNoIncorrectException
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult addfeek(PalletModel palletModel) throws DeviceNoIncorrectException {
        PalletizeDto palletizeDto = new PalletizeDto();
        palletizeDto.setHouseId(palletModel.getHouseId());
        palletizeDto.setHouseNo(palletModel.getHouseNo());
        palletizeDto.setBattery_barcode(palletModel.getBattery_barcode());
        palletizeDto.setPalletNo(palletModel.getPalletNo());
        palletizeDto.setChannel_no(palletModel.getChannel_no());
        palletizeDto.setWork_procedure(palletModel.getWorkProcedure());

        return manualProcedureAppService.addFeekBatteryToBallet(palletModel.getWorkProcedure(), palletizeDto);
    }

    /**
     * 组盘完成
     *
     * @param houseNo
     * @param palletNo
     * @param workProcedure
     * @return
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult finish(String houseNo, String palletNo, WorkProcedure workProcedure) {
        return manualProcedureAppService.palletFinish(houseNo, palletNo, workProcedure);
    }

    /**
     * 单个电池拆
     *
     * @return
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult removeBattery(String palletNo, String battery_barcode) {

        return manualProcedureAppService.removePalletizeBattery(palletNo, battery_barcode);
    }

    /**
     * 调整工序
     *
     * @param houseNo
     * @param palletNo
     * @param workProcedure
     * @return
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult changeProcedure(String houseNo, String palletNo, WorkProcedure workProcedure) {
        return manualProcedureAppService.setProcedure(houseNo, palletNo, workProcedure);
    }

}
