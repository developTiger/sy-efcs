package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import com.wxzd.efcs.business.application.queryService.BatteryInfoExQueryService;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by LYK on 2017/4/19
 */
@Service
public class BatteryInfoExQueryServiceImpl implements BatteryInfoExQueryService {
    private GaiaQuery getQuery(PalletBatteryExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("SELECT ").append(SqlUtl.getColumns(PalletBatteryDto.class, "ba"))
                .append(" ,pd.pallet_dispatch_id,pd.channel_no,pd.form_no,pd.pallet_no,pd.from_equip_no,pd.from_clamp_no,pd.from_pos_type," +
                        " pd.from_pos_no,pd.from_pos_channel_no,pd.to_equip_no,pd.to_clamp_no,pd.to_pos_type,pd.to_pos_no,pd.to_pos_channel_no,pd.is_resort, " +
                        " wa.house_no, sk.sku_name, sk.sku_no, pa.pos_type, pa.current_pos, pa.work_procedure  " +
                        " from fcs_battery_info ba " +
                        " left join fcs_pallet_inner_detail pd on ba.battery_barcode = pd.battery_barcode  " +
                        " left join fcs_pallet_dispatch pa on pd.pallet_dispatch_id = pa. id " +
                        " left join wms_warehouse wa on pa.house_id = wa. id  " +
                        " left join wms_sku sk on ba.sku_id = sk. id where ba.is_active = '1' ");
        if (exQuery != null) {
            if (exQuery.getId() != null) {
                sql.append(" and ba.id=:id ");
                query.putMap("id", exQuery.getId());
            }
            if (exQuery.getPalletId() != null) {
                sql.append(" and pd.pallet_dispatch_id = :palletId");
                query.putMap("palletId", exQuery.getPalletId());
            }
            if (!StringUtl.isEmpty(exQuery.getBattery_barcode())) {
                sql.append(" and ba.battery_barcode like :battery_barcode");
                query.putMap("battery_barcode", "%" + exQuery.getBattery_barcode() + "%");
            }
            if (!StringUtl.isEmpty(exQuery.getBattery_status())) {
                sql.append(" and ba.battery_status in ( ")
                        .append(exQuery.getBattery_status())
                        .append(" )");
            }
            if (exQuery.getDispatch_status() != null) {
                sql.append(" and pa.dispatch_status = :dispatch_status");
                query.putMap("dispatch_status", exQuery.getDispatch_status());
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
        }
        sql.append(" order by ba.create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }


    @Override
    public PageResult<PalletBatteryDto> getPalletBatteryPaged(PalletBatteryExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), PalletBatteryDto.class);
    }

    @Override
    public List<PalletBatteryDto> getPalletBatteryById(PalletBatteryExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), PalletBatteryDto.class);
    }
}
