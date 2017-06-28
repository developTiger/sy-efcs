package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.FmProcedureDto;
import com.wxzd.efcs.business.application.querys.FmProcedureExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;

/**
 * Created by LYK on 2017/4/27.
 */
public interface FmProcedureExQueryService {

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
