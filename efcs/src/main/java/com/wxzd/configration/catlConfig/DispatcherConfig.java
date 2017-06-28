package com.wxzd.configration.catlConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wxzd.efcs.business.domain.enums.WorkProcedure;

/**
 * Created by zhouzh on 2017/4/17.
 */
public class DispatcherConfig {
	
	/**
	 * 当前拉线号
	 */
	public static String currentLine = "29";
	
    public static HashMap<String, String> batteryProfixCheck = new HashMap<>();
    static {
        batteryProfixCheck.put("26", "15S");
        batteryProfixCheck.put("27", "15S");
        batteryProfixCheck.put("28", "15S");
        batteryProfixCheck.put("29", "15S");
    }

    /********************************  系统默认参数  *********************************************/
    // 托盘前缀校验
    public static String pallet_no_profix = "L29";
    public static List<String> pallet_profix_check_device = new ArrayList<>();
    static {
        pallet_profix_check_device.add("1230");
        pallet_profix_check_device.add("3071");
        pallet_profix_check_device.add("4190");
    }
    // 化成段托盘号校验规则，托盘号小于配置项的托盘允许在化成段使用
    public static int foramtion_pallet_check_no = 2000;


    /********************************  通讯参数  *********************************************/

    /********************************  空托盘注册设备号  *********************************************/
    public static List<String> empty_pallet_regist_device = new ArrayList<>();
    static {
        // TODO 临时测试使用
        empty_pallet_regist_device.add("1170");

        empty_pallet_regist_device.add("1230");
        empty_pallet_regist_device.add("3071");
        empty_pallet_regist_device.add("4190");
    }

    /********************************  需要MES校验托盘号的设备  *********************************************/
    public static List<String> mes_pallet_check_device = new ArrayList<>();
    static {
        mes_pallet_check_device.add("1170");
        mes_pallet_check_device.add("3110");
//        mes_pallet_check_device.add("4190");
    }

    public static List<String> mes_pallet_split_device = new ArrayList<>();
    static {
        mes_pallet_split_device.add("2060");
        mes_pallet_split_device.add("4060");
        mes_pallet_split_device.add("4170");
    }
    public static List<WorkProcedure> mes_pallet_split_workprocedure = new ArrayList<>();
    static {
        mes_pallet_split_workprocedure.add(WorkProcedure.Formation_Split);
        mes_pallet_split_workprocedure.add(WorkProcedure.Test_Pallet_Split);
    }

    /********************************  静置时间参数  *********************************************/
    public static HashMap<String, Integer> mes_hightemprature_time = new HashMap<>();
    static {
        mes_hightemprature_time.put("26", 6*60);
        mes_hightemprature_time.put("27", 6*60);
        mes_hightemprature_time.put("28", 6*60);
        mes_hightemprature_time.put("29", 6*60);
    }

    public static HashMap<String, Integer> mes_normalemprature_1_time = new HashMap<>();
    static {
        mes_normalemprature_1_time.put("26", 15*60);
        mes_normalemprature_1_time.put("27", 15*60);
        mes_normalemprature_1_time.put("28", 15*60);
        mes_normalemprature_1_time.put("29", 15*60);
    }

    public static HashMap<String, Integer> mes_normalemprature_2_time = new HashMap<>();
    static {
        mes_normalemprature_2_time.put("26", 30*60);
        mes_normalemprature_2_time.put("27", 30*60);
        mes_normalemprature_2_time.put("28", 30*60);
        mes_normalemprature_2_time.put("29", 30*60);
    }

    /********************************  化成段 物流移动设备号（托盘到位、指令完成）  *********************************************/

    public static String formation_stocker_1 = "高温库堆垛机";
    public static String formation_stocker_2 = "化成堆垛机";
    public static String test_stocker_1 = "常温1#机";
    public static String test_stocker_2 = "常温2#机";


    /********************************  化成段 机械手（工位、工位对应库位）  *********************************************/


    /********************************  测试段 物流移动设备号（托盘到位、指令完成）  *********************************************/

    /********************************  测试段 机械手（工位、工位对应库位）  *********************************************/

    /********************************  无需处理指令完成的节点  *********************************************/
    public static List<String> noDealPalletArrivedLocation = new ArrayList<>();
    static {
//        noDealPalletArrivedLocation.add("1190"); // TODO 确认机械手是到位还是完成
//        noDealPalletArrivedLocation.add("1200"); // TODO 确认机械手是到位还是完成
        noDealPalletArrivedLocation.add("1360");

    }


