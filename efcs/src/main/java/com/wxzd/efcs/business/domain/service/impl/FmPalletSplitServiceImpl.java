package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.FmPalletSplitService;
import com.wxzd.efcs.business.repositorys.FmPalletSplitRepository;
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
public class FmPalletSplitServiceImpl implements FmPalletSplitService {

    @Resource
    private FmPalletSplitRepository repository;

    @Override
    public ObjectResult<FmPalletSplit> getById(UUID id) {
        return GaiaResultFactory.getObject(repository.getById(id));
    }

    @Override
    public ObjectResult<FmPalletSplit> getByFormNo(String form_No) {
        return GaiaResultFactory.getObject(repository.getByNo(form_No));
    }

    @Override
    public GaiaResult saveFmPalletSplit(FmPalletSplit fmPalletSplit) {
        if(StringUtl.isEmpty(fmPalletSplit.getForm_no())){
            fmPalletSplit.setForm_no(SerialNoGenerator.getSerialNo("PS"));
        }
        repository.saveById(fmPalletSplit);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult updateFmStatus(String formNo, FmStatus status) {
        FmPalletSplit split = repository.getByNo(formNo);
        split.setFm_status(status);
        repository.saveById(split);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult setFmError(String formNo, String errorCode, String errorMsg) {
        FmPalletSplit split = repository.getByNo(formNo);
        split.setError_code(EfcsErrorCode.valueOf(errorCode));
        split.setError_desc(errorMsg);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public FmPalletSplit getProcedureByPallet(String pallet_no, WorkProcedure workProcedure) {
        return repository.getByPalletNo(pallet_no,workProcedure);
    }
}
