package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
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
public class PalletDispatchRepository extends DomainRepository<PalletDispatch> {
    public PalletDispatch getByNo(String palletNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("container_no", palletNo);
        params.put("is_active", true);
        params.put("dispatch_status", PalletDispatchStatus.Dispatching);

        List<PalletDispatch> dispatch=query.from(PalletDispatch.class).where(params).select(PalletDispatch.class).toList();
        if(dispatch.size()>0)
            return dispatch.get(0);

        else
            return null;
    }
}
