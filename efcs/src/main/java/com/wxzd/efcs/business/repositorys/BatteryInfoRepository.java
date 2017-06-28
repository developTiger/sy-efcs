package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.wms.ddd.DomainRepository;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouzh on 2017/4/18.
 */
@Repository
public class BatteryInfoRepository extends DomainRepository<BatteryInfo> {

    public BatteryInfo getByBarcode(String barcode) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("battery_barcode", barcode);
        params.put("is_active", true);

        List<BatteryInfo> result = query.from(BatteryInfo.class).where(params).select(BatteryInfo.class).toList();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
    }


    public List<BatteryInfo> getByBarcodes(List<String> barcodeList) {

        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("battery_barcode", barcodeList);
        params.put("is_active", true);

        List<BatteryInfo> result = query.from(BatteryInfo.class).where(params).select(BatteryInfo.class).toList();
        return result;


    }
}
