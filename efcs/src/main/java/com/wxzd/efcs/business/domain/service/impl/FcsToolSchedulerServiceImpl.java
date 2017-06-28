package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.efcs.business.domain.service.FcsToolSchedulerService;
import com.wxzd.efcs.business.repositorys.FcsToolSchedulerLocationsRepository;
import com.wxzd.efcs.business.repositorys.FcsToolSchedulerRepository;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.SerialNoGenerator;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/29.
 */
@Service
public class FcsToolSchedulerServiceImpl implements FcsToolSchedulerService {

    @Autowired
    FcsToolSchedulerRepository fcsToolSchedulerRepository;
    @Autowired
    FcsToolSchedulerLocationsRepository fcsToolSchedulerLocationsRepository;

    @Autowired
    LocationAllotQyery locationAllotQyery;

    /**
     * 根据状态获取工装计划
     *
     * @param status
     * @return
     */
    @Override
    public List<FcsToolScheduler> getSchedulesByStatus(SchedulerStatus status) {
        return fcsToolSchedulerRepository.getByStatus(status);
    }

    /**
     * 根据计划标识获取需处理工单
     *
     * @param scheduleId
     * @param status
     * @return
     */
    @Override
    public List<FcsToolSchedulerLocations> getSchedulesByStatus(UUID scheduleId, AppointmentStatus status) {
        return fcsToolSchedulerLocationsRepository.getByStatus(scheduleId,status);
    }

    /**
     * 根据计划编号获取需处理工单
     *
     * @param scheduleNo
     * @param status
     * @return
     */
    @Override
    public List<FcsToolSchedulerLocations> getSchedulesByStatus(String scheduleNo, AppointmentStatus status) {
        return fcsToolSchedulerLocationsRepository.getByStatus(scheduleNo,status);
    }

    /**
     * 获取目前所有需要执行的计划
     *
     * @return
     */
    @Override
    public List<FcsToolScheduler> getLiveSchedules() {
        return fcsToolSchedulerRepository.getLiveScheduler();
    }

    /**
     * 获取目前所有需要执行的工装单
     *
     * @return
     */
    @Override
    public List<FcsToolSchedulerLocations> getLiveLocations(UUID scheduleId) {
        return fcsToolSchedulerLocationsRepository.getLiveLocations(scheduleId);
    }

    /**
     * 获取目前所有需要执行的工装单
     *
     * @return
     */
    @Override
    public List<FcsToolSchedulerLocations> getLiveLocations(String scheduleNo) {
        return fcsToolSchedulerLocationsRepository.getLiveLocations(scheduleNo);
    }

    /**
     * 调度设置当前工装所在位置
     *
     * @param houseNo
     * @param toolNo
     * @param currentLocationNo
     * @return
     */
    @Override
    public ObjectResult<Boolean> setToolCurrentLocation(String houseNo, String toolNo, String currentLocationNo) {
        FcsToolScheduler scheduler = fcsToolSchedulerRepository.getByCondition(
                houseNo, toolNo, SchedulerStatus.Running);
        if (null == scheduler) return new ObjectResult<>(false, "库号：" + houseNo + "工装号:" + toolNo + "不存在或不处于运行状态");
        scheduler.setCurrent_location_no(currentLocationNo);
        UUID scheduleId = fcsToolSchedulerRepository.saveById(scheduler);
        return new ObjectResult<>(null != scheduleId, "");
    }

