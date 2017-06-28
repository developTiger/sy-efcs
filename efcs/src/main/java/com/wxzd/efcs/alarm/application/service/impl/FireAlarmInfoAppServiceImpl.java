package com.wxzd.efcs.alarm.application.service.impl;

import com.wxzd.configration.catlConfig.AlarmInfoConfig;
import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.dtos.FireAlarmInfoDto;
import com.wxzd.efcs.alarm.application.dtos.LocationChartTemplate;
import com.wxzd.efcs.alarm.application.queryService.AlarmHandleInfoExQueryService;
import com.wxzd.efcs.alarm.application.queryService.FireAlarmInfoExQueryService;
import com.wxzd.efcs.alarm.application.querys.AlarmHandleInfoExQuery;
import com.wxzd.efcs.alarm.application.querys.FireAlarmInfoExQuery;
import com.wxzd.efcs.alarm.application.service.FireAlarmInfoAppService;
import com.wxzd.efcs.alarm.domain.entities.AlarmHandleInfo;
import com.wxzd.efcs.alarm.domain.service.AlarmHandleInfoService;
import com.wxzd.efcs.alarm.domain.service.AlarmInfoService;
import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.efcs.business.application.realtime.dto.DtsTemperatureRealtimeInfo;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.protocol.dts.TemperatureReport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by LYK on 2017/4/22
 */
@Service
public class FireAlarmInfoAppServiceImpl implements FireAlarmInfoAppService {

    @Resource
    private FireAlarmInfoExQueryService exQueryService;

    @Resource
    private AlarmHandleInfoExQueryService handleInfoExQueryService;

    @Resource
    private AlarmHandleInfoService alarmHandleInfoService;

    @Resource
    private AlarmInfoService infoService;

    @Resource
    private DeviceRealtimeInfoService deviceRealtimeInfoService;

    /**
     * 获取常温库信息
     *
     * @param houseId
     * @param xPos
     */
    @Override
    public List<LocationChartTemplate> getNormalLocationInfos(UUID houseId, Integer xPos) {
        String locInfo = AlarmInfoConfig.getRowMap().get(xPos).toString();
        //查询库位信息
        List<LocationChartTemplate> templateList = exQueryService.getLocationInfosForCharts(houseId, xPos);
        if (templateList != null) {
            for (LocationChartTemplate loc : templateList) {
                String info = AlarmInfoConfig.getRowMap().get(loc.getX_pos()).toString();
                loc.setLocInfo(info);
            }
        }
        //常温段查询温度
        if ("NorlmalTemplate".equals(locInfo)) {
            //查询库号
            Map<String, Object> map = exQueryService.getHouseNo(houseId);
            if (map != null && map.get("house_no") != null) {
                String houseNo = map.get("house_no").toString();
                DtsTemperatureRealtimeInfo info = deviceRealtimeInfoService.getDtsTemperatureInfo(houseNo);
                if (info != null) {
                    List<TemperatureReport> list = info.getRowInfo(xPos);
                    for (TemperatureReport report : list) {
                        updateTemperature(templateList, report);
                    }
                }
            }
        }
        return templateList;
    }

    private void updateTemperature(List<LocationChartTemplate> templateList, TemperatureReport report) {
        if (templateList != null && report != null) {
            String locNo = ApplicationConfig.getLocationNo(report.getX(), report.getY(), report.getZ());
            double temperature = report.getTemperature();
            for (LocationChartTemplate loc : templateList) {
                if (locNo.equals(loc.getLoc_no())) {
                    loc.setTemperature(temperature);
                }
            }
        }
    }

    /**
     * 获取温度信息
     *
     * @param houseId
     * @param xPos
     * @return
     */
    @Override
    public List<TemperatureReport> getTemperatureInfo(UUID houseId, Integer xPos) {
        Map<String, Object> map = exQueryService.getHouseNo(houseId);
        if (map != null && map.get("house_no") != null) {
            String houseNo = map.get("house_no").toString();
            DtsTemperatureRealtimeInfo info = deviceRealtimeInfoService.getDtsTemperatureInfo(houseNo);
            if (info != null)
                return info.getRowInfo(xPos);
        }
        return null;
    }


    @Override
    public LocationChartTemplate getLoctionInfoByLocId(UUID locId) {
        LocationChartTemplate template = exQueryService.getLoctionInfoByLocId(locId);
        if (template != null) {
            List<PalletDetail> list = exQueryService.getbetteryLists(template.getId());
            if (list != null)
                template.setBetteryList(list);
        }
        return template;
    }

    /**
     * 根据houseId和x获取规格，y,z
     *
     * @param houseId
     * @param xPos
     * @return
     */
    @Override
    public Map<String, Object> getSizebyhouseIdAndXpos(UUID houseId, Integer xPos) {
        Map<String, Object> map = exQueryService.getSizebyhouseIdAndXpos(houseId, xPos);
        if (map != null) {
            map.put("locInfo", AlarmInfoConfig.getRowMap().get(xPos).toString());
        }
        return map;
    }

    /**
     * 化成统计
     *
     * @param houseId
     * @param xPos
     * @return
     */
    @Override
    public Map<Integer, Map<String, Object>> getStatisticsTemplated(UUID houseId, Integer xPos) {
        String locInfo = AlarmInfoConfig.getRowMap().get(xPos).toString();
        if ("Formation".equals(locInfo)) {
            return exQueryService.getStatisticsTemplated(houseId, xPos);
        }
        return null;
    }

    /**
     * 获取火警分页信息
     *
     * @param exQuery
     * @return
     */
    @Override
    public PageResult<FireAlarmInfoDto> getFireAlarmInfoPaged(FireAlarmInfoExQuery exQuery) {
        return exQueryService.getFireAlarmInfoPaged(exQuery);
    }

    /**
     * 获取火警不分页信息
     *
     * @param exQuery
     * @return
     */
    @Override
    public List<FireAlarmInfoDto> getFireAlarmInfoList(FireAlarmInfoExQuery exQuery) {
        return exQueryService.getFireAlarmInfoList(exQuery);
    }

    /**
     * 根据报警id获取消警记录
     *
     * @param id
     */
    @Override
    public FireAlarmInfoDto getFireAlarmInfoDtoList(UUID id) {
        FireAlarmInfoDto fireAlarmInfoDto = exQueryService.getById(id);
        if (fireAlarmInfoDto != null) {
            AlarmHandleInfoExQuery exQuery = new AlarmHandleInfoExQuery(id);
            List<AlarmHandleInfoDto> list = handleInfoExQueryService.getAlarmHandleInfoList(exQuery);
            if (list != null) {
                fireAlarmInfoDto.setHandleInfos(list);
            }
        }
        return updateWarningSource(fireAlarmInfoDto);
    }

    private FireAlarmInfoDto updateWarningSource(FireAlarmInfoDto dto) {
        if (dto != null && dto.getX_pos() != null && dto.getX_pos() != 0) {
            String str = AlarmInfoConfig.getRowMap().get(dto.getX_pos()).toString();
            dto.setWarning_source(str);
        }
        return dto;
    }

    /**
     * 新增消警记录
     *
     * @param dto
     */
    @Override
    public GaiaResult saveAlarmHandleInfo(AlarmHandleInfoDto dto) {
        AlarmHandleInfo handleInfo = BeanUtl.copyProperties(dto, AlarmHandleInfo.class);
        GaiaResult result = alarmHandleInfoService.saveAlarmHandleInfo(handleInfo);
        if (result.isSuccess()) {
            infoService.updateAlarmInfoisHandled(dto.getAlarm_id());
        }
        return result;
    }


}
