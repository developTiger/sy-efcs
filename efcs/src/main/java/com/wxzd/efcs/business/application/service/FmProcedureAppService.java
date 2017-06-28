package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.FmProcedureDto;
import com.wxzd.efcs.business.application.querys.FmProcedureExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27
 * 出入库表单
 */
public interface FmProcedureAppService {

    /**
     * 根据ID查询
     *
     * @return
     */
    FmProcedureDto getById(UUID id);

    /**
     * 分页查询
     * @param exQuery
     * @return
     */
    PageResult<FmProcedureDto> getFmProcedurePaged(FmProcedureExQuery exQuery);


    /**
     * 不分页查询
     * @param exQuery
     * @return
     */
    List<FmProcedureDto> getFmProcedureList(FmProcedureExQuery exQuery);






}
