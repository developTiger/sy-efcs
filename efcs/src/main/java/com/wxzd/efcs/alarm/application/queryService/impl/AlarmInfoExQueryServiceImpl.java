package com.wxzd.efcs.alarm.application.queryService.impl;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.alarm.application.queryService.AlarmInfoExQueryService;
import com.wxzd.efcs.alarm.application.querys.AlarmInfoExQuery;
import com.wxzd.efcs.alarm.domain.enums.AlarmLevel;
import com.wxzd.efcs.alarm.domain.enums.LocationType;
import com.wxzd.gaia.common.base.core.map.MapWrap;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by LYK on 2017/4/22
 * 异常信息
 */
@Service
public class AlarmInfoExQueryServiceImpl implements AlarmInfoExQueryService {

    private GaiaQuery getQuery(AlarmInfoExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder().append("select ").append(SqlUtl.getColumns(AlarmInfoDto.class))
                .append(" from fcs_alarm_info where is_active='1'  ");
        if (exQuery != null) {
            //报警类型
            if (exQuery.getAlarm_type() != null) {
                sql.append(" and alarm_type = :alarm_type ");
                query.putMap("alarm_type", exQuery.getAlarm_type());
            }
            //拉线
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            //位置类型
            if (exQuery.getLocation_type() != null) {
                sql.append(" and location_type = :location_type ");
                query.putMap("location_type", exQuery.getLocation_type());
            }
            // 库位号/设备号
            if (!StringUtl.isEmpty(exQuery.getLocation())) {
                sql.append(" and location like :location ");
                query.putMap("location", "%" + exQuery.getLocation() + "%");
            }
            //报警等级
            if (exQuery.getAlarm_level() != null) {
                sql.append(" and alarm_level =:alarm_level ");
                query.putMap("alarm_level", exQuery.getAlarm_level());
            }
            // 标题
            if (!StringUtl.isEmpty(exQuery.getTitle())) {
                sql.append(" and title like :title ");
                query.putMap("title", "%" + exQuery.getTitle() + "%");
            }
            // 内容
            if (!StringUtl.isEmpty(exQuery.getContent())) {
                sql.append(" and content like :content ");
                query.putMap("content", "%" + exQuery.getContent() + "%");
            }
            // 库位号/设备号
            if (exQuery.getHandled() != null) {
                sql.append(" and handled =:handled ");
                query.putMap("handled", exQuery.getHandled());
            }
        }
        sql.append(" order by handled, create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }

    /**
     * 获取异常信息列表分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<AlarmInfoDto> getAlarmInfoPaged(AlarmInfoExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), AlarmInfoDto.class);
    }

    /**
     * 获取异常信息列表无分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<AlarmInfoDto> getAlarmInfoList(AlarmInfoExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), AlarmInfoDto.class);
    }


    /**
     * 查询展示异常信息(按异常级别和时间排序)
     */
    @Override
    public List<AlarmInfoDto> getAlarmInfoSortList(Date lastRefreshTime) {
        Map<String, Object> param = new HashMap<>();
        String cmd = "";
        if (lastRefreshTime != null) {
            cmd = " and create_datetime > :create_datetime  ";
            param.put("create_datetime", lastRefreshTime);
        }
        StringBuilder sql = new StringBuilder().append("SELECT * FROM ( select ").append(SqlUtl.getColumns(AlarmInfoDto.class))
                .append(" from fcs_alarm_info where is_active='1' and alarm_level=:highlevel and handled=:handled ").append(cmd).append(" order by create_datetime desc) ");
        StringBuilder normalSql = new StringBuilder().append(" SELECT * FROM ( select ").append(SqlUtl.getColumns(AlarmInfoDto.class))
                .append(" from fcs_alarm_info where is_active='1' and alarm_level=:normalevel and handled=:handled ").append(cmd).append(" order by create_datetime desc) ");
        StringBuilder lowSql = new StringBuilder().append("SELECT * FROM ( select ").append(SqlUtl.getColumns(AlarmInfoDto.class))
                .append(" from fcs_alarm_info where is_active='1' and alarm_level=:lowlevel and handled=:handled ").append(cmd).append("  order by create_datetime desc) ");
        sql.append(" UNION ALL (").append(normalSql).append(" ) UNION ALL  ( ").append(lowSql).append(" ) ");
        param.put("highlevel", AlarmLevel.High);
        param.put("normalevel", AlarmLevel.Normal);
        param.put("lowlevel", AlarmLevel.Low);
        param.put("handled", Boolean.FALSE);
        return DatabaseExecuter.queryBeanList(sql, param, AlarmInfoDto.class);
    }

    /**
     * 根据配置推送数量--获取异常信息（最新推送）
     *
     * @return
     */
    @Override
    public List<AlarmInfoDto> getAlarmInfoWithPushConfig(Integer amount) {
        Map<String, Object> param = new HashMap<>();

        StringBuilder sql = new StringBuilder("select sl.x_pos,sl.y_pos,sl.z_pos,")
                .append(SqlUtl.getColumns(AlarmInfoDto.class, "fa")).append(" FROM fcs_alarm_info fa left JOIN WMS_STORAGE_LOCATION sl ON FA.location = SL.LOC_NO " +
                        "AND FA.location_type = 'Storage' and sl.is_active='1' and fa.HOUSE_ID = SL.HOUSE_ID")
                .append(" where fa.is_active='1'  ")
                .append(" order by fa.create_datetime desc ");
        param.put("amount", amount);
        List<AlarmInfoDto> list = DatabaseExecuter.queryBeanList(sql, param, AlarmInfoDto.class);
        List<AlarmInfoDto> list1 = new ArrayList<>();
//        for (int i = 0; i < amount; i++) {
//            list1.add(list.get(i));
//        }
        for (int i = 0; i < list.size() && i < amount; i++) {
            list1.add(list.get(i));
        }
        return list1;
    }

    /**
     * 根据ID获取异常信息
     *
     * @param id
     * @return
     */
    @Override
    public AlarmInfoDto getAlarmInfoById(UUID id) {
        StringBuilder sql = new StringBuilder("select sl.x_pos,sl.y_pos,sl.z_pos ,")
                .append(SqlUtl.getColumns(AlarmInfoDto.class, "fa")).append(" FROM fcs_alarm_info fa LEFT JOIN WMS_STORAGE_LOCATION sl " +
                        "ON FA. \"LOCATION\" = SL.LOC_NO AND FA.location_type = :location_type  and sl.HOUSE_ID = fa.HOUSE_ID  where fa.is_active = '1' and fa.id = :id ");
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("location_type", LocationType.Storage);
        return DatabaseExecuter.queryBeanEntity(sql, param, AlarmInfoDto.class);
    }


    /**
     * 根据ID获取异常信息
     *
     * @param LocId@return
     */
    @Override
    public AlarmInfoDto getAlarmInfoByLocId(UUID LocId) {
        StringBuilder sql = new StringBuilder("select sl.x_pos,sl.y_pos,sl.z_pos ,")
                .append(SqlUtl.getColumns(AlarmInfoDto.class, "fa")).append(" FROM fcs_alarm_info fa JOIN WMS_STORAGE_LOCATION sl " +
                        "ON FA.location = SL.LOC_NO AND fa.HOUSE_ID = SL.HOUSE_ID where fa.is_active = '1' and fa.location = :id ");
        Map<String, Object> param = new HashMap<>();
        param.put("id", LocId);
        return DatabaseExecuter.queryBeanEntity(sql, param, AlarmInfoDto.class);
    }

    /**
     * 根据house_no获取house_id
     *
     * @param houseNo
     */
    @Override
    public UUID getHouseIdByNo(String houseNo) {
        if (!StringUtl.isEmpty(houseNo)) {
            StringBuilder sql = new StringBuilder("SELECT id FROM WMS_WAREHOUSE WHERE is_active=:active and HOUSE_NO = :houseNo");
            Map<String, Object> parma = new HashMap<>();
            parma.put("active", Boolean.TRUE);
            parma.put("houseNo", houseNo);
            Object obj = DatabaseExecuter.queryValue(sql, parma);
            if (obj != null)
                return UUID.fromString(obj.toString());
        }
        return null;
    }
}
