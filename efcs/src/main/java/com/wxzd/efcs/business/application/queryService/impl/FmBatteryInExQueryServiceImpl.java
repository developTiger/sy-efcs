package com.wxzd.efcs.business.application.queryService.impl;

import com.wxzd.efcs.business.application.dtos.FmBatteryDto;
import com.wxzd.efcs.business.application.queryService.FmBatteryInExQueryService;
import com.wxzd.efcs.business.application.querys.FmBatteryExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by LYK on 2017/4/27
 */
@Service
public class FmBatteryInExQueryServiceImpl implements FmBatteryInExQueryService {

    private GaiaQuery getQuery(FmBatteryExQuery exQuery) {
        GaiaQuery query = new GaiaQuery();
        StringBuilder sql = new StringBuilder("select pa.id pallet_detail_id,ws.sku_name,ba.battery_status, ")
                .append(SqlUtl.getColumns(FmBatteryDto.class,"fb"))
                .append(" from fcs_fm_battery_in fb left join fcs_pallet_inner_detail pa on fb.battery_barcode = pa.battery_barcode ")
                .append(" LEFT JOIN FCS_BATTERY_INFO ba ON fb.battery_barcode = ba.battery_barcode ")
                .append(" LEFT JOIN WMS_SKU ws ON ba.sku_id = ws.id ")
                .append(" where fb.is_active = '1' ");
        if (exQuery != null) {
            if (exQuery.getId() != null) {
                sql.append(" and fb.id=:id ");
                query.putMap("id", exQuery.getId());
            }
            //表单
            if (!StringUtl.isEmpty(exQuery.getForm_no())) {
                sql.append(" and fb.form_no like :form_no ");
                query.putMap("form_no", "%" + exQuery.getForm_no() + "%");
            }
            //电池条码
            if (!StringUtl.isEmpty(exQuery.getBattery_barcode())) {
                sql.append(" and fb.battery_barcode like :battery_barcode ");
                query.putMap("battery_barcode", "%" + exQuery.getBattery_barcode() + "%");
            }
            //电池状态
            if (!StringUtl.isEmpty(exQuery.getBattery_status())) {
                sql.append(" and ba.battery_status in ( ")
                        .append(exQuery.getBattery_status())
                        .append(" ) ");
            }
            //工序
            if (exQuery.getWork_procedure() != null) {
                sql.append(" and fb.work_procedure=:procedure ");
                query.putMap("procedure", exQuery.getWork_procedure());
            }
            //拉线
            if (!StringUtl.isEmpty(exQuery.getHouse_no())) {
                sql.append(" and fb.house_no like :house_no ");
                query.putMap("house_no", "%" + exQuery.getHouse_no() + "%");
            }
            //设备号
            if (!StringUtl.isEmpty(exQuery.getEquip_no())) {
                sql.append(" and fb.equip_no like :equip_no ");
                query.putMap("equip_no", "%" + exQuery.getEquip_no() + "%");
            }
            //拉线位置
            if (!StringUtl.isEmpty(exQuery.getLine_pos())) {
                sql.append(" and fb.line_pos like :line_pos ");
                query.putMap("line_pos", "%" + exQuery.getLine_pos() + "%");
            }
            //拉线通道号
            if (!StringUtl.isEmpty(exQuery.getLine_channel_no())) {
                sql.append(" and fb.line_channel_no =:line_channel_no ");
                query.putMap("line_channel_no", exQuery.getLine_channel_no());
            }
            //入库时间
            if (exQuery.getStart_time() != null) {
                sql.append(" and fb.operate_datetime >= :operate_datetime  ");
                query.putMap("operate_datetime", exQuery.getStart_time());
            }
            //入库时间
            if (exQuery.getComplete_time() != null) {
                sql.append(" and fb.operate_datetime <= :operate_datetime_end  ");
                query.putMap("operate_datetime_end", exQuery.getComplete_time());
            }

        }
        sql.append(" order by fb.create_datetime desc ");
        query.setQuerySql(sql.toString());
        return query;
    }


    /**
     * 电池入库单分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FmBatteryDto> getFmBatteryInPaged(FmBatteryExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanPaged(query.getQuerySql(), query.getMap(), exQuery.getPage(), exQuery.getRow(), FmBatteryDto.class);
    }

    /**
     * 电池入库单不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmBatteryDto> getFmBatteryInList(FmBatteryExQuery exQuery) {
        GaiaQuery query = getQuery(exQuery);
        return DatabaseExecuter.queryBeanList(query.getQuerySql(), query.getMap(), FmBatteryDto.class);
    }
}
