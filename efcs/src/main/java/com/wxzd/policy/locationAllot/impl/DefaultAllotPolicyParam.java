package com.wxzd.policy.locationAllot.impl;

import com.wxzd.policy.locationAllot.AllotPolicyParam;

/**
 * Created by zhouzh on 2017/4/17.
 */
public class DefaultAllotPolicyParam extends AllotPolicyParam {


    private  Boolean is_EmptyContainer;


    private String equipNo;

    public String getEquipNo() {
        return equipNo;
    }

    public void setEquipNo(String equipNo) {
        this.equipNo = equipNo;
    }

    public Boolean getIs_EmptyContainer() {
        return is_EmptyContainer;
    }

    public void setIs_EmptyContainer(Boolean is_EmptyContainer) {
        this.is_EmptyContainer = is_EmptyContainer;
    }
}
