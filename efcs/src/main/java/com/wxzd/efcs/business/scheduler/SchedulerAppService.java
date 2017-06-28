package com.wxzd.efcs.business.scheduler;

import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.gaia.common.base.core.result.GaiaResult;

import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/16.
 */
public interface SchedulerAppService {


    /**
     * 收数
     * @param fcsScheduler
     * @return
     */
    GaiaResult chargeNumberAndOut(FcsScheduler fcsScheduler);


    /**
     * 空托出库
     * @param fcsScheduler
     * @return
     */
    GaiaResult emptyPalletOut(FcsScheduler fcsScheduler);


    /**
     * 高温和常温静置出库
     * @param fcsScheduler
     * @return
     */
    GaiaResult storageSchedulerOut(FcsScheduler fcsScheduler);


    /**
     * 分配库位
     * @param fcsScheduler
     * @return
     */
    GaiaResult allotLocation(FcsScheduler fcsScheduler);


    /**
     * 空托上架策略
     * @return
     */
    GaiaResult palletMoveUp(FcsScheduler fcsScheduler) ;


}
