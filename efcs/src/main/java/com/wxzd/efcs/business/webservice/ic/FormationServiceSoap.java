package com.wxzd.efcs.business.webservice.ic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlmes.ws.celltest.CellTestResultResponse;
import com.atlmes.ws.celltest.ChannelStatusRequest;
import com.atlmes.ws.celltest.ChannelStatusResponse;
import com.atlmes.ws.celltest.FormationCompleteRequest;
import com.atlmes.ws.celltest.FormationCompleteResponse;
import com.atlmes.ws.celltest.IcResultRequest;
import com.atlmes.ws.celltest.IcResultResponse;
import com.atlmes.ws.celltest.ProcessFilePathRequest;
import com.atlmes.ws.celltest.ProcessFilePathResponse;
import com.atlmes.ws.celltest.SfcResult;
import com.atlmes.ws.machineintegration.ChangeResourceAndProcessLotRequest;
import com.atlmes.ws.machineintegration.MiCommonResponse;
import com.sap.me.production.StartSfcResponse;
import com.wxzd.catl.CatlWebServiceFunction;
import com.wxzd.catl.ICWebServiceFunction;
import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.configration.catlConfig.FormationConfig;
import com.wxzd.configration.catlConfig.InstructionConfig;
import com.wxzd.efcs.alarm.domain.enums.FireAlarmDeviceType;
import com.wxzd.efcs.alarm.domain.enums.FireAlarmEventType;
import com.wxzd.efcs.alarm.domain.events.FireAlarmEvent;
import com.wxzd.efcs.business.application.dtos.PalletLocationDto;
import com.wxzd.efcs.business.application.dtos.PalletWithTypeDto;
import com.wxzd.efcs.business.application.exception.ProcedureException;
import com.wxzd.efcs.business.application.queryService.PalletDetailExQueryService;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.InstructionType;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.exception.ExceptionUtl;
import com.wxzd.gaia.common.base.core.log.FileLogFactory;
import com.wxzd.gaia.common.base.core.log.FileLogUnity;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.wms.catl.fcs.webservice.ICConfig;
import com.wxzd.wms.catl.fcs.webservice.log.ICWebServiceLogFactory;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ApplyCalibrationRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ApplyPressOpenResponse;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ApplyPressOpenResquest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ChangeLocationRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ChangeLocationStatusRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.EquipDto;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.FireWarnRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.FormationMode;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.GetLocationTrayRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.GetLocationTrayResponse;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.GetLockChannelResponse;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.GetTrayInfoResponse;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.NoticeTakeTrayRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ResultDto;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ResultDtoFactory;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.RoadType;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.StartRequestEx;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.StartSfcRequestEx;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.WarnType;
import com.wxzd.wms.core.application.service.StorageLocationAppService;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;

/** 
 * 由于N2项目化成不直接和mes连接，需要用调度服务中转，所以需要调度开发web服务给化成使用 。
 * 
 * 现在化成用到的MES接口
 * 
 * 获取流程 IC->FMS->MES 接口名称：getProcessFilePathByTrayId
 * 获取通道状态 IC->FMS->MES 接口名称：getChannelStatus
 * 获取电芯条码 IC->FMS->MES（弃用 改为IC->FMS获取） 接口名称：getCellListByTrayId
 * 开始 IC->FMS->MES 接口名称：startSfc
 * 传数 IC->FMS->MES 接口名称：postICResult
 * 获取结果 FMS->MES 接口名称：getCellTestResultByTrayId
 * 完成 FMS->MES 接口名称：completeFormation
 * 
 * OCV：
 * 获取电芯条码：IC->FMS->MES（弃用 改为IC->FMS获取） 接口名称：getCellListByTrayId
 * 传数： IC->FMS->MES 接口名称： miDataCollectForProcessLotForEach
 * 
 * 详细请参考MES文档
 * 请双方尽快安排人员对接，以免影响项目进度。
 * 
 * @version 1
 * @author y
 * @.create 2017-03-19
 */
