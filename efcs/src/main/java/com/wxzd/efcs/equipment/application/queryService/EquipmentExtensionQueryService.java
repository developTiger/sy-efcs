package com.wxzd.efcs.equipment.application.queryService;

import com.wxzd.efcs.equipment.application.dtos.EquipmentExtensionDto;

import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/17
 */
public interface EquipmentExtensionQueryService {


    /**
     * 根据设备id获取所有扩展信息
     * @param equipId
     * @return
     */
    List<EquipmentExtensionDto> queryByEquipId(UUID equipId);

}
