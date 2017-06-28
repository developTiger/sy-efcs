package com.wxzd.efcs.report.domain.service.impl;

import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.report.application.dtos.DataSummaryDto;
import com.wxzd.efcs.report.application.dtos.DetailListDto;
import com.wxzd.efcs.report.application.dtos.LocationChartExtendDto;
import com.wxzd.efcs.report.application.dtos.LocationWithNoPallet;
import com.wxzd.efcs.report.application.querys.DetailListExQuery;
import com.wxzd.efcs.report.domain.service.StorageDetailService;
import com.wxzd.gaia.common.base.core.map.MapWrap;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/18
 */
@Service
public class StorageDetailServiceImpl implements StorageDetailService {


    @Override
    public Map<Integer, Map<String, Object>> getStatusStatistics(LocationWithNoPallet locationWithNoPallet) {
        if (locationWithNoPallet != null) {
            //有空托盘统计
            if (locationWithNoPallet.getEmptyPallet()) {
                Map<String, Object> params = new HashMap<>();
                params.put("zoneId", locationWithNoPallet.getZone_id());
                params.put("houseId", locationWithNoPallet.getHouse_id());
                StringBuilder cmd = new StringBuilder(" AND sl.loc_type <> :locType  ");
                params.put("locType", LocationType.virtual);
                if (locationWithNoPallet.getX_pos() != null) {
                    cmd.append(" AND sl.x_pos=:xPos ");
                    params.put("xPos", locationWithNoPallet.getX_pos());
                }
                StringBuilder sql = new StringBuilder().append("SELECT sl.x_pos,sl.storage_status ,COUNT (1) counts ")
                        .append(" FROM wms_storage_location sl JOIN ( SELECT location_id, zone_id FROM wms_relate_zone_and_location wl JOIN wms_zone wz ON wl.zone_id = wz.id AND wz.id =:zoneId ) t ON t.location_id = sl.id  ")
                        .append(" WHERE  sl.house_id = :houseId  ").append(cmd)
                        .append(" GROUP BY sl.x_pos, sl.storage_status ORDER BY sl.x_pos ");

                List<Map<String, Object>> list = DatabaseExecuter.queryList(sql, params);
                Map<Integer, Map<String, Object>> mapList = new HashMap<>();
                for (Map<String, Object> map : list) {
                    Map<String, Object> val_map = mapList.get(Integer.valueOf(map.get("x_pos").toString()));
                    //异常统计
                    Integer errorCount = errorStatistics(locationWithNoPallet.getHouse_id(), locationWithNoPallet.getZone_id(), Integer.valueOf(map.get("x_pos").toString()));
                    //空托盘统计
                    Integer emptyPalletCount = emptyPalletStatistics(locationWithNoPallet.getHouse_id(), locationWithNoPallet.getZone_id(), Integer.valueOf(map.get("x_pos").toString()));
                    //总数
                    Integer total = totalStatistic(locationWithNoPallet.getHouse_id(), locationWithNoPallet.getZone_id(), Integer.valueOf(map.get("x_pos").toString()));

                    if (val_map == null) {
                        val_map = new HashMap<>();
                        val_map.put(map.get("storage_status").toString(), Integer.valueOf(map.get("counts").toString()));
                        val_map.put(map.get("storage_status").toString() + "Per", total == 0 ? "0.00" : MeterFormat((float) Integer.valueOf(map.get("counts").toString()) * 100 / total));
                        mapList.put(Integer.valueOf(map.get("x_pos").toString()), val_map);
                    } else {
                        val_map.put(map.get("storage_status").toString(), Integer.valueOf(map.get("counts").toString()));
                        val_map.put(map.get("storage_status").toString() + "Per", total == 0 ? "0.00" : MeterFormat((float) Integer.valueOf(map.get("counts").toString()) * 100 / total));
                    }
                    val_map.put("error", errorCount);
                    val_map.put("errorPer", total == 0 ? "0.00" : MeterFormat((float) errorCount * 100 / total));
                    val_map.put("emptyPallet", emptyPalletCount);
                    val_map.put("emptyPalletPer", total == 0 ? "0.00" : MeterFormat((float) emptyPalletCount * 100 / total));
                }
                return mapList;
            }
        }
        return null;
    }


