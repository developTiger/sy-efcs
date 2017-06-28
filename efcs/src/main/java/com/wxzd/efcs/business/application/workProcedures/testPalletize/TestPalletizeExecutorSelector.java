package com.wxzd.efcs.business.application.workProcedures.testPalletize;


import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureSelector;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testPalletizeExecutorSelector")
public class TestPalletizeExecutorSelector implements WorkProcedureSelector {
    private final WorkProcedure currentBusinessWorkProcedure = WorkProcedure.Test_Palletize;

    @Autowired
    private TestPalletizeExecutorService testPalletizeExecutorService;

    @Override
    public WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure) {
        if (currentBusinessWorkProcedure == workProcedure) {
            return testPalletizeExecutorService;
        }

        return null;
    }
}
