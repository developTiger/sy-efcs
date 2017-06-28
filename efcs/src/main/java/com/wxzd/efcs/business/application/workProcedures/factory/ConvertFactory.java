package com.wxzd.efcs.business.application.workProcedures.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.efcs.business.application.workProcedures.dto.BatteryInPrecedureInfo;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
import com.wxzd.efcs.business.domain.entities.BatteryInfo;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryIn;
import com.wxzd.efcs.business.domain.entities.form.FmBatteryOut;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.FmStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.enums.StorageType;

/**
 * Created by zhouzh on 2017/4/19.
 */
public class ConvertFactory {

    public static List<BatteryInfo> ConvertToBatteryInfo(List<BatteryInContainer> info, String houseNo) {
        List<BatteryInfo> batteryInfoList = new ArrayList<>();
        for (BatteryInContainer container : info) {
            BatteryInfo batteryInfo = new BatteryInfo();
            //TODO 设置skuid
            batteryInfo.setSku_id(ProcedureConfig.getHouseSkuId(houseNo));
            batteryInfo.setBattery_barcode(container.getBattery_no());
            batteryInfo.setBattery_status(container.getBattery_status());
            batteryInfo.setRemark(container.getInfo());
            batteryInfoList.add(batteryInfo);
        }
        return batteryInfoList;
    }


    public static PalletDispatch convertOldDispatchToNewOne(PalletDispatch oldDispatch) {
        PalletDispatch dispatch = new PalletDispatch();
        BeanUtl.copyProperties(oldDispatch, dispatch);
        dispatch.Reset(null);

        return dispatch;
    }

    public static List<FmBatteryIn> convertToFmBatteryIn(UUID houseId, WorkProcedure workProcedure, BatteryInPrecedureInfo info, List<BatteryInContainer> batteryInContainers) {
        List<FmBatteryIn> fmBatteryInList = new ArrayList<>();
        for (BatteryInContainer battery : batteryInContainers) {
            FmBatteryIn fmBatteryIn = new FmBatteryIn();
            //TODO 设置skuid
            fmBatteryIn.setSku_id(ProcedureConfig.getHouseSkuId(info.getHouseNo()));
            fmBatteryIn.setWork_procedure(workProcedure);
            fmBatteryIn.setBattery_barcode(battery.getBattery_no());
            //TODO linePos
            fmBatteryIn.setLine_pos(info.getCurrentPos());
            fmBatteryIn.setLine_channel_no(String.valueOf(battery.getPos_channel_no()));
            fmBatteryIn.setCreate_mode(FmCreateMode.System);
            fmBatteryIn.setOperate_datetime(new Date());
            fmBatteryIn.setEquip_no(info.getCurrentPos());
            fmBatteryIn.setHouse_id(houseId);
            fmBatteryIn.setHouse_no(info.getHouseNo());
            fmBatteryIn.setFm_status(FmStatus.Finished);

            fmBatteryInList.add(fmBatteryIn);

        }
        //TODO
        return fmBatteryInList;
    }


    public static List<Storage> convertToStorage(UUID houseId, UUID locationId, String palletNo, String containerNo, String houseNo, List<BatteryInContainer> info) {

        List<Storage> storageList = new ArrayList<>();
        for (BatteryInContainer battery : info) {
            Storage storage = new Storage(locationId, palletNo, StorageType.sku);
            storage.setHouse_id(houseId);
            storage.setSku_id(ProcedureConfig.getHouseSkuId(houseNo));
            storage.setSku_barcode(battery.getBattery_no());
            storage.setSto_count(1);
            storage.setSto_unit("个");
            storageList.add(storage);
        }

        //TODO
        return storageList;
    }


    public static FmBatteryOut convertToFmBatteryOut(PalletizePrecedureInfo procedureInfo) {

        FmBatteryOut fmBatteryOut = new FmBatteryOut();
        //TODO 设置skuid
        fmBatteryOut.setSku_id(ProcedureConfig.getHouseSkuId(procedureInfo.getHouseNo()));
        fmBatteryOut.setWork_procedure(procedureInfo.getWorkProcedure());
        fmBatteryOut.setBattery_barcode(procedureInfo.getBattery_no());
        //TODO linePos
        fmBatteryOut.setLine_pos(procedureInfo.getCurrentPos());
        fmBatteryOut.setLine_channel_no(String.valueOf(procedureInfo.getFrom_pos_channel_no()));
        fmBatteryOut.setCreate_mode(FmCreateMode.System);
        fmBatteryOut.setOperate_datetime(new Date());
        fmBatteryOut.setEquip_no(procedureInfo.getCurrentPos());
        fmBatteryOut.setHouse_id(procedureInfo.getWarehouse().getId());
        fmBatteryOut.setHouse_no(procedureInfo.getHouseNo());
        fmBatteryOut.setFm_status(FmStatus.Finished);
        return fmBatteryOut;


    }

    public static DefaultProcedure convertToCleanProcedure(DefaultProcedure procedure){

        DefaultProcedure defaultProcedure = new DefaultProcedure(procedure.getHouseNo(),procedure.getCurrentPos(),procedure.getFmCreateMode());
        defaultProcedure.setPalletOperate(procedure.getPalletOperate());
        defaultProcedure.setPallet_no(procedure.getPallet_no());
        defaultProcedure.setWorkProcedure(procedure.getWorkProcedure());
        return defaultProcedure;


    }
}