    /**
     * @param houseNo    工装单
     * @param toolNo     toolNo 工装编号
     * @param locationNo locationNo  执行工装的化成库位
     * @return 返回是否需要继续执行，如果当前Location对应的Schedule已经全部完成表示不需要继续执行
     */
    @Override
    public ObjectResult<Boolean> setLocationWorkDoneNeedContinue(String houseNo, String toolNo, String locationNo) {

        FcsToolScheduler scheduler = fcsToolSchedulerRepository.getByCondition(
                houseNo, toolNo, SchedulerStatus.Running);//返回的计划任务
        if(null== scheduler)//手动停止的计划仍需等待工单执行结束
            scheduler = fcsToolSchedulerRepository.getByCondition(
                    houseNo, toolNo, SchedulerStatus.Stop);//返回的计划任务

        FcsToolSchedulerLocations location = fcsToolSchedulerLocationsRepository.getByWordDoneCondition(
                houseNo, toolNo, locationNo, AppointmentStatus.Appointmented);//返回的工单
        //TODO location done
        location.setAppointment_status(AppointmentStatus.Finished);
        location.setDescription("调度执行结束");
        location.setComplete_time(new Date());
        fcsToolSchedulerLocationsRepository.saveById(location);

        scheduler.setComplete_loc_count(scheduler.getComplete_loc_count() + 1);

        //校验计划是否需要继续分配工单
        List<FcsToolSchedulerLocations> waitingLocations = getSchedulesByStatus(scheduler.getSc_no(), AppointmentStatus.Waiting);//计划下待执行的工单
        if (null != waitingLocations && waitingLocations.size() > 0)//need continue
            return new ObjectResult<>(true, "continue");
        else {//不需要继续分配工单
            List<FcsToolSchedulerLocations> runningLocations = getSchedulesByStatus(scheduler.getSc_no(), AppointmentStatus.Appointmented);//计划下执行中的工单
            if (null == runningLocations || runningLocations.size() == 0)//如果工单全部执行结束
            {//计划工单已全部完成
                if (scheduler.getScheduler_end_time().after(new Date()))//如果本次计划执行结束而计划截止时间未到则重新绑定新工单
                {
                    scheduler.setScheduler_status(SchedulerStatus.OnScheduler);
                    scheduler.executeNextTime();
                    //TODO 更新计划完成并重新为计划绑定工单，重新绑定正常完成的工单
                    List<FcsToolSchedulerLocations> finishLocations = getSchedulesByStatus(scheduler.getSc_no(), AppointmentStatus.Finished);//计划下执行中的工单
                    List<FcsToolSchedulerLocations> newLocations = new ArrayList<>();
                    for (FcsToolSchedulerLocations finishLocation : finishLocations) {
                        FcsToolSchedulerLocations newLocation = //重新绑定时ScheduleNo将重新自动生成
                                new FcsToolSchedulerLocations(null, finishLocation.getTool_type(),
                                        finishLocation.getTool_no(), scheduler.getId(), finishLocation.getLocation_id(),
                                        finishLocation.getLocation_no(), finishLocation.getPriority());
                        newLocations.add(newLocation);
                    }
                    innerResetToolScheduler(scheduler, newLocations);
                } else {//否则计划直接完成
                    scheduler.setScheduler_status(SchedulerStatus.Finish);
                }
                scheduler.setRun_times(scheduler.getRun_times() + 1);
            }
            return new ObjectResult<>(false, "库号：" + houseNo + ",工装设备号：" + toolNo + ",库位号：" + locationNo);
        }
    }

    /**
     * 根据组合条件获取工装单
     *
     * @param houseNo    库号
     * @param toolNo     工装编号（拖盘）
     * @param locationNo 库位号
     * @return
     */
    @Override
    public ObjectResult<FcsToolSchedulerLocations> getLocation(String houseNo, String toolNo, String locationNo) {
        if (StringUtl.isEmpty(houseNo) || StringUtl.isEmpty(toolNo) || StringUtl.isEmpty(locationNo))
            return new ObjectResult<>(false, "param id is null");
        FcsToolSchedulerLocations locations = fcsToolSchedulerLocationsRepository.getByWordDoneCondition(
                houseNo, toolNo, locationNo, AppointmentStatus.Appointmented);

        ObjectResult<FcsToolSchedulerLocations> locationsObjectResult =
                new ObjectResult<>(locations != null, "库号：" + houseNo + ",工装设备号：" + toolNo + ",库位号：" + locationNo);
        locationsObjectResult.setObject(locations);
        return locationsObjectResult;
    }

    @Override
    public FcsToolScheduler getById(UUID id) {
        return fcsToolSchedulerRepository.getById(id);
    }

    /**
     * 按调度计划编号获取调度计划
     *
     * @param scheduleNo
     * @return
     */
    @Override
    public FcsToolScheduler getByNo(String scheduleNo) {
        return fcsToolSchedulerRepository.getByNo(scheduleNo.trim());
    }

    /**
     * 按照工单标识获取工单信息
     *
     * @param scheduleLocationId 工单编号
     * @return
     */
    @Override
    public FcsToolSchedulerLocations getLocation(UUID scheduleLocationId) {
        return fcsToolSchedulerLocationsRepository.getById(scheduleLocationId);
    }

    /**
     * 新增计划
     *
     * @param scheduler
     * @param locationIds
     * @return
     */
    @Override
    public Boolean createToolScheduler(FcsToolScheduler scheduler, List<UUID> locationIds) {
        scheduler.setRun_times(0);
        return resetToolScheduler(scheduler, locationIds);
    }

