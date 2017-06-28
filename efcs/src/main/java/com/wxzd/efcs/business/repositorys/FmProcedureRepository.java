package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
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
public class FmProcedureRepository extends DomainRepository<FmProcedure> {

    public FmProcedure getByNo(String formNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("form_no", formNo);
        params.put("is_active", true);

        List<FmProcedure> procedures=query.from(FmProcedure.class).where(params).select(FmProcedure.class).toList();
        if(procedures.size()>0)
            return procedures.get(0);
        else
            return null;
    }


    public FmProcedure getByPalletNo(String pallet_no, WorkProcedure workProcedure){
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("pallet_no", pallet_no);
        params.put("is_active", true);
        params.put("fm_status", FmStatus.Executing);
        if(workProcedure!=null) {
            params.put("work_procedure", workProcedure);
        }

        List<FmProcedure> procedures=query.from(FmProcedure.class).where(params).select(FmProcedure.class).toList();
        if(procedures.size()>0)
            return procedures.get(0);
        else
            return null;
    }
}
