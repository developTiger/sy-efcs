package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18.
 */
public interface FmPalletizeService {


    /**
     * 根据id获取 表单信息
     * @param id 主键id
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmPalletize> getById(UUID id);


    /**
     * 更具单据号获取组盘信息
     * @param form_No 单据号
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmPalletize> getByFormNo(String form_No);
    /**
     * 新增组盘单
     * @param fmPalletize 组盘信息
     * @return GaiaResult
     */
    GaiaResult saveFmPalletize(FmPalletize fmPalletize);


    /**
     * 更新租盘状态
     * @param formNo 单据号
     * @param status 单据状态
     * @return GaiaResult
     */
    GaiaResult updateFmStatus(String formNo, FmStatus status);


    /**
     * 异常信息
     * @param errorCode 异常代码
     * @param errorMsg 异常信息
     * @return  GaiaResult
     */
    GaiaResult setFmError(String formNo,String errorCode,String errorMsg);



    FmPalletize getProcedureByPallet(String pallet_no);


}