    public static List<String> noDealCommandFinishedLocation = new ArrayList<>();
    static {
        // TODO 正式系统需要将下面的注解解开

    	noDealCommandFinishedLocation.add("1190"); // TODO 确认机械手是到位还是完成
    	noDealCommandFinishedLocation.add("1200"); // TODO 确认机械手是到位还是完成
        noDealCommandFinishedLocation.add("1300");
        noDealCommandFinishedLocation.add("1310");
        noDealCommandFinishedLocation.add("1170");

        noDealCommandFinishedLocation.add("1220");
        noDealCommandFinishedLocation.add("1360");  // 2号机 A区上架点
        
        noDealCommandFinishedLocation.add("2060");  // 2号机 B区下架点
        
        //2010、2020、2030、2040、2050
        noDealCommandFinishedLocation.add("2010");
        noDealCommandFinishedLocation.add("2020");
        noDealCommandFinishedLocation.add("2030");
        noDealCommandFinishedLocation.add("2040");
        noDealCommandFinishedLocation.add("2050");
        
        noDealCommandFinishedLocation.add("2090");
        noDealCommandFinishedLocation.add("2110");
        noDealCommandFinishedLocation.add("2260");

        noDealCommandFinishedLocation.add("3211");
        noDealCommandFinishedLocation.add("3101");
        noDealCommandFinishedLocation.add("3110");
        
        noDealCommandFinishedLocation.add("4220");
        noDealCommandFinishedLocation.add("4100");
        noDealCommandFinishedLocation.add("4121");
        noDealCommandFinishedLocation.add("4011");
    }



    public  static List<String> noNeedSendNextPosition= new ArrayList<>();
    static {
        noNeedSendNextPosition.add("1010");
        noNeedSendNextPosition.add("1060");
        noNeedSendNextPosition.add("3011");
        noNeedSendNextPosition.add("3121");
        noNeedSendNextPosition.add("2140");
        noNeedSendNextPosition.add("2110");
        noNeedSendNextPosition.add("hczpjxs");
    }


    public final static String formation_empty_pallet_temp_cubic_location="LA020201";


    /**
     * 人工操作虚拟库位
     */
    public final static String manual_temp_location = "manual_temp";

    public final static Integer test_palletize_catch_location_count = 4;
    public final static String test_palletize_end_1 = "3120";
    public final static String test_palletize_end_2 = "3010";
    public final static String test_normal_temperature_down_1 = "4120";
    public final static String test_normal_temperature_down_2 = "4010";
    public final static String test_palletize_device_no = "cwzpjxs";
    public final static String test_palletize_scaner_no = "3110";
    public final static String test_palletize_equip_1 = "3210";
    public final static String test_palletize_equip_2 = "3100";
    public final static String formation_palletize_equip_ok = "cwzpjxs4";
    public final static String test_palletize_pos_ng = "5";
    public final static String test_palletize_pos_cache = "6";
    public final static String test_palletize_cache_cwzpjxs1 = "3121";
    public final static String test_palletize_cache_cwzpjxs2 = "3011";
    public final static String test_normal_temperature2_up1 = "4121";
    public final static String test_normal_temperature2_up2 = "4011";
    public final static String test_ocv1_pos = "TTOCV0001";
    public final static String test_ocv1_out = "4170";
    public final static String test_ocv2_pos = "TTOCV0002";
    public final static String test_ocv2_out = "4060";
    public static String test_split_scan_no = "4110";
    public final static String test_split_equip_1 = "4220";
    public final static String test_split_equip_2 = "4100";
    public final static String test_enter_exit = "3220";
    public final static String test_enter_exit_scan = "3071";
    /**
     * 测试拆盘机械手设备号
     */
    public static String test_split_device_no = "cwfjjxs";
    /**
     * 测试拆盘机械手工位
     */
    public static String test_split_pos_1 = "1";
    public static String test_split_pos_2 = "2";
    public static String test_split_pos_ok = "3";
    public static String test_split_pos_ng = "4";
    public static String test_split_pos_cache = "5";
    public static String test_split_pos_cache_device_no = "cwfjjxs_cache";

    public final static String wcs_battery_barcode_no_read = "noread";

    public final static Integer pallet_battery_count = 24;


    public static HashMap<String, String> new_pallet_type_names = new HashMap<>();

    static {
    	//TODO 电池名称需要根据数据库中进行调整
        new_pallet_type_names.put("26", "1.5P电池托盘");
        new_pallet_type_names.put("27", "1.5P电池托盘");
        new_pallet_type_names.put("28", "1.5P电池托盘");
        new_pallet_type_names.put("29", "1.5P电池托盘");
    }

