package com.wxzd.efcs.alarm.domain.service.impl;

import com.wxzd.efcs.alarm.domain.entities.AlarmHandleInfo;
import com.wxzd.efcs.alarm.domain.service.AlarmHandleInfoService;
import com.wxzd.efcs.alarm.repositorys.AlarmHandleInfoRepository;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.web.user.UserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
@Service
public class AlarmHandleInfoServiceImpl implements AlarmHandleInfoService {

    @Resource
    private AlarmHandleInfoRepository repository;

    @Override
    public ObjectResult<AlarmHandleInfo> getById(UUID id) {
        return GaiaResultFactory.getObject(repository.getById(id));
    }


    @Override
    public GaiaResult saveAlarmHandleInfo(AlarmHandleInfo info) {
        info.setHandle_time(new Date());
        if (UserContext.getId() != null) {
            info.setUser_id(UUID.fromString(UserContext.getId()));
        }
        if (UserContext.getName() != null) {
            info.setCreate_by(UserContext.getName());
        }
        UUID id = repository.saveById(info);
        if (id != null) {
            String json = JsonUtl.parse(repository.getById(id));
            return GaiaResultFactory.getSuccess(json);
        }
        return GaiaResultFactory.getError("保存失败");
    }
}
