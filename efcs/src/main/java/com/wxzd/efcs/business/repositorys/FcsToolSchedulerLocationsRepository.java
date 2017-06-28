package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.DomainRepository;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by zhouzh on 2017/4/18.
 */
@Repository
public class FcsToolSchedulerLocationsRepository extends DomainRepository<FcsToolSchedulerLocations> {



    public List<FcsToolSchedulerLocations> getByType(String schedulerType) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_type", schedulerType);
        params.put("is_active", true);

        List<FcsToolSchedulerLocations> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }

    public List<FcsToolSchedulerLocations> getByStatus(String schedultNo, SchedulerStatus status) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("sc_no", schedultNo);
        params.put("appointment_status", status);
        params.put("is_active", true);

        List<FcsToolSchedulerLocations> locations = query.from(clazz).where(params).select(clazz).toList();
        return locations;
    }

    public List<FcsToolSchedulerLocations> getByStatus(UUID schedultId, AppointmentStatus status) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_id", schedultId);
        params.put("appointment_status", status);
        params.put("is_active", true);

        List<FcsToolSchedulerLocations> locations = query.from(clazz).where(params).select(clazz).toList();
        return locations;
    }


    public List<FcsToolSchedulerLocations> getByScheduleNo(String scheduleNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("sc_no", scheduleNo);
        params.put("is_active", true);

        List<FcsToolSchedulerLocations> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }

    public List<FcsToolSchedulerLocations> getByStatus(String scheduleNo,AppointmentStatus status) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("sc_no", scheduleNo);
        params.put("appointment_status", status);

        List<FcsToolSchedulerLocations> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }

    /**
     * 获取计划任务下所有待执行工单
     * @param scheduleId
     * @return
     */
    public List<FcsToolSchedulerLocations> getLiveLocations(UUID scheduleId) {

        StringBuffer sql = new StringBuffer();
        sql.append("select ")
                .append(SqlUtl.getColumns(FcsToolSchedulerLocations.class))
                .append(" from ")
                .append(tableName)
                .append(" where is_active = 1 and appointment_status=:appointment_status and next_run_time<:date order by CREATE_DATETIME ");
        Map<String, Object> params = new HashMap<>();
        if(null!= scheduleId)
            params.put("scheduler_id", scheduleId);
        params.put("appointment_status", AppointmentStatus.Waiting);
        params.put("is_active", true);
        params.put("date", new Date());
        return DatabaseExecuter.queryBeanList(sql, params, FcsToolSchedulerLocations.class);

    }

    /**
     * 获取计划任务下所有待执行工单
     * @param scheduleNo
     * @return
     */
    public List<FcsToolSchedulerLocations> getLiveLocations(String scheduleNo) {

        StringBuffer sql = new StringBuffer();
        sql.append("select ")
                .append(SqlUtl.getColumns(FcsToolSchedulerLocations.class))
                .append(" from ")
                .append(tableName)
                .append(" where is_active = 1 and sc_no =:sc_no and appointment_status=:appointment_status and appointment_time<:date order by CREATE_DATETIME ");
        //FIXME 对字段
        Map<String, Object> params = new HashMap<>();
        params.put("sc_no", scheduleNo);
        params.put("appointment_status", AppointmentStatus.Waiting);
        params.put("is_active", true);
        params.put("date", new Date());
        return DatabaseExecuter.queryBeanList(sql, params, FcsToolSchedulerLocations.class);
    }


    /**
     * 获取计划任务下在待执行工单
     * @param scheduleNo
     * @return
     */
    public List<FcsToolSchedulerLocations> getOnSchedulerLocations(String scheduleNo) {

        StringBuffer sql = new StringBuffer();
        sql.append("select ")
                .append(SqlUtl.getColumns(FcsToolSchedulerLocations.class))
                .append(" from ")
                .append(tableName)
                .append(" where is_active = 1 and sc_no =:sc_no and (appointment_status=:appointmented_status or appointment_status=:running_status) and appointment_time<:date order by CREATE_DATETIME ");
        //FIXME 对字段
        Map<String, Object> params = new HashMap<>();
        params.put("sc_no", scheduleNo);
        params.put("status1", AppointmentStatus.Waiting);
        params.put("appointmented_status", AppointmentStatus.Appointmented);
        params.put("is_active", true);
        params.put("date", new Date());
        return DatabaseExecuter.queryBeanList(sql, params, FcsToolSchedulerLocations.class);
    }

    public List<FcsToolSchedulerLocations> getToolLocationBySchedulerId(UUID uuid) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_id", uuid);
        params.put("is_active", true);

        List<FcsToolSchedulerLocations> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }


    public List<FcsToolSchedulerLocations> getToolLocationBySchedulerNotRun(String schedulerNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("sc_no", schedulerNo);
        params.put("is_active", true);

        List<FcsToolSchedulerLocations> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }

    public FcsToolSchedulerLocations getByWordDoneCondition(String houseNo,String toolNo,String locationNo,AppointmentStatus status) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("house_no", houseNo);
        params.put("tool_no", toolNo);
        params.put("location_no", locationNo);
        params.put("appointment_status", status);

        List<FcsToolSchedulerLocations> dispatch = query.from(FcsToolSchedulerLocations.class).where(params).select(FcsToolSchedulerLocations.class).toList();
        if (dispatch.size() > 0)
            return dispatch.get(0);
        else
            return null;
    }

}
