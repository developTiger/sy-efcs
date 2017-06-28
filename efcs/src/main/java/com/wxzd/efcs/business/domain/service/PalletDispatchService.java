package com.wxzd.efcs.business.domain.service;

import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.PalletMoveDetail;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18
 */
public interface PalletDispatchService {


    /**
     * 根据id获取 调度信息
     *
     * @param id 主键id
     * @return FmProcedure
     */
    PalletDispatch getById(UUID id);




    /**
     * 根据托盘号获取调度信息
     *
     * @param palletNo 单据号
     * @return ObjectResult<lel>
     */
    PalletDispatch getByPalletNo(String palletNo);

    /**
     * 新增调度
     *
     * @param palletDispatch 调度信息
     * @return GaiaResult
     */
    UUID newPalletDispatch(PalletDispatch palletDispatch);


    /**
     * 新增palletDispatch 并做位置记录
     *
     * @param palletDispatch
     * @param isLogDetail    是否记录位置
     * @return
     */
    UUID newPalletDispatchWithMoveDetail(PalletDispatch palletDispatch, Boolean isLogDetail);


    GaiaResult saveDispatchWithErrorMoveDetail(PalletDispatch palletDispatch, EfcsErrorCode errorCode, String errorMsg);

    /**
     * 根据电池barcode获取活动托盘的电池信息
     *
     * @param palletNo
     * @param barcode
     * @return
     */
    PalletDetail getByPalletDetailByBarcode(String palletNo, String barcode);

    /**
     * 更新托盘位置
     *
     * @param disId      调度id
     * @param posType    位置类型
     * @param currentPos 当前位置
     * @return GaiaResult
     */
    GaiaResult updatePalletStatusAndPos(UUID disId, PalletStatus palletStatus, PositionType posType, String currentPos, String formNo);


    /**
     * 更新托盘位置
     *
     * @param palletDispatch 调度id
     * @param posType        位置类型
     * @param currentPos     当前位置
     * @return GaiaResult
     */
    GaiaResult updatePalletStatusAndPos(PalletDispatch palletDispatch, PalletStatus palletStatus, WorkProcedure workProcedure, PalletDispatchStatus palletDispatchStatus, PositionType posType, String currentPos, String formNo);


    /**
     * 设置托盘状态
     *
     * @param disPatchId   调度id
     * @param palletStatus 托盘状态
     * @return
     */
    GaiaResult setPalletStatus(UUID disPatchId, PalletStatus palletStatus);
    /**
     * 设置托盘状态
     *
     * @param dispatch   调度号
     * @param palletStatus 托盘状态
     * @return
     */
    GaiaResult setPalletStatus(PalletDispatch dispatch, PalletStatus palletStatus);

    /**
     * 设置托盘状态并更新位置
     *
     * @param disId
     * @param palletStatus
     * @param posType
     * @param currentPos
     * @return
     */
    GaiaResult setPalletStatus(UUID disId, PalletStatus palletStatus, PalletDispatchStatus palletDispatchStatus, PositionType posType, String currentPos, String formNo);

    /**
     * /**
     * 异常设置
     *
     * @param disId     调度id
     * @param errorCode 错误代码
     * @param errorMsg  错误描述
     * @return
     */
    GaiaResult setError(UUID disId, EfcsErrorCode errorCode, String errorMsg);


    /**
     * 新增托盘货物明细
     *
     * @param palletDetail
     * @return
     */
    GaiaResult savePalletInnerDetail(PalletDetail palletDetail);

    /**
     * 新增托盘货物明细
     *
     * @param palletDetail
     * @return是
     */
    GaiaResult savePalletInnerDetail(List<PalletDetail> palletDetail);


    /**
     * 查询palletDetail
     *
     * @param containerNo
     * @param batteryBarcode
     * @return
     */
    PalletDetail getPalletInnerDetail(String containerNo, String batteryBarcode);

    /**
     * 查询palletDetail
     *
     * @param containerNo
     * @return
     */
    List<PalletDetail> getPalletInnerDetail(String containerNo);
    List<PalletDetail> getActivePalletDetailByPalletWithStatus(String containerNo);

    /**
     * 查询palletDetail
     *
     * @param dispatchId
     * @return
     */
    List<PalletDetail> getPalletInnerDetailResort(UUID dispatchId);


    /**
     * 新增移动记录
     *
     * @param palletMoveDetail
     * @return
     */
    GaiaResult addPalletMoveDetail(PalletMoveDetail palletMoveDetail);


    PalletMoveDetail createMoveDetailFromPalletDispatch(PalletDispatch dispatch, String formNo);


    /**
     * 组盘
     *
     * @param batterylists
     * @return
     */
    GaiaResult palletGroup(List<PalletDetail> batterylists);


    /**
     * 拆盘
     *
     * @param palletNo
     * @param batterylists
     * @return
     */
    GaiaResult palletSplit(String palletNo, List<Map<String, Object>> batterylists);




	PalletDetail getPalletInnerDetail(String to_pallet_no, int from_pos_channel_no);


    void deleteDetailHard(UUID id);

    PalletDetail getActivePalletDetailByPalletWithStatus(String palletNo, String batteryNo);


    /**
     * 获取托盘电池数量
     * @param disId
     * @return
     */
    Integer getInnerDetailCountByPalletDispatchId(UUID disId);


    GaiaResult clearLocationStatus(UUID locationId);
}
