package com.wxzd.efcs.business.webservice.ic;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlmes.ws.machineintegration.DataCollectForProcessLotForEachRequest;
import com.atlmes.ws.machineintegration.DataCollectForProcessLotForEachResponse;
import com.atlmes.ws.machineintegration.DataCollectResultArrayData;
import com.wxzd.catl.ICWebServiceFunction;
import com.wxzd.catl.log.CatlWebserviceLogFactory;
import com.wxzd.configration.catlConfig.ApplicationConfig;
import com.wxzd.configration.catlConfig.OCVConfig;
import com.wxzd.efcs.alarm.domain.events.ProceduresErrorEvent;
import com.wxzd.efcs.business.application.exception.ProcedureException;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.gaia.common.base.core.exception.ExceptionUtl;
import com.wxzd.gaia.common.base.core.log.FileLogUnity;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.wms.catl.fcs.webservice.ICConfig;
import com.wxzd.wms.catl.fcs.webservice.log.ICWebServiceLogFactory;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.EquipDto;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.GetOcvOperationMessageResponse;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.OcvType;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ResultDto;
import com.wxzd.wms.catl.fcs.webservice.webservice.ic.dto.ResultDtoFactory;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-16
 */
@WebService(serviceName = "OCVService")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class OCVServiceSoap {

	private static final Logger log = LoggerFactory.getLogger(OCVServiceSoap.class);

	@Autowired
	private ProcedureAppService procedureAppService;
	
	@Autowired
	private BatteryInfoService batteryInfoService;

	/**
	 * ocv进站
	 */
	@Transaction
	@WebMethod(operationName = "ocvInPort")
	@WebResult(name = "response")
	public ResultDto ocvInPort(@WebParam(name = "request") EquipDto request) {
		System.out.println("OCVServiceSoap method ocvInPort in");
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("ocvInPort", "");
		//		FileLogUnity logTemp = CatlWebserviceLogFactory.getWebserviceFileLogUnity("ocvInPort", "id");//临时测试用
		try {
			logUnity.begin("ocvIn", JsonUtl.parseWithoutException(request));
			//			logTemp.begin(JsonUtl.parseWithoutException(request));

			// ocv进站
			procedureAppService.procedureArrive(request.getLine(), request.getId(), request.getTray_no());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("ocvIn", JsonUtl.parseWithoutException(result));
			//			logTemp.end(JsonUtl.parseWithoutException(result));
		}

		return result;
	}

	/**
	 * ocv获取操作信息
	 */
	@WebMethod(operationName = "getOcvOperationMessagePort")
	@WebResult(name = "response")
	public GetOcvOperationMessageResponse getOcvOperationMessagePort(@WebParam(name = "request") EquipDto request) {
		GetOcvOperationMessageResponse result = new GetOcvOperationMessageResponse(0, null);
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("getOcvOperationMessagePort", "");
		try {
			logUnity.begin("getOcvOperationMessage", JsonUtl.parseWithoutException(request));

			// ocv获取操作信息
			WorkProcedure workProcedure = procedureAppService.getCurrentWorkProcedure(request.getLine(), request.getTray_no());
			if (workProcedure == WorkProcedure.Test_OCV_1) {
				result.setOcvType(OcvType.ovc1);
			} else if (workProcedure == WorkProcedure.Test_OCV_2) {
				result.setOcvType(OcvType.ovc2);
			} else {
				// 报工序异常的错误消息
				String errorMsg = String.format("OCV设备：%s 无法处理托盘（%s）的当前工序（%s）", request.getId(), request.getTray_no(), workProcedure);
				
				
				ProceduresErrorEvent event = new ProceduresErrorEvent();
				event.setHouseNo(request.getLine());
				event.setDeviceNo(request.getId());
				event.setPalletNo(request.getTray_no());
				event.setLocation(ApplicationConfig.getLocationNo(request.getX(), request.getY(), request.getZ()));
				event.setContent(String.format("OCV设备：%s 无法处理托盘（%s）的当前工序（%s）", request.getId(), request.getTray_no(), workProcedure));
				event.setSource(this);
				ApplicationEventPublisher.trigger(event);

				throw new ProcedureException("当前设备不能处理当前工序");
			}
		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new GetOcvOperationMessageResponse(500, e.getMessage());
		} finally {
			logUnity.end("getOcvOperationMessage", JsonUtl.parseWithoutException(result));
		}
		return result;
	}

	/**
	 * 需要调用Mes
	 * 
	 * OCV 传数
	 */
	@WebMethod(operationName = "miDataCollectForProcessLotForEach")
	@WebResult(name = "response")
	public DataCollectForProcessLotForEachResponse miDataCollectForProcessLotForEach(@WebParam(name = "equip") EquipDto equip,//
			@WebParam(name = "request") DataCollectForProcessLotForEachRequest request) {
		DataCollectForProcessLotForEachResponse response = new DataCollectForProcessLotForEachResponse();
		if (ICConfig.getIsCommunicateFake()) {
			response.setCode(0);
			return response;
		}
		
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("miDataCollectForProcessLotForEach", "");
		response.setCode(0);
		try {
			logUnity.begin("miDataCollectForProcessLotForEach", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(request));

			response = ICWebServiceFunction.miDataCollectForProcessLotForEach(request.getOperation(), request.getResource(), request.getProcessLot(), request.getUser(), request.getSfcArray());
			
			if (OCVConfig.fakeReturn) {
				//TODO 测试数据
				List<DataCollectResultArrayData> arr = response.getResultArray();
				for (int i = 0; i < 24; i++) {
					DataCollectResultArrayData data = new DataCollectResultArrayData();
					data.setResultCode(0);
					arr.add(data);
				}
				response.setCode(0);
				//测试数据模拟结束
			}

		
		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			response = new DataCollectForProcessLotForEachResponse();
			response.setCode(500);
			response.setMessage(e.getMessage());
		} finally {
			logUnity.end("miDataCollectForProcessLotForEach", JsonUtl.parseWithoutException(equip) + JsonUtl.parseWithoutException(response));
		}

		return response;
	}

	/**
	 * ocv出站
	 */
	@WebMethod(operationName = "ocvOutPort")
	@WebResult(name = "response")
	public ResultDto ocvOutPort(@WebParam(name = "request") EquipDto request) {
		ResultDto result = ResultDtoFactory.getSuccess();
		if (ICConfig.getIsCommunicateFake()) {
			return result;
		}
		FileLogUnity logUnity = ICWebServiceLogFactory.getWebServiceFileLogUnity("ocvOutPort","");
		try {
			logUnity.begin("ocvOut", JsonUtl.parseWithoutException(request));

			// ocv出站
			procedureAppService.procedureFinished(request.getLine(), request.getId(), request.getTray_no());

		} catch (Exception e) {
			log.warn("", e);
			logUnity.warn(ExceptionUtl.getExceptionDetail(e));
			result = new ResultDto(500, e.getMessage());
		} finally {
			logUnity.end("ocvOut", JsonUtl.parseWithoutException(result));
		}

		return result;
	}

}
