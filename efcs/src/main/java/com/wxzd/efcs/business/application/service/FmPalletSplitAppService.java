package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.FmPalletSplitDto;
import com.wxzd.efcs.business.application.querys.FmPalletSplitExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27.
 */
public interface FmPalletSplitAppService {

    /**
     * 根据ID获取拆盘单
     * @param id
     * @return
     */
    FmPalletSplitDto getById(UUID id);


    /**
     * 获取拆盘单列表
     * @param exQuery
     * @return
     */
    PageResult<FmPalletSplitDto> getFmPalletSplitPaged(FmPalletSplitExQuery exQuery);


    /**
     * 获取拆盘单列表
     * @param exQuery
     * @return
     */
    List<FmPalletSplitDto> getFmPalletSplitList(FmPalletSplitExQuery exQuery);


}
