package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
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
public class FmPalletizeRepository extends DomainRepository<FmPalletize> {
    public FmPalletize getByNo(String formNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("form_no", formNo);
        params.put("is_active", true);

        List<FmPalletize> result=query.from(FmPalletize.class).where(params).select(FmPalletize.class).toList();
        if(result.size()>0)
            return result.get(0);
        else
            return null;
    }

    public FmPalletize getByPalletNo(String pallet_no) {

        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("pallet_no", pallet_no);
        params.put("is_active", true);

        List<FmPalletize> procedures=query.from(FmPalletize.class).where(params).select(FmPalletize.class).toList();
        if(procedures.size()>0)
            return procedures.get(0);
        else
            return null;
    }
}
