package com.wxzd.efcs.report.controller;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.efcs.business.application.service.BatteryAppService;
import com.wxzd.efcs.report.ModuleEfcsReport;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 电池信息查询
 * Created by jade on 2017/4/18.
 */
@Module(ModuleEfcsReport.name)
@Controller
@RequestMapping
public class BatteryController {

    @Resource
    private StorageDetailAppService storageDetailAppService;

    @Resource
    private BatteryAppService batteryAppService;

    /**
     * 展示
     */
    @RequestMapping
    public void index_view() {

    }

    /**
     * 数据
     */
    @RequestMapping
    @ResponseBody
    public PageResult<PalletBatteryDto> grid(PalletBatteryExQuery exQuery) {
        return storageDetailAppService.getPalletBatteryPaged(exQuery);
    }

    /**
     * 电池详情
     */
    @RequestMapping
    public void detail_view(String battery_barcode, Model model) {
        PalletBatteryDto palletBatteryDto = batteryAppService.getBatteryMoveDetail(battery_barcode);
        model.addAttribute("palletBatteryDto", palletBatteryDto);
    }
}
