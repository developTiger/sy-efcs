package com.wxzd.efcs.business.application.workProcedures.dto;

/**
 * Created by zhouzh on 2017/4/22.
 */
public enum PalletOperate {
    /**
     * 托盘到位
     */
    Arrived,
    /**
     * 指令完成
     */
    CommandFinished,
    /**
     * 电池移动
     */
    MoveItem,
    /**
     * 托盘操作结束
     */
    OperateComplete,
    /**
     * 重新分配库位
     */
    RedistributeLocation,

    ChangeProcedure,

    ChargeNumberSuccess, /**
     * 更换库位
     */
    ChangeLocation,

    AllotStorageLocation
}
