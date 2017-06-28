package com.wxzd.efcs.equipment.application.queryService.impl;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.equipment.application.queryService.EquipmentQueryService;
import com.wxzd.efcs.equipment.application.querys.EquipmentExQuery;
import com.wxzd.efcs.report.application.dtos.EquipErrorDesc;
import com.wxzd.efcs.report.application.querys.enums.DashBoardCountType;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/16
 */
@Service
public class EquipmentQueryServiceImpl implements EquipmentQueryService {

    private GaiaQuery getQuery(EquipmentExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder()
                .append("select ").append(SqlUtl.getColumns(EquipmentDto.class)).append(" from fcs_equipment").append(" where is_active='1' ");
        if (!StringUtl.isEmpty(exQuery.getEquip_name())) {
            sql.append(" and equip_name like :equipName");
            query.putMap("equipName", "%" + exQuery.getEquip_name() + "%");
        }
        if (!StringUtl.isEmpty(exQuery.getEquip_no())) {
            sql.append(" and equip_no= :equipNo");
            query.putMap("equipNo", exQuery.getEquip_no());
        }
        if (exQuery.getEquip_type() != null) {
            sql.append(" and equip_type = :equipType ");
            query.putMap("equipType", exQuery.getEquip_type());
        }
        if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
            sql.append(" and house_no = :house_no");
            query.putMap("house_no", exQuery.getHouse_no());
        }
        if (!StringUtl.isEmpty(exQuery.getEquip_vender())) {
            sql.append(" and equip_vender like :equipVender");
            query.putMap("equipVender", "%" + exQuery.getEquip_vender() + "%");
        }
        query.setQuerySql(sql.toString());
        return query;
    }


    @Override
    public PageResult<EquipmentDto> getAllEquipmentsPaged(EquipmentExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), EquipmentDto.class);
    }


    @Override
    public ListResult<EquipmentDto> getAllEquipments(EquipmentExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        List<EquipmentDto> list = DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), EquipmentDto.class);
        return GaiaResultFactory.getCollection(list);
    }

    //    todo
    @Override
    public List<Map<String, Object>> getAllEquipVender() {
        StringBuilder sql = new StringBuilder(" select distinct equip_vender from fcs_equipment where is_active ='1' ");
        return DatabaseExecuter.queryList(sql);
    }

    @Override
    public List<Map<String, Object>> getAllHouseNo() {
        StringBuilder sql = new StringBuilder(" select distinct house_No from fcs_equipment where is_active ='1' ");
        return DatabaseExecuter.queryList(sql);
    }

    @Override
    public AlarmInfoDto getEquipErrorDesc(UUID houseId, UUID equipId) {
        StringBuilder sql = new StringBuilder("select").append(AlarmInfoDto.class)
                .append(" from FROM fcs_equipment eq LEFT JOIN FCS_ALARM_INFO al ON EQ.HOUSE_ID = AL.HOUSE_ID")
                .append(" AND EQ.EQUIP_NO = AL.location AND AL.location_type = 'Device' ")
                .append(" WHERE eq.is_active='1' and eq.HOUSE_ID = :house_id and EQ.id = :id ");
        Map<String, Object> param = new HashMap<>();
        param.put("house_id", houseId);
        param.put("id", equipId);
        return DatabaseExecuter.queryBeanEntity(sql, param, AlarmInfoDto.class);
    }
}
