package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryIn;
import com.wxzd.efcs.business.domain.service.FmBatteryInService;
import com.wxzd.efcs.business.repositorys.BatteryInfoRepository;
import com.wxzd.efcs.business.repositorys.FmBatteryInRepository;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/18.
 */
@Service
public class FmBatteryInServiceImpl implements FmBatteryInService {

    @Resource
    private FmBatteryInRepository repository;

    @Resource
    private BatteryInfoRepository batteryInfoRepository;

    @Override
    public ObjectResult<FmBatteryIn> getById(UUID id) {
        return GaiaResultFactory.getObject(repository.getById(id));
    }

    @Override
    public ObjectResult<FmBatteryIn> getByFormNo(String form_No) {
        return GaiaResultFactory.getObject(repository.getByNo(form_No));
    }

    @Override
    public GaiaResult save(FmBatteryIn fmBatteryIn) {
        repository.saveById(fmBatteryIn);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult save(List<FmBatteryIn> fmBatteryIns) {
        repository.saveById(fmBatteryIns);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult save(BatteryInfo batteryInfo) {
        batteryInfoRepository.saveById(batteryInfo);
        return GaiaResultFactory.getSuccess();
    }
}
