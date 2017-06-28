package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.FmPalletizeDto;
import com.wxzd.efcs.business.application.querys.FmPalletizeExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;

/**
 * Created by LYK on 2017/4/27.
 */
public interface FmPalletizeExQueryService {

    /**
     * 分页查询
     *
     * @return
     */
    PageResult<FmPalletizeDto> getFmPalletizePaged(FmPalletizeExQuery exQuery);


    /**
     * 查询不分页
     *
     * @param exQuery
     * @return
     */
    List<FmPalletizeDto> getFmPalletizeList(FmPalletizeExQuery exQuery);



}
