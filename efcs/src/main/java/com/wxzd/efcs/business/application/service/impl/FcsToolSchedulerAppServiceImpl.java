package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.FcsSchedulerDto;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerDto;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerLocationDto;
import com.wxzd.efcs.business.application.dtos.FmProcedureDto;
import com.wxzd.efcs.business.application.querys.FcsToolSchedulerExQuery;
import com.wxzd.efcs.business.application.querys.FcsToolSchedulerLocationExQuery;
import com.wxzd.efcs.business.application.service.FcsToolSchedulerAppService;
import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.efcs.business.domain.service.FcsToolSchedulerService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.*;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.SerialNoGenerator;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/29.
 */
@Service
public class FcsToolSchedulerAppServiceImpl implements FcsToolSchedulerAppService {

    @Resource
    private FcsToolSchedulerService schedulerService;

    // region schedule

    /**
     * 按标识获取工装计划
     *
     * @param schedulerId
     * @return
     */
    @Override
    public ObjectResult<FcsToolSchedulerDto> getById(UUID schedulerId) {
        try {
            ObjectResult result;
            FcsToolScheduler scheduler = schedulerService.getById(schedulerId);
            if (null == schedulerId || null == scheduler)
                result = new ObjectResult(false, "param is null or obj not's exist");
            else
                result = GaiaResultFactory.getObject(BeanUtl.copyProperties(scheduler, FcsToolSchedulerDto.class));
            return result;
        } catch (Exception ex) {
            return new ObjectResult<>(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    /**
     * 按调度计划编号获取调度计划
     *
     * @param scheduleNo
     * @return
     */
    @Override
    public ObjectResult<FcsToolSchedulerDto> getByNo(String scheduleNo) {
        try {
            ObjectResult result;
            FcsToolScheduler scheduler = schedulerService.getByNo(scheduleNo);
            if (null == scheduleNo || null == scheduler)
                result = new ObjectResult(false, "param is null or obj not's exist");
            else
                result = GaiaResultFactory.getObject(BeanUtl.copyProperties(scheduler, FcsToolSchedulerDto.class));
            return result;
        } catch (Exception ex) {
            return new ObjectResult<>(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }


    /**
     * 按照指定条件组合查询ToolSchedule
     *
     * @param query 工装计划查询对象
     * @return
     */
    @Override
    public ListResult<FcsToolSchedulerDto> findToolSchedules(FcsToolSchedulerExQuery query) {
        try {
            GaiaQuery gaiaQuery = getScheduleQuery(query);
            return GaiaResultFactory.getCollection(
                    DatabaseExecuter.queryBeanList(gaiaQuery.getQuerySql(), gaiaQuery.getMap(), FcsToolSchedulerDto.class));
        } catch (Exception ex) {
            return new ListResult<>(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    /**
     * 按照条件分页获取ToolSchedule
     *
     * @param query 工装计划查询对象
     * @return
     */
    @Override
    public PageResult<FcsToolSchedulerDto> findToolSchedulesPaged(FcsToolSchedulerExQuery query) {
        try {
            GaiaQuery innerQuery = getScheduleQuery(query);
            return DatabaseExecuter.queryBeanPaged(
                    innerQuery.getQuerySql(), innerQuery.getMap(), query.getPage(), query.getRow(), FcsToolSchedulerDto.class);
        } catch (Exception ex) {
            return new PageResult<>();//FIXME why PageResult not's implement GaiaResult
        }
    }


    /**
     * 新增计划
     *
     * @param scheduler
     * @param locationIds
     * @return
     */
    @Override
    public GaiaResult addToolScheduler(FcsToolSchedulerDto scheduler, List<UUID> locationIds) {
        try {
            final FcsToolScheduler createScheduler = BeanUtl.copyProperties(scheduler, FcsToolScheduler.class);
            //TODO确认重新写入必要条件
            createScheduler.setSc_no(SerialNoGenerator.getSerialNo("TRS"));
            createScheduler.setScheduler_status(SchedulerStatus.OnScheduler);
            createScheduler.setRun_times(0);
            createScheduler.setTotal_loc_count(locationIds.size());
            createScheduler.setNext_run_time(new Date());
            if (isToolSchedulerCanCreate(scheduler.getSc_name(), scheduler.getHouse_no(), scheduler.getTool_no(), scheduler.getScheduler_type())) {
                schedulerService.createToolScheduler(createScheduler, locationIds);
                return GaiaResultFactory.getSuccess();
            }
            return GaiaResultFactory.getError(
                    String.format("param error：scName:%s,houseNo:%s,toolNo:%s,ScheduleType:%s"
                            , scheduler.getSc_name(), scheduler.getHouse_no(), scheduler.getScheduler_type().toString()));


        } catch (Exception ex) {
            return GaiaResultFactory.getError(ex.getMessage() + "\r\n" + ex.getStackTrace().toString());

        }
    }


    /**
     * 重置已存在计划工作单
     *
     * @param scheduler
     * @param locationIds
     * @return
     */
    @Override
    public GaiaResult resetScheduleLocation(FcsToolSchedulerDto scheduler, List<UUID> locationIds) {
        try {
            //TODO 确保没有在执行或者已预约的工单，否则会造成工单执行反馈对接异常
            if(!schedulerService.isSchedulerHasRunningLocations(scheduler.getSc_no()))
                return new GaiaResult(false, "工装计划仍有在执行工单，请先停止计划并确保在执行跟已预约的工单执行完成后再重置计划" );

            final FcsToolScheduler existScheduler = schedulerService.getById(scheduler.getId());
            final FcsToolScheduler resetScheduler = BeanUtl.copyProperties(existScheduler, FcsToolScheduler.class);
            resetScheduler.setSc_name(scheduler.getSc_name());
            resetScheduler.setScheduler_start_time(scheduler.getScheduler_start_time());
            resetScheduler.setScheduler_end_time(scheduler.getScheduler_end_time());
            resetScheduler.setDescription(scheduler.getDescription());
            schedulerService.resetToolScheduler(resetScheduler, locationIds);
            return GaiaResultFactory.getSuccess();
        } catch (Exception ex) {
            return new GaiaResult(false, ex.getMessage() + "\r\n" + ex.getStackTrace().toString());
        }
    }


    /**
     * 停止任务
     *
     * @param schedulerNo
     * @return
     */
    @Override
    public GaiaResult stopScheduler(String schedulerNo) {
        try {
            return schedulerService.stopScheduler(schedulerNo, "手动停止任务")
                    ? GaiaResultFactory.getSuccess() : GaiaResultFactory.getError("stop error");
        } catch (Exception ex) {
            return new GaiaResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }


    /**
     * 启动任务
     *
     * @param schedulerNo
     * @return
     */
    @Override
    public GaiaResult startScheduler(String schedulerNo) {
        try {
            return schedulerService.startScheduler(schedulerNo)
                    ? GaiaResultFactory.getSuccess() : GaiaResultFactory.getError("start error");

        } catch (Exception ex) {
            return new GaiaResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    /**
     * 删除计划
     *
     * @param schedulerId
     * @return
     */
    @Override
    public GaiaResult deleteScheduler(UUID schedulerId) {
        try {
            return schedulerService.deleteScheduler(schedulerId)
                    ? GaiaResultFactory.getSuccess() : GaiaResultFactory.getError("delete error");
        } catch (Exception ex) {
            return new GaiaResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }
//
//    /**
//     * 更新计划状态
//     *
//     * @param schedulerId
//     * @param schedulerStatus
//     * @return
//     */
//    @Override
//    public GaiaResult updateSchedulerStatus(UUID schedulerId, SchedulerStatus schedulerStatus) {
//        try {
//            return schedulerService.updateSchedulerStatus(schedulerId, schedulerStatus)
//                    ? GaiaResultFactory.getSuccess() : GaiaResultFactory.getError("update error");
//
//        } catch (Exception ex) {
//            return new GaiaResult(false, ex.getMessage() + ex.getStackTrace());
//        }
//    }

    /**
     * 计划是否已结束
     *
     * @param schedulerNo
     * @return
     */
    @Override
    public GaiaResult isSchedulerComplete(String schedulerNo) {
        try {
            return new GaiaResult(schedulerService.isSchedulerComplete(schedulerNo), "success");
        } catch (Exception ex) {
            return new GaiaResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    //endregion

    //region location

    /**
     * 获取目前所有在执行的计划
     *
     * @return
     */
    @Override
    public ListResult<FcsToolSchedulerDto> getLiveToolSchedules() {
        try {
            List<FcsToolScheduler> schedulers = schedulerService.getLiveSchedules();
            List<FcsToolSchedulerDto> schedulerDtos = new ArrayList<>();
            for (FcsToolScheduler location : schedulers) {
                schedulerDtos.add(BeanUtl.copyProperties(
                        location, FcsToolSchedulerDto.class));
            }
            return GaiaResultFactory.getCollection(schedulerDtos);
        } catch (Exception ex) {
            return new ListResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    /**
     * 获取目前所有需要执行的工装单
     *
     * @return
     */
    @Override
    public ListResult<FcsToolSchedulerLocationDto> getLiveToolSchedulerLocations(String scheduleNo) {
        try {
            //FIXME Exception deal
            List<FcsToolSchedulerLocations> locations = schedulerService.getLiveLocations(scheduleNo);
            List<FcsToolSchedulerLocationDto> locationDtos = new ArrayList<>();
            for (FcsToolSchedulerLocations location : locations) {
                locationDtos.add(BeanUtl.copyProperties(
                        location, FcsToolSchedulerLocationDto.class));
            }
            return GaiaResultFactory.getCollection(locationDtos);
        } catch (Exception ex) {
            return new ListResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }


    /**
     * 按工单标识获取绑定的工装单
     *
     * @param locationId 工单标识
     * @return
     */
    @Override
    public ObjectResult<FcsToolSchedulerLocationDto> getScheduleLocation(UUID locationId) {
        try {
            //FIXME Exception deal
            FcsToolSchedulerLocations locations = schedulerService.getLocation(locationId);
            return GaiaResultFactory.getObject(BeanUtl.copyProperties(locations, FcsToolSchedulerLocationDto.class));
        } catch (Exception ex) {
            return new ObjectResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    /**
     * 按计划标识获取工装单
     *
     * @param schedulerId 计划标识
     * @return
     */
    @Override
    public ListResult<FcsToolSchedulerLocationDto> getScheduleLocations(UUID schedulerId) {
        try {
            //FIXME Exception deal
            FcsToolSchedulerLocationExQuery localQuery = new FcsToolSchedulerLocationExQuery();
            localQuery.setScheduler_id(schedulerId);
            GaiaQuery query = getLocalQuery(localQuery);
            return GaiaResultFactory.getCollection(
                    DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FcsToolSchedulerLocationDto.class));
        } catch (Exception ex) {
            return new ListResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    /**
     * 按计划编号获取工装单
     *
     * @param sc_no 计划编号
     * @return
     */
    @Override
    public ListResult<FcsToolSchedulerLocationDto> getScheduleLocations(String sc_no) {
        try {
            //FIXME Exception deal
            FcsToolSchedulerLocationExQuery localQuery = new FcsToolSchedulerLocationExQuery();
            localQuery.setSc_no(sc_no);
            GaiaQuery query = getLocalQuery(localQuery);
            return GaiaResultFactory.getCollection(
                    DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FcsToolSchedulerLocationDto.class));
        } catch (Exception ex) {
            return new ListResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    /**
     * 按工装单状态获取工装单
     *
     * @param scId   计划标识
     * @param status 工装单状态
     * @return
     */
    @Override
    public ListResult<FcsToolSchedulerLocationDto> getScheduleLocationsByStatus(UUID scId, AppointmentStatus status) {
        try {
            //FIXME Exception deal
            FcsToolSchedulerLocationExQuery localQuery = new FcsToolSchedulerLocationExQuery();
            if (null != scId)
                localQuery.setScheduler_id(scId);
            if (null != status)
                localQuery.setAppointment_status(status);
            GaiaQuery query = getLocalQuery(localQuery);
            return GaiaResultFactory.getCollection(
                    DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FcsToolSchedulerLocationDto.class));
        } catch (Exception ex) {
            return new ListResult(false, ex.getMessage() + ex.getStackTrace().toString());
        }
    }
    //endregion

    //region inner method
//
//    private String checkSchedule(String schelueName, String houseNo, String toolNo, SchedulerType schedulerType) {
//        StringBuilder sql = new StringBuilder("select ")
//                .append(SqlUtl.getColumns(FcsToolSchedulerDto.class))
//                .append(" from fcs_tool_scheduler ")
//                .append(" where is_active = '1' ");
//        if (!StringUtl.isEmpty(schelueName)) {
//            sql.append(" and sc_name = '" + schelueName.trim() + "'");
//        }
//        if (!StringUtl.isEmpty(houseNo)) {
//            sql.append(" and house_no =  '" + houseNo.trim() + "'");
//        }
//        if (!StringUtl.isEmpty(toolNo)) {
//            sql.append(" and tool_no = '" + toolNo.trim() + "'");
//        }
//        if (schedulerType != null) {
//            sql.append(" and scheduler_type =" + schedulerType);
//        }
//        return sql.toString();
//    }

    private GaiaQuery getScheduleQuery(FcsToolSchedulerExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select ")
                .append(SqlUtl.getColumns(FcsToolSchedulerDto.class))
                .append(" from fcs_tools_scheduler ")
                .append(" where is_active = '1' ");
        if (exQuery != null) {
            if (!StringUtl.isEmpty(exQuery.getSc_name())) {
                sql.append(" and sc_name like :sc_name ");
                query.putMap("sc_name", "%" + exQuery.getSc_name() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getTool_no())) {
                sql.append(" and tool_no like :tool_no ");
                query.putMap("tool_no", "%" + exQuery.getTool_no() + "%");
            }
            if (exQuery.getScheduler_type() != null) {
                sql.append(" and scheduler_type = :scheduler_type ");
                query.putMap("scheduler_type", exQuery.getScheduler_type());
            }
            if (exQuery.getScheduler_status() != null) {
                sql.append(" and scheduler_status = :scheduler_status ");
                query.putMap("scheduler_status", exQuery.getScheduler_status());
            }
        }
        sql.append(" order by create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }

    private GaiaQuery getLocalQuery(FcsToolSchedulerLocationExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select ")
                .append(SqlUtl.getColumns(FcsToolSchedulerLocationDto.class))
                .append(" from fcs_tools_scheduler_location ")
                .append(" where is_active = '1' ");
        if (exQuery != null) {
            if (!StringUtl.isEmpty(exQuery.getTool_no())) {
                sql.append(" and tool_no like :tool_no ");
                query.putMap("tool_no", "%" + exQuery.getTool_no() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getLocation_no())) {
                sql.append(" and location_no = :location_no ");
                query.putMap("location_no", "%" + exQuery.getLocation_no() + "%");
            }
            if (exQuery.getAppointment_status() != null) {
                sql.append(" and appointment_status = :appointment_status ");
                query.putMap("appointment_status", exQuery.getAppointment_status());
            }
            if (exQuery.getScheduler_id() != null) {
                sql.append(" and scheduler_id = :scheduler_id ");
                query.putMap("scheduler_id", exQuery.getScheduler_id());
            }
            if (exQuery.getSc_no() != null) {
                sql.append(" and sc_no = :sc_no ");
                query.putMap("sc_no", "%" + exQuery.getSc_no() + "%");
            }
            if (exQuery.getPriority() != null) {
                sql.append(" and priority = :priority ");
                query.putMap("priority", exQuery.getPriority());
            }
            if (exQuery.getTool_type() != null) {
                sql.append(" and tool_type = :tool_type ");
                query.putMap("tool_type", exQuery.getTool_type());
            }
            if (exQuery.getAppointment_time_from() != null) {
                sql.append(" and appointment_time >= :appointment_time_from ");
                query.putMap("appointment_time_from", exQuery.getAppointment_time_from());
            }
            if (exQuery.getAppointment_time_end() != null) {
                sql.append(" and appointment_time = :appointment_time_end ");
                query.putMap("appointment_time_end", exQuery.getAppointment_time_end());
            }
            if (exQuery.getComplete_time_from() != null) {
                sql.append(" and complete_time >= :complete_time_from ");
                query.putMap("complete_time_from", exQuery.getComplete_time_from());
            }
            if (exQuery.getComplete_time_end() != null) {
                sql.append(" and complete_time = :complete_time_end ");
                query.putMap("complete_time_end", exQuery.getComplete_time_end());
            }
        }
        sql.append(" order by create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }

    /**
     * 校验当前工装计划是否允许创建
     *
     * @param scheduleName  工装计划名称
     * @param houseNo       库号
     * @param toolNo        设备号
     * @param schedulerType 计划类型
     * @return
     * @remark 允许创建与已存在计划不冲突的工装计划
     */
    private Boolean isToolSchedulerCanCreate(String scheduleName, String houseNo, String toolNo, SchedulerType schedulerType) {
        return schedulerService.checkToolScheduler(scheduleName, houseNo, toolNo, schedulerType);
    }

    //endregion

}