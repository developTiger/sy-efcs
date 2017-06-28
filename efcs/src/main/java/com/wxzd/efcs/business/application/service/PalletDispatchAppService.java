package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import com.wxzd.efcs.business.application.dtos.PalletDispatchDto;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/20
 * 托盘调度
 */
public interface PalletDispatchAppService {

    /**
     * 获取当前工序
     *
     * @param houseNo
     * @param palletNo
     * @return
     */
    WorkProcedure getCurrentWorkProcedure(String houseNo, String palletNo);


    /**
     * 根据ID跟新调度工序
     * @param id
     * @param procedure 工序
     * @param desc 更改原因
     * @return
     */
    GaiaResult updatePalletDispatch(UUID id, WorkProcedure procedure, String desc);


    /**
     * 获取当期托盘调度信息
     *
     * @param palletNo
     * @return
     */
    PalletDispatchDto getCurrentPalletDiapatch(String palletNo);


    /**
     * 组盘
     *
     * @param batterylists
     * @return
     */
    GaiaResult palletGroup(PalletDispatchDto dispatchDto, Map<String, Object> batterylists);


    /**
     * 拆盘
     *
     * @param batterylists
     * @return
     */
    GaiaResult palletSplit(PalletDispatchDto dispatchDto, Map<String, Object> batterylists);


    /**
     * 获取电池信息
     *
     * @param battery_barcode 电池条码
     * @return
     */
    ObjectResult<PalletBatteryDto> getPalletDetail(String battery_barcode);


}
