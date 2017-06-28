package com.wxzd.efcs.alarm.application.queryService.impl;

import com.wxzd.efcs.alarm.application.dtos.FireAlarmInfoDto;
import com.wxzd.efcs.alarm.application.dtos.LocationChartTemplate;
import com.wxzd.efcs.alarm.application.queryService.FireAlarmInfoExQueryService;
import com.wxzd.efcs.alarm.application.querys.FireAlarmInfoExQuery;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.report.domain.service.StorageDetailService;
import com.wxzd.gaia.common.base.core.map.MapWrap;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22.
 */
@Service
public class FireAlarmInfoExQueryServiceImpl implements FireAlarmInfoExQueryService {

    @Resource
    private StorageDetailService detailService;


    /**
     * 根据houseId和x获取规格，y,z
     *
     * @param houseId
     * @param xPos
     * @return
     */
    @Override
    public Map<String, Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos) {
        return detailService.getSizebyhouseIdAndXpos(houseId, xPos);
    }

    /**
     * 获取化成库位信息
     *
     * @param houseId
     * @param xPos
     */
    @Override
    public List<LocationChartTemplate> getLocationInfosForCharts(UUID houseId, Integer xPos) {
        StringBuilder sql = new StringBuilder("select wa.house_no,pd.is_empty,").append(SqlUtl.getColumns(LocationChartTemplate.class, "st"))
                .append(" from wms_storage_location st left join wms_warehouse wa on st.house_id = wa.id ")
                .append(" left join wms_storage ws on st.id = ws.location_id ")
                .append(" left join fcs_pallet_dispatch pd on ws.sto_container_no = pd.container_no and pd.dispatch_status = :dispatch_status ")
                .append(" where st.is_active = 1 and st.x_pos = :xPos and st.house_id = :houseId and st.loc_type <> :locType ");
        Map<String, Object> param = new HashMap<>();
        param.put("locType", LocationType.virtual);
        param.put("xPos", xPos);
        param.put("houseId", houseId);
        param.put("dispatch_status", PalletDispatchStatus.Dispatching);
        return DatabaseExecuter.queryBeanList(sql, param, LocationChartTemplate.class);
    }

    /**
     * 化成统计
     *
     * @param houseId
     * @param xPos
     * @return
     */
    @Override
    public Map<Integer, Map<String, Object>> getStatisticsTemplated(UUID houseId, Integer xPos) {
        if (houseId != null && xPos != null) {
            StringBuilder sql = new StringBuilder("SELECT STORAGE_STATUS, COUNT (1) counts FROM WMS_STORAGE_LOCATION WHERE IS_ACTIVE = '1' " +
                    "AND HOUSE_ID = :houseId AND X_POS = :xPos GROUP BY STORAGE_STATUS");

            Map<String, Object> param = new HashMap<>();
            param.put("houseId", houseId);
            param.put("xPos", xPos);
            List<Map<String, Object>> list = DatabaseExecuter.queryList(sql, param);
            Map<Integer, Map<String, Object>> mapMap = new HashMap<>();
            Integer errorCount = errorStatistics(houseId, xPos);
            Integer total = totalStatistic(houseId, xPos);
            if (list != null) {
                Map<String, Object> objectMap = new HashMap<>();
                for (Map<String, Object> map : list) {
                    objectMap.put(map.get("storage_status").toString(), Integer.valueOf(map.get("counts").toString()));
                    objectMap.put(map.get("storage_status").toString() + "Per", total == 0 ? "0.00" : MeterFormat((float) Integer.valueOf(map.get("counts").toString()) * 100 / total));
                }
                objectMap.put("error", errorCount);
                objectMap.put("errorPer", total == 0 ? "0.00" : MeterFormat((float) errorCount * 100 / total));
                mapMap.put(xPos, objectMap);
            }
            return mapMap;
        }
        return null;
    }

    //百分比format
    private String MeterFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }


    @Override
    public LocationChartTemplate getLoctionInfoByLocId(UUID locId) {
        StringBuilder sql = new StringBuilder().append("select ").append(SqlUtl.getColumns(LocationChartTemplate.class, "st"))
                .append(" FROM WMS_STORAGE_LOCATION st LEFT JOIN WMS_WAREHOUSE wa on ST.HOUSE_ID = WA.id WHERE st.is_active='1' and ST.id = :id ");
        Map<String, Object> param = new HashMap<>();
        param.put("id", locId);
        return DatabaseExecuter.queryBeanEntity(sql, param, LocationChartTemplate.class);
    }

    private Integer errorStatistics(UUID houseId, Integer xPos) {
        StringBuilder sql = new StringBuilder("SELECT COUNT (1) counts FROM wms_storage_location " +
                "WHERE IS_ACTIVE = '1' AND house_id =:houseId  AND x_pos=:xPos " +
                "AND LOC_IS_ERROR = '1' ");
        return DatabaseExecuter.queryNumber(sql, new MapWrap().put("houseId", houseId).put("xPos", xPos).getMap()).intValue();
    }

    /**
     * 库位总数
     *
     * @param houseId
     * @param xPos
     * @return
     */
    private Integer totalStatistic(UUID houseId, Integer xPos) {
        StringBuilder sql = new StringBuilder("SELECT COUNT (1) counts FROM wms_storage_location " +
                "WHERE IS_ACTIVE = '1' AND house_id =:houseId  AND x_pos=:xPos ");
        return DatabaseExecuter.queryNumber(sql, new MapWrap().put("houseId", houseId).put("xPos", xPos).getMap()).intValue();
    }


    /**
     * 根据库位ID获取电池详情
     *
     * @param locId
     */
    @Override
    public List<PalletDetail> getbetteryLists(UUID locId) {
        StringBuilder sql = new StringBuilder("select ").append(SqlUtl.getColumns(PalletDetail.class))
                .append(" FROM FCS_PALLET_INNER_DETAIL pi JOIN FCS_PALLET_DISPATCH pd ON PI.PALLET_DISPATCH_ID = PD.id AND PD.IS_ACTIVE = '1' " +
                        "JOIN WMS_STORAGE st ON pd.container_no = st.sto_container_no AND st.IS_ACTIVE = '1' WHERE PI.IS_ACTIVE = '1' " +
                        "AND st.location_id = :locId where pi.is_active='1' ");
        Map<String, Object> param = new HashMap<>();
        param.put("locId", locId);
        return DatabaseExecuter.queryBeanList(sql, param, PalletDetail.class);
    }

    private GaiaQuery getQuery(FireAlarmInfoExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder().append("select ").append(SqlUtl.getColumns(FireAlarmInfoDto.class))
                .append(" from fcs_alarm_info where is_active='1' and alarm_type = 'FireAlarm'  ");
        if (exQuery != null) {
            //拉线
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            if (exQuery.getHandled() != null) {
                sql.append(" and handled = :handled ");
                query.putMap("handled", exQuery.getHandled());
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
            // \设备号
            if (!StringUtl.isEmpty(exQuery.getDevice_no())) {
                sql.append(" and device_no like :device_no ");
                query.putMap("device_no", "%" + exQuery.getDevice_no() + "%");
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
        sql.append(" order by handled , create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }


    /**
     * 获取火警分页信息
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FireAlarmInfoDto> getFireAlarmInfoPaged(FireAlarmInfoExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), FireAlarmInfoDto.class);
    }

    /**
     * 获取火警不分页信息
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FireAlarmInfoDto> getFireAlarmInfoList(FireAlarmInfoExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FireAlarmInfoDto.class);
    }

    @Override
    public FireAlarmInfoDto getById(UUID id) {
        StringBuilder sql = new StringBuilder("select sl.x_pos,sl.y_pos,sl.z_pos, ")
                .append(SqlUtl.getColumns(FireAlarmInfoDto.class, "fa")).append(" FROM fcs_alarm_info fa left JOIN WMS_STORAGE_LOCATION sl " +
                        "ON FA.location = SL.LOC_NO  where fa.is_active = '1' and fa.id = :id and alarm_type = 'FireAlarm'  ");
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return DatabaseExecuter.queryBeanEntity(sql, param, FireAlarmInfoDto.class);
    }


    @Override
    public Map<String, Object> getHouseNo(UUID id) {
        StringBuilder sql = new StringBuilder("SELECT HOUSE_NO FROM WMS_WAREHOUSE where id = :id ");
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return DatabaseExecuter.queryListFirst(sql, param);
    }
}
