package com.wxzd.efcs.business.scheduler;

import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.enums.TaskType;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wxzd.gaia.common.base.core.thread.ThreadUtl;
import com.wxzd.gaia.common.base.spring.core.Web;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 计划服务
 *
 * @author y
 * @version 1
 * @.create 2017-04-22
 */
@Web
@Component
@Lazy(false)
public class SchedulerAutoExecute implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SchedulerAutoExecute.class);

    @Autowired
    DefaultWebSecurityManager securityManager;


    @Autowired
    FcsSchedulerService fcsSchedulerService;

    @Autowired
    SchedulerAppService schedulerAppService;

    /**
     * 业务
     */
    public void work() {

        List<FcsScheduler> liveScheduler = fcsSchedulerService.getLiveScheduler();

//        try{
//
//            FcsScheduler scheduler=   fcsSchedulerService.getById(UUID.fromString("e85a4bd1-65e1-41b5-a24c-93035a4bdd68"));
//            if(scheduler!=null) {
//                schedulerAppService.palletMoveUp(scheduler);
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//
//        }

        if (liveScheduler.size() > 0) {
            for (FcsScheduler scheduler : liveScheduler) {
                try {
                    scheduler.setLast_run_time(new Date());
                    scheduler.setRun_times(scheduler.getRun_times()+1);
                    if (scheduler.getTask_type() == TaskType.AllotLocation) {
                        schedulerAppService.allotLocation(scheduler);
                    }
                    if (scheduler.getTask_type() == TaskType.EmptyPalletOut) {
                        schedulerAppService.emptyPalletOut(scheduler);
                    }
                    if (scheduler.getTask_type() == TaskType.MESChargeNumber) {
                        schedulerAppService.chargeNumberAndOut(scheduler);
                    }

                    if(scheduler.getTask_type() ==TaskType.EmptyPalletIn){
                        schedulerAppService.palletMoveUp(scheduler);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

    }

    /**
     * 加载数据库数据
     * 初始化内存质量
     */
    public void initial() {
        log.trace("初始化计划任务数据");
    }

    /**
     * 定时调度执行指令
     */
    public void execute() {
        new Thread() {
            @Override
            public void run() {
                //TODO 9 临时的为了解决数据库存储的时候异常
                SecurityUtils.setSecurityManager(securityManager);
                for (; ; ) {
                    try {
                        //核心逻辑
                        work();
                    } catch (Exception e) {
                        log.warn("", e);
                    }
                    ThreadUtl.sleep(10 * 1000);//可配置的轮询时间
                }
            }
        }.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initial();
        execute();
    }

}
