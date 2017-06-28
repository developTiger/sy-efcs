package com.wxzd.efcs.ddd.domain.entities;

import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.wms.ddd.IEntity;

/**
 * 带异常代码和异常描述的领域实体
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public class EfcsErrorEntity extends IEntity{

    /**
     * 异常代码
     */
    public EfcsErrorCode error_code;
    /**
     * 异常信息
     */
    public String error_desc;

    public EfcsErrorCode getError_code() {
        return error_code;
    }

    public void setError_code(EfcsErrorCode error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }
}
