package com.wxzd.configration.catlConfig;

import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.map.MapWrap;
import com.wxzd.wms.core.domain.entities.enums.Deep;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouzh on 2017/4/17.
 * 设备和库位关联关系
 */
public class EquitRelateLocationRowConfig {
    /**
     * equipRowMap
     *  key：库号_设备号
     *  value : 排信息 map
     *          key:排类型  （DeepType）
     *          value:排编号
     */
    private  static Map<String,Map<Deep,Integer>> equipRowMap = new HashMap<>();

    static {
         //高温入库分配库位
        Map<Deep,Integer > highTm = new HashMap<>();
        highTm.put(Deep.leftShallow,1);
        highTm.put(Deep.rightShallow,2);
        equipRowMap.put("1300",highTm);
        equipRowMap.put("highTm",highTm);

        //化成库位分配
        Map<Deep,Integer > formation =  new HashMap<>();
        formation.put(Deep.leftShallow,3);
        formation.put(Deep.rightShallow,4);
        equipRowMap.put("1310",formation);
        equipRowMap.put("formation",formation);



        Map<Deep,Integer > test1 =  new HashMap<>();
        test1.put(Deep.leftShallow,5);
        test1.put(Deep.rightShallow,6);


        Map<Deep,Integer > test2 =  new HashMap<>();
        test2.put(Deep.leftShallow,7);
        test2.put(Deep.rightShallow,8);

        Map<Deep,Integer > testAll =  new HashMap<>();
        testAll.putAll(test1);
        testAll.putAll(test2);

        equipRowMap.put("3121",test1);
        equipRowMap.put("3011",test2);

        equipRowMap.put("4121",test1);
        equipRowMap.put("4011",test2);

        equipRowMap.put("3120",test1);
        equipRowMap.put("3010",test2);


        equipRowMap.put("3220",testAll);
        equipRowMap.put("all",testAll);

    }


    /**
     * 获取设备关联排
     * @param houseNo 库号 （拉线号）
     * @param equipNo 设备号
     * @return Map<String,String>
     */
    public static Map<Deep,Integer> getEquipRowMap(WorkProcedure workProcedure, String houseNo, String equipNo){
        if(workProcedure==WorkProcedure.Formation_PalletMove){
            return equipRowMap.get("highTm");
        }
        if(workProcedure==WorkProcedure.Formation_Rework)
            return equipRowMap.get("formation");
//         return  equipRowMap.get(houseNo.toUpperCase()+"_"+equipNo);
       return  equipRowMap.get(equipNo);
    }


    public static Map<Deep,Integer> getRowMapByKey(String key){
        return  equipRowMap.get(key);
    }

}
