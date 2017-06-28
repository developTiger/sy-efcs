package com.wxzd.efcs.business.application.workProcedures.testOCV;


import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureSelector;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testOCVExecutorSelector")
public class TestOCVExecutorSelector implements WorkProcedureSelector {
    private final WorkProcedure[] currentBusinessWorkProcedure = new WorkProcedure[]{ WorkProcedure.Test_OCV_1,WorkProcedure.Test_OCV_2};

    @Autowired
    private TestOCVExecutorService testOCVExecutorService;

    @Override
    public WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure) {
        for(WorkProcedure procedure:currentBusinessWorkProcedure){
            if (procedure == workProcedure) {
                return testOCVExecutorService;
    }
}


        return null;
    }
}
