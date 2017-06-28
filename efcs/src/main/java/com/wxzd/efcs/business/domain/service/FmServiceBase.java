package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.form.base.FmEntity;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.wms.ddd.IEntity;

/**
 * Created by zhouzh on 2017/4/18.
 */
public abstract class FmServiceBase<T extends FmEntity> {





    protected void updateFmStatus(T t, FmStatus status){

    }



}
