package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.gaia.common.base.core.result.ListResult;
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
public class FcsSchedulerRepository extends DomainRepository<FcsScheduler> {



    public List<FcsScheduler> getByType(String schedulerType) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_type", schedulerType);
        params.put("is_active", true);

        List<FcsScheduler> schedulers = query.from(clazz).where(params).select(clazz).toList();
        return schedulers;
    }

    public List<FcsScheduler> getLiveScheduler() {

        StringBuffer sql = new StringBuffer();
        sql.append("select ")
                .append(SqlUtl.getColumns(FcsScheduler.class))
                .append(" from ")
                .append(tableName)
                .append(" where is_active = 1 and scheduler_status=:scheduler_status and next_run_time<:date order by CREATE_DATETIME ");

        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_status", SchedulerStatus.Running);
        params.put("is_active", true);
        params.put("date", new Date());
        return DatabaseExecuter.queryBeanList(sql, params, FcsScheduler.class);

    }

    public List<FcsScheduler> getLiveSchedulerByDevicce(UUID house_id, String device_no) {

        StringBuffer sql = new StringBuffer();
        sql.append("select ")
                .append(SqlUtl.getColumns(FcsScheduler.class))
                .append(" from ")
                .append(tableName)
                .append(" where is_active = 1 and scheduler_status=:scheduler_status and house_id = :houseId and device_no = :deviceNo ")
        ;

        Map<String, Object> params = new HashMap<>();
        params.put("scheduler_status", SchedulerStatus.Running);
        params.put("houseId",house_id);
        params.put("deviceNo",device_no);
        return DatabaseExecuter.queryBeanList(sql, params, FcsScheduler.class);

    }
}
