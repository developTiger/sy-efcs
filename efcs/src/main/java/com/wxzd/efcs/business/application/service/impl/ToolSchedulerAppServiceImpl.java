package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerDto;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerLocationDto;
import com.wxzd.efcs.business.application.dtos.ToolSchedulerDto;
import com.wxzd.efcs.business.application.querys.ToolSchedulerExQuery;
import com.wxzd.efcs.business.application.service.ToolSchedulerAppService;
import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.service.FcsToolSchedulerLocationService;
import com.wxzd.efcs.business.domain.service.FcsToolSchedulerService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/30.
 */
public class ToolSchedulerAppServiceImpl implements ToolSchedulerAppService {


//    @Autowired
//    StorageLocationService storageLocationService;
//
//    @Autowired
//    LocationAllotQyery locationAllotQyery;
//
//    @Autowired
//    FcsToolSchedulerService toolSchedulerService;
//
//    @Autowired
//    FcsToolSchedulerLocationService toolSchedulerLocationService;
//
//
//    @Override
//    public ToolSchedulerDto getSchedulerByid(UUID id) {
//        return BeanUtl.copyProperties(toolSchedulerService.getById(id), ToolSchedulerDto.class);
//    }
//
//    /**
//     * 新建任务计划
//     *
//     * @param dto
//     * @return
//     */
//    @Override
//    public GaiaResult addToolScheduler(ToolSchedulerDto dto,List<UUID> locationId){
//        //TODO 是否有重复或排他任务
//        FcsToolScheduler toolScheduler = new FcsToolScheduler();
//        BeanUtl.copyProperties(dto, toolScheduler);
//
//        toolScheduler.executeNextTime();
//        List<StorageLocation> locations = new ArrayList<>();
//        if (dto.getIs_all()) {
//            List<Integer> rows = new ArrayList<>();
//            rows.add(3);
//            rows.add(4);
//
//            locations = locationAllotQyery.getAllLocationsByRows(dto.getHouse_id(), rows);
//        } else {
//            locations = locationAllotQyery.getLocationByLocationIds(dto.getLocationIds());
//        }
//        toolScheduler.setTotal_loc_count(dto.getLocationIds().size());
//        toolSchedulerService.addOrUpdateToolScheduler(toolScheduler);
//        return toolSchedulerService.bindToolSchedulerLocations(toolScheduler, locations);
//
//    }
//
//    /**
//     * 更改计划状态
//     *
//     * @param scheduleId
//     * @param schedulerStatus
//     * @return
//     */
//    @Override
//    public GaiaResult updateSchedulerStatus(UUID scheduleId, SchedulerStatus schedulerStatus) {
//        return toolSchedulerService.updateSchedulerStatus(scheduleId, schedulerStatus);
//    }
//
//    /**
//     * 删除任务计划
//     *
//     * @param scheduleId
//     * @return
//     */
//    @Override
//    public GaiaResult deleteScheduler(UUID scheduleId) {
//        return toolSchedulerService.deleteScheduler(scheduleId);
//    }
//
//    /**
//     * 重新设置计划库位
//     *
//     * @param schedulerId
//     * @param locationIds
//     * @return
//     */
//    @Override
//    public GaiaResult resetSchedulerLocations(UUID schedulerId, List<UUID> locationIds) {
//        FcsToolScheduler toolScheduler = toolSchedulerService.getById(schedulerId);
//        toolScheduler.executeNextTime();
//        List<StorageLocation> locations = new ArrayList<>();
//        locations = locationAllotQyery.getLocationByLocationIds(locationIds);
//        toolScheduler.setTotal_loc_count(locationIds.size());
//        toolSchedulerService.addOrUpdateToolScheduler(toolScheduler);
//        return toolSchedulerService.bindToolSchedulerLocations(toolScheduler, locations);
//    }
//
//    /**
//     * 修改计划库位状态
//     *
//     * @param schedulerId
//     * @param appointmentStatus
//     * @return
//     */
//    @Override
//    public GaiaResult setLocationStatus(UUID schedulerId, AppointmentStatus appointmentStatus) {
//        return toolSchedulerLocationService.setLocationStatus(schedulerId,appointmentStatus);
//    }
//
//    /**
//     * 获取计划
//     *
//     * @param query
//     * @return
//     */
//    @Override
//    public List<FcsToolSchedulerDto> getAllScheduler(ToolSchedulerExQuery query) {
//        return null;
//    }
//
//    /**
//     * 分页获取计划
//     *
//     * @param query
//     * @return
//     */
//    @Override
//    public PageResult<FcsToolSchedulerDto> getSchedulerPaged(ToolSchedulerExQuery query) {
//        return null;
//    }
//
//    @Override
//    public List<FcsToolSchedulerLocationDto> getSchedulerLoations(UUID scheduleId) {
//        return null;
//    }
//
//    /**
//     * @param scheduleId
//     * @return
//     */
//    @Override
//    public PageResult<FcsToolSchedulerLocationDto> getSchedulerLoationsPaged(UUID scheduleId) {
//        return null;
//    }

}
