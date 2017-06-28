package com.wxzd.efcs.business.domain.entities.form;

/***********************************************************************
 * Module:  fsc_fm_pallet_split.java
 * Author:  zhouzh
 * Purpose: Defines the Class fsc_fm_pallet_split
 ***********************************************************************/

import com.wxzd.gaia.common.base.bean.AliasName;

/**
 * 拆盘单实体
 */
@AliasName("fsc_fm_pallet_split")
public class FmPalletSplit extends FmPallet {

    /**
     * 拆盘策略
     */
    private String split_policy;

    public String getSplit_policy() {
        return split_policy;
    }

    public void setSplit_policy(String split_policy) {
        this.split_policy = split_policy;
    }
}