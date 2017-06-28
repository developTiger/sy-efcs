package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.PalletMoveDetailDto;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
public interface PalletMoveDetailExQuery {


    /**
     * 托盘移动记录分页
     *
     * @param exQuery
     * @return
     */
    PageResult<PalletMoveDetailDto> getPalletMoveDetailPaged(PalletDispatchExQuery exQuery);


    /**
     * 根据电池ID或者条码获取托盘移动记录
     */
    List<PalletMoveDetailDto> getPalletMoveDetailById(UUID detailId, String barcode);


    /**
     * 获取托盘移动记录
     *
     * @param disId
     * @return
     */
    List<PalletMoveDetailDto> getPalletMoveDetailByDisId(UUID disId);


}
