package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.FmProcedureDto;
import com.wxzd.efcs.business.application.queryService.FmProcedureExQueryService;
import com.wxzd.efcs.business.application.querys.FmProcedureExQuery;
import com.wxzd.efcs.business.application.service.FmProcedureAppService;
import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27
 */
@Service
public class FmProcedureAppServiceImpl implements FmProcedureAppService {

    @Autowired
    private FmProcedureExQueryService fmProcedureExQueryService;

    @Autowired
    private FmProcedureService fmProcedureService;

    /**
     * 根据ID查询
     *
     * @return
     */
    @Override
    public FmProcedureDto getById(UUID id) {
        if (id != null) {
            FmProcedure fmProcedure = fmProcedureService.getById(id).getObject();
            if (fmProcedure != null) {
                return BeanUtl.copyProperties(fmProcedure, FmProcedureDto.class);
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FmProcedureDto> getFmProcedurePaged(FmProcedureExQuery exQuery) {
        return fmProcedureExQueryService.getFmProcedurePaged(exQuery);
    }

    /**
     * 不分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmProcedureDto> getFmProcedureList(FmProcedureExQuery exQuery) {
        return fmProcedureExQueryService.getFmProcedureList(exQuery);
    }
}
