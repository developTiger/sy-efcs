package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.form.FmBatteryOut;
import com.wxzd.efcs.business.domain.service.FmBatteryOutService;
import com.wxzd.efcs.business.repositorys.FmBatteryOutRepository;
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
public class FmBatteryOutServiceImpl implements FmBatteryOutService {

    @Resource
    private FmBatteryOutRepository repository;

    @Override
    public ObjectResult<FmBatteryOut> getById(UUID id) {
        return GaiaResultFactory.getObject(repository.getById(id));
    }

    @Override
    public ObjectResult<FmBatteryOut> getByFormNo(String form_No) {
        return GaiaResultFactory.getObject(repository.getByNo(form_No));
    }

    @Override
    public GaiaResult save(FmBatteryOut fmBatteryOut) {
        repository.saveById(fmBatteryOut);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult save(List<FmBatteryOut> fmBatteryOuts) {
        repository.saveById(fmBatteryOuts);
        return GaiaResultFactory.getSuccess();
    }
}