    /**
     * 校验计划是否存在
     *
     * @param schedulerName
     * @param houseNo
     * @param toolNo
     * @param schedulerType
     * @return
     */
    @Override
    public Boolean checkToolScheduler(String schedulerName, String houseNo, String toolNo, SchedulerType schedulerType) {
       return null== fcsToolSchedulerRepository.getByCondition(houseNo,toolNo,schedulerType) &&
               null ==fcsToolSchedulerRepository.getByName(schedulerName);
    }

    /**
     * 重置更新计划
     *
     * @param scheduler
     * @param locationIds
     * @return
     */
    @Override
    public Boolean resetToolScheduler(FcsToolScheduler scheduler, List<UUID> locationIds) {
        //TODO remove exist binded location
        removeAllLocations(scheduler.getSc_no(), "数据初始化");

        List<StorageLocation> locations = locationAllotQyery.getLocationByLocationIds(locationIds);
        final List<FcsToolSchedulerLocations> createLocations = new ArrayList<>();
        for (Integer i = 0; i < locations.size(); i++) {
            FcsToolSchedulerLocations location =
                    new FcsToolSchedulerLocations(scheduler.getSc_no(), scheduler.getTool_type(),
                            scheduler.getTool_no(), scheduler.getId(),
                            locations.get(i).getId(), locations.get(i).getLoc_no(),0);
            createLocations.add(location);
        }
        scheduler.setSc_no(SerialNoGenerator.getSerialNo("TRS"));
        scheduler.setScheduler_status(SchedulerStatus.OnScheduler);
        scheduler.executeNextTime();
        scheduler.setTotal_loc_count(createLocations.size());
        fcsToolSchedulerRepository.saveById(scheduler);

        //TODO location and schedule store
        return bindToolSchedulerLocations(scheduler, createLocations);
    }
    /**
     * 重置更新计划
     *
     * @param scheduler
     * @param locations
     * @return
     */
    private Boolean innerResetToolScheduler(FcsToolScheduler scheduler, List<FcsToolSchedulerLocations> locations) {
        scheduler.setSc_no(SerialNoGenerator.getSerialNo("TRS"));
        scheduler.setScheduler_status(SchedulerStatus.OnScheduler);
        scheduler.executeNextTime();
        scheduler.setComplete_loc_count(locations.size());
        fcsToolSchedulerRepository.saveById(scheduler);

        //TODO remove exist binded location
        removeAllLocations(scheduler.getSc_no(), "计划结束");

        //TODO location and schedule store
        return bindToolSchedulerLocations(scheduler, locations);
    }

    /**
     * 绑定任务库位
     *
     * @param scheduler
     * @param locationList
     * @return
     * @remark 绑定新库位前排除已绑定的所有任务工单。
     */
    @Override
    public Boolean bindToolSchedulerLocations(FcsToolScheduler scheduler, List<FcsToolSchedulerLocations> locationList) {
        for (FcsToolSchedulerLocations location : locationList) {
            location.setAppointment_status(AppointmentStatus.Waiting);
            location.setSc_no(scheduler.getSc_no());
            location.setScheduler_id(scheduler.getId());
        }
        return !fcsToolSchedulerLocationsRepository.saveById(locationList).isEmpty();
    }

    /**
     * 移除计划下所有工单
     *
     * @param scheduleNo
     * @param description 移除原因
     * @return
     */
    @Override
    public Boolean removeAllLocations(String scheduleNo, final String description) {
        if (StringUtl.isEmpty(scheduleNo)) return true;
        List<FcsToolSchedulerLocations> locations = fcsToolSchedulerLocationsRepository.getByScheduleNo(scheduleNo);
        return stopSchedulerLocation(locations, description, true);
    }

    /**
     * 停止任务
     *
     * @param scheduleNo
     * @param description 停止原因
     * @return
     */
    @Override
    public Boolean stopScheduler(String scheduleNo, final String description) {
        FcsToolScheduler scheduler = fcsToolSchedulerRepository.getByNo(scheduleNo);
        //MARK 此处停掉未预约的工单，在执行及已预约的工单需等待外部调用结束
        List<FcsToolSchedulerLocations> locations = fcsToolSchedulerLocationsRepository.getByStatus(scheduleNo,AppointmentStatus.Waiting);
        return stopScheduler(scheduler) && stopSchedulerLocation(locations, description, false);
    }


