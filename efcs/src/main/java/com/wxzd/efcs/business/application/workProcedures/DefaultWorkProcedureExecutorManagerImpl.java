package com.wxzd.efcs.business.application.workProcedures;

import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.web.i18n.I18nContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("workProcedureExcutorManager")
public class DefaultWorkProcedureExecutorManagerImpl implements WorkProcedureExecutorManager {

    @Resource
    private List<WorkProcedureSelector> workProcedureSelectors;


    @Override
    public WorkProcedureExecutorService getWorkProcedureExecutorService(WorkProcedure workProcedure) {
        for (WorkProcedureSelector selector : workProcedureSelectors) {
            if (selector.getWorkProcedureService(workProcedure) != null) {
                return selector.getWorkProcedureService(workProcedure);
            }
        }

        throw new RuntimeException(I18nContext.getMessage("未找到工序实现服务。") + "工序（" + workProcedure.toString() + "）");
    }
}
