package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.FmPalletSplitDto;
import com.wxzd.efcs.business.application.dtos.FmPalletizeDto;
import com.wxzd.efcs.business.application.queryService.FmPalletSplitExQueryService;
import com.wxzd.efcs.business.application.querys.FmPalletSplitExQuery;
import com.wxzd.efcs.business.application.querys.FmPalletizeExQuery;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LYK on 2017/4/27
 */
@Service
public class FmPalletSplitExQueryServiceImpl implements FmPalletSplitExQueryService {

    private GaiaQuery getQuery(FmPalletSplitExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select ")
                .append(SqlUtl.getColumns(FmPalletSplitDto.class))
                .append(" from fsc_fm_pallet_split ")
                .append(" where is_active = '1' ");
        if (exQuery != null) {
            if (exQuery.getId() != null) {
                sql.append(" and id=:id ");
                query.putMap("id", exQuery.getId());
            }
            //库编号
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            //表单
            if (!StringUtl.isEmpty(exQuery.getForm_no())) {
                sql.append(" and form_no like :form_no ");
                query.putMap("form_no", "%" + exQuery.getForm_no() + "%");
            }
            //设备号
            if (!StringUtl.isEmpty(exQuery.getEquip_no())) {
                sql.append(" and equip_no like :equip_no ");
                query.putMap("equip_no", "%" + exQuery.getEquip_no() + "%");
            }
            //工序
            if (exQuery.getWork_procedure() != null) {
                sql.append(" and work_procedure=:procedure ");
                query.putMap("procedure", exQuery.getWork_procedure());
            }
            //组拆盘状态
            if (exQuery.getPalletize_status() != null) {
                sql.append(" and palletize_status = :palletize_status ");
                query.putMap("palletize_status", exQuery.getPalletize_status());
            }
            //托盘号
            if (!StringUtl.isEmpty(exQuery.getPallet_no())) {
                sql.append(" and pallet_no like :pallet_no ");
                query.putMap("pallet_no", "%" + exQuery.getPallet_no() + "%");
            }
            //开始时间
            if (exQuery.getProc_start_time() != null) {
                sql.append(" and proc_start_time >= :proc_start_time ");
                query.putMap("proc_start_time", exQuery.getProc_start_time());
            }
            //结束时间
            if (exQuery.getProc_complete_time() != null) {
                sql.append(" and proc_complete_time <= :proc_complete_time ");
                query.putMap("proc_complete_time", exQuery.getProc_complete_time());
            }
            //单据状态
            if (exQuery.getFm_status() != null) {
                sql.append(" and fm_status = :fm_status ");
                query.putMap("fm_status", exQuery.getFm_status());
            }
            if (exQuery.getHouse_id() != null) {
                sql.append(" and house_id = :house_id ");
                query.putMap("house_id", exQuery.getHouse_id());
            }
            if (exQuery.getUserDefinedStatus() != null) {
                switch (exQuery.getUserDefinedStatus()) {
                    case Finished: {
                        sql.append(" and fm_status = :userDefine ");
                        query.putMap("userDefine", FmStatus.Finished);
                        break;
                    }
                    case Executing: {
                        sql.append(" and fm_status in (:created,:executing) ");
                        query.putMap("created", FmStatus.Created);
                        query.putMap("executing", FmStatus.Executing);
                        break;
                    }
                }
            }
        }
        sql.append(" order by create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }

    /**
     * 获取拆盘单列表
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FmPalletSplitDto> getFmPalletSplitPaged(FmPalletSplitExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), FmPalletSplitDto.class);
    }

    /**
     * 获取拆盘单列表
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmPalletSplitDto> getFmPalletSplitList(FmPalletSplitExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FmPalletSplitDto.class);
    }
}
