package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.form.FmPalletSplit;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18.
 */
public interface FmPalletSplitService {



    /**
     * 根据id获取 拆盘信息
     * @param id 主键id
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmPalletSplit> getById(UUID id);


    /**
     * 更具单据号获取出库信息
     * @param form_No 单据号
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmPalletSplit> getByFormNo(String form_No);


    /**
     * 新建拆盘单
     * @param fmPalletSplit 拆盘单信息
     * @return GaiaResult
     */
    GaiaResult saveFmPalletSplit(FmPalletSplit fmPalletSplit);


    /**
     * 更新表单状态
     * @param formNo 单据号
     * @param status 状态
     * @return  GaiaResult
     */
    GaiaResult updateFmStatus(String formNo, FmStatus status);


    /**
     * 设置异常
     * @param errorCode 异常代码
     * @param errorMsg 异常信息
     * @return GaiaResult
     */
    GaiaResult setFmError(String formNo,String errorCode,String errorMsg);

    FmPalletSplit getProcedureByPallet(String pallet_no, WorkProcedure workProcedure);

}
