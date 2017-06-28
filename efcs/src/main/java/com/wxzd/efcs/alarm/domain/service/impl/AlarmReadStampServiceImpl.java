package com.wxzd.efcs.alarm.domain.service.impl;

import com.wxzd.efcs.alarm.domain.entities.AlarmReadStamp;
import com.wxzd.efcs.alarm.domain.service.AlarmReadStampService;
import com.wxzd.efcs.alarm.repositorys.AlarmReadStampRepository;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22.
 */
@Service
public class AlarmReadStampServiceImpl implements AlarmReadStampService {

    @Resource
    private AlarmReadStampRepository repository;

    /**
     * 获取用户最新一次拉取记录
     *
     * @param userId
     * @return
     */
    @Override
    public ObjectResult<AlarmReadStamp> getByUserId(UUID userId) {
        return GaiaResultFactory.getObject(repository.getByUserId(userId));
    }

    /**
     * 跟新用户拉取记录
     *
     * @param alarmReadStamp
     * @return
     */
    @Override
    public GaiaResult saveAlarmReadStamp(AlarmReadStamp alarmReadStamp) {
        repository.saveById(alarmReadStamp);
        return GaiaResultFactory.getSuccess();
    }

}
