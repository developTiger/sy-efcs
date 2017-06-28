package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18
 */
public interface FmProcedureService {


    /**
     * 根据id获取 表单信息
     * @param id 主键id
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmProcedure> getById(UUID id);


    /**
     * 更具单据号获取工序信息
     * @param form_No 单据号
     * @return ObjectResult<FmProcedure>
     */
    ObjectResult<FmProcedure> getByFormNo(String form_No);

    /**
     * 新建工序
     * @param fmProcedure 工序单信息
     * @return GaiaResult
     */
    GaiaResult saveFmProcedure(FmProcedure fmProcedure);


    /**
     * 新增
     * @param formNo 单据号
     * @param status 单据状态
     * @return GaiaResult
     */
    GaiaResult updateFmStatus(String formNo, FmStatus status);



    GaiaResult setFmError(String formNo,String errorCode, String errorMsg);

//    FmProcedure getProcedureByPallet(String pallet_no);

    FmProcedure getProcedureByPallet(String pallet_no, WorkProcedure workProcedure);


}
