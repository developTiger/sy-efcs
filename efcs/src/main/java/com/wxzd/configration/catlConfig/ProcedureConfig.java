package com.wxzd.configration.catlConfig;

import com.wxzd.efcs.business.domain.enums.PalletChannelPolicy;
import com.wxzd.wms.core.domain.entities.enums.Deep;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/19.
 */
public class ProcedureConfig {

    /**
     * 拉线生产的SKUid
     * key : 库号（拉线号）
     * value ：skuid
     */
    private  static Map<String,UUID> houseSkuId = new HashMap<>();

    static {
        houseSkuId.put("29",UUID.fromString("590ae0e3-61d7-4ecf-be1c-8df4bb1f25f6"));
        houseSkuId.put("28",UUID.fromString("590ae0e3-61d7-4ecf-be1c-8df4bb1f25f6"));
        houseSkuId.put("27",UUID.fromString("590ae0e3-61d7-4ecf-be1c-8df4bb1f25f6"));
        houseSkuId.put("26",UUID.fromString("590ae0e3-61d7-4ecf-be1c-8df4bb1f25f6"));

    }


    public static UUID getHouseSkuId(String houseNo){
        return houseSkuId.get(houseNo);
    }


    public final static PalletChannelPolicy PalletChannel_Policy = PalletChannelPolicy.N;


    public  final static  String Formation_Palletize="117,hczpjxs,122";

    public  final static  String High_Temperature="122,129,auto,131";

    public  final static  String Formation="131,auto,201";



    public  final static  String Formation_Pallet_split="201,206,hccpjxs,225_";

    public  final static  String Formation_Palletize_rework="208,225";



    public  final static  String Formation_Rework="225,auto,201";

}
