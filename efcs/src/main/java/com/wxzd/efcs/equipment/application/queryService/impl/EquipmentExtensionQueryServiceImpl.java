package com.wxzd.efcs.equipment.application.queryService.impl;

import com.wxzd.efcs.equipment.application.dtos.EquipmentExtensionDto;
import com.wxzd.efcs.equipment.application.queryService.EquipmentExtensionQueryService;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/17
 */
@Service
public class EquipmentExtensionQueryServiceImpl implements EquipmentExtensionQueryService {


    @Override
    public List<EquipmentExtensionDto> queryByEquipId(UUID equipId) {
        StringBuilder sql = new StringBuilder()
                .append("select ").append(SqlUtl.getColumns(EquipmentExtensionDto.class))
                .append(" from fcs_equipment_extension where 1=1 ");
        Map<String, Object> param = new HashMap<>();
        if (equipId != null) {
            sql.append(" and equip_id = :equipId ");
            param.put("equipId", equipId);
        }
        return DatabaseExecuter.queryBeanList(sql, param, EquipmentExtensionDto.class);
    }
}
