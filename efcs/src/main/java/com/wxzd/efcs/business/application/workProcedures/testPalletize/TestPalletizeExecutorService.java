package com.wxzd.efcs.business.application.workProcedures.testPalletize;

import com.wxzd.efcs.business.application.workProcedures.IProcedure;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureExecutorService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;

/**
 * Created by zhouz on 2016/5/18.
 */

public interface TestPalletizeExecutorService<T extends IProcedure,R extends GaiaResult> extends WorkProcedureExecutorService<T,R> {



}
