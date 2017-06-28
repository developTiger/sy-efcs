package com.wxzd.efcs.business.application.workProcedures.highTemperature;


import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureSelector;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("highTemperatureExcutorSelector")
public class HighTemperatureExecutorSelector implements WorkProcedureSelector {
    private final WorkProcedure currentBusinessWorkProcedure = WorkProcedure.High_Temperature;

    @Autowired
    private HighTemperatureExecutorService highTemperatureExcutorService;

    @Override
    public WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure) {
                if (currentBusinessWorkProcedure == workProcedure) {
            return highTemperatureExcutorService;
        }

        return null;
    }
}
