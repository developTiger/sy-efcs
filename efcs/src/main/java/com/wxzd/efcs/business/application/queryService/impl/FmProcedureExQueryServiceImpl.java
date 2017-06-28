package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.FmProcedureDto;
import com.wxzd.efcs.business.application.queryService.FmProcedureExQueryService;
import com.wxzd.efcs.business.application.querys.FmProcedureExQuery;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LYK on 2017/4/27.
 */
@Service
public class FmProcedureExQueryServiceImpl implements FmProcedureExQueryService {

    private GaiaQuery getQuery(FmProcedureExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select ")
                .append(SqlUtl.getColumns(FmProcedureDto.class))
                .append(" from fsc_fm_procedure ")
                .append(" where is_active = '1' ");
        if (exQuery != null) {
            if (exQuery.getId() != null) {
                sql.append(" and id=:id ");
                query.putMap("id", exQuery.getId());
            }
            //表单
            if (!StringUtl.isEmpty(exQuery.getForm_no())) {
                sql.append(" and form_no like :form_no ");
                query.putMap("form_no", "%" + exQuery.getForm_no() + "%");
            }
            //工序
            if (exQuery.getWork_procedure() != null) {
                sql.append(" and work_procedure=:procedure ");
                query.putMap("procedure", exQuery.getWork_procedure());
            }
            //库编号
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            //托盘号
            if (!StringUtl.isEmpty(exQuery.getPallet_no())) {
                sql.append(" and pallet_no like :pallet_no ");
                query.putMap("pallet_no", "%" + exQuery.getPallet_no() + "%");
            }
            //托盘状态
            if (exQuery.getPallet_status() != null) {
                sql.append(" and pallet_status = :pallet_status ");
                query.putMap("pallet_status", exQuery.getPallet_status());
            }
            //入库库位
            if (!StringUtl.isEmpty(exQuery.getIn_loc_no())) {
                sql.append(" and in_loc_no = :in_loc_no ");
                query.putMap("in_loc_no", exQuery.getIn_loc_no());
            }
            //开始时间
            if (exQuery.getStart_time() != null) {
                sql.append(" and in_time >= :in_time ");
                query.putMap("in_time", exQuery.getStart_time());
            }
            //完成时间
            if (exQuery.getComplete_time() != null) {
                sql.append(" and in_time <= :end_time ");
                query.putMap("end_time", exQuery.getComplete_time());
            }
            if (exQuery.getHouse_id() != null) {
                sql.append(" and house_id = :house_id ");
                query.putMap("house_id", exQuery.getHouse_id());
            }
            if (exQuery.getUserDefinedStatus() != null) {
                switch (exQuery.getUserDefinedStatus()) {
                    case Executing: {
                        sql.append(" and fm_status in (:created,:executing) ");
                        query.putMap("created", FmStatus.Created);
                        query.putMap("executing", FmStatus.Executing);
                        break;
                    }
                    case Finished: {
                        sql.append(" and fm_status = :userDefine ");
                        query.putMap("userDefine", FmStatus.Finished);
                        break;
                    }
                }
            }
            if (exQuery.getFm_status() != null) {
                sql.append(" and fm_status = :fm_status ");
                query.putMap("fm_status", exQuery.getFm_status());
            }
        }
        sql.append(" order by create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }


    /**
     * 分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FmProcedureDto> getFmProcedurePaged(FmProcedureExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), FmProcedureDto.class);
    }

    /**
     * 不分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmProcedureDto> getFmProcedureList(FmProcedureExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FmProcedureDto.class);
    }
}