    /**
     * 启动任务
     *
     * @param schedulerNo 计划编号
     * @return
     */
    @Override
    public Boolean startScheduler(String schedulerNo) {
        FcsToolScheduler scheduler = fcsToolSchedulerRepository.getByNo(schedulerNo);
        List<FcsToolSchedulerLocations> locations = fcsToolSchedulerLocationsRepository.getByStatus(schedulerNo,AppointmentStatus.ErrorFinished);

        scheduler.setScheduler_status(SchedulerStatus.OnScheduler);
        for (FcsToolSchedulerLocations location : locations) {
            location.setAppointment_status(AppointmentStatus.Waiting);
        }
        fcsToolSchedulerLocationsRepository.saveById(locations);
        return null != fcsToolSchedulerRepository.saveById(scheduler);
    }

    /**
     * 删除计划
     *
     * @param uuid
     * @return
     */
    @Override
    public Boolean deleteScheduler(UUID uuid) {
        fcsToolSchedulerRepository.softDeleteById(uuid);
        List<FcsToolSchedulerLocations> locations = fcsToolSchedulerLocationsRepository.getToolLocationBySchedulerId(uuid);
        for (FcsToolSchedulerLocations l : locations) {
            l.softDelete();
        }
        return null != fcsToolSchedulerLocationsRepository.saveById(locations);
    }

    /**
     * 更新计划状态
     *
     * @param uuid
     * @param schedulerStatus
     * @return
     */
    @Override
    public Boolean updateSchedulerStatus(UUID uuid, SchedulerStatus schedulerStatus) {
        FcsToolScheduler scheduler = fcsToolSchedulerRepository.getById(uuid);
        return null != updateSchedulerStatus(scheduler, schedulerStatus);
    }


    /**
     * 更新计划状态
     *
     * @param fcsToolScheduler
     * @param schedulerStatus
     * @return
     */
    private Boolean updateSchedulerStatus(FcsToolScheduler fcsToolScheduler, SchedulerStatus schedulerStatus) {
        fcsToolScheduler.setScheduler_status(schedulerStatus);

        return null != fcsToolSchedulerRepository.saveById(fcsToolScheduler);
    }

    /**
     * 计划是否已结束
     *
     * @param schedulerNo
     * @return
     */
    @Override
    public Boolean isSchedulerComplete(String schedulerNo) {
        List<FcsToolSchedulerLocations> locations = fcsToolSchedulerLocationsRepository.getLiveLocations(schedulerNo);
        return locations.size() == 0;
    }

    @Override
    /**
     * 校验当前计划是否存在在执行及已预约的工单
     * @param schedulerNo
     * @return
     */
    public Boolean isSchedulerHasRunningLocations(String schedulerNo) {
        List<FcsToolSchedulerLocations> locations = fcsToolSchedulerLocationsRepository.getOnSchedulerLocations(schedulerNo);
        return locations.size() == 0;
    }

    //region inner method

    /**
     * 完成计划工单
     *
     * @param locations
     * @param description
     * @return
     */
    private Boolean finishSchedulerLocation(List<FcsToolSchedulerLocations> locations, String description) {
        for (FcsToolSchedulerLocations location : locations) {
            location.setComplete_time(new Date());
            location.setAppointment_status(AppointmentStatus.Finished);
            location.setDescription(description);
        }
        return !fcsToolSchedulerLocationsRepository.saveById(locations).isEmpty();
    }

    /**
     * 停止工单
     *
     * @param locations
     * @param description
     * @param isDel       是否需要删除
     * @return
     */
    private Boolean stopSchedulerLocation(List<FcsToolSchedulerLocations> locations, String description, Boolean isDel) {
        if (locations.isEmpty()) return true;
        for (FcsToolSchedulerLocations location : locations) {
            location.setComplete_time(new Date());
            location.setAppointment_status(AppointmentStatus.ErrorFinished);
            location.setDescription(description);
            location.setIs_active(isDel);
        }
        return !fcsToolSchedulerLocationsRepository.saveById(locations).isEmpty();
    }

    /**
     * 停止任务
     *
     * @param scheduler
     * @return
     */
    private Boolean stopScheduler(FcsToolScheduler scheduler) {
        scheduler.setScheduler_status(SchedulerStatus.Stop);
        return null != fcsToolSchedulerRepository.saveById(scheduler);
    }

    //endregion
}
