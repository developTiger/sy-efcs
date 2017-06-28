package com.wxzd.efcs.business.application.workProcedures.highTemperature;

import com.wxzd.efcs.business.application.workProcedures.IProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;

/**
 * Created by zhouz on 2016/5/18.
 */

public interface HighTemperatureExecutorService<T extends IProcedure,R extends GaiaResult> extends WorkProcedureExecutorService<T,R>{


}
