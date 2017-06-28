package com.wxzd.efcs.dashboard.controller;

import com.wxzd.efcs.business.application.dtos.MeterCounts;
import com.wxzd.efcs.dashboard.ModuleEfcsDashboard;
import com.wxzd.efcs.report.application.dtos.DashBoardStatisticsDto;
import com.wxzd.efcs.report.application.dtos.FormatStatisticsDto;
import com.wxzd.efcs.report.application.dtos.TestStatisticsDto;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 生产仪表盘
 * <p>
 * Created by jade on 2017/4/14.
 */
@Module(ModuleEfcsDashboard.name)
@Controller
@RequestMapping
public class ProductionController {

    @Resource
    private StorageDetailAppService storageDetailAppService;

    /**
     * 概要
     */
    @RequestMapping
    public void index_view(Model model) {
        List<MeterCounts> meterCountses = getProductDashboardStatistics();
        List<DashBoardStatisticsDto> dashBoardStatisticsDtos = getCabelStatistics();

        model.addAttribute("dashBoardStatisticsDtos", dashBoardStatisticsDtos);
        model.addAttribute("meterCountses", meterCountses);
    }

    /**
     * 获取生产统计
     *
     * @return
     */
    @RequestMapping
    @ResponseBody
    public List<MeterCounts> getProductDashboardStatistics() {
        return storageDetailAppService.getProductDashboardStatistics();
    }

    /**
     * 获取拉线统计
     */
    @RequestMapping
    @ResponseBody
    public List<DashBoardStatisticsDto> getCabelStatistics() {
        return storageDetailAppService.getDashBoardStatistics();
    }

    /**
     * 详情
     */
    @RequestMapping
    public void detail_view(UUID id, String type,String houseNo, Model model) throws Exception {
        if (id == null) {
            throw new Exception("id必须");
        }
        if (StringUtl.isEqual(type, "Formation")) {
            FormatStatisticsDto formatStatisticsDto = storageDetailAppService.getFormatStatistics(id);
            model.addAttribute("formatStatisticsDto",formatStatisticsDto);
        }else{
            type = "Test";
            TestStatisticsDto testStatisticsDto = storageDetailAppService.getTestStatistics(id);
            model.addAttribute("testStatisticsDto",testStatisticsDto);
        }

        model.addAttribute("type", type);
        model.addAttribute("id", id);
        model.addAttribute("houseNo",houseNo);

    }

    /**
     * 设备异常
     */
    @RequestMapping
    public void pallet_error_view(){

    }
}
