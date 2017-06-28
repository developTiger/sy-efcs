package com.wxzd.efcs.alarm.repositorys;

import com.wxzd.efcs.alarm.domain.entities.AlarmHandleInfo;
import com.wxzd.wms.ddd.DomainRepository;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
@Repository
public class AlarmHandleInfoRepository extends DomainRepository<AlarmHandleInfo> {


    /**
     * 根据用户ID获取消警记录
     * @param userId
     * @return
     */
    public AlarmHandleInfo getByUserId(UUID userId) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("is_active", true);

        List<AlarmHandleInfo> result = query.from(AlarmHandleInfo.class).where(params).select(AlarmHandleInfo.class).toList();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
    }

}
