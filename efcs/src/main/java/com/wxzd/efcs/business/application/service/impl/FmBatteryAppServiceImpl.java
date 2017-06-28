package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.FmBatteryDto;
import com.wxzd.efcs.business.application.queryService.FmBatteryInExQueryService;
import com.wxzd.efcs.business.application.queryService.FmBatteryOutExQueryService;
import com.wxzd.efcs.business.application.querys.FmBatteryExQuery;
import com.wxzd.efcs.business.application.service.FmBatteryAppService;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryIn;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryOut;
import com.wxzd.efcs.business.domain.service.FmBatteryInService;
import com.wxzd.efcs.business.domain.service.FmBatteryOutService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27
 */
@Service
public class FmBatteryAppServiceImpl implements FmBatteryAppService {

    @Autowired
    private FmBatteryInService inService;

    @Autowired
    private FmBatteryInExQueryService inExQueryService;

    @Autowired
    private FmBatteryOutService outService;

    @Autowired
    private FmBatteryOutExQueryService outExQueryService;

    /**
     * 根据Id获取电池入库单
     *
     * @param id
     * @return
     */
    @Override
    public FmBatteryDto getFmBatteryInById(UUID id) {
        if (id != null) {
            FmBatteryIn fmBatteryIn = inService.getById(id).getObject();
            if (fmBatteryIn != null)
                return BeanUtl.copyProperties(fmBatteryIn, FmBatteryDto.class);
        }
        return null;
    }

    /**
     * 电池入库单分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FmBatteryDto> getFmBatteryInPaged(FmBatteryExQuery exQuery) {
        return inExQueryService.getFmBatteryInPaged(exQuery);
    }

    /**
     * 电池入库单不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmBatteryDto> getFmBatteryInList(FmBatteryExQuery exQuery) {
        return inExQueryService.getFmBatteryInList(exQuery);
    }

    /**
     * 根据Id获取电池入库单
     *
     * @param id
     * @return
     */
    @Override
    public FmBatteryDto getFmBatteryOutById(UUID id) {
        if (id != null) {
            FmBatteryOut out = outService.getById(id).getObject();
            if (out != null)
                return BeanUtl.copyProperties(out, FmBatteryDto.class);
        }
        return null;
    }

    /**
     * 电池入库单分页查询
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FmBatteryDto> getFmBatteryOutPaged(FmBatteryExQuery exQuery) {
        return outExQueryService.getFmBatteryOutPaged(exQuery);
    }

    /**
     * 电池入库单不分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FmBatteryDto> getFmBatteryOutList(FmBatteryExQuery exQuery) {
        return outExQueryService.getFmBatteryOutList(exQuery);
    }
}
