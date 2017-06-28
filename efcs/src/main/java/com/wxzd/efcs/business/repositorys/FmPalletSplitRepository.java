package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
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
public class FmPalletSplitRepository  extends DomainRepository<FmPalletSplit> {

    public FmPalletSplit getByNo(String formNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("form_no", formNo);
        params.put("is_active", true);

        List<FmPalletSplit> result=query.from(clazz).where(params).select(clazz).toList();
        if(result.size()>0)
            return result.get(0);
        else
            return null;
    }

    public FmPalletSplit getByPalletNo(String pallet_no, WorkProcedure workProcedure) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("pallet_no", pallet_no);
        params.put("is_active", true);
        if(workProcedure!=null){
            params.put("work_procedure",workProcedure);
        }
        params.put("fm_status", FmStatus.Executing);

        List<FmPalletSplit> procedures=query.from(clazz).where(params).select(clazz).toList();
        if(procedures.size()>0)
            return procedures.get(0);
        else
            return null;
    }
}
