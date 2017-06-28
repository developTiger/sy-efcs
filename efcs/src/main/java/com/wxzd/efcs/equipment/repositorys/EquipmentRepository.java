package com.wxzd.efcs.equipment.repositorys;

import com.wxzd.efcs.equipment.domain.entities.Equipment;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.DomainRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/16
 */
@Repository
public class EquipmentRepository extends DomainRepository<Equipment> {

    public List<Equipment> queryByColumn(UUID id, String equipName, String equipNo) {

        StringBuffer sql = new StringBuffer()
                .append(" select ")
                .append(SqlUtl.getColumns(Equipment.class))
                .append(" from ")
                .append(tableName)
                .append(" where is_active = 1 and ( equip_name =:equipName  or equip_no = :equipNo ) ");

        Map<String, Object> map = new HashMap<>();
        map.put("equipName", equipName);
        map.put("equipNo", equipNo);
        return DatabaseExecuter.queryBeanList(sql, map, Equipment.class);
    }

}
