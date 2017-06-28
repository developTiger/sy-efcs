package com.wxzd.efcs.business.domain.entities.form.base;


import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.entities.EfcsErrorEntity;

import java.util.UUID;

/**
 * 所有业务表单的基类，提供业务表单相同字段的统一访问方法
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public class FmEntity extends EfcsErrorEntity {

    /**
     * 单据号
     */
    private String form_no;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 库id
     */
    private UUID house_id;     // TODO: 2017/4/27
    /**
     * 库编号
     */
    private String house_no;
    /**
     * 设备号
     */
    private String equip_no;   // TODO: 2017/4/27
    /**
     * 创建方式
     */
    private FmCreateMode create_mode;
    /**
     * 表单状态
     */
    private FmStatus fm_status;
    /**
     * 备注
     */
    private String remark;

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getEquip_no() {
        return equip_no;
    }

    public void setEquip_no(String equip_no) {
        this.equip_no = equip_no;
    }

    public FmCreateMode getCreate_mode() {
        return create_mode;
    }

    public void setCreate_mode(FmCreateMode create_mode) {
        this.create_mode = create_mode;
    }

    public FmStatus getFm_status() {
        return fm_status;
    }

    public void setFm_status(FmStatus fm_status) {
        this.fm_status = fm_status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
