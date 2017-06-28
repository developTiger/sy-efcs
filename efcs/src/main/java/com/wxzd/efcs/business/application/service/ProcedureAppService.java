package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.exception.DataNotFoundException;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.protocol.wcs.battery.feedback.BatteryCheckFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFinishedFeedbackEvent;
import com.wxzd.protocol.wcs.transport.feedback.CommandFeedbackEvent;
import com.wxzd.protocol.wcs.transport.feedback.PalletArriveFeedbackEvent;

/**
 * Created by zhouzh on 2017/4/18.
 */
public interface ProcedureAppService {

    /**
     * 电池入库
     * @param event
     * @return
     * @throws Exception
     */
    BatteryCheckFeedbackEvent batteryIn(BatteryCheckFeedbackEvent event) throws Exception;

    /**
     * 机械手抓取电池
     * @param event
     * @return
     * @throws Exception
     */
    BatteryGrabFeedbackEvent batteryGrab(BatteryGrabFeedbackEvent event) throws Exception;

    /**
     * 机械手组盘/拆盘完成
     * @param event
     * @return
     * @throws Exception
     */
    BatteryGrabFinishedFeedbackEvent batteryGrabFinished(BatteryGrabFinishedFeedbackEvent event) throws Exception;

    /**
     * 托盘到位触发业务
     * @param event
     * @return
     * @throws Exception
     */
    PalletArriveFeedbackEvent palletArrive(PalletArriveFeedbackEvent event) throws Exception;

    /**
     * 指令完成触发业务
     * @param event
     * @return
     * @throws Exception
     */
    CommandFeedbackEvent commandFinished(CommandFeedbackEvent event) throws Exception;

    /**
     * 获取当前工序
     * @param houseNo
     * @param palletNo
     * @return
     */
    WorkProcedure getCurrentWorkProcedure(String houseNo,String palletNo);

    /**
     * 托盘到位
     * @param houseNo
     * @param palletNo
     */
    GaiaResult procedureArrive(String houseNo, String positionNo, String palletNo) throws Exception;


    /**
     * 申请重新分配库位
     * @param houseNo
     * @param positionNo
     * @param palletNo
     * @return
     * @throws Exception
     */
    GaiaResult procedureAllotLocation(String houseNo, String positionNo, String palletNo)throws Exception;
    /**
     * 托盘离开
     * @param houseNo
     * @param palletNo
     */
    GaiaResult procedureFinished(String houseNo, String positionNo, String palletNo) throws Exception;

    /**
     * 重新分配库位
     * 得到指令以后下发
     * @param houseNo 库号
     * @param palletNo 托盘号
     * @param location 当前位置
     * @return 新库位地址
     */
    Instruction redistributeStorageLocation(String houseNo, String location, String palletNo) throws Exception;
//
//    /**
//     * 转库，由调度自动下发指令
//     * @param houseNo
//     * @param palletNo
//     * @throws Exception
//     */
//    GaiaResult changeStorageLocation(String houseNo, String location, String palletNo) throws Exception;

    /**
     * 更改工序
     * @param houseNo
     * @param palletNo
     * @param newProcedure
     * @throws Exception
     */
    GaiaResult changeProcedure(String houseNo, String palletNo,String currentPos, WorkProcedure newProcedure, boolean isNeedChangeLocation, String remark) throws Exception;
    /**
     * 更改工序
     * @param houseNo
     * @param palletNo
     * @param newProcedure
     * @throws Exception
     */
    GaiaResult changeProcedure(String houseNo, String palletNo, WorkProcedure newProcedure, boolean isNeedChangeLocation, String remark) throws Exception;

    /**
     * 托盘转异常工序，需要异常排出
     * @param houseNo
     * @param location
     * @param palletNo
     * @return
     * @throws Exception
     */
    GaiaResult changeToErrorProcedure(String houseNo, String location, String palletNo, String remark) throws Exception;


    /**
     * 收数
     * @param workProcedure
     * @param procedure
     * @return
     */
    GaiaResult chargeNumber(WorkProcedure workProcedure, DefaultProcedure procedure);

    /**
     * 更换库位
     * @param houseNo
     * @param positionNo
     * @param palletNo
     * @return
     */
    GaiaResult changeLocation(String houseNo, String positionNo, String palletNo,String remark) throws DataNotFoundException;

}
