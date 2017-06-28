package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.InstructionDto;
import com.wxzd.efcs.business.application.queryService.InstructionExQueryService;
import com.wxzd.efcs.business.application.querys.InstructionExQuery;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
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
public class InstructionExQueryServiceImpl implements InstructionExQueryService {

    private GaiaQuery getQuery(InstructionExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select ")
                .append(SqlUtl.getColumns(InstructionDto.class))
                .append(" from fcs_instruction ")
                .append(" where is_active = '1' ");
        if (exQuery != null) {
            if (exQuery.getId() != null) {
                sql.append(" and id=:id ");
                query.putMap("id", exQuery.getId());
            }
            //指令号
            if (!StringUtl.isEmpty(exQuery.getInstr_no())) {
                sql.append(" and instr_no like :instr_no ");
                query.putMap("instr_no", "%" + exQuery.getInstr_no() + "%");
            }
            //指令类型
            if (exQuery.getInstr_type() != null) {
                sql.append(" and instr_type = :instr_type ");
                query.putMap("instr_type", exQuery.getInstr_type());
            }
            if (!StringUtl.isEmpty(exQuery.getQueue_no())) {
                sql.append(" and queue_no like :queue_no ");
                query.putMap("queue_no", "%" + exQuery.getQueue_no() + "%");
            }
            //下发类型
            if (exQuery.getSend_type() != null) {
                sql.append(" and send_type=:send_type ");
                query.putMap("send_type", exQuery.getSend_type());
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
            //开始时间
            if (exQuery.getStart_time() != null) {
                sql.append(" and send_time >= :start_time ");
                query.putMap("start_time", exQuery.getStart_time());
            }
            //完成时间
            if (exQuery.getComplete_time() != null) {
                sql.append(" and send_time <= :end_time ");
                query.putMap("end_time", exQuery.getComplete_time());
            }
            //执行状态
            if (exQuery.getInstr_status() != null) {
                sql.append(" and instr_status = :instr_status ");
                query.putMap("instr_status", exQuery.getInstr_status());
            }
            //执行中指令查询
            if (exQuery.getUserDefinedStatus() != null) {
                switch (exQuery.getUserDefinedStatus()) {
                    case Executing: {
                        sql.append(" and instr_status <> :defined_status ");
                        break;
                    }
                    case Finished: {
                        sql.append(" and instr_status = :defined_status ");
                        break;
                    }
                }
                query.putMap("defined_status", InstructionStatus.Finished);
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
    public PageResult<InstructionDto> getInstructionPaged(InstructionExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), InstructionDto.class);
    }

    /**
     * 不分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<InstructionDto> getInstructionList(InstructionExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), InstructionDto.class);
    }
}
