package com.wxzd.efcs.business.controller;

import com.wxzd.configration.catlConfig.AlarmInfoConfig;
import com.wxzd.efcs.business.ModuleEfcsBusiness;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerDto;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerLocationDto;
import com.wxzd.efcs.business.application.querys.FcsToolSchedulerExQuery;
import com.wxzd.efcs.business.application.service.FcsToolSchedulerAppService;
import com.wxzd.efcs.report.application.dtos.LocationChartExtendDto;
import com.wxzd.efcs.report.application.service.StorageDetailAppService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.wms.core.application.dtos.ContainerDto;
import com.wxzd.wms.core.application.dtos.ContainerTypeDto;
import com.wxzd.wms.core.application.dtos.WarehouseDto;
import com.wxzd.wms.core.application.queryService.ContainerQueryService;
import com.wxzd.wms.core.application.queryService.ContainerTypeQueryService;
import com.wxzd.wms.core.application.querys.ContainerExQuery;
import com.wxzd.wms.core.application.querys.ContainerTypeExQuery;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jade on 2017/6/16.
 * 工装计划
 */
@Module(ModuleEfcsBusiness.name)
@Controller
@RequestMapping
public class WorkSchedulerController {

    @Resource
    private FcsToolSchedulerAppService fcsToolSchedulerAppService;

    @Resource
    private WareHouseAppService wareHouseAppService;

    @Resource
    private StorageDetailAppService storageDetailAppService;

    @Resource
    private ContainerTypeQueryService containerTypeQueryService;

    @Resource
    private ContainerQueryService containerQueryService;

    /**
     * 电池托盘类型
     */
    private static String Battery_Type_Name = "1.5P电池";

    @RequestMapping
    public void index_view(Model model) {
        ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();
        model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());
    }

    @RequestMapping
    @ResponseBody
    public PageResult<FcsToolSchedulerDto> grid(FcsToolSchedulerExQuery query) {
        return fcsToolSchedulerAppService.findToolSchedulesPaged(query);
    }

    /**
     * 计划创建页面
     */
    @RequestMapping
    public void form_view(Model model, UUID id) {
        ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();
        model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());

        ListResult<ContainerTypeDto> containerTypeResult = containerTypeQueryService.gridNoPage(new ContainerTypeExQuery());
        if (containerTypeResult != null) {
            List<ContainerTypeDto> containerTypeDtos = containerTypeResult.getItems();
            for (int i = 0; i < containerTypeDtos.size(); i++) {
                if (containerTypeDtos.get(i).getContainer_name().indexOf(Battery_Type_Name) > -1) {
                    containerTypeDtos.remove(i);
                }
            }
            model.addAttribute("containerTypeDtos", containerTypeDtos);
        }

        if (id != null) {
            ObjectResult<FcsToolSchedulerDto> fcsToolSchedulerDto = fcsToolSchedulerAppService.getById(id);
            model.addAttribute("schedulerDto", fcsToolSchedulerDto.getObject());
            //todo 获取库位号
            ListResult<FcsToolSchedulerLocationDto> listResult = fcsToolSchedulerAppService.getScheduleLocations(fcsToolSchedulerDto.getObject().getId());
            if (listResult.isSuccess()) {
                String location_ids ="";
                if (listResult.getItems().size() > 0) {
                    for (FcsToolSchedulerLocationDto dto : listResult.getItems()) {
                        location_ids+=","+dto.getId();
                    }
                    model.addAttribute("location_ids",location_ids.substring(1));
                }

            }
        }
    }

        /**
         * 获取工装类型对应的托盘
         *
         * @param query
         * @return
         */
        @RequestMapping
        @ResponseBody
        public ListResult<ContainerDto> getContainer (ContainerExQuery query){
            return containerQueryService.detailGridNoPage(query);
        }

        /**
         * 化成库位页面
         *
         * @param model
         */
        @RequestMapping
        public void location_view (Model model, UUID sc_id){
            ListResult<WarehouseDto> wareHouseDtos = wareHouseAppService.getAllStorehouse();
            model.addAttribute("rowMap", AlarmInfoConfig.getRowMap1());
            if (wareHouseDtos != null) {
                model.addAttribute("wareHouseDtos", wareHouseDtos.getItems());
            }
            if (sc_id != null) {
                ListResult<FcsToolSchedulerLocationDto> result = fcsToolSchedulerAppService.getScheduleLocations(sc_id);
                model.addAttribute("locationDtos", result.getItems());
            }
        }

        /**
         * 化成库位信息获取
         *
         * @param houseId
         * @param type
         * @param x
         * @return
         */
        @RequestMapping
        @ResponseBody
        public List<LocationChartExtendDto> getLocationInfos (UUID houseId, UUID type, String x){
            return storageDetailAppService.getLocationInfosForCharts(houseId, null, Integer.valueOf(x));
        }

        /**
         * 计划创建
         *
         * @param fcsToolSchedulerDto
         * @param ids
         * @return
         */
        @RequestMapping
        @ResponseBody
        public GaiaResult create (FcsToolSchedulerDto fcsToolSchedulerDto, String ids, UUID id){
            List<UUID> locationIds = new ArrayList<>();
            if (!"".equals(ids)) {
                String[] arr = ids.split(",");
                for (String item : arr) {
                    locationIds.add(UUID.fromString(item));
                }
            }

            if (id == null) {
                return fcsToolSchedulerAppService.addToolScheduler(fcsToolSchedulerDto, locationIds);
            } else {
                return fcsToolSchedulerAppService.resetScheduleLocation(fcsToolSchedulerDto, locationIds);
            }

        }

        /**
         * 任务列表
         */
        @RequestMapping
        public void task_view (UUID sc_id, Model model){
            model.addAttribute("sc_id", sc_id);
        }

        /**
         * 根据计划id获取任务工单
         *
         * @param sc_id
         */
        @RequestMapping
        @ResponseBody
        public ListResult<FcsToolSchedulerLocationDto> task_grid (UUID sc_id){
            return fcsToolSchedulerAppService.getScheduleLocations(sc_id);
        }


    /**
     * 修改状态启动与停止
     * @param sc_no
     */
    @RequestMapping
    @ResponseBody
    public GaiaResult updateststus(String sc_no ,String selectvalue){
        if("Running".equals(selectvalue)){//调用启动接口
            return fcsToolSchedulerAppService.startScheduler(sc_no);
        }else{//调用停止接口
            return fcsToolSchedulerAppService.stopScheduler(sc_no);
        }
    }
    }
