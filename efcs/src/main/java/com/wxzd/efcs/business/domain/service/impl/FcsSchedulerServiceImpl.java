package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.enums.SchedulerStatus;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import com.wxzd.efcs.business.repositorys.FcsSchedulerRepository;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.wms.core.domain.entities.Warehouse;
import com.wxzd.wms.core.domain.service.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/15.
 */
@Service
public class FcsSchedulerServiceImpl implements FcsSchedulerService {

    @Autowired
    FcsSchedulerRepository fcsSchedulerRepository;

    @Autowired
    WareHouseService wareHouseService;

    /**
     * 根据id获取 计划信息
     *
     * @param id 主键id
     * @return ObjectResult<FcsScheduler>
     */
    @Override
    public FcsScheduler getById(UUID id) {
        return fcsSchedulerRepository.getById(id);
    }

    /**
     * 新增计划
     *
     * @param fcsScheduler
     * @return
     */
    @Override
    public GaiaResult saveScheduler(FcsScheduler fcsScheduler) {
        if(fcsScheduler.getId()==null){
           List<FcsScheduler> schedulers= fcsSchedulerRepository.getLiveSchedulerByDevicce(fcsScheduler.getHouse_id(),fcsScheduler.getDevice_no());
           if(schedulers.size()>0){
               for(FcsScheduler scheduler:schedulers){
                   scheduler.setError_code(444);
                   scheduler.setError_msg("重复创建，异常结束");
               }
               fcsSchedulerRepository.saveById(schedulers);
           }
        }
        fcsSchedulerRepository.saveById(fcsScheduler);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 设置计划状态
     *
     * @param id
     * @param schedulerStatus
     * @return
     */
    @Override
    public GaiaResult setSchedulerStatus(UUID id, SchedulerStatus schedulerStatus) {
        FcsScheduler fcsScheduler = fcsSchedulerRepository.getById(id);
        fcsScheduler.setScheduler_status(schedulerStatus);

        fcsSchedulerRepository.saveById(fcsScheduler);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 设置计划状态
     *
     * @param fcsScheduler
     * @param schedulerStatus
     * @return
     */
    @Override
    public GaiaResult setSchedulerStatus(FcsScheduler fcsScheduler, SchedulerStatus schedulerStatus) {
        fcsScheduler.setScheduler_status(schedulerStatus);

        fcsSchedulerRepository.saveById(fcsScheduler);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 请求次数加1
     *
     * @param id
     */
    @Override
    public GaiaResult addRunTimes(UUID id) {

        FcsScheduler fcsScheduler = fcsSchedulerRepository.getById(id);
        fcsScheduler.setRun_times(fcsScheduler.getRun_times() + 1);
        fcsSchedulerRepository.saveById(fcsScheduler);

        return GaiaResultFactory.getSuccess();
    }

    /**
     * 请求次数加1
     *
     * @param fcsScheduler
     */
    @Override
    public GaiaResult addRunTimes(FcsScheduler fcsScheduler) {

        fcsScheduler.setRun_times(fcsScheduler.getRun_times() + 1);
        fcsSchedulerRepository.saveById(fcsScheduler);

        return GaiaResultFactory.getSuccess();
    }

    /**
     * 异常
     * 连续异常一定的次数 则结束任务计划
     *
     * @param id
     * @param error_code
     * @param error_message
     */
    @Override
    public GaiaResult setSchedulerError(UUID id, EfcsErrorCode error_code, String error_message) {


        FcsScheduler fcsScheduler = fcsSchedulerRepository.getById(id);
        fcsScheduler.setError_times(fcsScheduler.getError_times() + 1);
        fcsSchedulerRepository.saveById(fcsScheduler);

        return GaiaResultFactory.getSuccess();
    }

    /**
     * 异常
     * 连续异常一定的次数 则结束任务计划
     *
     * @param fcsScheduler
     * @param error_code
     * @param error_message
     */
    @Override
    public GaiaResult setSchedulerError(FcsScheduler fcsScheduler, EfcsErrorCode error_code, String error_message) {
        fcsScheduler.setError_times(fcsScheduler.getError_times() + 1);
        fcsSchedulerRepository.saveById(fcsScheduler);

        return GaiaResultFactory.getSuccess();     }

    /**
     * 获取活动状态的任务计划
     * 状态为runing 的任务
     */
    @Override
    public List<FcsScheduler> getLiveScheduler() {
        List<FcsScheduler>  listResult = fcsSchedulerRepository.getLiveScheduler();
        return listResult;
    }

    /**
     * 根据设备号 获取任务
     *
     * @param house_id
     * @param device_no
     * @return
     */
    @Override
    public List<FcsScheduler> getLiveSchedulerByDevicce(UUID house_id, String device_no) {
        return fcsSchedulerRepository.getLiveSchedulerByDevicce(house_id,device_no);
    }


    @Override
    public GaiaResult closeALlSchedulerByDevice(String houseNo, String device_no){
        Warehouse house=wareHouseService.getByNo(houseNo);
        List<FcsScheduler> fcsSchedulers = this.getLiveSchedulerByDevicce(house.getId(),device_no);
        for(FcsScheduler scheduler:fcsSchedulers){
            scheduler.setScheduler_status(SchedulerStatus.ErrorFinished);
        }
        fcsSchedulerRepository.saveById(fcsSchedulers);

        return GaiaResultFactory.getSuccess();
    }
    @Override
   public GaiaResult closeALlSchedulerByDevice(UUID house_id, String device_no){

        List<FcsScheduler> fcsSchedulers = this.getLiveSchedulerByDevicce(house_id,device_no);
        for(FcsScheduler scheduler:fcsSchedulers){
            scheduler.setScheduler_status(SchedulerStatus.ErrorFinished);
        }
        fcsSchedulerRepository.saveById(fcsSchedulers);

        return GaiaResultFactory.getSuccess();
    }
}
