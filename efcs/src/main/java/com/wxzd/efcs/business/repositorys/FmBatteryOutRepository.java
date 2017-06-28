package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.form.FmBatteryOut;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
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
public class FmBatteryOutRepository extends DomainRepository<FmBatteryOut> {

    public FmBatteryOut getByNo(String formNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("form_no", formNo);
        params.put("is_active", true);

        List<FmBatteryOut> result=query.from(FmBatteryOut.class).where(params).select(FmBatteryOut.class).toList();
        if(result.size()>0)
            return result.get(0);
        else
            return null;
    }
}
