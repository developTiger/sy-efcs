package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.FcsSchedulerDto;
import com.wxzd.efcs.business.application.queryService.FcsSchedulerExQueryService;
import com.wxzd.efcs.business.application.querys.FcsSchedulerExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LYK on 2017/5/18
 */
@Service
public class FcsSchedulerExQueryServiceImpl implements FcsSchedulerExQueryService {


    private GaiaQuery getQuery(FcsSchedulerExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select ")
                .append(SqlUtl.getColumns(FcsSchedulerDto.class))
                .append(" from fcs_scheduler ")
                .append(" where is_active = '1' ");
        if (exQuery != null) {
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getDevice_no())) {
                sql.append(" and device_no like :device_no ");
                query.putMap("device_no", "%" + exQuery.getDevice_no() + "%");
            }
            if (exQuery.getScheduler_type() != null) {
                sql.append(" and scheduler_type = :scheduler_type ");
                query.putMap("scheduler_type", exQuery.getScheduler_type());
            }
            if (exQuery.getScheduler_status() != null) {
                sql.append(" and scheduler_status = :scheduler_status ");
                query.putMap("scheduler_status", exQuery.getScheduler_status());
            }
            if (exQuery.getTask_type() != null) {
                sql.append(" and task_type = :task_type ");
                query.putMap("task_type", exQuery.getTask_type());
            }
        }
        sql.append(" order by create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }
    /**
     * 分页查询调度信息
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FcsSchedulerDto> getSchedulerPaged(FcsSchedulerExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), FcsSchedulerDto.class);
    }

    /**
     * 查询调度信息不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FcsSchedulerDto> getSchedulerNoPaged(FcsSchedulerExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FcsSchedulerDto.class);
    }
}
