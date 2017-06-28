package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.FmPalletizeDto;
import com.wxzd.efcs.business.application.queryService.FmPalletizeExQueryService;
import com.wxzd.efcs.business.application.querys.FmPalletizeExQuery;
import com.wxzd.efcs.business.application.service.FmPalletizeAppService;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.service.FmPalletizeService;
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
public class FmPalletizeAppServiceImpl implements FmPalletizeAppService {

    @Autowired
    private FmPalletizeService fmPalletizeService;

    @Autowired
    private FmPalletizeExQueryService fmPalletizeExQueryService;

    /**
     * 根据Id获取组盘单
     *
     * @param id
     * @return
     */
    @Override
    public FmPalletizeDto getById(UUID id) {
        if (id != null) {
            FmPalletize fmPalletize = fmPalletizeService.getById(id).getObject();
            if (fmPalletize != null)
                return BeanUtl.copyProperties(fmPalletize, FmPalletizeDto.class);
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
    public PageResult<FmPalletizeDto> getFmPalletizePaged(FmPalletizeExQuery exQuery) {
        return fmPalletizeExQueryService.getFmPalletizePaged(exQuery);
    }

    /**
     * 查询不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmPalletizeDto> getFmPalletizeList(FmPalletizeExQuery exQuery) {
        return fmPalletizeExQueryService.getFmPalletizeList(exQuery);
    }
}
