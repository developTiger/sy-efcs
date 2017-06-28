package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.PalletizeDto;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.event.exception.EventException;
import com.wxzd.protocol.wcs.domain.exception.DeviceNoIncorrectException;

import java.util.List;

/**
 * Created by zhouzh on 2017/4/18.
 */
public interface ManualProcedureAppService {


    GaiaResult clearCatchLocationStorage(String houseNo,String location, List<String> containerNos);

    /**
     * 检查工序
     *
     * @param houseNo
     * @param palletNo
     * @param workProcedure
     * @return
     */
    GaiaResult checkProcedure(String houseNo, String palletNo, WorkProcedure workProcedure);


    /**
     * 工序结束
     *
     * @param houseNo
     * @param palletNo
     * @param workProcedure
     * @return
     */
    GaiaResult palletFinish(String houseNo, String palletNo, WorkProcedure workProcedure);


    /**
     * 设置托盘工序为人工
     *
     * @param houseNo
     * @param palletNo
     * @param workProcedure
     * @return
     * @throws EventException
     */
    GaiaResult setPalletToManual(String houseNo, String palletNo, WorkProcedure workProcedure) throws EventException;

    /**
     * 人工组盘,添加电池信息
     *
     * @param workProcedure
     * @param palletizeDto
     * @return
     */
    GaiaResult manualPalletize(WorkProcedure workProcedure, PalletizeDto palletizeDto) throws DeviceNoIncorrectException;


    /**
     * 新增假电池
     *
     * @param workProcedure
     * @param palletizeDto
     * @return
     */
    GaiaResult addFeekBatteryToBallet(WorkProcedure workProcedure, PalletizeDto palletizeDto);


    /**
     * 设置工序
     * @param houseNo
     * @param palletNo
     * @param newWorkProcedure
     * @return
     */
    GaiaResult setProcedure(String houseNo, String palletNo, WorkProcedure newWorkProcedure);


    /**
     * 移除组盘电池
     * @param palletNO
     * @param batteryNo
     * @return
     */
    GaiaResult removePalletizeBattery(String palletNO, String batteryNo);


}
