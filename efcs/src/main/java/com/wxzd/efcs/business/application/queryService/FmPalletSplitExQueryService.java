package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.FmPalletSplitDto;
import com.wxzd.efcs.business.application.querys.FmPalletSplitExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;

/**
 * Created by LYK on 2017/4/27.
 */
public interface FmPalletSplitExQueryService {
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
