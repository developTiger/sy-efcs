package com.wxzd.efcs.business.application.queryService;

import com.wxzd.efcs.business.application.dtos.InstructionDto;
import com.wxzd.efcs.business.application.querys.InstructionExQuery;
import com.wxzd.gaia.common.base.core.result.PageResult;

import java.util.List;

/**
 * Created by LYK on 2017/4/27.
 */
public interface InstructionExQueryService {

    /**
     * 分页查询
     * @param exQuery
     * @return
     */
    PageResult<InstructionDto> getInstructionPaged(InstructionExQuery exQuery);


    /**
     * 不分页查询
     * @param exQuery
     * @return
     */
    List<InstructionDto> getInstructionList(InstructionExQuery exQuery);

}
