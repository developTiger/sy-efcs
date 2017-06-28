package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.Instruction;
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
public class InstructionRepository   extends DomainRepository<Instruction> {

    public Instruction getByNo(String formNo) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("instr_no", formNo);
        params.put("is_active", true);

        List<Instruction> dispatch=query.from(Instruction.class).where(params).select(Instruction.class).toList();
        if(dispatch.size()>0)
            return dispatch.get(0);
        else
            return null;
    }
}
