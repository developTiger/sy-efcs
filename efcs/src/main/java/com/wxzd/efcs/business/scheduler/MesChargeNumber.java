package com.wxzd.efcs.business.scheduler;

import com.atlmes.ws.machine.ProcessLotDcResponseEx;
import com.atlmes.ws.machineintegration.DataCollectForProcessLotForEachResponse;
import com.atlmes.ws.machineintegration.DataCollectResultArrayData;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.webservice.MESAppWebservice;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;

import java.util.List;

/**
 * Created by zhouzh on 2017/5/16.
 */
public class MesChargeNumber {

//    private static


    public static ChargeResult chargeNumber(WorkProcedure workProcedure, DefaultProcedure procedureInfo, List<String> batteries, Integer time) {

//        Boolean checkBatteryStatus = false;
        if (workProcedure == WorkProcedure.High_Temperature) {
            try {
                ProcessLotDcResponseEx responseEx = MESAppWebservice.hightDataCollectForProcessLotExCore(procedureInfo.getHouseNo(), procedureInfo.getPallet_no(), time);
                if (responseEx.getCode().equals(0)) {
                    return new ChargeResult(0, "success", true);
                } else if (responseEx.getCode() > 0) {
                    return new ChargeResult(responseEx.getCode(), "收数错误：" + responseEx.getMessage(), true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ChargeResult(1, "收数错误" + ex.getMessage(), false);

            }
        }
        if (workProcedure == WorkProcedure.Normal_Temperature_1) {
            try {
                //调用normalDataCollectForProcessLotExCore
                DataCollectForProcessLotForEachResponse responseEx = MESAppWebservice.normalDataCollectForProcessLotExCore(procedureInfo.getHouseNo(), procedureInfo.getPallet_no(), batteries, time);
//                if (checkBatteryStatus) {
//                    if (responseEx.getCode().equals(0)) {
//                        return new ChargeResult(0, "success", true);
//                    } else if (responseEx.getCode() > 0) {
//                        if (responseEx.getResultArray() != null && responseEx.getResultArray().size() > 0) {
//                            for (DataCollectResultArrayData data : responseEx.getResultArray()) {
//                                if (data.getResultCode().equals(0)) {
//                                    return new ChargeResult(0, "success", true);
//                                }
//                            }
//                        }
//                        return new ChargeResult(responseEx.getCode(), "收数错误：" + responseEx.getMessage(), false);
//                    }
//                } else {
                return new ChargeResult(0, "success", true);

//                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ChargeResult(1, "收数错误" + ex.getMessage(), false);

            }
        }
        if (workProcedure == WorkProcedure.Normal_Temperature_2) {
            try {
                //调用normalNextDataCollectForProcessLotExCore
                DataCollectForProcessLotForEachResponse responseEx = MESAppWebservice.normalNextDataCollectForProcessLotExCore(procedureInfo.getHouseNo(), procedureInfo.getPallet_no(), batteries, time);
//                if (checkBatteryStatus) {
//                    if (responseEx.getCode().equals(0)) {
//                        return new ChargeResult(0, "success", true);
//                    } else if (responseEx.getCode() > 0) {
//                        if (responseEx.getResultArray() != null && responseEx.getResultArray().size() > 0) {
//                            for (DataCollectResultArrayData data : responseEx.getResultArray()) {
//                                if (data.getResultCode().equals(0)) {
//                                    return new ChargeResult(0, "success", true);
//                                }
//                            }
//                        }
//                        return new ChargeResult(responseEx.getCode(), "收数错误：" + responseEx.getMessage(), true);
//                    }
//                } else {
                return new ChargeResult(0, "success", true);
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ChargeResult(1, "收数错误" + ex.getMessage(), false);
            }
        }

        return new ChargeResult(1, "工序异常,该工序无需收数", true);

    }


}