    /**
     * 库存明细显示锁信息
     *
     * @param locationWithNoPallet
     * @return
     */
    @Override
    public Map<Integer, Map<String, Object>> getStatusStatisticsWithLock(LocationWithNoPallet locationWithNoPallet) {
        if (locationWithNoPallet != null) {
            //有空托盘统计
            if (locationWithNoPallet.getEmptyPallet()) {
                Map<String, Object> params = new HashMap<>();
                params.put("zoneId", locationWithNoPallet.getZone_id());
                params.put("houseId", locationWithNoPallet.getHouse_id());
                StringBuilder cmd = new StringBuilder(" AND sl.loc_type <> :locType  ");
                params.put("locType", LocationType.virtual);
                if (locationWithNoPallet.getX_pos() != null) {
                    cmd.append(" AND sl.x_pos=:xPos ");
                    params.put("xPos", locationWithNoPallet.getX_pos());
                }
                StringBuilder sql = new StringBuilder().append("SELECT sl.x_pos,sl.storage_status ,COUNT (1) counts ")
                        .append(" FROM wms_storage_location sl JOIN ( SELECT location_id, zone_id FROM wms_relate_zone_and_location wl JOIN wms_zone wz ON wl.zone_id = wz.id AND wz.id =:zoneId ) t ON t.location_id = sl.id  ")
                        .append(" WHERE  sl.house_id = :houseId  ").append(cmd)
                        .append(" GROUP BY sl.x_pos, sl.storage_status ORDER BY sl.x_pos ");
                List<Map<String, Object>> list = DatabaseExecuter.queryList(sql, params);
                Map<Integer, Map<String, Object>> mapList = new HashMap<>();
                for (Map<String, Object> map : list) {
                    Map<String, Object> val_map = mapList.get(Integer.valueOf(map.get("x_pos").toString()));
                    //锁统计
                    Map<String, Object> lockMap = locationLockStatistic(locationWithNoPallet.getHouse_id(), locationWithNoPallet.getZone_id(), Integer.valueOf(map.get("x_pos").toString()));
                    Integer business = lockMap.get("business") != null ? Integer.valueOf(lockMap.get("business").toString()) : 0;
                    Integer artificial = lockMap.get("artificial") != null ? Integer.valueOf(lockMap.get("artificial").toString()) : 0;
                    Integer doublelock = lockMap.get("doublelock") != null ? Integer.valueOf(lockMap.get("doublelock").toString()) : 0;
                    //空托盘统计
                    Integer emptyPalletCount = emptyPalletStatistics(locationWithNoPallet.getHouse_id(), locationWithNoPallet.getZone_id(), Integer.valueOf(map.get("x_pos").toString()));
                    //总数
                    Integer total = totalStatistic(locationWithNoPallet.getHouse_id(), locationWithNoPallet.getZone_id(), Integer.valueOf(map.get("x_pos").toString()));
                    if (val_map == null) {
                        val_map = new HashMap<>();
                        val_map.put(map.get("storage_status").toString(), Integer.valueOf(map.get("counts").toString()));
                        val_map.put(map.get("storage_status").toString() + "Per", total == 0 ? "0.00" : MeterFormat((float) Integer.valueOf(map.get("counts").toString()) * 100 / total));
                        mapList.put(Integer.valueOf(map.get("x_pos").toString()), val_map);
                    } else {
                        val_map.put(map.get("storage_status").toString(), Integer.valueOf(map.get("counts").toString()));
                        val_map.put(map.get("storage_status").toString() + "Per", total == 0 ? "0.00" : MeterFormat((float) Integer.valueOf(map.get("counts").toString()) * 100 / total));
                    }
                    val_map.put("business", business);
                    val_map.put("businessPer", total == 0 ? "0.00" : MeterFormat((float) business * 100 / total));
                    val_map.put("artificial", artificial);
                    val_map.put("artificialPer", total == 0 ? "0.00" : MeterFormat((float) artificial * 100 / total));
                    val_map.put("doublelock", doublelock);
                    val_map.put("doublelockPer", total == 0 ? "0.00" : MeterFormat((float) doublelock * 100 / total));
                    val_map.put("emptyPallet", emptyPalletCount);
                    val_map.put("emptyPalletPer", total == 0 ? "0.00" : MeterFormat((float) emptyPalletCount * 100 / total));
                }
                return mapList;
            }
        }
        return null;
    }

