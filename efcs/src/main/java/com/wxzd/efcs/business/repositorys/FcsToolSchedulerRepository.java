package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.FcsToolScheduler;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
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
public class FcsToolSchedulerRepository extends DomainRepository<FcsToolScheduler> {

    public List<FcsToolScheduler> getByType(String schedulerType) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_type", schedulerType);
        params.put("is_active", true);

        List<FcsToolScheduler> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }

    public List<FcsToolScheduler> getByStatus(SchedulerStatus status) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_status", status);
        params.put("is_active", true);

        List<FcsToolScheduler> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }


    public List<FcsToolScheduler> getLiveScheduler() {
        StringBuffer sql = new StringBuffer();
        sql.append("select ")
                .append(SqlUtl.getColumns(FcsToolScheduler.class))
                .append(" from ")
                .append(tableName)
                .append(" where is_active = 1 and scheduler_status=:scheduler_status and next_run_time<:date order by CREATE_DATETIME ");

        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_status", SchedulerStatus.Running);
        params.put("is_active", true);
        params.put("date", new Date());
        return DatabaseExecuter.queryBeanList(sql, params, FcsToolScheduler.class);

    }

    public FcsToolScheduler getByNo(String sc_no) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("sc_no", sc_no);
        params.put("is_active", true);

        List<FcsToolScheduler> dispatch = query.from(FcsToolScheduler.class).where(params).select(FcsToolScheduler.class).toList();
        if (dispatch.size() > 0)
            return dispatch.get(0);
        else
            return null;
    }
    public FcsToolScheduler getByName(String scheduleName) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("sc_name", scheduleName);
        params.put("is_active", true);

        List<FcsToolScheduler> dispatch = query.from(FcsToolScheduler.class).where(params).select(FcsToolScheduler.class).toList();
        if (dispatch != null && dispatch.size() > 0)
            return dispatch.get(0);
        else
            return null;
    }

    public FcsToolScheduler getByCondition(String houseNo,String toolNo,SchedulerStatus status) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("house_no", houseNo);
        params.put("tool_no", toolNo);
        params.put("scheduler_status", status);
        params.put("is_active", true);

        List<FcsToolScheduler> dispatch = query.from(FcsToolScheduler.class).where(params).select(FcsToolScheduler.class).toList();
        if (dispatch.size() > 0)
            return dispatch.get(0);
        else
            return null;
    }

    public FcsToolScheduler getByCondition(String houseNo,String toolNo,SchedulerType type) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("house_no", houseNo);
        params.put("tool_no", toolNo);
        params.put("scheduler_type", type);
        params.put("is_active", true);

        List<FcsToolScheduler> dispatch = query.from(FcsToolScheduler.class).where(params).select(FcsToolScheduler.class).toList();
        if (dispatch.size() > 0)
            return dispatch.get(0);
        else
            return null;
    }
}
