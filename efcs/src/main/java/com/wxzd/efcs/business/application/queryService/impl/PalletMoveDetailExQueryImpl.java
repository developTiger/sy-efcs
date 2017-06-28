package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.PalletMoveDetailDto;
import com.wxzd.efcs.business.application.queryService.PalletMoveDetailExQuery;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
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
 * Created by LYK on 2017/4/19
 */
@Service
public class PalletMoveDetailExQueryImpl implements PalletMoveDetailExQuery {


    @Override
    public PageResult<PalletMoveDetailDto> getPalletMoveDetailPaged(PalletDispatchExQuery exQuery) {
        if (exQuery == null || exQuery.getId() == null) return null;
        StringBuilder sql = new StringBuilder("SELECT wa.house_no,").append(SqlUtl.getColumns(PalletMoveDetailDto.class, "pm"))
                .append(" FROM FCS_PALLET_MOVE_DETAIL pm JOIN FCS_PALLET_DISPATCH pd ON PM.PALLET_DISPATCH_ID = PD. ID  " +
                        " LEFT JOIN WMS_WAREHOUSE wa ON pd.HOUSE_ID = wa. ID where pm.PALLET_DISPATCH_ID = :id order by pm.create_datetime desc ");
        Map map = new HashMap();
        map.put("id", exQuery.getId());
        return DatabaseExecuter.queryBeanPaged(sql, map, exQuery.getPage(), exQuery.getRow(), PalletMoveDetailDto.class);
    }

    /**
     * @param detailId
     * @param barcode
     */
    @Override
    public List<PalletMoveDetailDto> getPalletMoveDetailById(UUID detailId, String barcode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pa.pallet_no, ").append(SqlUtl.getColumns(PalletMoveDetailDto.class, "pm"))
                .append(" from fcs_pallet_move_detail pm ")
                .append(" join fcs_pallet_inner_detail pa on pm.pallet_dispatch_id = pa.pallet_dispatch_id ")
                .append(" where pa.is_active = :active  ");
        Map<String, Object> param = new HashMap<>();
        param.put("active", Boolean.TRUE);
        if (detailId != null) {
            sql.append(" and pa.id=:id ");
            param.put("id", detailId);
        }
        if (!StringUtl.isEmpty(barcode)) {
            sql.append(" and pa.battery_barcode=:barcode ");
            param.put("barcode", barcode);
        }
        sql.append(" order by pm.create_datetime ASC ");
        return DatabaseExecuter.queryBeanList(sql, param, PalletMoveDetailDto.class);
    }

    /**
     * 获取托盘移动记录
     *
     * @param disId
     * @return
     */
    @Override
    public List<PalletMoveDetailDto> getPalletMoveDetailByDisId(UUID disId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pd.container_no pallet_no, ").append(SqlUtl.getColumns(PalletMoveDetailDto.class, "pm"))
                .append(" from fcs_pallet_move_detail pm ")
                .append(" join fcs_pallet_dispatch pd on pm.pallet_dispatch_id = pd.id ")
                .append(" where pm.is_active = :active  ");
        Map<String, Object> param = new HashMap<>();
        param.put("active", Boolean.TRUE);
        if (disId != null) {
            sql.append(" and pd.id=:id ");
            param.put("id", disId);
        }
        sql.append(" order by pm.create_datetime desc ");
        return DatabaseExecuter.queryBeanList(sql, param, PalletMoveDetailDto.class);
    }
}
