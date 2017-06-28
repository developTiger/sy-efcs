package com.wxzd.efcs.equipment.repositorys;

import com.wxzd.efcs.equipment.domain.entities.EquipmentExtension;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.DomainRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LYK on 2017/4/17
 */
@Repository
public class EquipmentExtensionRepository extends DomainRepository<EquipmentExtension> {

    public EquipmentExtension getColumns(String extKey) {
        StringBuilder sql = new StringBuilder()
                .append("select ")
                .append(SqlUtl.getColumns(EquipmentExtension.class))
                .append(" from ")
                .append(tableName).append(" where ext_key= :extKey ");
        Map<String, Object> param = new HashMap<>();
        param.put("ext_key", extKey);
        return DatabaseExecuter.queryBeanEntity(sql, param, EquipmentExtension.class);
    }

}
