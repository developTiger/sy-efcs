package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.FmBatteryDto;
import com.wxzd.efcs.business.application.querys.FmBatteryExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/27
 * 电池出入库单
 */
public interface FmBatteryAppService {

    /**
     * 根据Id获取电池入库单
     *
     * @param id
     * @return
     */
    FmBatteryDto getFmBatteryInById(UUID id);

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




    /*---      电池出库    --------*/


    /**
     * 根据Id获取电池出库单
     *
     * @param id
     * @return
     */
    FmBatteryDto getFmBatteryOutById(UUID id);


    /**
     * 电池出库单分页查询
     *
     * @return
     */
    PageResult<FmBatteryDto> getFmBatteryOutPaged(FmBatteryExQuery exQuery);

    /**
     * 电池入库单不分页
     *
     * @param exQuery
     * @return
     */
    List<FmBatteryDto> getFmBatteryOutList(FmBatteryExQuery exQuery);


}
