package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.efcs.business.repositorys.FmProcedureRepository;
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
public class FmProcedureServiceImpl implements FmProcedureService {

    @Resource
    private FmProcedureRepository repository;


    @Override
    public ObjectResult<FmProcedure> getById(UUID id) {
        return GaiaResultFactory.getObject(repository.getById(id));
    }

    @Override
    public ObjectResult<FmProcedure> getByFormNo(String form_No) {
        return GaiaResultFactory.getObject(repository.getByNo(form_No));
    }

    @Override
    public GaiaResult saveFmProcedure(FmProcedure fmProcedure) {
        if(StringUtl.isEmpty(fmProcedure.getForm_no())){
            fmProcedure.setForm_no(SerialNoGenerator.getSerialNo("PC"));
        }
        repository.saveById(fmProcedure);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult updateFmStatus(String formNo, FmStatus status) {
        FmProcedure fmProcedure = repository.getByNo(formNo);
        fmProcedure.setFm_status(status);
        return GaiaResultFactory.getSuccess();
    }

    @Override
    public GaiaResult setFmError(String formNo, String errorCode, String errorMsg) {
        FmProcedure fmProcedure = repository.getByNo(formNo);
        fmProcedure.setError_code(EfcsErrorCode.valueOf(errorCode));
        fmProcedure.setError_desc(errorMsg);
        repository.saveById(fmProcedure);
        return GaiaResultFactory.getSuccess();
    }
//
//    @Override
//    public FmProcedure getProcedureByPallet(String pallet_no) {
//        return repository.getByPalletNo(pallet_no,null);
//    }

    @Override
    public FmProcedure getProcedureByPallet(String pallet_no, WorkProcedure workProcedure) {
        return repository.getByPalletNo(pallet_no,workProcedure);
    }
}
