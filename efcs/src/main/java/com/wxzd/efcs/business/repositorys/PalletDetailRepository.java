package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.DomainRepository;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18.
 */
@Repository
public class PalletDetailRepository extends DomainRepository<PalletDetail> {


    public PalletDetail getActivePalletDetailByBarcode(String palletNo, String barcode) {

        //TODO *
        String sql = "select  " + SqlUtl.getColumns(PalletDetail.class, "d") + " from  fcs_pallet_dispatch p join  fcs_pallet_inner_detail d on p.id = d.pallet_dispatch_id " +
                " where p.is_active= 1 and  p.dispatch_status = :dispatchStatus and p.container_no =:palletNo and d.battery_barcode =:barcode";

        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("dispatchStatus", PalletDispatchStatus.Dispatching);
        params.put("barcode", barcode);
        params.put("palletNo", palletNo);

        List<PalletDetail> procedures = DatabaseExecuter.queryBeanList(sql, params, PalletDetail.class);
        if (procedures.size() > 0)
            return procedures.get(0);
        else
            return null;
    }

    public List<PalletDetail> getActivePalletDetailByPallet(String containerNo) {

        String sql = "select  " + SqlUtl.getColumns(PalletDetail.class, "d") + " from  fcs_pallet_dispatch p join  fcs_pallet_inner_detail d on p.id = d.pallet_dispatch_id " +
                " where p.is_active= 1 and  p.dispatch_status = :dispatchStatus and p.container_no =:palletNo ";

        Map<String, Object> params = new HashMap<>();

        params.put("dispatchStatus", PalletDispatchStatus.Dispatching);
        params.put("palletNo", containerNo);
        return DatabaseExecuter.queryBeanList(sql, params, PalletDetail.class);


    }


    public List<PalletDetail> getActivePalletDetailByPalletWithStatus(String containerNo) {

        String sql = "select  d.battery_barcode, i.battery_status, d.channel_no, d.create_by, d.create_datetime, d.form_no," +
                " d.from_clamp_no, d.from_equip_no, d.from_pos_channel_no, d.from_pos_no, d.from_pos_type, d.id, d.is_active, " +
                "d.is_resort, d.last_modify_by, d.last_modify_datetime, d.pallet_dispatch_id, d.pallet_no, d.sku_id, " +
                "d.to_clamp_no, d.to_equip_no, d.to_pos_channel_no, d.to_pos_no, d.to_pos_type from  fcs_pallet_dispatch p join  fcs_pallet_inner_detail d on p.id = d.pallet_dispatch_id" +
                " join fcs_battery_info i on d.battery_barcode=i.battery_barcode  " +
                " where p.is_active= 1 and  p.dispatch_status = :dispatchStatus and p.container_no =:palletNo  and d.to_pos_no is  null";

        Map<String, Object> params = new HashMap<>();

        params.put("dispatchStatus", PalletDispatchStatus.Dispatching);
        params.put("palletNo", containerNo);
        return DatabaseExecuter.queryBeanList(sql, params, PalletDetail.class);


    }

    public List<PalletDetail> getPalletInnerDetailResort(UUID dispatchId) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("pallet_dispatch_id", dispatchId);
        params.put("is_resort", true);
        List<PalletDetail> details = query.from(clazz).where(params).select(clazz).toList();
        return details;
    }

    public PalletDetail getActivePalletDetailByChannel(String pallet_no, int from_pos_channel_no) {
        String sql = "select  " + SqlUtl.getColumns(PalletDetail.class, "d") + " from  fcs_pallet_dispatch p join  fcs_pallet_inner_detail d on p.id = d.pallet_dispatch_id " +
                " where p.is_active= 1 and  p.dispatch_status = :dispatchStatus and p.container_no =:palletNo and d.channel_no =:channel_no";

        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("dispatchStatus", PalletDispatchStatus.Dispatching);
        params.put("channel_no", from_pos_channel_no);
        params.put("palletNo", pallet_no);

        List<PalletDetail> procedures = DatabaseExecuter.queryBeanList(sql, params, PalletDetail.class);
        if (procedures.size() > 0)
            return procedures.get(0);
        else
            return null;
    }

    public void hardDelete(UUID id) {
        String sql = "delete from fcs_pallet_inner_detail where id =:id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        DatabaseExecuter.update(sql, params);
    }

    public PalletDetail getActivePalletDetailByPalletWithStatus(String palletNo, String batteryNo) {
        String sql = "select  d.battery_barcode, i.battery_status, d.channel_no, d.create_by, d.create_datetime, d.form_no," +
                " d.from_clamp_no, d.from_equip_no, d.from_pos_channel_no, d.from_pos_no, d.from_pos_type, d.id, d.is_active, " +
                "d.is_resort, d.last_modify_by, d.last_modify_datetime, d.pallet_dispatch_id, d.pallet_no, d.sku_id, " +
                "d.to_clamp_no, d.to_equip_no, d.to_pos_channel_no, d.to_pos_no, d.to_pos_type from  fcs_pallet_dispatch p join  fcs_pallet_inner_detail d on p.id = d.pallet_dispatch_id" +
                " join fcs_battery_info i on d.battery_barcode=i.battery_barcode  " +
                " where p.is_active= 1 and  p.dispatch_status = :dispatchStatus and p.container_no =:palletNo  and d.to_pos_no is  null and d.battery_barcode=:batteryNo ";

        Map<String, Object> params = new HashMap<>();

        params.put("dispatchStatus", PalletDispatchStatus.Dispatching);
        params.put("palletNo", palletNo);
        params.put("batteryNo", batteryNo);
        List<PalletDetail> details = DatabaseExecuter.queryBeanList(sql, params, PalletDetail.class);
        if (details.size() > 0) {
            return details.get(0);
        }
        return null;
    }

    public Integer getInnerDetailCountByPalletDispatchId(UUID disId) {
        String sql = "select count(*) from  fcs_pallet_inner_detail d where d.is_active=1 and d.pallet_dispatch_id =:dispatchId";

        Map<String, Object> parems = new HashMap<>();
        parems.put("dispatchId", disId);

        Integer count = DatabaseExecuter.queryNumber(sql, parems).intValue();

        return count;
    }
}
