package com.wxzd.efcs.business.application.workProcedures.normalTemperature;


import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureSelector;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("normalTemperatureExecutorSelector")
public class NormalTemperatureExecutorSelector implements WorkProcedureSelector {
    private final WorkProcedure[] currentBusinessWorkProcedure = new WorkProcedure[]{ WorkProcedure.Normal_Temperature_1,WorkProcedure.Normal_Temperature_2};

    @Autowired
    private NormalTemperatureExecutorService normalTemperatureExecutorService;

    @Override
    public WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure) {

        for(WorkProcedure procedure:currentBusinessWorkProcedure){
            if (procedure == workProcedure) {
                return normalTemperatureExecutorService;
            }
        }

        return null;
    }
}
