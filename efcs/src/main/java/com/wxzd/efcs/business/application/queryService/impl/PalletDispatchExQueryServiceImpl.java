package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.PalletDispatchDto;
import com.wxzd.efcs.business.application.dtos.PalletWithTypeDto;
import com.wxzd.efcs.business.application.queryService.PalletDispatchExQueryService;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/5/18
 */
public class PalletDispatchExQueryServiceImpl implements PalletDispatchExQueryService {

    private GaiaQuery getQuery(PalletDispatchExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select wa.house_no, ")
                .append(SqlUtl.getColumns(PalletDispatchDto.class, "pd"))
                .append(" from fcs_pallet_dispatch pd ")
                .append(" left join wms_warehouse wa on pd.house_id = wa. id ")
                .append(" where pd.is_active = :active ");
        if (exQuery != null) {
            //库编号
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" AND wa.house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            //调度状态
            if (exQuery.getDispatchStatus() != null) {
                sql.append(" and pd.dispatch_status = :dispatchStatus ");
                query.putMap("dispatchStatus", exQuery.getDispatchStatus());
            }
            //托盘状态
            if (exQuery.getPallet_status() != null) {
                sql.append(" AND PD.pallet_status =:pallet_status ");
                query.putMap("pallet_status", exQuery.getPallet_status());
            }
            //容器条码
            if (!StringUtl.isEmpty(exQuery.getContainer_no())) {
                sql.append(" AND PD.container_no like :container_no ");
                query.putMap("container_no", "%" + exQuery.getContainer_no() + "%");
            }
            //工序
            if (exQuery.getWork_procedure() != null) {
                sql.append(" AND PD.WORK_PROCEDURE =:work_procedure ");
                query.putMap("work_procedure", exQuery.getWork_procedure());
            }
            //当前位置
            if (!StringUtl.isEmpty(exQuery.getCurrent_pos())) {
                sql.append(" AND PD.current_pos like :current_pos ");
                query.putMap("current_pos", "%" + exQuery.getCurrent_pos() + "%");
            }
        }
        query.putMap("active", Boolean.TRUE);
        sql.append(" order by pd.create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }


    /**
     * 分页查询托盘调度信息
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<PalletDispatchDto> getPalletDispatchPaged(PalletDispatchExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), PalletDispatchDto.class);
    }

    /**
     * 查询托盘调度信息不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<PalletDispatchDto> getPalletDispatchNoPaged(PalletDispatchExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), PalletDispatchDto.class);
    }

    /**
     * 根据托盘ID获取详情
     *
     * @param id
     */
    @Override
    public PalletDispatchDto getPalletDispatch(UUID id) {
        StringBuilder sql = new StringBuilder(" select ")
                .append(SqlUtl.getColumns(PalletDispatchDto.class, "pd"))
                .append(" ,wa.house_no,wt.sku_max_count ")
                .append(" from fcs_pallet_dispatch pd ")
                .append(" left join wms_warehouse wa on pd.house_id = wa.id  ")
                .append(" left join wms_container wc on pd.container_id = wc. id  ")
                .append(" left join wms_container_type wt on wc.type_id = wt. id ")
                .append(" where pd.is_active = '1' ");
        Map<String, Object> params = new HashMap<>();
        if (id != null) {
            sql.append(" and pd.id = :id ");
            params.put("id", id);
            return DatabaseExecuter.queryBeanEntity(sql, params, PalletDispatchDto.class);
        }
        return null;
    }

    /**
     * 根据托盘号查询托盘详情
     *
     * @param palletNo
     * @return
     */
    @Override
    public PalletDispatchDto getPalletDispatchByNo(String palletNo) {
        StringBuilder sql = new StringBuilder(" select wa.house_no, wt.sku_max_count,")
                .append(SqlUtl.getColumns(PalletDispatchDto.class, "pd"))
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
        if (!StringUtl.isEmpty(houseNo) && !StringUtl.isEmpty(locNo)) {
            parems.put("houseNo", houseNo);
            parems.put("locNo", locNo);
            parems.put("dispatchStatus", PalletDispatchStatus.Dispatching);
            List<PalletWithTypeDto> dispatches = DatabaseExecuter.queryBeanList(sql, parems, PalletWithTypeDto.class);
            if (dispatches.size() > 0) {
                return dispatches.get(0);
            }
        }
        return null;
    }
}
