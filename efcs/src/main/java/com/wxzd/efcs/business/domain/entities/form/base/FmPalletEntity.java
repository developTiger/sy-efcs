package com.wxzd.efcs.business.domain.entities.form.base;

import com.wxzd.efcs.business.domain.enums.PalletStatus;

import java.util.UUID;

/**
 * 和托盘信息有关的表单基类
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public class FmPalletEntity extends FmEntity {

    /**
     * 托盘号
     */
    private String pallet_no;
    /**
     * 货物托盘id
     */
    private UUID pallet_cargo_id;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public UUID getPallet_cargo_id() {
        return pallet_cargo_id;
    }

    public void setPallet_cargo_id(UUID pallet_cargo_id) {
        this.pallet_cargo_id = pallet_cargo_id;
    }

    public PalletStatus getPallet_status() {
        return pallet_status;
    }

    public void setPallet_status(PalletStatus pallet_status) {
        this.pallet_status = pallet_status;
    }
}