@WebService(serviceName = "FormationService")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class FormationServiceSoap {

	private static final Logger log = LoggerFactory.getLogger(FormationServiceSoap.class);

	@Autowired
	private PalletDetailExQueryService palletDetailExQueryService;

	@Autowired
	private StorageLocationAppService storageLocationAppService;

	@Autowired
	private ProcedureAppService procedureAppService;

	@Autowired
	private MemoryInstructionAppService memoryInstructionAppService;

	@Autowired
	private BatteryInfoService batteryInfoService;

	//	@Autowired
	//	private FormationAppService formationAppService;

	//	private String getLocation(EquipDto equip) {
	//		String location = null;
	//		if (equip != null) {
	//			String x = String.valueOf(equip.getX());
	//			String y = equip.getY();
	//			String z = equip.getZ();
	//		}
	//		return location;
	//	}

	/**
	 * 烟雾警报
	 */
	@WebMethod(operationName = "fireWarnPort")
	@WebResult(name = "response")
	public ResultDto fireWarnPort(@WebParam(name = "request") FireWarnRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("fireWarnPort", "");
		try {
			logUnity.begin("fireWarn", JsonUtl.parseWithoutException(request));
			
			//烟雾警报操作
			EquipDto equipDto = request.getEquipDto();
			WarnType warnType = request.getWarnType();
			
			FireAlarmEvent alarmEvent = new FireAlarmEvent();
			alarmEvent.setHouseNo(equipDto.getLine());
			alarmEvent.setDeviceNo(equipDto.getId());
			alarmEvent.setFireAlarmDeviceType(FireAlarmDeviceType.Smoke_Sensor);
			switch (warnType) {
			case clean:
				alarmEvent.setEventType(FireAlarmEventType.clean);
				break;
			case warn:
				alarmEvent.setEventType(FireAlarmEventType.warn);
				break;
			}
			alarmEvent.setLocation(ApplicationConfig.getLocationNo(equipDto.getX(), equipDto.getY(), equipDto.getZ()));
			//			alarmEvent.setLocation(equipDto.getId());
			//			alarmEvent.setX(equipDto.getX());
			//			alarmEvent.setY(equipDto.getY());
			//			alarmEvent.setZ(equipDto.getZ());
			alarmEvent.setTray_no(equipDto.getTray_no());
			alarmEvent.setContent(request.getMessage());
			alarmEvent.setSource(this);

			// 发布事件
			ApplicationEventPublisher.trigger(alarmEvent);
		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn("fireWarn", ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("fireWarn", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 预约工装
	 * 
	 * 工装类型已变更为字符串
	 * 具体见EquipType 原始的
	 */
	@WebMethod(operationName = "applyCalibrationPort")
	@WebResult(name = "response")
	public ResultDto applyCalibrationPort(@WebParam(name = "request") ApplyCalibrationRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("applyCalibrationPort", "");
		try {
			logUnity.begin("applyCalibration", JsonUtl.parseWithoutException(request));

			//TODO 3 预约工装

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("applyCalibration", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 获取库位托盘状态，
	 */
	@WebMethod(operationName = "getLocationTrayPort")
	@WebResult(name = "response")
	public GetLocationTrayResponse getLocationTrayPort(@WebParam(name = "request") GetLocationTrayRequest request) {
		GetLocationTrayResponse result = new GetLocationTrayResponse(0, null);
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity log = ICWebServiceLogFactory.getWebServiceFileLogUnity("getLocationTrayPort", "");
		try {
			log.begin("getLocationTrayRequest", JsonUtl.parseWithoutException(request));

			// 获取库位托盘状态
			String houseNo = request.getLine();
			List<Integer> rows = new ArrayList<>();
			rows.add(3);
			rows.add(4);
			List<PalletLocationDto> locationDtos = palletDetailExQueryService.getPalletLocation(houseNo, rows);

			List<EquipDto> equipDtos = new ArrayList<>();
			for (PalletLocationDto dto : locationDtos) {
				EquipDto item = new EquipDto();

				item.setLine(houseNo);
				item.setTray_no(dto.getContainer_no());
				item.setX(dto.getX_pos());
				item.setY(dto.getY_pos());
				item.setZ(dto.getZ_pos());

				equipDtos.add(item);
			}
			result.setList(equipDtos);

		} catch (Exception e) {
			log.warn("", e);
			log.warn(ExceptionUtl.getExceptionDetail(e));
			result = new GetLocationTrayResponse(500, e.getMessage());
			return result;
		} finally {
			log.end("getLocationTrayRequest", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 库位锁定状态变更
	 */
	@WebMethod(operationName = "changeLocationStatusPort")
	@WebResult(name = "response")
	public ResultDto changeLocationStatusPort(@WebParam(name = "request") ChangeLocationStatusRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("changeLocationStatusPort", "");
		try {
			logUnity.begin("changeLocationStatus", JsonUtl.parseWithoutException(request));

			// 库位状态变更
			EquipDto equipDto = request.getEquipDto();
			String houseNo = request.getEquipDto().getLine();
			String locationNo = ApplicationConfig.getLocationNo(equipDto.getX(), equipDto.getY(), equipDto.getZ());
			String lockKey = equipDto.getId();
			String remark = request.getReason();

			switch (request.getLock()) {
			case lock:
				// 双向锁
				storageLocationAppService.setLocationBusinessLock(houseNo, locationNo, LockDirection.inLock, true, lockKey, remark);
				storageLocationAppService.setLocationBusinessLock(houseNo, locationNo, LockDirection.outLock, true, lockKey, remark);
				break;
			case inLock:
				// 入锁
				storageLocationAppService.setLocationBusinessLock(houseNo, locationNo, LockDirection.inLock, true, lockKey, remark);
				break;
			case outLock:
				// 出锁
				storageLocationAppService.setLocationBusinessLock(houseNo, locationNo, LockDirection.outLock, true, lockKey, remark);
				break;
			case unLock:
				// 发布unlock消息
				storageLocationAppService.setLocationBusinessLock(houseNo, locationNo, LockDirection.inLock, false, lockKey, remark);
				storageLocationAppService.setLocationBusinessLock(houseNo, locationNo, LockDirection.outLock, false, lockKey, remark);
				break;
			}

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("changeLocationStatus", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 获取托盘信息
	 */
	@WebMethod(operationName = "getTrayInfoPort")
	@WebResult(name = "response")
	public GetTrayInfoResponse getTrayInfoPort(@WebParam(name = "request") EquipDto request) {
		GetTrayInfoResponse result = new GetTrayInfoResponse(0, null);
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("getTrayInfoPort", "");
		try {
			logUnity.begin("getTrayInfo", JsonUtl.parseWithoutException(request));

			//获取托盘信息
			String localtionNo = ApplicationConfig.getLocationNo(request.getX(), request.getY(), request.getZ());

			PalletWithTypeDto palletInfo = palletDetailExQueryService.getPalletInfoByPosition(request.getLine(), localtionNo);
			if (palletInfo == null) {
				result.setCode(404);
				result.setMessage("托盘信息不存在");
				return result;
			}
			result.setTray_no(palletInfo.getContainer_no());
			result.setEquipType(palletInfo.getTypeName());
//			result.setEquipType(EquipType.tray);
			//			result.setEquipType(EquipType.valueOf(palletInfo.getAliasTypeName()));
			switch (palletInfo.getWork_procedure()) {
			case Formation_Rework:
				result.setFormationMode(FormationMode.reset);
				break;
			case Formation:
			default:
				result.setFormationMode(FormationMode.normal);
				break;
			}
			result.setChangeTimes(palletInfo.getLoc_change_times());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new GetTrayInfoResponse(500, e.getMessage());
		} finally {
			logUnity.end("getTrayInfo", JsonUtl.parseWithoutException(result));
		}
		return result;
	}

	/**
	 * 需要调用Mes
	 * 
	 * 获取化成配方
	 * 
	 * @param request
	 * @return response
	 */
	@WebMethod(operationName = "getProcessFilePathByTrayId")
	@WebResult(name = "response")
	public ProcessFilePathResponse getProcessFilePathByTrayId(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") ProcessFilePathRequest request) {
		ProcessFilePathResponse response = new ProcessFilePathResponse();
		if (ICConfig.getIsCommunicateFake()) {
			return response;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("getProcessFilePathByTrayId", "");
		try {
			logUnity.begin("getProcessFilePathByTrayId", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));

			response = ICWebServiceFunction.getProcessFilePathByTrayId(request.getOperation(), equip.getTray_no(), request.getResource(), request.getMode());
//			//TODO 如果正常模拟一个正常的返回值
//			if(response.getCode()==0){
//				response.setProcessFilePath("e:\\111.xml");
//			}
		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			response = new ProcessFilePathResponse();
			response.setCode(500);
			response.setMessage(e.getMessage());
		} finally {
			logUnity.end("getProcessFilePathByTrayId", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(response));
		}

		return response;
	}

	/**
	 * 需要调用Mes
	 * 
	 * 获取化成通道状态
	 * 
	 */
	@WebMethod(operationName = "getChannelStatus")
	@WebResult(name = "response")
	public ChannelStatusResponse getChannelStatus(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") ChannelStatusRequest request) {
		ChannelStatusResponse response = new ChannelStatusResponse();
		if (ICConfig.getIsCommunicateFake()) {
			return response;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("getChannelStatus", "");
		response.setReturnCode(0);
		try {
			logUnity.begin("getChannelStatus", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));

			response = ICWebServiceFunction.getChannelStatus(request.getResource(), request.getTier());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			response = new ChannelStatusResponse();
			response.setReturnCode(500);
			response.setMessage(e.getMessage());
		} finally {
			logUnity.end("getChannelStatus", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(response));
		}

		return response;
	}

	/**
	 * 获取锁定通道
	 * 返回结果是锁定的通道号，不锁定的不返回
	 */
	@WebMethod(operationName = "getLockChannelPort")
	@WebResult(name = "response")
	public GetLockChannelResponse getLockChannelPort(@WebParam(name = "request") EquipDto request) {
		GetLockChannelResponse result = new GetLockChannelResponse(0, null);
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("getLockChannelPort", "");

		try {
			logUnity.begin("getLockChannel", JsonUtl.parseWithoutException(request));

			// 获取通道锁定要求（暂无业务要求）
			// result.getChannels().add(1);锁定的通道

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new GetLockChannelResponse(500, e.getMessage());
		} finally {
			logUnity.end("getLockChannel", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 需要调用Mes
	 * 
	 * 化成开始
	 */
	@WebMethod(operationName = "start")
	@WebResult(name = "response")
	public List<StartSfcResponse> start(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") StartRequestEx request) {
		List<StartSfcResponse> result = new LinkedList<>();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("start", "");
		try {
			logUnity.begin("start", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));

			result = ICWebServiceFunction.start(request.getOperation(), request.getResource(), request.getBatterys(), request.getUser());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new LinkedList<>();
		} finally {
			logUnity.end("start", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 需要调用Mes
	 * 
	 * 化成开始
	 */
	@WebMethod(operationName = "startSfc")
	@WebResult(name = "response")
	public StartSfcResponse startSfc(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") StartSfcRequestEx request) {
		StartSfcResponse response = new StartSfcResponse();
		if (ICConfig.getIsCommunicateFake()) {
			return response;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("startSfc", "");
		try {
			logUnity.begin("startSfc", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));

			response = ICWebServiceFunction.startSfc(request.getOperation(), request.getResource(), request.getBattery(), request.getUser());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			response = new StartSfcResponse();
		} finally {
			logUnity.end("startSfc", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(response));
		}

		return response;
	}

	/**
	 * 需要调用Mes
	 * 
	 * 异常库位更换
	 */
	@WebMethod(operationName = "miChangeResourceAndProcessLot")
	@WebResult(name = "response")
	public MiCommonResponse miChangeResourceAndProcessLot(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") ChangeResourceAndProcessLotRequest request) {
		MiCommonResponse response = new MiCommonResponse();
		if (ICConfig.getIsCommunicateFake()) {
			return response;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("miChangeResourceAndProcessLot", "");
		response.setCode(0);
		try {
			logUnity.begin("miChangeResourceAndProcessLot", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));

			response = ICWebServiceFunction.miChangeResourceAndProcessLot(request.getOperation(), request.getResource(), request.getPreviousProcessLot(), request.getCurrentProcessLot());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			response = new MiCommonResponse();
			response.setCode(500);
			response.setMessage(e.getMessage());
		} finally {
			logUnity.end("miChangeResourceAndProcessLot", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(response));
		}

		return response;
	}

	/**
	 * 触发库位异常更换
	 * 库位可能不需要变更，但是状态会变成复测
	 */
	@WebMethod(operationName = "changeLocationPort")
	@WebResult(name = "response")
	public ResultDto changeLocationPort(@WebParam(name = "request") ChangeLocationRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("changeLocationPort", "");
		try {
			logUnity.begin("changeLocation", JsonUtl.parseWithoutException(request));

			EquipDto d = request.getEquipDto();
			// 获取当前库位
			String oldLocation = ApplicationConfig.getLocationNo(d.getX(), d.getY(), d.getZ());

			// 更换工序
			GaiaResult r = procedureAppService.changeLocation(d.getLine(),oldLocation,d.getTray_no(),"化成柜触发异常更换库位");
			if (!r.isSuccess()) {
				throw new ProcedureException(result.getMessage());
			}
			//
			//			// 库位异常更换
			//			if (request.isNeedChange()) {
			//				// 更换库位，下发指令
			//				GaiaResult r = procedureAppService.changeStorageLocation(d.getLine(), oldLocation, d.getTray_no());
			//				if (!r.isSuccess()) {
			//					throw new ProcedureException("无法更换库位");
			//				}
			//			}

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("changeLocation", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 需要调用Mes
	 * 
	 * 化成DC传数
	 */
	@WebMethod(operationName = "postICResult")
	@WebResult(name = "response")
	public IcResultResponse postICResult(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") IcResultRequest request) {
		IcResultResponse response = new IcResultResponse();
		if (ICConfig.getIsCommunicateFake()) {
			return response;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("postICResult", "");
		response.setCode(0);
		try {
			logUnity.begin("postICResult", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));

			response = ICWebServiceFunction.postICResult(request.getOperation(), request.getResource(), request.getSfc(), request.getUser(), request.getControlSystemID(), request.getChannelID(),
					request.getParametricDataArray());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			response = new IcResultResponse();
			response.setCode(500);
			response.setMessage(e.getMessage());
		} finally {
			logUnity.end("postICResult", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(response));
		}

		return response;
	}
	
	static Map<String, Integer> times = new ConcurrentHashMap<String, Integer>();
	
	/**
	 * 申请压床脱开
	 * 
	 * 存在2个问题点：
	 * 1.一开始申请的那批不再申请了
	 * 2.有别的出库任务
	 * 
	 */
	@WebMethod(operationName = "applyPressOpenPort")
	@WebResult(name = "response")
	@Transaction
	public ApplyPressOpenResponse applyPressOpenPort(@WebParam(name = "request") ApplyPressOpenResquest request) {
		synchronized (FormationServiceSoap.class) {//就化成设备一个在调用，所以整个方法加同步锁不会对效率产生影响
		//debug formation not apply
//		FileLogUnity logDemo = new FileLogUnity("", "/demo", "applyPressOpenPort");
		
		ApplyPressOpenResponse result = new ApplyPressOpenResponse(0, null);
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("applyPressOpenPort", "");
		try {
			logUnity.begin("applyPressOpen", JsonUtl.parseWithoutException(request));
			
			// 申请压床脱开
			List<EquipDto> equipDtos = request.getList();
			List<EquipDto> allowEquipDtos = new ArrayList<>();

			/*
			 * 因为设备是根据库号不一致的，所以需要根据库号拆分
			 */
			Map<String, List<EquipDto>> linesEquipMap = new HashMap<>();
			//如果设备包含多个库的那么需要拆分
			for (EquipDto dto : equipDtos) {
				String line = dto.getLine();
				List<EquipDto> list = null;
				if (linesEquipMap.containsKey(line)) {
					list = linesEquipMap.get(line);
					list.add(dto);
				} else {
					list = new LinkedList<>();
					list.add(dto);
					linesEquipMap.put(line, list);
				}
			}

			//强制校验，如果眼花再用，空指针异常
			equipDtos = null;

			Set<String> keySet = linesEquipMap.keySet();
			for (String line : keySet) {
				
//				if (times.get(line) != null && times.get(line) > 5) {
//					synchronized (FormationServiceSoap.class) {
						if (times.get(line) != null && times.get(line) >= 6) {//容错6次，因为目前只有一个库如果是多库的话不适用
							/*
							 * 目前一共有2台电脑在申请
							 */
//							System.out.println("重置申请脱开压床");
							times.put(line, 1);
							//处理掉所有的create状态的指令
							FileLogFactory.warnCustomer("/ic/formationMessage", "", "","重置申请脱开压床");//
							List<Instruction> instructionByQueueNo = memoryInstructionAppService.getInstructionByQueueNo(line, InstructionConfig.Formation_Stocker_Device_No).getItems();
							for (Instruction instruction : instructionByQueueNo) {
								if (InstructionType.Stock_Out.equals(instruction.getInstr_type()) && InstructionStatus.Created.equals(instruction.getInstr_status())) {
									memoryInstructionAppService.abnormalFinish(instruction.getInstr_no(), EfcsErrorCode.instruction_scrapped, "化成柜存在取消脱开压床的情况，主动作废");
									FileLogFactory.warnCustomer("/ic/formationMessage", "", "","主动作废编号："+instruction.getInstr_no()+";托盘号："+instruction.getPallet_no());//
								}
							}
						}
//					}
//				}
				
				List<EquipDto> lineEquips = linesEquipMap.get(line);
				
//				logDemo.info("lineEquips",JsonUtl.parseWithoutException(lineEquips));
				/**
				 * 如果库位号不存在则创建
				 * 1.获取stock_out的任务
				 * 2.如果库位号不存在则创建
				 */
				//获取排序过后的某队列的指令号
				List<Instruction> instructionByQueueNo = memoryInstructionAppService.getInstructionByQueueNo(line, InstructionConfig.Formation_Stocker_Device_No).getItems();
				
				
				/*
				 * 只取STOCK_OUT任务
				 */
				List<Instruction> stockOuts = new LinkedList<>();
				List<String> locations = new LinkedList<>();
				for (Instruction instruction : instructionByQueueNo) {
					if (InstructionType.Stock_Out.equals(instruction.getInstr_type())) {
						stockOuts.add(instruction);
						locations.add(instruction.getFrom_pos());
					}
				}
				/*
				 * 如果此任务不存在队列中在则创建
				 */
				for (EquipDto equipDto : lineEquips) {
					String locationNo = ApplicationConfig.getLocationNo(equipDto.getX(), equipDto.getY(), equipDto.getZ());
					if (!locations.contains(locationNo)) {
						if(!StringUtl.isEmpty(equipDto.getTray_no())){
							try {
								procedureAppService.procedureFinished(line, locationNo, equipDto.getTray_no());
							} catch (Exception e) {
								log.warn("",e);
							}
						}else{
							FileLogFactory.warnCustomer("/ic/formationMessage", "", "","申请脱开压床托盘号为空："+locationNo);//
						}

						//						Instruction instruction = new Instruction();
						//						//						instruction.setEquip_no(equipDto.getId());
						//						instruction.setEquip_no(InstructionConfig.Formation_Stocker_Device_No);
						//						instruction.setHouse_no(equipDto.getLine());
						//						instruction.setInstr_status(InstructionStatus.Created);
						//						instruction.setInstr_level(5);//TODO 转配置 化成出库任务的默认优先级
						//						instruction.setInstr_type(InstructionType.Stock_Out);
						//						instruction.setSend_type(SendType.QueuesDirect);
						//						instruction.setPallet_no(equipDto.getTray_no());
						//						instruction.setFrom_pos(locationNo);
						//						//						instruction.setTo_pos(to_pos);
						//						memoryInstructionAppService.createInstr(instruction);
					}
				}

				/**
				 * 计算是否允许脱开
				 * 
				 * 其中涉及到如果空拖回流满了，那么必须先走掉空拖，否则可能出现线体堵死
				 */
				//会有创建所以要获取最新的
				instructionByQueueNo = memoryInstructionAppService.getInstructionByQueueNo(line, InstructionConfig.Formation_Stocker_Device_No).getItems();

				/*
				 * 只取STOCK_OUT任务
				 */
				boolean hasExecute = false;//是否有执行状态的出库任务
				stockOuts = new LinkedList<>();
				for (Instruction instruction : instructionByQueueNo) {
					if (InstructionType.Stock_Out.equals(instruction.getInstr_type())) {
						stockOuts.add(instruction);
						if (!InstructionStatus.Created.equals(instruction.getInstr_status())) {
							hasExecute = true;
						}
					}
				}
				
				/*
				 * 允许脱开的任务（包含send，executing，waiting，create的）
				 * 只取排序过后的前几位
				 */
				int allowNum = FormationConfig.pressMaxAllow;//默认能够同时脱开的压床数量
//				allowNum = 4;
				List<String> allowLocations = new LinkedList<>();
				int i = 1;
				for (Instruction instruction : stockOuts) {
					if (i > allowNum) {
						break;
					}
					//					boolean hasData = formationAppService.hasData();//是否已经获取到化成结果了
					//					if(!hasData){//
					//						continue;
					//					}
//					logDemo.info("allowLocation",JsonUtl.parseWithoutException(instruction));
					allowLocations.add(instruction.getFrom_pos());
					i++;
				}
				
//				logDemo.info("allowLocations",JsonUtl.parseWithoutException(allowLocations));
				
				/*
				 * 如果允许脱开则返回
				 * 
				 */
				boolean tag = false;
				for (EquipDto d : lineEquips) {
					String locationNo = ApplicationConfig.getLocationNo(d.getX(), d.getY(), d.getZ());
					if (allowLocations.contains(locationNo)) {
						allowEquipDtos.add(d);
						tag = true;
					}
				}
				
//				synchronized (FormationServiceSoap.class) {
				if(!hasExecute){
					if (!tag) {
//						System.out.println("请求无果的次数:"+times.get(line));
						FileLogFactory.warnCustomer("/ic/formationMessage", "", "","请求无果的次数:"+times.get(line));
						if (times.get(line) != null) {
							times.put(line, times.get(line) + 1);
						} else {
							times.put(line, 0);
						}
					} else {
						times.put(line, 0);
					}
				}else{
					times.put(line, 0);
				}
//				}

			}
			
			
			result.setList(allowEquipDtos);
			return result;
		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ApplyPressOpenResponse(500, e.getMessage());
		} finally {
			logUnity.end("applyPressOpen", JsonUtl.parseWithoutException(result));
//			logDemo.end(JsonUtl.parseWithoutException(result));
		}

		return result;
		}
	}

	/**
	 * 通知取走托盘
	 */
//	@SuppressWarnings("rawtypes")
	@WebMethod(operationName = "noticeTakeTrayPort")
	@WebResult(name = "response")
	@Transaction
	public ResultDto noticeTakeTrayPort(@WebParam(name = "request") NoticeTakeTrayRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("noticeTakeTrayPort", "");
		try {
			logUnity.begin("noticeTakeTray", JsonUtl.parseWithoutException(request));
			/**
			 * 1.获取当前设备的所有指令
			 * 2.找到指令并设定状态为执行
			 */
			EquipDto equipDto = request.getEquipDto();
			if (equipDto == null || request.getType() == null) {
				return new ResultDto(501, "参数不全");
			}
			List<Instruction> instructionByQueueNo = memoryInstructionAppService.getInstructionByQueueNo(equipDto.getLine(), InstructionConfig.Formation_Stocker_Device_No).getItems();
			String locationNo = ApplicationConfig.getLocationNo(equipDto.getX(), equipDto.getY(), equipDto.getZ());
			boolean tag = false;
			for (Instruction instruction : instructionByQueueNo) {
				if (InstructionType.Stock_Out.equals(instruction.getInstr_type())) {
					if (StringUtl.isEqual(locationNo, instruction.getFrom_pos())) {
						tag = true;
						if (InstructionStatus.Created.equals(instruction.getInstr_status())) {
							//TODO 9 可以考虑加上判断避免化成发送别的库位号的指令
							if (RoadType.normal.equals(request.getType())) {
								memoryInstructionAppService.setInstrSatus(instruction, InstructionStatus.Waiting);
								//TODO 此指令如果完成了，如果2260有货物那么优先级提高
								//考虑价格指令完成的时候跑事件来后置处理
							} else {
								//注意修改出口地址
								memoryInstructionAppService.abnormalFinish(instruction.getInstr_no(), EfcsErrorCode.instruction_exception, "化成柜通知异常出库");
								procedureAppService.changeToErrorProcedure(equipDto.getLine(), locationNo, equipDto.getTray_no(), "化成柜通知异常出库");
							}
							break;
						} else {
							logUnity.warn("指令为非create状态，不执行");
						}
					}
				}
			}
			if (!tag) {
	        	FileLogFactory.warnCustomer("/ic/formationMessage", "", "","直接创建取盘任务",locationNo);//
	        	logUnity.info("直接创建取盘任务");
				try {
					@SuppressWarnings("rawtypes")
					ObjectResult obj = (ObjectResult) procedureAppService.procedureFinished(equipDto.getLine(), locationNo, equipDto.getTray_no());
					Instruction ins = (Instruction) obj.getObject();
//					System.out.println(JsonUtl.parseWithoutException(ins));
					if (InstructionStatus.Created.equals(ins.getInstr_status())) {
						if (RoadType.normal.equals(request.getType())) {
							memoryInstructionAppService.setInstrSatus(ins, InstructionStatus.Waiting);
							//TODO 此指令如果完成了，如果2260有货物那么优先级提高
							//考虑价格指令完成的时候跑事件来后置处理
						} else {
							//注意修改出口地址
							memoryInstructionAppService.abnormalFinish(ins.getInstr_no(), EfcsErrorCode.instruction_exception, "化成柜通知异常出库");
							procedureAppService.changeToErrorProcedure(equipDto.getLine(), locationNo, equipDto.getTray_no(), "化成柜通知异常出库");
						}
					}
				} catch (Exception e) {
					log.warn("", e);
				}
			}
			return result;
		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("noticeTakeTray", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 化成结束
	 */
	@WebMethod(operationName = "completeFormation")
	@WebResult(name = "response")
	@Transaction
	public FormationCompleteResponse completeFormation(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") FormationCompleteRequest request) {
		FormationCompleteResponse response = new FormationCompleteResponse();
		if (ICConfig.getIsCommunicateFake()) {
			response.setCode(0);
			return response;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("completeFormation", "");
		response.setCode(0);
		try {
			logUnity.begin("completeFormation", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));
			// 获取化成结果
			CellTestResultResponse cellTestResult = CatlWebServiceFunction.getCellTestResultByTrayId(request.getResource(), equip.getTray_no());
			if(cellTestResult.getCode()!=0){
				response.setCode(404);
				response.setMessage(cellTestResult.getCode()+":"+cellTestResult.getMessage());
				return response;
			}
			List<SfcResult> sfcResults = cellTestResult.getResultArray();
			for (SfcResult r : sfcResults) {
				// 更新电池信息
				String rBarcode = r.getSfc();

				//                String rResult = DispatcherConfig.battery_default_ng;
				//                if (r.getResult() == 0) {
				//                    rResult = DispatcherConfig.battery_default_ok;
				//                }
				batteryInfoService.updateBatteryStatus(rBarcode, Integer.toString(r.getResult()), "MES化成结果获取");
			}

			// 化成结束
			response = ICWebServiceFunction.completeFormation(request.getOperation(), request.getResource(), request.getProcessLot());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn("completeFormation", ExceptionUtl.getExceptionDetail(e));
			response = new FormationCompleteResponse();
			response.setCode(500);
			response.setMessage(e.getMessage());
		} finally {
			logUnity.end("completeFormation", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(response));
		}

		return response;
	}
}
