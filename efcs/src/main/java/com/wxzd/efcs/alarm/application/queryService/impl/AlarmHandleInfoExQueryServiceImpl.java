package com.wxzd.efcs.alarm.application.queryService.impl;

import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.queryService.AlarmHandleInfoExQueryService;
import com.wxzd.efcs.alarm.application.querys.AlarmHandleInfoExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
@Service
public class AlarmHandleInfoExQueryServiceImpl implements AlarmHandleInfoExQueryService {

    private GaiaQuery getQuery(AlarmHandleInfoExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder().append("select  ").append(SqlUtl.getColumns(AlarmHandleInfoDto.class,"fa"))
                .append(" from fcs_alarm_handle_info fa where fa.is_active='1'  ");
        if (exQuery != null) {
            if (exQuery.getAlarmId() != null) {
                sql.append(" and fa.alarm_id=:alarm_id ");
                query.putMap("alarm_id", exQuery.getAlarmId());
            }
            if (exQuery.getUser_id() != null) {
                sql.append(" and fa.user_id=:user_id ");
                query.putMap("user_id", exQuery.getUser_id());
            }
        }
        sql.append(" order by fa.create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }


    /**
     * 根据报警id获取消警记录
     *
     * @param exQuery
     */
    @Override
    public List<AlarmHandleInfoDto> getAlarmHandleInfoList(AlarmHandleInfoExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), AlarmHandleInfoDto.class);
    }

    /**
     * 根据报警id获取消警记录分页
     *
     * @param exQuery
     */
    @Override
    public PageResult<AlarmHandleInfoDto> getAlarmHandleInfoPaged(AlarmHandleInfoExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), AlarmHandleInfoDto.class);
    }
}
