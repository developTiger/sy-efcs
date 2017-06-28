package com.wxzd.efcs.alarm.controller;

import com.wxzd.efcs.alarm.ModuleEfcsAlarm;
import com.wxzd.efcs.alarm.application.dtos.AlarmHandleInfoDto;
import com.wxzd.efcs.alarm.application.dtos.AlarmInfoDto;
import com.wxzd.efcs.alarm.application.querys.AlarmHandleInfoExQuery;
import com.wxzd.efcs.alarm.application.querys.AlarmInfoExQuery;
import com.wxzd.efcs.alarm.application.service.AlarmHandleInfoAppService;
import com.wxzd.efcs.alarm.application.service.AlarmInfoAppService;
import com.wxzd.efcs.alarm.domain.entities.AlarmHandleInfo;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 异常信息查询
 * Created by jade on 2017/4/21.
 */
@Module(ModuleEfcsAlarm.name)
@Controller
@RequestMapping
public class ErrorInfoController {

	@Resource
	private AlarmInfoAppService alarmInfoAppService;

	@Resource
	private AlarmHandleInfoAppService alarmHandleInfoAppService;

	@RequestMapping
	public void index_view() {
	}

	@RequestMapping
	@ResponseBody
	public PageResult<AlarmInfoDto> grid(AlarmInfoExQuery alarmInfoExQuery) {
		return alarmInfoAppService.getAlarmInfoPaged(alarmInfoExQuery);
	}

	/**
	 * 异常详情
	 */
	@RequestMapping
	public void detail_view(UUID id, Model model) {

		ObjectResult<AlarmInfoDto> alarmInfoDtoObjectResult = alarmInfoAppService.getById(id);
		if (alarmInfoDtoObjectResult != null) {
			model.addAttribute("alarmInfo", alarmInfoDtoObjectResult.getObject());
		}
	}

	/**
	 * 异常处理列表
	 */
	@RequestMapping
	public List<AlarmHandleInfoDto> alarmHandleInfo() {

		return alarmHandleInfoAppService.getAlarmHandleInfoList(new AlarmHandleInfoExQuery());
	}

	/**
	 * 异常处理说明
	 */
	@RequestMapping
	public void handle_view() {

	}

	/**
	 * 异常处理
	 */
	@RequestMapping
	@ResponseBody
	public GaiaResult handle(AlarmHandleInfo alarmHandleInfo) {
		return alarmHandleInfoAppService.saveAlarmHandleInfo(alarmHandleInfo);
	}

	/**
	 * 异常弹框
	 */
	@RequestMapping
	public void dialog_view() {

	}

	/**
	 * 获取需要提示的异常信息
	 */
	@RequestMapping
	@ResponseBody
	public List<AlarmInfoDto> getAlarmLists() {
		return alarmInfoAppService.getAlarmInfoSortList();
	}

	/**
	 * 获取最新的5条记录
	 * 
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<AlarmInfoDto> getAlarmInfoList() {
		return alarmInfoAppService.getAlarmInfoWithPushConfig();
	}

	/**
	 * 设置读取状态
	 */
	@RequestMapping
	@ResponseBody
	public GaiaResult updateAlarmReadStamp() {
		return alarmInfoAppService.updateAlarmReadStamp(true);
	}

}
