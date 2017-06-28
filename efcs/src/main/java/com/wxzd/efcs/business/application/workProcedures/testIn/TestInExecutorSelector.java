package com.wxzd.efcs.business.application.workProcedures.testIn;


import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureSelector;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testInExecutorSelector")
public class TestInExecutorSelector implements WorkProcedureSelector {
    private final WorkProcedure currentBusinessWorkProcedure = WorkProcedure.Test_In;

    @Autowired
    private TestInExecutorService testInExecutorService;

    @Override
    public WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure) {
                if (currentBusinessWorkProcedure == workProcedure) {
            return testInExecutorService;
        }

        return null;
    }
}
