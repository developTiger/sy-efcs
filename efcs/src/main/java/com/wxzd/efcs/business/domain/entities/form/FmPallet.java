package com.wxzd.efcs.business.domain.entities.form;

import com.wxzd.efcs.business.domain.entities.form.base.FmPalletEntity;
import com.wxzd.efcs.business.domain.enums.*;

import java.util.Date;
import java.util.UUID;

/**
 * 组盘拆盘表单基类
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public class FmPallet extends FmPalletEntity {

    /**
     * 开始时间
     */
    private Date proc_start_time;
    /**
     * 完成时间
     */
    private Date proc_complete_time;
    /**
     * 通道策略
     */
    private PalletChannelPolicy channel_policy;
    /**
     * 组拆盘状态
     */
    private PalletizeStatus palletize_status;


    public void newProcedure(WorkProcedure workProcedure, String palletNo, UUID dispatchPalletId, String equipNo, PalletChannelPolicy palletChannelPolicy) {
        this.setPallet_status(PalletStatus.In_Waiting);
        this.setWork_procedure(workProcedure);
        this.setFm_status(FmStatus.Executing);
        this.setPallet_cargo_id(dispatchPalletId);
        this.setPalletize_status(PalletizeStatus.Waiting);
        this.setEquip_no(equipNo);
        this.setChannel_policy(palletChannelPolicy);
        this.setPallet_no(palletNo);


    }

    public Date getProc_start_time() {
        return proc_start_time;
    }

    public void setProc_start_time(Date proc_start_time) {
        this.proc_start_time = proc_start_time;
    }

    public Date getProc_complete_time() {
        return proc_complete_time;
    }

    public void setProc_complete_time(Date proc_complete_time) {
        this.proc_complete_time = proc_complete_time;
    }

    public PalletChannelPolicy getChannel_policy() {
        return channel_policy;
    }

    public void setChannel_policy(PalletChannelPolicy channel_policy) {
        this.channel_policy = channel_policy;
    }

    public PalletizeStatus getPalletize_status() {
        return palletize_status;
    }

    public void setPalletize_status(PalletizeStatus palletize_status) {
        this.palletize_status = palletize_status;
    }
}
