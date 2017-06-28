package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.FmBatteryDto;
import com.wxzd.efcs.business.application.querys.FmBatteryExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;

/**
 * Created by LYK on 2017/4/27
 */
public interface FmBatteryInExQueryService {

    /**
     * 电池入库单分页查询
     *
     * @return
     */
    PageResult<FmBatteryDto> getFmBatteryInPaged(FmBatteryExQuery exQuery);

    /**
     * 电池入库单不分页
     *
     * @param exQuery
     * @return
     */
    List<FmBatteryDto> getFmBatteryInList(FmBatteryExQuery exQuery);




}