    /**
     * 化成段异常口入口扫码设备号
     */
    public final static String formation_enter_scaner_no = "1230";
    /**
     * 化成段组盘前空托盘扫码设备号
     */
    public final static String formation_palletize_scaner_no = "1170";

    // TODO 下面的参数需要修改名称
    public final static String formation_empty_pallet_storage_location_no = "palletmove";
    public final static String test_empty_pallet_storage_location_no = "test_palletmove";


    public final static String formation_pallet_to_hczpjxs = "1060";

    /**
     * 出入口
     */
    public final static String Formation_enter_exit = "1230";

    // TODO 下面的参数需要修改名称
    public final static String formation_error_exit_storage_location = "error_export";
    public final static String test_error_exit_storage_location = "test_error_export";
    /**
     * 化成组盘机械手设备号
     */
    public static String formation_palletize_device_no = "hczpjxs";
    /**
     * 机械手工位
     */
    public final static String formation_palletize_pos_1 = "1";
    public final static String formation_palletize_pos_2 = "2";
    public final static String formation_palletize_pos_rework = "3";
    public final static String formation_palletize_pos_ok = "4";
    public final static String formation_palletize_pos_ng = "5";
    public final static String formation_palletize_pos_cache = "6";
    public final static String formation_palletize_pos_in = "7";


    /**
     * 化成组盘机械手工位设备号
     */
    public final static String formation_palletize_equip_1 = "1190";
    public final static String formation_palletize_equip_2 = "1200";
    public final static String formation_palletize_equip_cache = "hczpjxs";


    public final static String formation_palletize_exit_pos = "1220";

//    /**
//     * 测试组盘机械手工位
//     */
//    public static String test_palletize_pos_1 = "1";
//    public static String test_palletize_pos_2 = "2";
//    public static String test_palletize_pos_ok = "4";
//    public static String test_palletize_pos_ng = "5";
//    public static String test_palletize_pos_cache = "6";

    /**
     * 化成拆盘机械手设备号
     */
    public final static String formation_split_device_no = "hcfjjxs";
    /**
     * 化成拆盘机械手工位
     */
    public final static String formation_split_pos_1 = "1";
    public final static String formation_split_pos_2 = "2";
    public final static String formation_split_pos_rework = "3";
    public final static String formation_split_pos_ok = "4";
    public final static String formation_split_pos_ng = "5";
    public final static String formation_split_pos_cache = "6";

    /**
     * 化成拆盘机械手工位对应设备号
     */
    public final static String formation_split_equip_1 = "2090";
    public final static String formation_split_equip_2 = "2110";
    public final static String formation_split_equip_rework = "2080";
    public final static String formation_split_equip_ok = "hczpjxs4";
    public final static String formation_split_equip_cache = "hcfjjxs_cache";


    /********************************  电池状态映射配置  *********************************************/
    // MES的电池状态要求为 ok:0  rework:1、2、4  nc: -1  ng:其他
    public final static String battery_default_fake = "-2";       // 中鼎标识的 fake 电池
    public final static String battery_default_nc = "-1";         // 中鼎标识的 nc 电池
    public final static String battery_default_ok = "0";          // 中鼎标识的 ok 电池
    public final static String battery_default_ng = "3";          // 中鼎标识的 ng 电池
    public final static String battery_default_rework = "4";      // 中鼎标识的 rework 电池

    // 机械手的电池状态要求为 ok:1  ng:2  rework:3
    public static HashMap<String, String> battery_status_mapping = new HashMap<>();
    // rework 电池状态码
    public static List<String> rework_battery_nos = new ArrayList<>();

    static {
        battery_status_mapping.put("0", "1");       // ok
        battery_status_mapping.put("-1", "2");      // nc 对应 ng
        battery_status_mapping.put("3", "2");       // ng
        battery_status_mapping.put("-2", "3");      // fake
//        battery_status_mapping.put("1", "4");       // rework
//        battery_status_mapping.put("2", "4");       // rework
//        battery_status_mapping.put("4", "4");       // rework
        battery_status_mapping.put("1", "2");       // rework
        battery_status_mapping.put("2", "2");       // rework
        battery_status_mapping.put("4", "2");       // rework
        
        rework_battery_nos.add("1");
        rework_battery_nos.add("2");
        rework_battery_nos.add("4");
    }

    public final static String battery_split_ng = "2";        // 机械手需要的ng判定标识

