package com.wxzd.efcs.business.application.queryService;

import com.sun.xml.bind.v2.TODO;
import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import com.wxzd.efcs.business.application.dtos.PalletDispatchDto;
import com.wxzd.efcs.business.application.dtos.PalletLocationDto;
import com.wxzd.efcs.business.application.dtos.PalletWithTypeDto;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.efcs.business.application.querys.PalletDispatchExQuery;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
public interface PalletDetailExQueryService {

    /**
     * TODO 托盘调度接口 （待日后移出）
     **/

    PageResult<PalletDispatchDto> getPalletDispatchPaged(PalletDispatchExQuery exQuery);

    /**
     * 托盘详情
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


    // TODO: 2017/5/19  托盘货物明细

    /**
     * 分页查询托盘货物明细
     *
     * @param exQuery
     * @return
     */
    PageResult<PalletDetailDto> getInnerDetailPaged(PalletBatteryExQuery exQuery);


    /**
     * 托盘货物明细不分页
     *
     * @param exQuery
     * @return
     */
    List<PalletDetailDto> getInnerDetailNoPaged(PalletBatteryExQuery exQuery);


    /**
     * 根据托盘号查询电池信息
     *
     * @param palletNo
     * @return
     */
    List<PalletDetailDto> getInnerDetailByPalletNo(String palletNo);


    /**
     * 根据托盘id查询组盘，实时，拆盘电池信息
     *
     * @return
     */
    List<PalletDetailDto> getInnerDetailById(UUID id);


    /**
     * 根据排获取 所有托盘为位置
     *
     * @param houseNo
     * @param row
     * @return
     */
    List<PalletLocationDto> getPalletLocation(String houseNo, List<Integer> row);


    // TODO: 2017/5/19  需要剥离到库存Serice

    /**
     * 获取house_no
     *
     * @param house_id
     * @return
     */
    Map<String, Object> getHouseNobyId(UUID house_id);


}
