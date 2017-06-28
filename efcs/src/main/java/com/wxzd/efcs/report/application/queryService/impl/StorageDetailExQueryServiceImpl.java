package com.wxzd.efcs.report.application.queryService.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.report.application.querys.enums.OverdueState;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import org.springframework.stereotype.Service;

import com.wxzd.efcs.business.application.dtos.StorageListDto;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.report.application.dtos.DashBoardStatisticsDto;
import com.wxzd.efcs.report.application.dtos.DataSummaryDto;
import com.wxzd.efcs.report.application.dtos.DetailListDto;
import com.wxzd.efcs.report.application.dtos.FormatStatisticsDto;
import com.wxzd.efcs.report.application.dtos.StorageBatteryDetailDto;
import com.wxzd.efcs.report.application.dtos.TestStatisticsDto;
import com.wxzd.efcs.report.application.queryService.StorageDetailExQueryService;
import com.wxzd.efcs.report.application.querys.DetailListExQuery;
import com.wxzd.efcs.report.application.querys.StorageListExQuery;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.core.domain.entities.enums.HouseCategory;

/**
 * Created by LYK on 2017/4/18
 */
@Service
public class StorageDetailExQueryServiceImpl implements StorageDetailExQueryService {

    @Override
    public PageResult<StorageListDto> getStorageRealTimePaged(StorageListExQuery exQuery) {
        StringBuilder sql = new StringBuilder("SELECT wa.house_no, sl.LOC_NO, WS.pallet_no, ws.sto_type, ws.sto_container_no, ws.sku_barcode, sk.sku_name, ws.sto_count, ws.sto_unit, ws.create_datetime " +
                " FROM WMS_STORAGE ws " +
                " LEFT JOIN WMS_STORAGE_LOCATION sl ON WS.LOCATION_ID = sl.id " +
                " LEFT JOIN WMS_WAREHOUSE wa ON sl.house_id = wa. ID " +
                " LEFT JOIN WMS_SKU sk ON ws.sku_id = sk. ID " +
                " WHERE WS.IS_ACTIVE = '1' ");
        Map<String, Object> params = new HashMap<>();
        if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
            sql.append(" and wa.house_no like :house_no");
            params.put("house_no", "%" + exQuery.getHouse_no() + "%");
        }
        if (!StringUtl.isEmpty(exQuery.getLoc_no())) {
            sql.append(" and sl.loc_no = :locNo");
            params.put("locNo", exQuery.getLoc_no());
        }
        if (!StringUtl.isEmpty(exQuery.getPallet_no())) {
            sql.append(" and ws.sto_container_no like :sto_container_no");
            params.put("sto_container_no", "%" + exQuery.getPallet_no() + "%");
        }
        if (!StringUtl.isEmpty(exQuery.getSku_id())) {
            sql.append(" and sk.id = :sku");
            params.put("sku", exQuery.getSku_id());
        }
        if (!StringUtl.isEmpty(exQuery.getSku_barcode())) {
            sql.append(" and ws.sku_barcode like :sku_barcode");
            params.put("sku_barcode", "%" + exQuery.getSku_barcode() + "%");
        }
        //我们程序自动适应时间的，没必要用to_char,应该可以直接用Date进行比较的
//        if (exQuery.getCreate_datetime() != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            sql.append(" and TO_CHAR(ws.create_datetime,'yyyy-mm-dd') = :create_datetime ");
//            params.put("create_datetime", sdf.format(exQuery.getCreate_datetime()));
//        }
        //缺少的托盘查询以添加，是否缺少别的查询条件，并且查询是否都应该用%
        if (!StringUtl.isEmpty(exQuery.getPallet_no())) {
            sql.append(" and WS.pallet_no like :palletNo");
            params.put("palletNo", "%" + exQuery.getPallet_no() + "%");
        }
        sql.append(" order by create_datetime desc ");
        return DatabaseExecuter.queryBeanPaged(sql, params, exQuery.getPage(), exQuery.getRow(), StorageListDto.class);
    }

    @Override
    public StorageBatteryDetailDto getBatteryDetailBylocationId(UUID locationId) {
        StringBuilder sql = new StringBuilder(" select ")
                .append(SqlUtl.getColumns(StorageBatteryDetailDto.class, "pd"))
                .append(",sl.x_pos xPos, sl.y_pos yPos, sl.z_pos zPos, PD.container_no pallet_no, wa.house_no ")
                .append(" from fcs_pallet_dispatch pd ")
                .append(" LEFT JOIN WMS_STORAGE st ON PD.CONTAINER_NO = st.STO_CONTAINER_NO ")
                .append(" LEFT JOIN WMS_STORAGE_LOCATION sl ON st.location_id = sl.id ")
                .append(" LEFT JOIN WMS_WAREHOUSE wa on pd.house_id = wa.id ")
                .append(" WHERE pd.is_active = '1' and  st.sto_type =:sto_type AND sl. ID =:locationId AND pd.dispatch_status =:dispatch_status ");
        Map<String, Object> map = new HashMap<>();
        map.put("sto_type", StorageType.container);
        map.put("locationId", locationId);
        map.put("dispatch_status", PalletDispatchStatus.Dispatching);
        return DatabaseExecuter.queryBeanEntity(sql, map, StorageBatteryDetailDto.class);
    }

    @Override
    public PageResult<DataSummaryDto> getDataSumPaged(DetailListExQuery exQuery) {
        StringBuilder sql = new StringBuilder(" select sl.x_pos,count (1) pallet_total, ")
                .append(" count ( case when pd.dispatch_status = 'dispatching' then 1 else null end ) dispatching,")
                .append(" count ( case when pd.pallet_status = 'out_waiting' then 1 else null end ) outwaiting, ")
                .append(" count ( case when floor (( fp.out_time - fp.out_plan_time ) * 24 ) >= 2 then 1 else null end ) overdue_twohour, ")
                .append(" count ( case when floor (( fp.out_time - fp.out_plan_time ) * 24 ) >= 4 then 1 else null end ) overdue_fourhour, ")
                .append(" count ( case when floor (( fp.out_time - fp.out_plan_time ) * 24 ) >= 8 then 1 else null end ) overdue_eighthour ")
                .append(" from wms_storage ws left join wms_storage_location sl on ws.location_id = sl.id ")
                .append(" left join wms_relate_zone_and_location wz on sl. id = wz.location_id ")
                .append(" left join fcs_pallet_dispatch pd on ws.sto_container_no = pd.container_no  ")
                .append(" left join fsc_fm_procedure fp on pd.current_form_no = fp.form_no ")
                .append(" where ws.is_active = '1' and pd.is_active = '1' and ws.sto_type = 'container' and sl.loc_type <> :loc_type ");
        Map<String, Object> params = new HashMap<>();
        params.put("loc_type", LocationType.virtual);
        if (exQuery != null) {
            if (exQuery.getHouse_id() != null) {
                sql.append(" and ws.house_id = :house_id ");
                params.put("house_id", exQuery.getHouse_id());
            }
            if (exQuery.getZone_id() != null) {
                sql.append(" and wz.zone_id = :zone_id ");
                params.put("zone_id", exQuery.getZone_id());
            }
        }
        sql.append(" group by sl.x_pos ");
        return DatabaseExecuter.queryBeanPaged(sql, params, exQuery.getPage(), exQuery.getRow(), DataSummaryDto.class);
    }

    @Override
    public PageResult<DetailListDto> getDetailListPaged(DetailListExQuery exQuery) {
        StringBuilder sql = new StringBuilder("SELECT SL.X_POS, SL.Y_POS, SL.Z_POS, WS.STO_CONTAINER_NO, pd.pallet_status, fp.out_plan_time,")
                .append("( CASE WHEN fp.out_plan_time > SYSDATE THEN 'NotExpired' WHEN fp.out_plan_time < SYSDATE THEN 'Overdue' ELSE NULL END ) overdueState,")
                .append(" fp.stay_time, ( CASE WHEN fp.stay_time > fp.stay_plan_time THEN ( fp.stay_time - fp.stay_plan_time ) WHEN fp.stay_time < fp.stay_plan_time THEN 0 ELSE NULL END ) overdue_time ")
                .append(" from wms_storage ws ")
                .append(" join wms_storage_location sl on ws.location_id = sl.id ")
                .append(" left join wms_relate_zone_and_location wz on sl. id = wz.location_id ")
                .append(" left join fcs_pallet_dispatch pd on ws.sto_container_no = pd.container_no ")
                .append(" left join fsc_fm_procedure fp on pd.current_form_no = fp.form_no ")
                .append(" where ws.is_active = '1' and pd.is_active = '1' and ws.sto_type = 'container' and sl.loc_type <> 'virtual' ");
        Map<String, Object> params = new HashMap<>();
        params.put("loc_type", LocationType.virtual);
        if (exQuery != null) {
            if (exQuery.getHouse_id() != null) {
                sql.append(" and ws.house_id = :house_id ");
                params.put("house_id", exQuery.getHouse_id());
            }
            if (exQuery.getZone_id() != null) {
                sql.append(" and wz.zone_id = :zone_id ");
                params.put("zone_id", exQuery.getZone_id());
            }
            if (exQuery.getxPos() != null) {
                sql.append(" and sl.x_pos = :x_pos ");
                params.put("x_pos", exQuery.getxPos());
            }
            if (exQuery.getyPos() != null) {
                sql.append(" and sl.x_pos = :x_pos ");
                params.put("x_pos", exQuery.getxPos());
            }
            if (exQuery.getyPos() != null) {
                sql.append(" and sl.y_pos = :y_pos ");
                params.put("y_pos", exQuery.getyPos());
            }
            if (exQuery.getzPos() != null) {
                sql.append(" and sl.z_pos = :z_pos ");
                params.put("z_pos", exQuery.getzPos());
            }
            if (!StringUtl.isEmpty(exQuery.getPallet_no())) {
                sql.append(" and ws.sto_container_no = :pallet_no ");
                params.put("pallet_no", exQuery.getPallet_no());
            }
            if (exQuery.getPalletStatus() != null) {
                sql.append(" and pd.pallet_status = :pallet_status ");
                params.put("pallet_status", exQuery.getPalletStatus());
            }
            if (exQuery.getOverdueState() != null) {
                if (OverdueState.Overdue.equals(exQuery.getOverdueState())) {
                    sql.append(" and fp.out_plan_time > SYSDATE ");
                } else if (OverdueState.NotExpired.equals(exQuery.getOverdueState())) {
                    sql.append(" and fp.out_plan_time < SYSDATE ");
                }
            }
        }
        sql.append(" order by sl.y_pos asc,sl.z_pos asc ");
        return DatabaseExecuter.queryBeanPaged(sql, params, exQuery.getPage(), exQuery.getRow(), DetailListDto.class);
    }


    /**
     * 获取已制和优品数量
     *
     * @return
     */
    @Override
    public Map<String, Object> getProductDashboardStatistics() {
        StringBuilder sql = new StringBuilder("SELECT COUNT (1) total, " +
                " COUNT ( CASE WHEN BATTERY_STATUS = '0' AND TEST_COMPLETE_TIME IS NOT NULL THEN '1' END ) excellent, " +
                " COUNT ( CASE WHEN BATTERY_STATUS <> '3' AND TEST_COMPLETE_TIME IS NOT NULL THEN '1' END ) hasmade, " +
                " COUNT ( CASE WHEN BATTERY_STATUS <> '3' AND TEST_COMPLETE_TIME IS NULL THEN '1' END ) instorage " +
                " FROM FCS_BATTERY_INFO WHERE IS_ACTIVE = '1'");
        return DatabaseExecuter.queryListFirst(sql);
    }


    /**
     * 首页仪表盘
     */
    @Override
    public List<DashBoardStatisticsDto> getDashBoardStatistics() {
        StringBuilder sql = new StringBuilder("SELECT WA. ID houseId, WA.HOUSE_NO houseNo, WA.HOUSE_NAME, WORK_PROCEDURE, COUNT (1) counts " +
                "FROM FCS_PALLET_DISPATCH pd LEFT JOIN wms_warehouse wa ON PD.HOUSE_ID = WA. ID WHERE PD.is_active = '1'  " +
                "GROUP BY WA. ID, WA.HOUSE_NO, WA.HOUSE_NAME, WORK_PROCEDURE");
        List<DashBoardStatisticsDto> list = DatabaseExecuter.queryBeanList(sql, DashBoardStatisticsDto.class);
        Map<String, DashBoardStatisticsDto> mapList = new HashMap<>();
        for (DashBoardStatisticsDto dto : list) {
            DashBoardStatisticsDto statisticsDto = mapList.get(dto.getHouseNo());
            if (statisticsDto == null) {
                statisticsDto = new DashBoardStatisticsDto(dto.getHouseId(), dto.getHouseNo(), dto.getHouseName());
            }
            dtoToMerge(dto, statisticsDto);
            mapList.put(dto.getHouseNo(), statisticsDto);
        }
        List<DashBoardStatisticsDto> dtos = dtoToHevy(mapList);

        List<DashBoardStatisticsDto> allDtos = getAllWarehouse();
        for (DashBoardStatisticsDto dto : allDtos) {
            copyHouse(dto, dtos);
        }
        return allDtos;
    }


    private List<DashBoardStatisticsDto> getAllWarehouse() {
        StringBuilder sql = new StringBuilder(" SELECT id houseId,HOUSE_NO houseNo,HOUSE_NAME houseName FROM WMS_WAREHOUSE WHERE IS_ACTIVE = :active and house_type=:type ");
        Map<String, Object> param = new HashMap<>();
        param.put("active", Boolean.TRUE);
        param.put("type", HouseCategory.storehouse);
        return DatabaseExecuter.queryBeanList(sql, param, DashBoardStatisticsDto.class);
    }


    private void copyHouse(DashBoardStatisticsDto dto, List<DashBoardStatisticsDto> dtos) {
        for (DashBoardStatisticsDto statisticsDto : dtos) {
            if (dto.getHouseId().equals(statisticsDto.getHouseId())) {
                BeanUtl.copyProperties(statisticsDto, dto);
            }
        }
    }


    //Map转list输出
    private List<DashBoardStatisticsDto> dtoToHevy(Map<String, DashBoardStatisticsDto> mapList) {
        List<DashBoardStatisticsDto> list = new ArrayList<>();
        for (DashBoardStatisticsDto dto : mapList.values()) {
            if (!list.contains(dto)) list.add(dto);
        }
        return list;
    }

    private void dtoToMerge(DashBoardStatisticsDto origDto, DashBoardStatisticsDto descDto) {
        WorkProcedure workProcedure = origDto.getWork_procedure();
        Integer counts = origDto.getCounts();
        if (workProcedure != null) {
            switch (workProcedure) {
                case Formation_Palletize: {
                    descDto.setFormation_Palletize(counts);
                    break;
                }
                case High_Temperature: {
                    descDto.setHighCount(counts);
                    break;
                }
                case Formation: {
                    descDto.setFormationCount(counts);
                    break;
                }
                case Formation_Split: {
                    descDto.setFormationSplit(counts);
                    break;
                }
                case Formation_Rework: {
                    descDto.setFormationReworkCount(counts);
                    break;
                }
                case Test_Palletize: {
                    descDto.setTestPalletizeCount(counts);
                    break;
                }
                case Normal_Temperature_1: {
                    descDto.setNormalTemperature1Count(counts);
                    break;
                }
                case Normal_Temperature_2: {
                    descDto.setNormalTemperature2Count(counts);
                    break;
                }
                case Test_Pallet_Split: {
                    descDto.setTestSplitCount(counts);
                    break;
                }
            }
        }
    }


    @Override
    public FormatStatisticsDto getFormatStatistics(UUID houseId) {
        StringBuilder sql = new StringBuilder("SELECT WA. ID, WA.HOUSE_NO, WA.HOUSE_NAME, WORK_PROCEDURE, PALLET_STATUS,COUNT (1) counts " +
                "FROM FCS_PALLET_DISPATCH pd LEFT JOIN wms_warehouse wa ON PD.HOUSE_ID = WA. ID WHERE PD.is_active = '1'  " +
                "GROUP BY WA. ID, WA.HOUSE_NO, WA.HOUSE_NAME, WORK_PROCEDURE,PALLET_STATUS");
        List<FormatStatisticsDto> list = DatabaseExecuter.queryBeanList(sql, FormatStatisticsDto.class);
        FormatStatisticsDto formatStatisticsDto = new FormatStatisticsDto();
        for (FormatStatisticsDto dto : list) {
            WorkProcedure workProcedure = dto.getWork_procedure();
            PalletStatus palletStatus = dto.getPallet_status();
            Integer counts = dto.getCounts();
            switch (workProcedure) {
                case Formation_Palletize: {
                    formatStatisticsDto.setFormation_Palletize(counts);
                    break;
                }
                case High_Temperature: {
                    switch (palletStatus) {
                        case In_Waiting:
                            formatStatisticsDto.setHighWait(counts);
                            break;
                        case In_Finished:
                            formatStatisticsDto.setHighFinish(counts);
                            break;
                        case Out_Finished:
                            formatStatisticsDto.setHighOut(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case Formation: {
                    switch (palletStatus) {
                        case In_Waiting:
                            formatStatisticsDto.setFormatWait(counts);
                            break;
                        case In_Finished:
                            formatStatisticsDto.setFormatFinish(counts);
                            break;
                        case Out_Finished:
                            formatStatisticsDto.setFormatOut(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case Formation_Rework_Palletize: {
                    formatStatisticsDto.setFormationReworkPalletize(counts);
                    break;
                }
                case Formation_Rework: {
                    switch (palletStatus) {
                        case In_Waiting:
                            formatStatisticsDto.setFormationReworkWait(counts);
                            break;
                        case In_Finished:
                            formatStatisticsDto.setFormationReworkFinish(counts);
                            break;
                        case Out_Finished:
                            formatStatisticsDto.setFormationReworkOut(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case Formation_Split: {
                    switch (palletStatus) {
                        case In_Waiting:
                            formatStatisticsDto.setFormationSplitWait(counts);
                            break;
                        case In_Finished:
                            formatStatisticsDto.setFormationSplitFinish(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }

        }
        formatStatisticsDto.setIsEmpty(getEmptyCount());
        return formatStatisticsDto;
    }

    /**
     * 获取空托盘数量
     *
     * @return
     */
    private Integer getEmptyCount() {
        StringBuilder sql = new StringBuilder("SELECT count(1) FROM FCS_PALLET_DISPATCH where IS_ACTIVE = '1' AND IS_EMPTY='1'");
        return Integer.valueOf(DatabaseExecuter.queryNumber(sql).toString());
    }


    @Override
    public TestStatisticsDto getTestStatistics(UUID houseId) {
        StringBuilder sql = new StringBuilder("SELECT WA. ID, WA.HOUSE_NO, WA.HOUSE_NAME, WORK_PROCEDURE, PALLET_STATUS,COUNT (1) counts " +
                "FROM FCS_PALLET_DISPATCH pd LEFT JOIN wms_warehouse wa ON PD.HOUSE_ID = WA. ID WHERE PD.is_active = '1'  " +
                "GROUP BY WA. ID, WA.HOUSE_NO, WA.HOUSE_NAME, WORK_PROCEDURE,PALLET_STATUS");
        List<TestStatisticsDto> list = DatabaseExecuter.queryBeanList(sql, TestStatisticsDto.class);
        TestStatisticsDto testStatisticsDto = new TestStatisticsDto();
        for (TestStatisticsDto dto : list) {
            WorkProcedure workProcedure = dto.getWork_procedure();
            PalletStatus palletStatus = dto.getPallet_status();
            Integer counts = dto.getCounts();
            switch (workProcedure) {
                case Test_Palletize: {
                    testStatisticsDto.setTestPalletize(counts);
                    break;
                }
                case Normal_Temperature_1: {
                    switch (palletStatus) {
                        case In_Waiting:
                            testStatisticsDto.setNormalTemperature1Wait(counts);
                            break;
                        case In_Finished:
                            testStatisticsDto.setNormalTemperature1Finish(counts);
                            break;
                        case Out_Finished:
                            testStatisticsDto.setNormalTemperature1Out(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case Test_OCV_1: {
                    switch (palletStatus) {
                        case In_Waiting:
                            testStatisticsDto.setTestOCV1Wait(counts);
                            break;
                        case In_Finished:
                            testStatisticsDto.setTestOCV1(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case Normal_Temperature_2: {
                    switch (palletStatus) {
                        case In_Waiting:
                            testStatisticsDto.setNormalTemperature2Wait(counts);
                            break;
                        case In_Finished:
                            testStatisticsDto.setNormalTemperature2Finish(counts);
                            break;
                        case Out_Finished:
                            testStatisticsDto.setNormalTemperature2Out(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case Test_OCV_2: {
                    switch (palletStatus) {
                        case In_Waiting:
                            testStatisticsDto.setTestOCV2Wait(counts);
                            break;
                        case In_Finished:
                            testStatisticsDto.setTestOCV2(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case Test_Pallet_Split: {
                    switch (palletStatus) {
                        case In_Waiting:
                            testStatisticsDto.setTestPalletSplitWait(counts);
                            break;
                        case In_Finished:
                            testStatisticsDto.setTestPalletSplitFinish(counts);
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }
        }
        return testStatisticsDto;
    }

    //设置时间区间
    private String getTime(Date date, String state) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if ("begin".equals(state)) {
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        if ("end".equals(state)) {
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        }
        return sdf.format(calendar.getTime());
    }


    @Override
    public List<PalletDetailDto> betteryList(UUID id) {
        StringBuilder sql = new StringBuilder().append("select ").append(SqlUtl.getColumns(PalletDetail.class)).append(" from fcs_pallet_inner_detail where is_active='1'")
                .append(" and pallet_dispatch_id=:id");
        Map map = new HashMap();
        map.put("id", id);
        return DatabaseExecuter.queryBeanList(sql, map, PalletDetailDto.class);
    }

    @Override
    public Integer getSkuCounstByHouseId(UUID houseId) {
        StringBuilder sql = new StringBuilder("SELECT count(1) counts FROM ( SELECT DISTINCT SKU_ID FROM WMS_STORAGE WHERE IS_ACTIVE = '1' " +
                "AND SKU_ID IS NOT NULL AND HOUSE_ID =:houseId )");
        Map<String, Object> param = new HashMap<>();
        param.put("houseId", houseId);
        return DatabaseExecuter.queryNumber(sql, param).intValue();
    }
}
