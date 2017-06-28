package com.wxzd.efcs.business.application.workProcedures.FormationOut;


import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureSelector;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("formationOutExcutorSelector")
public class FormationOutExecutorSelector implements WorkProcedureSelector {
    private final WorkProcedure currentBusinessWorkProcedure = WorkProcedure.Formation_Out;

    @Autowired
    private FormationOutExecutorService formationInExcutorService;

    @Override
    public WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure) {
                if (currentBusinessWorkProcedure == workProcedure) {
            return formationInExcutorService;
        }

        return null;
    }
}
