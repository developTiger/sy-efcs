package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryIn;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18.
 */
public interface FmBatteryInService {



    /**
     * 根据id获取 表单信息
     * @param id 主键id
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmBatteryIn> getById(UUID id);


    /**
     * 更具单据号获取入库信息
     * @param form_No 单据号
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmBatteryIn> getByFormNo(String form_No);

    /**
     * 新增入库记录
     * @param fmBatteryIn
     * @return
     */
    GaiaResult save(FmBatteryIn fmBatteryIn);


    /**
     * 批量新增
     * @param fmBatteryIns
     * @return
     */
    GaiaResult save(List<FmBatteryIn> fmBatteryIns);


    /**
     * 保存电池记录
     * @param batteryInfo
     * @return
     */
    GaiaResult save(BatteryInfo batteryInfo);

}
