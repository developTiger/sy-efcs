package com.wxzd.efcs.business.application.workProcedures.testOCV;

import com.wxzd.efcs.business.application.workProcedures.IProcedure;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;

/**
 * Created by zhouz on 2016/5/18.
 */

public interface TestOCVExecutorService<T extends IProcedure,R extends GaiaResult> extends WorkProcedureExecutorService<T,R>{


}