    //百分比format
    private String MeterFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }


    @Override
    public List<LocationChartExtendDto> getLocationInfosForCharts(UUID houseId, UUID zoneId, Integer xPos) {
        if (houseId != null) {
            StringBuilder cmd = new StringBuilder(" AND l.loc_type <> :locType ");
            Map<String, Object> param = new HashMap<>();
            param.put("houseId", houseId);
            param.put("locType", LocationType.virtual);
            if (xPos != null) {
                cmd.append(" AND x_pos = :xPos ");
                param.put("xPos", xPos);
            }
            if (zoneId != null) {
                cmd.append(" AND z. ID = :zoneId ");
                param.put("zoneId", zoneId);
            }
            StringBuilder sql = new StringBuilder("SELECT l. ID, l.create_datetime, y.is_empty, w. ID house_id, w.house_name house_name, w.house_no house_no,")
                    .append(" x_pos, y_pos, z_pos, artificial_in_lock, artificial_out_lock, business_in_lock, business_out_lock, loc_allow_weight, loc_barcode, loc_is_error, ")
                    .append("loc_error_reason, loc_heigh, loc_length, loc_no, loc_type, loc_use_status, loc_vol, loc_vol_unit, loc_width, standard_id, storage_status, forbid_in, ")
                    .append("forbid_out,z.id zone_id FROM wms_storage_location l LEFT JOIN wms_storage_location_standard s ON l.standard_id = s. ID LEFT JOIN wms_warehouse w ON l.house_id = w. ID ")
                    .append("LEFT JOIN ( SELECT st.location_id, pd.is_empty FROM WMS_STORAGE st LEFT JOIN FCS_PALLET_DISPATCH pd ON st.sto_container_no = pd.container_no ")
                    .append("WHERE st.is_active = '1' and pd.is_active='1' and pd.dispatch_status = :dispatch_status  ) y ON l. ID = y.location_id ")
                    .append("LEFT JOIN ( SELECT zl.location_id, z. ID FROM WMS_RELATE_ZONE_AND_LOCATION zl LEFT JOIN WMS_ZONE z ON zl.zone_id = z. ID WHERE z.is_active = 1 ) z ON l. ID = z.location_id ")
                    .append(" WHERE l.is_active = 1 AND w.is_active = 1 AND house_id = :houseId " + cmd +
                            " ORDER BY l.x_pos ASC, l.y_pos DESC, z_pos ASC");
            param.put("dispatch_status", PalletDispatchStatus.Dispatching);
            return DatabaseExecuter.queryBeanList(sql, param, LocationChartExtendDto.class);
        }
        return null;
    }

    /**
     * 异常统计
     *
     * @param houseId
     * @param zoneId
     * @param xPos
     * @return
     */
    private Integer errorStatistics(UUID houseId, UUID zoneId, Integer xPos) {
        StringBuilder sql = new StringBuilder("SELECT COUNT (1) counts FROM wms_storage_location sl LEFT JOIN WMS_RELATE_ZONE_AND_LOCATION wa " +
                "ON SL.id = wa.LOCATION_ID LEFT JOIN WMS_ZONE WZ ON wa.ZONE_ID = wz. ID WHERE sl.IS_ACTIVE = '1' AND sl.house_id =:houseId " +
                "AND sl.x_pos =:xPos AND wz.id=:zoneId AND sl.LOC_IS_ERROR = '1' AND sl.loc_type <> :locType  ");
        return DatabaseExecuter.queryNumber(sql, new MapWrap()
                .put("houseId", houseId)
                .put("xPos", xPos)
                .put("zoneId", zoneId)
                .put("locType", LocationType.virtual)
                .getMap()).intValue();
    }

    /**
     * 锁类型
     *
     * @param houseId
     * @param zoneId
     * @param xPos
     * @return
     */
    private Map<String, Object> locationLockStatistic(UUID houseId, UUID zoneId, Integer xPos) {
        StringBuilder sql = new StringBuilder("select ")
                .append(" count ( case when business_in_lock = '1' or business_out_lock = '1' then 1 else null end ) business, ")
                .append(" count ( case when artificial_in_lock = '1' or artificial_out_lock = '1' then 1 else null end ) artificial, ")
                .append(" count ( case when ( artificial_in_lock = '1' or artificial_out_lock = '1' ) and ( business_in_lock = '1' or business_out_lock = '1' ) then 1 else null end ) doublelock ")
                .append(" from wms_storage_location sl ")
                .append(" left join wms_relate_zone_and_location wa on sl. id = wa.location_id ")
                .append(" left join wms_zone wz on wa.zone_id = wz. id ")
                .append(" where sl.is_active = '1' AND house_id =:houseId AND sl.x_pos =:xPos AND wz.id=:zoneId AND sl.loc_type <> :locType ");
        return DatabaseExecuter.queryEntity(sql, new MapWrap().put("houseId", houseId).put("xPos", xPos).put("zoneId", zoneId).put("locType", LocationType.virtual).getMap());
    }

    /**
     * 库位总数
     *
     * @param houseId
     * @param zoneId
     * @param xPos
     * @return
     */
    private Integer totalStatistic(UUID houseId, UUID zoneId, Integer xPos) {
        StringBuilder sql = new StringBuilder("SELECT COUNT (1) FROM wms_storage_location sl " +
                "JOIN ( SELECT location_id, zone_id FROM wms_relate_zone_and_location wl JOIN wms_zone wz ON wl.zone_id = wz. ID AND wz. ID = :zoneId ) T " +
                "ON T .location_id = sl. ID WHERE sl.house_id = :houseId AND sl.loc_type <> :locType AND sl.x_pos = :xPos ");
        return DatabaseExecuter.queryNumber(sql, new MapWrap()
                .put("houseId", houseId)
                .put("xPos", xPos)
                .put("zoneId", zoneId)
                .put("locType", LocationType.virtual)
                .getMap()).intValue();
    }


    /**
     * 空托盘统计
     */
    private Integer emptyPalletStatistics(UUID houseId, UUID zoneId, Integer xPos) {
        StringBuilder sql = new StringBuilder("SELECT COUNT (1) FROM FCS_PALLET_DISPATCH pd " +
                "JOIN WMS_STORAGE ws ON PD.CONTAINER_NO = WS.sto_container_no " +
                "JOIN WMS_STORAGE_LOCATION sl ON ws.location_id = SL.id " +
                "JOIN WMS_RELATE_ZONE_AND_LOCATION wl ON sl. ID = wl.location_id " +
                "WHERE pd.IS_ACTIVE = '1' AND PD.IS_EMPTY = '1' AND pd.HOUSE_ID = :houseId AND wl.zone_id = :zoneId " +
                "AND SL.X_POS = :xPos  AND sl.loc_type <> :locType AND PD.DISPATCH_STATUS = :dispatch_status ");
        return DatabaseExecuter.queryNumber(sql, new MapWrap().put("houseId", houseId)
                .put("zoneId", zoneId)
                .put("xPos", xPos)
                .put("locType", LocationType.virtual)
                .put("dispatch_status", PalletDispatchStatus.Dispatching)
                .getMap()).intValue();
    }


    @Override
    public Map<String, Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos) {
        StringBuilder sql = new StringBuilder("SELECT WR.ROW_MAX_Y row_y, WR.ROW_MAX_Z row_z FROM WMS_WAREHOUSE wa " +
                "LEFT JOIN WMS_WAREHOUSE_ROW wr ON WA. ID = WR.HOUSE_ID WHERE WA.IS_ACTIVE = '1' AND WA.id = :houseId AND WR.ROW_X = :row_x");
        Map<String, Object> param = new HashMap<>();
        param.put("houseId", houseId);
        param.put("row_x", xPos);
        return DatabaseExecuter.queryListFirst(sql, param);
    }

}
