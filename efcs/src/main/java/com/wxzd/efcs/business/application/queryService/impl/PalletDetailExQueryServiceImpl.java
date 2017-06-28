package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.*;
import com.wxzd.efcs.business.application.queryService.PalletDetailExQueryService;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
@Service
public class PalletDetailExQueryServiceImpl implements PalletDetailExQueryService {
    @Override
    public PageResult<PalletDispatchDto> getPalletDispatchPaged(PalletDispatchExQuery exQuery) {
        StringBuilder sql = new StringBuilder("SELECT ").append(SqlUtl.getColumns(PalletDispatchDto.class, "pd"))
                .append(" ,wa.house_no FROM FCS_PALLET_DISPATCH pd LEFT JOIN WMS_WAREHOUSE wa ON pd.HOUSE_ID = wa. ID WHERE pd.is_active = :active ");
        Map<String, Object> map = new HashMap<>();
        map.put("active", Boolean.TRUE);
        if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
            sql.append(" AND wa.house_no like :house_no ");
            map.put("house_no", "%" + exQuery.getHouse_no() + "%");
        }
        if (exQuery.getDispatchStatus() != null) {
            sql.append(" and pd.dispatch_status = :dispatchStatus ");
            map.put("dispatchStatus", exQuery.getDispatchStatus());
        }
        if (exQuery.getPallet_status() != null) {
            sql.append(" AND PD.pallet_status =:pallet_status ");
            map.put("pallet_status", exQuery.getPallet_status());
        }
        if (!StringUtl.isEmpty(exQuery.getContainer_no())) {
            sql.append(" AND PD.container_no like :container_no ");
            map.put("container_no", "%" + exQuery.getContainer_no() + "%");
        }
        if (exQuery.getWork_procedure() != null) {
            sql.append(" AND PD.WORK_PROCEDURE =:work_procedure ");
            map.put("work_procedure", exQuery.getWork_procedure());
        }
        if (!StringUtl.isEmpty(exQuery.getCurrent_pos())) {
            sql.append(" AND PD.current_pos like :current_pos ");
            map.put("current_pos", "%" + exQuery.getCurrent_pos() + "%");
        }
        sql.append(" order by pd.create_datetime desc ");
        return DatabaseExecuter.queryBeanPaged(sql, map, exQuery.getPage(), exQuery.getRow(), PalletDispatchDto.class);
    }


    @Override
    public PalletDispatchDto getPalletDispatch(UUID id) {
        StringBuilder sql = new StringBuilder("SELECT wa.house_no, CASE pos_type WHEN :loc THEN CURRENT_POS ELSE NULL END loc_no, CASE pos_type WHEN :equip THEN CURRENT_POS ELSE NULL END equip_no ,")
                .append(SqlUtl.getColumns(PalletDispatchDto.class, "pd"))
                .append(" FROM fcs_pallet_dispatch pd JOIN wms_warehouse wa ON pd.house_id = wa. ID WHERE pd.is_active=1  ");
        Map<String, Object> map = new HashMap<>();
        map.put("loc", PositionType.Storage_Location);
        map.put("equip", PositionType.Transport_Location);
//        map.put("dispatch_status", PalletDispatchStatus.Dispatching);
        if (id != null) {
            sql.append(" and pd.id = :id");
            map.put("id", id);
        }
        return DatabaseExecuter.queryBeanEntity(sql, map, PalletDispatchDto.class);
    }

    /**
     * 根据托盘号查询托盘详情
     *
     * @param palletNo
     * @return
     */
    @Override
    public PalletDispatchDto getPalletDispatchByNo(String palletNo) {
        StringBuilder sql = new StringBuilder(" select ")
                .append(SqlUtl.getColumns(PalletDispatchDto.class, "pd"))
                .append(" ,wa.house_no,wt.sku_max_count ")
                .append(" from fcs_pallet_dispatch pd ")
                .append(" left join wms_warehouse wa on pd.house_id = wa.id  ")
                .append(" left join wms_container wc on pd.container_id = wc. id  ")
                .append(" left join wms_container_type wt on wc.type_id = wt. id ")
                .append(" where pd.is_active = '1' and pd.dispatch_status =:dispatch_status ");
        Map<String, Object> params = new HashMap<>();
        params.put("dispatch_status", PalletDispatchStatus.Dispatching);
        if (!StringUtl.isEmpty(palletNo)) {
            sql.append(" and pd.container_no = :palletNo ");
            params.put("palletNo", palletNo);
        }
        List<PalletDispatchDto> list = DatabaseExecuter.queryBeanList(sql, params, PalletDispatchDto.class);
        if (list != null)
            return list.get(0);
        return null;
    }


    /**
     * 根据位置 获取托盘信息
     *
     * @param houseNo
     * @param locNo
     * @return
     */
    @Override
    public PalletWithTypeDto getPalletInfoByPosition(String houseNo, String locNo) {

        String sql = "select c.type_name aliasTypeName, t.container_alias_name typeName," + SqlUtl.getColumns(PalletDispatch.class, "d") + " from fcs_pallet_dispatch d join wms_container c on d.container_id = c.id " +
                "join wms_container_type t on t.id = c.type_id join wms_warehouse w on d.house_id =w.id  " +
                "where w.house_no = :houseNo and d.current_pos = :locNo and d.dispatch_status=:dispatchStatus";
        Map<String, Object> parems = new HashMap<>();
        parems.put("houseNo", houseNo);
        parems.put("locNo", locNo);
        parems.put("dispatchStatus", PalletDispatchStatus.Dispatching);
        List<PalletWithTypeDto> dispatches = DatabaseExecuter.queryBeanList(sql, parems, PalletWithTypeDto.class);

        if (dispatches.size() > 0) {
            return dispatches.get(0);
        }
        return null;

    }


    /*         ******** 分割线  ***********         */

    private GaiaQuery getQuery(PalletBatteryExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("SELECT ").append(SqlUtl.getColumns(PalletDetailDto.class, "pd"))
                .append(" ,wa.house_no, sk.sku_name, sk.sku_no, pa.pos_type, pa.current_pos, pa.work_procedure  " +
                        " from fcs_pallet_inner_detail pd  " +
                        " left join fcs_pallet_dispatch pa on pd.pallet_dispatch_id = pa.id " +
                        " left join wms_warehouse wa on pa.house_id = wa.id  " +
                        " left join wms_sku sk on pd.sku_id = sk. id where pd.is_active = :active ");
        query.putMap("active", Boolean.TRUE);
        if (exQuery != null) {
            if (exQuery.getId() != null) {
                sql.append(" and pd.id=:id ");
                query.putMap("id", exQuery.getId());
            }
            if (exQuery.getPalletId() != null) {
                sql.append(" and pd.pallet_dispatch_id = :palletId");
                query.putMap("palletId", exQuery.getPalletId());
            }else{
                sql.append("and pa.dispatch_status= :dispatch_status ");
                query.putMap("dispatch_status", PalletDispatchStatus.Dispatching);

            }
            if (!StringUtl.isEmpty(exQuery.getBattery_barcode())) {
                sql.append(" and pd.battery_barcode like :battery_barcode");
                query.putMap("battery_barcode", "%" + exQuery.getBattery_barcode() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getBattery_status())) {
                sql.append(" and pd.battery_status in ( ")
                        .append(exQuery.getBattery_status())
                        .append(" )");
            }
            if (!StringUtl.isEmpty(exQuery.getForm_no())) {
                sql.append(" and pd.form_no like :form_no");
                query.putMap("form_no", "%" + exQuery.getForm_no() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getContainer_no())) {
                sql.append(" and pd.pallet_no like :pallet_no");
                query.putMap("pallet_no", "%" + exQuery.getContainer_no() + "%");
            }
            if (exQuery.getWork_procedure() != null) {
                sql.append(" and pa.work_procedure = :work_procedure");
                query.putMap("work_procedure", exQuery.getWork_procedure());
            }
            if (!StringUtl.isEmpty(exQuery.getCurrent_pos())) {
                sql.append(" and pa.current_pos like :current_pos");
                query.putMap("current_pos", "%" + exQuery.getCurrent_pos() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and wa.house_no like :houseNo");
                query.putMap("houseNo", "%" + exQuery.getHouse_no() + "%");
            }
        }else{
            sql.append("and pa.dispatch_status= :dispatch_status ");
            query.putMap("dispatch_status", PalletDispatchStatus.Dispatching);

        }

        sql.append(" order by pa.create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }


    /**
     * 分页查询托盘货物明细
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<PalletDetailDto> getInnerDetailPaged(PalletBatteryExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), PalletDetailDto.class);
    }

    /**
     * 托盘货物明细不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<PalletDetailDto> getInnerDetailNoPaged(PalletBatteryExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), PalletDetailDto.class);
    }


    /**
     * 根据托盘号查询电池信息
     *
     * @param palletNo
     * @return
     */
    @Override
    public List<PalletDetailDto> getInnerDetailByPalletNo(String palletNo) {
        String sql = "select  " + SqlUtl.getColumns(PalletDetail.class, "d") + " from  fcs_pallet_dispatch p join  fcs_pallet_inner_detail d on p.id = d.pallet_dispatch_id " +
                " where p.is_active= 1 and  p.container_no =:palletNo and p.dispatch_status= :dispatch_status order by create_datetime desc";
        Map<String, Object> params = new HashMap<>();
        params.put("dispatch_status", PalletDispatchStatus.Dispatching);
        params.put("palletNo", palletNo);
        return DatabaseExecuter.queryBeanList(sql, params, PalletDetailDto.class);
    }

    /**
     * 根据托盘id查询组盘，实时，拆盘电池信息
     *
     * @param id
     * @return
     */
    @Override
    public List<PalletDetailDto> getInnerDetailById(UUID id) {
        String sql = "select d.* from  fcs_pallet_dispatch p join  fcs_pallet_inner_detail d on p.id = d.pallet_dispatch_id" +
                " join fcs_battery_info i on d.battery_barcode=i.battery_barcode  " +
                " where p.is_active= 1  and p.id=:id ";
        Map<String, Object> params = new HashMap<>();
//        params.put("dispatchStatus", PalletDispatchStatus.Dispatching);
        params.put("id", id);
        return DatabaseExecuter.queryBeanList(sql, params, PalletDetailDto.class);
    }


    @Override
    public List<PalletLocationDto> getPalletLocation(String houseNo, List<Integer> row) {
        String sql = "Select s.sto_container_no container_no,l.x_pos,l.y_pos,l.z_pos from  wms_storage s  " +
                "join wms_warehouse w on w.id = s.house_id  " +
                "join wms_storage_location l on l.id= s.location_id where w.house_no = :houseNo and x_pos in(:xpos)  and s.sto_type= :stoType";
        Map<String, Object> params = new HashMap<>();

        params.put("stoType", StorageType.container);
        params.put("houseNo", houseNo);
        params.put("xpos", row);
        return DatabaseExecuter.queryBeanList(sql, params, PalletLocationDto.class);
    }


    /**
     * 获取house_no
     *
     * @param house_id
     * @return
     */
    @Override
    public Map<String, Object> getHouseNobyId(UUID house_id) {
        StringBuilder sql = new StringBuilder("select house_no from wms_warehouse where is_active=:active and id=:id ");
        Map<String, Object> param = new HashMap<>();
        param.put("active", Boolean.TRUE);
        param.put("id", house_id);
        return DatabaseExecuter.queryListFirst(sql, param);
    }
}
