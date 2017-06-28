package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.form.FmBatteryOut;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18.
 */
public interface FmBatteryOutService {


    /**
     * 根据id获取 出口信息
     * @param id 主键id
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmBatteryOut> getById(UUID id);


    /**
     * 更具单据号获取出库信息
     * @param form_No 单据号
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmBatteryOut> getByFormNo(String form_No);


    /**
     * 新增出库记录
     * @param fmBatteryOut
     * @return
     */
    GaiaResult save(FmBatteryOut fmBatteryOut);


    /**
     * 库存出库
     * @param fmBatteryOuts
     * @return
     */
    GaiaResult save(List<FmBatteryOut> fmBatteryOuts);
}
