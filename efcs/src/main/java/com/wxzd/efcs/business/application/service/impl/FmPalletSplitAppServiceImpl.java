package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.FmPalletSplitDto;
import com.wxzd.efcs.business.application.queryService.FmPalletSplitExQueryService;
import com.wxzd.efcs.business.application.querys.FmPalletSplitExQuery;
import com.wxzd.efcs.business.application.service.FmPalletSplitAppService;
import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
import com.wxzd.efcs.business.domain.service.FmPalletSplitService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27.
 */
@Service
public class FmPalletSplitAppServiceImpl implements FmPalletSplitAppService {

    @Autowired
    private FmPalletSplitExQueryService fmPalletSplitExQueryService;

    @Autowired
    private FmPalletSplitService fmPalletSplitService;

    /**
     * 根据ID获取拆盘单
     *
     * @param id
     * @return
     */
    @Override
    public FmPalletSplitDto getById(UUID id) {
        if (id != null) {
            FmPalletSplit fmPalletSplit = fmPalletSplitService.getById(id).getObject();
            if (fmPalletSplit != null) {
                return BeanUtl.copyProperties(fmPalletSplit, FmPalletSplitDto.class);
            }
        }
        return null;
    }

    /**
     * 获取拆盘单列表
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FmPalletSplitDto> getFmPalletSplitPaged(FmPalletSplitExQuery exQuery) {
        return fmPalletSplitExQueryService.getFmPalletSplitPaged(exQuery);
    }

    /**
     * 获取拆盘单列表
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmPalletSplitDto> getFmPalletSplitList(FmPalletSplitExQuery exQuery) {
        return fmPalletSplitExQueryService.getFmPalletSplitList(exQuery);
    }
}
