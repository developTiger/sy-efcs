package com.wxzd.efcs.business.application.workProcedures;

import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;

/**
 * Created by zhouz on 2016/5/18.
 */
public interface WorkProcedureExecutorService<T extends IProcedure,R extends GaiaResult> {

       R doWorkProcedure(T procedureInfo);

       /**
        * 是否需要创建下一道工序
        * @param workProcedure
        * @return
        */
       Boolean needActiveNextProcedure(WorkProcedure workProcedure, PalletStatus palletStatus, String currentPos);


       /**
        * 获取下一道工序
        * @return
        */
       WorkProcedure getNextProcedure(WorkProcedure workProcedure);



       /**
        * 异常结束工序
        * @param workProcedure
        * @param workProcedure
        * @return
        */
       R errorFinishProcedure(WorkProcedure workProcedure,String palletNo,String remark);




       /**
        * 非正常的开始一个新工序
        * @param workProcedure
        * @param iProcedure
        * @return
        */
       R initProcedureCurrenPosImproper(WorkProcedure workProcedure, T iProcedure);


}
