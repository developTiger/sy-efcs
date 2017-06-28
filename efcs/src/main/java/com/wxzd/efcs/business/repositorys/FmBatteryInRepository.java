package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.form.FmBatteryIn;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryOut;
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
public class FmBatteryInRepository  extends DomainRepository<FmBatteryIn> {
    public FmBatteryIn getByNo(String formNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("form_no", formNo);
        params.put("is_active", true);

        List<FmBatteryIn> result=query.from(FmBatteryIn.class).where(params).select(FmBatteryIn.class).toList();
        if(result.size()>0)
            return result.get(0);
        else
            return null;
    }
}
