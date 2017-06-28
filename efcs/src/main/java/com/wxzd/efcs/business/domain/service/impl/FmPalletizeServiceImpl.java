package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.service.FmPalletizeService;
import com.wxzd.efcs.business.repositorys.FmPalletizeRepository;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.wms.core.SerialNoGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/18.
 */
@Service
public class FmPalletizeServiceImpl implements FmPalletizeService {

    @Resource
    private FmPalletizeRepository repository;

    @Override
    public ObjectResult<FmPalletize> getById(UUID id) {
        return GaiaResultFactory.getObject(repository.getById(id));
    }

    @Override
    public ObjectResult<FmPalletize> getByFormNo(String form_No) {
        return GaiaResultFactory.getObject(repository.getByNo(form_No));
    }

    @Override
    public GaiaResult saveFmPalletize(FmPalletize fmPalletize) {
        if(StringUtl.isEmpty(fmPalletize.getForm_no())){
            fmPalletize.setForm_no(SerialNoGenerator.getSerialNo("PT"));
        }
        repository.saveById(fmPalletize);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult updateFmStatus(String formNo, FmStatus status) {
        FmPalletize fmPalletize = repository.getByNo(formNo);
        fmPalletize.setFm_status(status);
        repository.saveById(fmPalletize);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult setFmError(String formNo, String errorCode, String errorMsg) {
        FmPalletize fmPalletize = repository.getByNo(formNo);
        fmPalletize.setError_code(EfcsErrorCode.valueOf(errorCode));
        fmPalletize.setError_desc(errorMsg);
        repository.saveById(fmPalletize);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public FmPalletize getProcedureByPallet(String pallet_no) {
        return repository.getByPalletNo(pallet_no);
    }
}
