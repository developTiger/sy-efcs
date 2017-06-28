package com.wxzd.efcs.business.application.workProcedures;

import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.gaia.web.i18n.I18nContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("workProcedureService")
public class DefaultWorkProcedureServiceImpl implements WorkProcedureService {

    @Autowired
    private WorkProcedureExecutorManager workProcedureExecutorManager;

    /**
     * 执行工序
     *
     * @param workProcedure     工序类型
     * @param workProcedureInfo 工序流转信息
     * @return GaiaResult
     */
    @Transaction
    @Override
    public GaiaResult doWork(WorkProcedure workProcedure, IProcedure workProcedureInfo) {
        workProcedureInfo.setWorkProcedure(workProcedure);
        return getService(workProcedure).doWorkProcedure(workProcedureInfo);

    }

    /**
     * 是否需要创建下一道工序
     *
     * @param workProcedure
     * @param currentPos
     * @return
     */
    @Override
    public Boolean needActiveNextProcedure(WorkProcedure workProcedure, PalletStatus palletStatus, String currentPos) {
        return getService(workProcedure).needActiveNextProcedure(workProcedure,palletStatus, currentPos);
    }

    /**
     * 获取下一道工序
     *
     * @return
     */
    @Override
    public WorkProcedure getNextProcedure(WorkProcedure workProcedure) {
        return getService(workProcedure).getNextProcedure(workProcedure);

    }

    /**
     * 异常结束工序
     *
     * @param workProcedure
     * @param palletNo
     * @param remark        @return
     */
    @Transaction
    @Override
    public GaiaResult errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
        return getService(workProcedure).errorFinishProcedure(workProcedure,palletNo,remark);
    }


    /**
     * 非正常的开始一个新工序
     *
     * @param workProcedure
     * @param procedureInfo
     * @return
     */
    @Transaction
    @Override
    public GaiaResult initProcedureCurrenPosImproper(WorkProcedure workProcedure, IProcedure procedureInfo) {
        return getService(workProcedure).initProcedureCurrenPosImproper(workProcedure,procedureInfo);
    }


    private WorkProcedureExecutorService getService(WorkProcedure workProcedure) {
        return workProcedureExecutorManager.getWorkProcedureExecutorService(workProcedure);

    }
}
