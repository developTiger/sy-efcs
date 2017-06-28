package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.PalletDispatchDto;
import com.wxzd.efcs.business.application.dtos.PalletWithTypeDto;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/5/18
 * 托盘调度接口，方便以后与 PalletDetailExQueryService 接口区别
 */
public interface PalletDispatchExQueryService {

    /**
     * 分页查询托盘调度信息
     *
     * @param exQuery
     * @return
     */
    PageResult<PalletDispatchDto> getPalletDispatchPaged(PalletDispatchExQuery exQuery);

    /**
     * 查询托盘调度信息不分页
     *
     * @param exQuery
     * @return
     */
    List<PalletDispatchDto> getPalletDispatchNoPaged(PalletDispatchExQuery exQuery);

    /**
     * 根据托盘ID获取详情
     */
    PalletDispatchDto getPalletDispatch(UUID id);

    /**
     * 根据托盘号查询托盘详情
     *
     * @param palletNo
     * @return
     */
    PalletDispatchDto getPalletDispatchByNo(String palletNo);

    /**
     * 根据位置 获取托盘信息
     *
     * @param houseNo
     * @param locNo
     * @return
     */
    PalletWithTypeDto getPalletInfoByPosition(String houseNo, String locNo);


}
