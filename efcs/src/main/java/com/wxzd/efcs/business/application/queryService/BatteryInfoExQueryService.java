package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
public interface BatteryInfoExQueryService {

    /**
     * 电池全部信息分页
     * @param exQuery
     * @return
     */
    PageResult<PalletBatteryDto> getPalletBatteryPaged(PalletBatteryExQuery exQuery);


    /**
     * 获取电池基本信息
     * @return
     */
    List<PalletBatteryDto> getPalletBatteryById(PalletBatteryExQuery exQuery);



}
