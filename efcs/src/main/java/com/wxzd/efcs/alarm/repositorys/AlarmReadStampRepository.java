package com.wxzd.efcs.alarm.repositorys;

import com.wxzd.efcs.alarm.domain.entities.AlarmReadStamp;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.wms.ddd.DomainRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 * 用户拉取记录
 */
@Repository
public class AlarmReadStampRepository extends DomainRepository<AlarmReadStamp> {

    /**
     * 根据用户ID获取消警记录
     *
     * @param userId
     * @return
     */
    public AlarmReadStamp getByUserId(UUID userId) {
        StringBuilder sql = new StringBuilder("select ")
                .append(SqlUtl.getColumns(AlarmReadStamp.class))
                .append(" from ")
                .append(tableName)
                .append(" where  is_active=:is_active and user_id=:user_id order by last_refresh_time desc ");
        Map<String, Object> param = new HashMap<>();
        param.put("is_active", true);
        param.put("user_id", userId);
        List<AlarmReadStamp> list = DatabaseExecuter.queryBeanList(sql, param, AlarmReadStamp.class);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }
}
