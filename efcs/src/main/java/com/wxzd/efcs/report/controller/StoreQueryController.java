package com.wxzd.efcs.report.controller;

import com.wxzd.efcs.business.application.dtos.StorageListDto;
import com.wxzd.efcs.report.ModuleEfcsReport;
import com.wxzd.efcs.report.application.querys.StorageListExQuery;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.wms.core.application.queryService.SkuQueryService;
import com.wxzd.wms.core.application.querys.SkuExQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 实时库存查询
 * Created by jade on 2017/4/18.
 */
@Module(ModuleEfcsReport.name)
@Controller
@RequestMapping
public class StoreQueryController {

    @Resource
    private SkuQueryService skuQueryService;

    @Resource
    private StorageDetailAppService storageDetailAppService;

    /**
     * 展示
     */
    @RequestMapping
    public void index_view(Model model) {
        model.addAttribute("skuInfos", skuQueryService.getSkuInfos(new SkuExQuery()).getItems());
    }

    /**
     * 数据
     */
    @RequestMapping
    @ResponseBody
    public PageResult<StorageListDto> grid(StorageListExQuery exQuery) {
        return storageDetailAppService.getStorageRealTimePaged(exQuery);
    }
}
