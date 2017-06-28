package com.wxzd.efcs.alarm.domain.service.impl;

import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.alarm.domain.entities.AlarmInfo;
import com.wxzd.efcs.alarm.domain.service.AlarmInfoService;
import com.wxzd.efcs.alarm.repositorys.AlarmInfoRepository;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
@Service
public class AlarmInfoServiceImpl implements AlarmInfoService {

    @Resource
    private AlarmInfoRepository repository;

    /**
     * 根据报警ID设置已处理状态
     *
     * @param id
     * @return
     */
    @Override
    public GaiaResult updateAlarmInfoisHandled(UUID id) {
        AlarmInfo alarmInfo = repository.getById(id);
        alarmInfo.setHandled(true);
        repository.saveById(alarmInfo);
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 根据ID获取异常信息
     *
     * @param id
     * @return
     */
    @Override
    public ObjectResult<AlarmInfo> getById(UUID id) {
        return GaiaResultFactory.getObject(repository.getById(id));
    }

    @Override
    public GaiaResult savegetAlarmInfoList(AlarmInfo alarmInfo) {
        repository.saveById(alarmInfo);
        return GaiaResultFactory.getSuccess();
    }
}
