package com.wxzd.efcs.alarm.application.service.impl;

import com.wxzd.configration.catlConfig.AlarmInfoConfig;
import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.alarm.application.queryService.AlarmHandleInfoExQueryService;
import com.wxzd.efcs.alarm.application.queryService.AlarmInfoExQueryService;
import com.wxzd.efcs.alarm.application.querys.AlarmHandleInfoExQuery;
import com.wxzd.efcs.alarm.application.querys.AlarmInfoExQuery;
import com.wxzd.efcs.alarm.application.service.AlarmInfoAppService;
import com.wxzd.efcs.alarm.domain.entities.AlarmInfo;
import com.wxzd.efcs.alarm.domain.entities.AlarmReadStamp;
import com.wxzd.efcs.alarm.domain.events.AlarmEvent;
import com.wxzd.efcs.alarm.domain.service.AlarmInfoService;
import com.wxzd.efcs.alarm.domain.service.AlarmReadStampService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.user.UserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/22
 */
@Service
public class AlarmInfoAppServiceImpl implements AlarmInfoAppService {

    @Resource
    private AlarmInfoExQueryService infoExQueryService;

    @Resource
    private AlarmInfoService infoService;

    @Resource
    private AlarmReadStampService stampService;

    @Resource
    private AlarmHandleInfoExQueryService alarmHandleInfoExQueryService;

    /**
     * 获取异常信息列表分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<AlarmInfoDto> getAlarmInfoPaged(AlarmInfoExQuery exQuery) {
        return infoExQueryService.getAlarmInfoPaged(exQuery);
    }

    /**
     * 获取异常信息列表无分页
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<AlarmInfoDto> getAlarmInfoList(AlarmInfoExQuery exQuery) {
        return infoExQueryService.getAlarmInfoList(exQuery);
    }

    /**
     * 查询展示异常信息(按异常级别和时间排序)
     */
    @Override
    public List<AlarmInfoDto> getAlarmInfoSortList() {
        //获取当前用户最新一次拉取记录
        AlarmReadStamp stamp = new AlarmReadStamp();
        if (UserContext.getId() != null) {
            stamp = stampService.getByUserId(UUID.fromString(UserContext.getId())).getObject();
        }
        //不做拉取记录新增，等待前端返回接受状态信息后新增拉取记录
        return infoExQueryService.getAlarmInfoSortList(stamp != null ? stamp.getLast_refresh_time() : null);
    }

    /**
     * 根据配置推送数量获取异常信息（最新推送）
     *
     * @return
     */
    @Override
    public List<AlarmInfoDto> getAlarmInfoWithPushConfig() {
        Integer amount = AlarmInfoConfig.getAlarm_Push_Amount();
        return infoExQueryService.getAlarmInfoWithPushConfig(amount);
    }


    /**
     * 根据前端接受信息后的返回状态，修改用户拉取信息
     *
     * @param state
     */
    @Override
    public GaiaResult updateAlarmReadStamp(Boolean state) {
        if (state) {
            //修改用户拉取记录
            AlarmReadStamp stamp = stampService.getByUserId(UUID.fromString(UserContext.getId())).getObject();
            if (stamp != null) {
                stamp.setLast_refresh_time(new Date());
            } else {
                stamp = new AlarmReadStamp(new Date(), UUID.fromString(UserContext.getId()));
            }
            stampService.saveAlarmReadStamp(stamp);
        }
        return GaiaResultFactory.getSuccess();
    }

    /**
     * 根据异常ID获取信息
     *
     * @param id
     * @return
     */
    @Override
    public ObjectResult<AlarmInfoDto> getById(UUID id) {
        AlarmInfoDto infoDto = infoExQueryService.getAlarmInfoById(id);
        if (infoDto != null) {
            AlarmHandleInfoExQuery exQuery = new AlarmHandleInfoExQuery(id);
            List<AlarmHandleInfoDto> list = alarmHandleInfoExQueryService.getAlarmHandleInfoList(exQuery);
            if (list != null) {
                infoDto.setHandleInfos(list);
            }
        }
        return GaiaResultFactory.getObject(updateWarningSource(infoDto));
    }


    /**
     * 根据库位号获取异常信息
     *
     * @param locId
     * @return
     */
    @Override
    public ObjectResult<AlarmInfoDto> getByLocId(UUID locId) {
        AlarmInfoDto infoDto = infoExQueryService.getAlarmInfoById(locId);
        if (infoDto != null) {
            AlarmHandleInfoExQuery exQuery = new AlarmHandleInfoExQuery(infoDto.getId());
            List<AlarmHandleInfoDto> list = alarmHandleInfoExQueryService.getAlarmHandleInfoList(exQuery);
            if (list != null) {
                infoDto.setHandleInfos(list);
            }
        }
        return GaiaResultFactory.getObject(updateWarningSource(infoDto));
    }

    /**
     * 根据xPos设置预警源
     *
     * @param dto
     * @return
     */
    private AlarmInfoDto updateWarningSource(AlarmInfoDto dto) {
        if (dto != null && dto.getX_pos() != null && dto.getX_pos() != 0) {
            String str = AlarmInfoConfig.getRowMap().get(dto.getX_pos()).toString();
            dto.setWarning_source(str);
        }
        return dto;
    }

    /**
     * 新增异常记录
     *
     * @param alarmInfoDto
     * @return
     */
    @Override
    public GaiaResult saveAlarmInfo(AlarmInfoDto alarmInfoDto) {
        if (alarmInfoDto != null) {
            return infoService.savegetAlarmInfoList(BeanUtl.copyProperties(alarmInfoDto, AlarmInfo.class));
        }
        return GaiaResultFactory.getError("数据为空");
    }

    @Override
    public void dealAlarmEvent(AlarmEvent event) {
        if (event != null) {
            UUID houseId = infoExQueryService.getHouseIdByNo(event.getHouseNo());
            AlarmInfo info = new AlarmInfo(houseId, event.getHouseNo(), event.getDeviceNo(), event.getLocation(), event.getGroup(), event.getContent());
            info.setAlarm_type(event.getAlarmType());
            info.setAlarm_level(event.getAlarmLevel());
            info.setTitle(event.getTitle());
            info.setLocation_type(event.getLocationType());

            infoService.savegetAlarmInfoList(info);
        }
    }
}
