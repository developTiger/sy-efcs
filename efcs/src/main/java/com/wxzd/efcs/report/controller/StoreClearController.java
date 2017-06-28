package com.wxzd.efcs.report.controller;

import com.wxzd.efcs.business.application.dtos.StorageListDto;
import com.wxzd.efcs.business.application.service.ManualProcedureAppService;
import com.wxzd.efcs.report.ModuleEfcsReport;
import com.wxzd.efcs.report.application.querys.StorageListExQuery;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.wms.core.application.dtos.WarehouseDto;
import com.wxzd.wms.core.application.queryService.SkuQueryService;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 库存清除
 * Created by jade on 2017/6/13.
 */
@Module(ModuleEfcsReport.name)
@Controller
@RequestMapping
public class StoreClearController {
    @Resource
    private SkuQueryService skuQueryService;

    @Resource
    private StorageDetailAppService storageDetailAppService;


    @Resource
    private WareHouseAppService wareHouseAppService;

    @Resource
    private ManualProcedureAppService manualProcedureAppService;

    /**
     * 展示
     */
    @RequestMapping
    public void index_view(Model model) {
        ListResult<WarehouseDto> wareHouseResult = wareHouseAppService.getAllStorehouse();
        model.addAttribute("houseDto", wareHouseResult.getItems());
    }

    /**
     * 数据
     */
    @RequestMapping
    @ResponseBody
    public PageResult<StorageListDto> grid(StorageListExQuery exQuery) {
        exQuery.setRow(10000);
        PageResult<StorageListDto> res = storageDetailAppService.getStorageRealTimePaged(exQuery);

        PageResult<StorageListDto> res1 = new PageResult<>();
        if(res !=null){
            List<StorageListDto> list = res.getRows();
            res1.setPage(1);
            for (StorageListDto item : list){
                if(item.getSto_type() == StorageType.container){
                    res1.getRows().add(item);
                }
            }
            int size = res1.getRows().size();
            res1.setRow(size);
            res1.setTotal(size);
            res1.setTotalPage(1);
        }
        return res1;
    }

    /**
     * 清除
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult clear (String houseNo, String ids, String locNo){

        String[] idArr = ids.split(",");
        List<String> listId = Arrays.asList(idArr);
        return manualProcedureAppService.clearCatchLocationStorage(houseNo,locNo,listId);
    }
}
