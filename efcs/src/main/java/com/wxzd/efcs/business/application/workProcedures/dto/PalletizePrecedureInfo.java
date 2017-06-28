package com.wxzd.efcs.business.application.workProcedures.dto;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.string.StringUtl;


/**
 * Created by zhouzh on 2017/4/19.
 */
public class PalletizePrecedureInfo extends DefaultProcedure {

    private String battery_no;
    private String clamp_no;
    private String from_pallet_no;
    private String from_pos_no;
    private int from_pos_channel_no;
    private String to_pallet_no;
    private String to_pos_no;
    private int to_pos_channel_no;


    public PalletizePrecedureInfo(String houseNo, String positionNo, FmCreateMode createMode) {
        super(houseNo, positionNo, createMode);
    }

    public String getBattery_no() {
        return battery_no;
    }

    public void setBattery_no(String battery_no) {
        this.battery_no = battery_no;
    }

    public String getClamp_no() {
        return clamp_no;
    }

    public void setClamp_no(String clamp_no) {
        this.clamp_no = clamp_no;
    }

    public String getFrom_pallet_no() {
        return from_pallet_no;
    }

    public void setFrom_pallet_no(String from_pallet_no) {
        this.from_pallet_no = from_pallet_no;
    }

    public String getFrom_pos_no() {
        return from_pos_no;
    }

    public void setFrom_pos_no(String from_pos_no) {
        this.from_pos_no = from_pos_no;
    }

    public int getFrom_pos_channel_no() {
        return from_pos_channel_no;
    }

    public void setFrom_pos_channel_no(int from_pos_channel_no) {
        this.from_pos_channel_no = from_pos_channel_no;
    }

    public String getTo_pallet_no() {
        return to_pallet_no;
    }

    public void setTo_pallet_no(String to_pallet_no) {
        this.to_pallet_no = to_pallet_no;
    }

    public String getTo_pos_no() {
        return to_pos_no;
    }

    public void setTo_pos_no(String to_pos_no) {
        this.to_pos_no = to_pos_no;
    }

    public int getTo_pos_channel_no() {
        return to_pos_channel_no;
    }

    public void setTo_pos_channel_no(int to_pos_channel_no) {
        this.to_pos_channel_no = to_pos_channel_no;
    }

//
//    @Override
//    public String getCurrentPos() {
//        String pos;
//        if (getWorkProcedure() == WorkProcedure.Formation_Palletize) {
//            if (super.getCurrentPos().equals(DispatcherConfig.formation_palletize_device_no)&&!StringUtl.isEmpty(getTo_pos_no())) {
//                switch (getTo_pos_no()) {
//                    case "1":
//                        pos = DispatcherConfig.formation_palletize_equip_1;
//                        break;
//                    case "2":
//                        pos = DispatcherConfig.formation_palletize_equip_2;
//                        break;
//                    case "4":
//                        pos =DispatcherConfig.formation_split_equip_ok;
//                        break;
//                    default:
//                        return super.getCurrentPos();
//                }
//                return pos;
//
//            }
//        } if (getWorkProcedure() == WorkProcedure.Test_Palletize) {
//            if (super.getCurrentPos().equals(DispatcherConfig.test_palletize_device_no)&&!StringUtl.isEmpty(getTo_pos_no())) {
//                switch (getTo_pos_no()) {
//                    case "1":
//                        pos = DispatcherConfig.test_palletize_equip_1;
//                        break;
//                    case "2":
//                        pos = DispatcherConfig.test_palletize_equip_2;
//                        break;
//                    case "4":
//                        pos =DispatcherConfig.formation_split_equip_ok;
//                        break;
//                    default:
//                        return super.getCurrentPos();
//                }
//                return pos;
//
//            }
//        }
//        if (getWorkProcedure() == WorkProcedure.Formation_Split) {
//            if (super.getCurrentPos().equals(DispatcherConfig.formation_split_device_no)&&!StringUtl.isEmpty(getTo_pos_no())) {
//                switch (getTo_pos_no()) {
//                    case "1":
//                        pos = DispatcherConfig.formation_split_equip_1;
//                        break;
//                    case "2":
//                        pos = DispatcherConfig.formation_split_equip_1;
//                        break;
//                    case "6":
//                        pos = DispatcherConfig.formation_split_equip_rework;
//                        break;
//                    default:
//                        return super.getCurrentPos();
//                }
//                return pos;
//
//            }
//        }
//
//
//        return  super.getCurrentPos();
//    }

}