    public static String getRobotBatteryStatus(String mesStatus) {
        if (battery_status_mapping.containsKey(mesStatus)) {
            return battery_status_mapping.get(mesStatus);
        } else {
            return battery_split_ng;
        }
    }

    /********************************  化成组盘段  *********************************************/

    /**
     * 化成任务出库任务默认优先级别
     * 5
     */
    public final static int formation_instruction_level = 5;

    /**
     * 高温放空托 阀值， 剩余空库位小于该值，
     * 则停止放入高温
     */
    public  final static Integer free_heightmp_storagelocation_count = 10;


    /**
     * 化成电池入库虚拟为
     */
    public final static String formation_battery_in_storage_location = "cwzpjxs4";


    /**
     * 化成段入库口组盘空托缓存位
     */
    public final static String formation_palletize_catch_location = "1010";

    public final static String formation_palletize_end = "1220";


    /**
     * 化成段入库口组盘空托缓数量
     */
    public final static Integer formation_palletize_catch_location_count = 6;

    /**
     * 化成段入库口组盘空托缓数量
     */
    public final static Integer formation_palletize_catch_location_min_count = 3;


    public final static String high_temperature_waitin = "1300";


    public final static String formation_allot_location = "1310";


    public final static String formation_down_location = "2010";

    /**
     * 化成段拆盘缓存位
     */
    public final static String formation_pallet_split_catch_location = "2060";

    /**
     * 化成段拆盘设备库位 左/上
     */
    public final static String formation_pallet_split_location_left = "2110";

    /**
     * 化成段拆盘设备库位 右/下
     */
    public final static String formation_pallet_split_location_right = "2090";

    /**
     * 化成段 Rework 组盘空托缓存等待位
     */
    public final static String formation_rework_pallet_catch_location = "2140";

    /**
     * 化成段 Rework 组盘空托缓存等待位
     */
    public final static String formation_rework_pallet_allot_location = "2150";


    /**
     * 化成段Rework 组盘位
     */
    public final static String H_REWORK_PALLET_LOCATION = "2080";

    public final static String formation_rework_waitin_location = "2260";


    public final static int max_ng_count = 5;


    /**
     * 测试段入库扫描位
     */
    public final static String T_STORAGE_IN_SCAN_LOCATION = "1020";


    // 根据设备号获取移动工序
    public static WorkProcedure getPalletMoveProcedure(String deviceNo) {
        if (Integer.parseInt(deviceNo) < 3000) {
            return WorkProcedure.Formation_PalletMove;
        } else {
            return WorkProcedure.Test_PalletMove;
        }
    }

    // 根据设备号获取异常排出工序
    public static WorkProcedure getPalletErrorProcedure(String deviceNo) {
        if (Integer.parseInt(deviceNo) < 3000) {
            return WorkProcedure.FORMATION_ERROR_EXPORT;
        } else {
            return WorkProcedure.TEST_ERROR_EXPORT;
        }
    }

    public static WorkProcedure getPalletErrorProcedureByLocation(String location) {
        WorkProcedure errorProcedure;
        try {
            int deviceInt = Integer.parseInt(location);
            if (deviceInt < 3000) {
                errorProcedure = WorkProcedure.FORMATION_ERROR_EXPORT;
            } else {
                errorProcedure = WorkProcedure.TEST_ERROR_EXPORT;
            }
        } catch (Exception ex) {
            int locationX = ApplicationConfig.getLocationX(location);
            if (locationX <= 4) {
                errorProcedure = WorkProcedure.FORMATION_ERROR_EXPORT;
            } else {
                errorProcedure = WorkProcedure.TEST_ERROR_EXPORT;
            }
        }
        return errorProcedure;
    }

    // 根据设备号获取空托盘流转库位号
    public static String getEmptyLocationNo(String deviceNo) {
        if (Integer.parseInt(deviceNo) < 3000) {
            return formation_empty_pallet_storage_location_no;
        } else {
            return test_empty_pallet_storage_location_no;
        }
    }

    // 根据设备号获取异常托盘流转库位号
    public static String getErrorLocationNo(String deviceNo) {
        if (Integer.parseInt(deviceNo) < 3000) {
            return formation_error_exit_storage_location;
        } else {
            return test_error_exit_storage_location;
        }
    }

    public static int convertPalletNoToInt(String palletNo) {
        String strNo = palletNo.substring(4);
        int iNo = -1;
        try {
            iNo = Integer.valueOf(strNo);
        } catch (Exception e) {
        }

        return iNo;
    }

}
