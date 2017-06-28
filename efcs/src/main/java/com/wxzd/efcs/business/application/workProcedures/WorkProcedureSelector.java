package com.wxzd.efcs.business.application.workProcedures;

import com.wxzd.efcs.business.domain.enums.WorkProcedure;

/**
 * Created by zhouz on 2016/5/18.
 */
public interface WorkProcedureSelector {
    WorkProcedureExecutorService getWorkProcedureService(WorkProcedure workProcedure);
}
