package com.wxzd.efcs.business.application.workProcedures.formationPalletizeRework;


import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureSelector;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("formationPalletizeReworkExecutorSelector")
public class FormationPalletizeReworkExecutorSelector implements WorkProcedureSelector {
    private final WorkProcedure currentBusinessWorkProcedure = WorkProcedure.Formation_Rework_Palletize;

    @Autowired
    private FormationPalletizeReworkExecutorService formationPalletizeExcutorService;

    @Override
    public WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure) {
        if (currentBusinessWorkProcedure == workProcedure) {
            return formationPalletizeExcutorService;
        }

        return null;
    }
}
