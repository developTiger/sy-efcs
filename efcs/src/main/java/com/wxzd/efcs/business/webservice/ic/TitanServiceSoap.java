package com.wxzd.efcs.business.webservice.ic;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wxzd.efcs.alarm.domain.events.FormationDeviceErrorEvent;
import com.wxzd.efcs.business.application.queryService.PalletDetailExQueryService;
import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfoService;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.gaia.common.base.core.exception.ExceptionUtl;
import com.wxzd.gaia.common.base.core.log.FileLogUnity;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.wms.catl.fcs.webservice.ICConfig;
import com.wxzd.wms.catl.fcs.webservice.log.ICWebServiceLogFactory;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.BatteryInfo;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.BeatheartRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.EquipDto;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.GetBatteryListResponse;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.PushExceptionMessageRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.PushLocationStatusRequest;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ResultDto;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ResultDtoFactory;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-17
 */
@WebService(serviceName = "TitanService")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class TitanServiceSoap {

	private static final Logger log = LoggerFactory.getLogger(TitanServiceSoap.class);

	@Autowired
	private PalletDetailExQueryService palletDetailExQueryService;

	@Autowired
	private DeviceRealtimeInfoService deviceRealtimeInfoService;

	/**
	 * 获取电芯列表
	 */
	@WebMethod(operationName = "getBatteryListPort")
	@WebResult(name = "response")
	public GetBatteryListResponse getBatteryListPort(@WebParam(name = "request") EquipDto request) {
		GetBatteryListResponse result = new GetBatteryListResponse(0, null);
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("getBatteryListPort", "");
		try {
			logUnity.begin("getBatteryList", JsonUtl.parseWithoutException(request));
			
			
			// 获取电芯列表
			List<PalletDetailDto> details = palletDetailExQueryService.getInnerDetailByPalletNo(request.getTray_no());
			List<BatteryInfo> infos = new ArrayList<>();
			for (PalletDetailDto d : details) {
				BatteryInfo info = new BatteryInfo();
				info.setCode(d.getBattery_barcode());
				info.setChannelNo(Integer.parseInt(d.getChannel_no()));

				infos.add(info);
			}
			
//			//TODO 测试用数据
//			infos = new ArrayList<>();
//			for(int i=1;i<=24;i++){
//				BatteryInfo info = new BatteryInfo();
//				info.setCode("15G70238826"+i);
//				info.setChannelNo(i);
//				infos.add(info);
//			}
//			//模拟数据结束
			
			result.setList(infos);

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new GetBatteryListResponse(500, e.getMessage());
		} finally {
			logUnity.end("getBatteryList", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 上传库位实时状态
	 * 锁定和解锁由化成通过另外一个接口确定
	 * 后面描述无效:需要和当前的库存情况进行比较，如果状态恢复，则解除锁定；如果状态异常，则禁止使用
	 */
	@WebMethod(operationName = "pushLocationStatusPort")
	@WebResult(name = "response")
	public ResultDto pushLocationStatusPort(@WebParam(name = "request") PushLocationStatusRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
//		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("pushLocationStatusPort", "");
		try {
//			logUnity.begin("pushLocationStatus", JsonUtl.parseWithoutException(request));

			deviceRealtimeInfoService.updateFormationInfo(request);

		} catch (Exception e) {
			log.warn("", e);
//			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
//			logUnity.end("pushLocationStatus", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 心跳
	 */
	@WebMethod(operationName = "beatheartPort")
	@WebResult(name = "response")
	public ResultDto beatheartPort(@WebParam(name = "request") BeatheartRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
//		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("beatheartPort", "");
		try {
//			logUnity.begin("beatheart", JsonUtl.parseWithoutException(request));

			// 心跳
			List<EquipDto> list = request.getList();
			for (EquipDto d : list) {
				deviceRealtimeInfoService.updateHeartbeat(d.getLine(), d.getId());
			}
		} catch (Exception e) {
			log.warn("", e);
//			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
//			logUnity.end("beatheart", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * 上传异常信息
	 */
	@WebMethod(operationName = "pushExceptionMessagePort")
	@WebResult(name = "response")
	public ResultDto pushExceptionMessagePort(@WebParam(name = "request") PushExceptionMessageRequest request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("pushExceptionMessagePort", "");
		try {
			logUnity.begin("pushExceptionMessage", JsonUtl.parseWithoutException(request));

			// 上报异常信息
			EquipDto d = request.getEquipDto();
			if (d != null) {
				FormationDeviceErrorEvent event = new FormationDeviceErrorEvent();
				event.setSource(this);
				event.setHouseNo(d.getLine());
				event.setDeviceNo(d.getId());
				event.setTray_no(d.getTray_no());
				event.setX(d.getX());
				event.setY(d.getY());
				event.setZ(d.getZ());
				event.setContent("化成设备自动上报异常：(" + request.getExceptionCode() + ")" + request.getExceptionMessage());

				ApplicationEventPublisher.trigger(event);
			}

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("pushExceptionMessage", JsonUtl.parseWithoutException(result));
		}
		return result;
	}

}
