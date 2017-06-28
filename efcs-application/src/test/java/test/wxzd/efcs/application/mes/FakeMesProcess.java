package test.wxzd.efcs.application.mes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlmes.ws.celltest.CellListRequest;
import com.atlmes.ws.celltest.CellListResponse;
import com.atlmes.ws.celltest.CellTestResultResponse;
import com.atlmes.ws.celltest.ChannelStatusResponse;
import com.atlmes.ws.celltest.FormationCompleteResponse;
import com.atlmes.ws.celltest.IcResultResponse;
import com.atlmes.ws.celltest.ModeProcessFilePath;
import com.atlmes.ws.celltest.ProcessFilePathResponse;
import com.atlmes.ws.celltest.ProcessLotReleaseResponse;
import com.atlmes.ws.celltest.SfcRemovalResponse;
import com.atlmes.ws.celltestintegration.CheckProcessLotResponse;
import com.atlmes.ws.celltestintegration.CustomSfcArrayData;
import com.atlmes.ws.celltestintegration.GetCurrentProcessLotResultResponse;
import com.atlmes.ws.machine.ProcessLotDcResponseEx;
import com.atlmes.ws.machineintegration.DataCollectForProcessLotForEachResponse;
import com.atlmes.ws.machineintegration.DataCollectResultArrayData;
import com.atlmes.ws.machineintegration.DataCollectSfcParametricData;
import com.atlmes.ws.machineintegration.MiCommonResponse;
import com.sap.me.datacollection.ParameterDataType;
import com.sap.me.production.StartSfcResponse;
import com.wxzd.catl.CatlWebServiceFunction;
import com.wxzd.catl.ICWebServiceFunction;
import com.wxzd.catl.base.CatlWebServiceConfig;
import com.wxzd.catl.webservice.base.CellTestIntegrationServiceBase;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.business.webservice.MESAppWebservice;
import com.wxzd.gaia.common.base.core.log.FileLogConfig;
import com.wxzd.gaia.common.base.json.JsonUtl;

import test.wxzd.efcs.application.mold.SpringDemoBase;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-05-12
 */
public class FakeMesProcess extends SpringDemoBase {

	private static final Logger log = LoggerFactory.getLogger(FakeMesProcess.class);

	List<String> cellList = new ArrayList<>();
	String trayId = "";
	String resource = "FXXX1241";
	String tire = "08";

	{
		cellList.add("15S7184J3709");
		cellList.add("15S7191J5223");
		cellList.add("15S7191J5244");
		cellList.add("15S7191J7690");
		cellList.add("15S7186J8829");
		trayId = "L29B0302";
	}

	@Before
	public void beforeTest() {
//		String username = "60024251";
//		String password = "Wh123456";
		FileLogConfig.setMes_log_path("D:/test/catl");
		//		CatlWebServiceConfig.setServerPath("http://127.0.0.1:8082");
		//		CatlWebServiceConfig.setServerPath("http://ndmes.catlbattery.com:8103");
		////		CatlWebServiceConfig.setUsername("60024251");
		////		CatlWebServiceConfig.setPassword("Wh123456");
		////		CatlWebServiceConfig.setUsername("60005272");
		////		CatlWebServiceConfig.setPassword("Gf123456");
		//		CatlWebServiceConfig.setUsername("SUP_L29_FORMN3");
		//		CatlWebServiceConfig.setHighTempUser("SUP_L29_HSTDY1");
		//		CatlWebServiceConfig.setPassword("CATLmespco1");
//		CatlWebServiceConfig.setServerPath("http://172.26.11.51:50000");
//		CatlWebServiceConfig.setNormalTempUser(username);
//		CatlWebServiceConfig.setNormalTempPassword(password);
//		CatlWebServiceConfig.setNormalTempResource("RTSX1001");
//		CatlWebServiceConfig.setNormalTempNextResource("RTSX1001");
//		CatlWebServiceConfig.setOvcUser(username);
//		CatlWebServiceConfig.setOvcPassword(password);
//		CatlWebServiceConfig.setOvcNextNextUser(username);
//		CatlWebServiceConfig.setOvcNextNextPassword(password);
//		CatlWebServiceConfig.setHighTempUser("SUP_L29_HSTDY1");
		CatlWebServiceConfig.setIsCommunicateFake(false);
	}

	@Autowired
	PalletDispatchService palletDispatchService;
	
	@Test
	public void xxx() throws Exception {
		System.out.println("...");
	}
	
	/**
	 * 此处为手动调用mes的静置1收数功能
	 */
	@Test
	public void normal_date_collect_ex() throws Exception {
		String trayId = "L29B0328";
		List<PalletDetail> batteries = palletDispatchService.getPalletInnerDetail(trayId);
		List<String> sfcBarcodes = new ArrayList<>();
		for (PalletDetail item : batteries) {
			sfcBarcodes.add(item.getBattery_barcode());
		}
		System.out.println(JsonUtl.parse(sfcBarcodes));

		DataCollectForProcessLotForEachResponse response = MESAppWebservice.normalDataCollectForProcessLotExCore("29", trayId, sfcBarcodes, 16*60*60);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}
	
	@Test
	public void high_check_sfc() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			MiCommonResponse response = CatlWebServiceFunction.miCheckSFCStatusEx_highTemp(CatlWebServiceConfig.getHighTempOpertion(), cellList.get(i));
			log.info("{}", JsonUtl.parseWithoutException(response));
		}
	}
	@Test
	public void high_check_sfc_single() throws Exception {
		    String cell = "15S7211JA488";
			MiCommonResponse response = CatlWebServiceFunction.miCheckSFCStatusEx_highTemp(CatlWebServiceConfig.getHighTempOpertion(), cell);
			log.info("{}", JsonUtl.parseWithoutException(response));
	}

	@Test
	public void high_check_tray() throws Exception {
		//		CheckProcessLotResponse response = CatlWebServiceFunction.miCheckProcessLot("L29B0324");
		CheckProcessLotResponse response = CatlWebServiceFunction.miCheckProcessLot_highTemp("L29B0311");
//		CheckProcessLotResponse response = CatlWebServiceFunction.miCheckProcessLot_highTemp("L29B0292");
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

	

	@Test
	public void high_release_tray() throws Exception {
		ProcessLotReleaseResponse response = CatlWebServiceFunction.releaseTrayId_high("L29B0309");
		log.info("{}", JsonUtl.parseWithoutException(response));
	}
	
	@Test
	public void normal_release_tray() throws Exception {
		ProcessLotReleaseResponse response = CatlWebServiceFunction.releaseTrayId_high("L29B1211");
		log.info("{}", JsonUtl.parseWithoutException(response));
	}
	
	/**
	 * 获取mes托盘sfcmessage
	 */
	@Test
	public void getCellListByTrayId() {
		CellListRequest request = new CellListRequest();
		request.setProcessLot("L29B0309");
		request.setSite(CatlWebServiceConfig.getSite());
		request.setMode(com.atlmes.ws.celltest.ModeTrayMatrix.fromValue(CatlWebServiceConfig.getTrayMode()));
		CellListResponse response = CellTestIntegrationServiceBase.getHighInstanceCellReleaseService().getCellListByTrayId(request);
		log.trace("{}", response.getCode());
		log.trace("{}", response.getMessage());
		List<String> resultArray = response.getSfcList();
		if (resultArray != null) {
			System.out.println(resultArray.size());
			int i = 0;
			for (String result : resultArray) {
				log.trace("{} sfc result: {}", i + 1, result);
				i++;
			}
		}
	}
	
	
	@Test
	public void check_sys_single() throws Exception {
		MiCommonResponse response = CatlWebServiceFunction.miCheckSFCStatusEx_highTemp(CatlWebServiceConfig.getHighTempOpertion(), "15S7204J8990");
		log.info("{}", JsonUtl.parseWithoutException(response));
	}
	
	@Test
	public void hight_bind() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			MiCommonResponse response = CatlWebServiceFunction.miBindSFCintoTray_highTemp(trayId, cellList.get(i), i + 1);
			log.info("{}", JsonUtl.parseWithoutException(response));
		}
	}

	/**
	 * 手动调用mes接口，静置收数
	 */
	@Test
	public void hightDatePost() throws Exception {
		ProcessLotDcResponseEx response = MESAppWebservice.hightDataCollectForProcessLotExCore("29", "L29B0082", 6 * 60 * 60);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

	@Test
	public void getChannelStatus() throws Exception {
		ChannelStatusResponse response = ICWebServiceFunction.getChannelStatus(resource, tire);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

	@Test
	public void getformationPath() throws Exception {
		ProcessFilePathResponse response = ICWebServiceFunction.getProcessFilePathByTrayId(CatlWebServiceConfig.getFormationOpertion(), trayId, resource, ModeProcessFilePath.NORMAL);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

	@Test
	public void formationStartSfc() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			StartSfcResponse response = ICWebServiceFunction.startSfc(CatlWebServiceConfig.getFormationOpertion(), resource, cellList.get(i), CatlWebServiceConfig.getFormationUserMap().get("29"));
			log.info("{}", JsonUtl.parseWithoutException(response));
		}
	}

	@Test
	public void postICResult() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			String cid = "";
			if (i + 1 < 10) {
				cid = "0" + (i + 1);
			} else {
				cid = "" + (i + 1);
			}
			IcResultResponse response = __postICResult(resource, cellList.get(i), tire, cid);
			log.info("{}", JsonUtl.parseWithoutException(response));
		}

	}

	private IcResultResponse __postICResult(String resource, String cell, String tier, String channelId) throws Exception {
		List<com.atlmes.ws.celltest.MachineIntegrationParametricData> list = new LinkedList<>();
		com.atlmes.ws.celltest.MachineIntegrationParametricData data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("CellNo");
		data.setValue(cell);
		data.setDataType(ParameterDataType.TEXT);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("ICC0");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("ICFV");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("ICLCC");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("ICLV");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("S2VOL");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("S3FLC");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("S4VOL");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);
		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
		data.setName("STEP");
		data.setValue("0");
		data.setDataType(ParameterDataType.NUMBER);
		list.add(data);

		IcResultResponse response = ICWebServiceFunction.postICResult(CatlWebServiceConfig.getFormationOpertion(), resource, cell, CatlWebServiceConfig.getFormationUser(), tier, channelId, list);
		return response;
	}

	@Test
	public void getFormationResult() throws Exception {
		CellTestResultResponse response = CatlWebServiceFunction.getCellTestResultByTrayId(resource, trayId);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

	@Test
	public void formationComplete() throws Exception {
		FormationCompleteResponse response = ICWebServiceFunction.completeFormation(CatlWebServiceConfig.getFormationOpertion(), resource, trayId);
		log.info("{}", JsonUtl.parseWithoutException(response));

	}

	@Test
	public void high_release_cell() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			SfcRemovalResponse response = CatlWebServiceFunction.releaseCell_high(trayId, cellList.get(i));
			log.info("{}", JsonUtl.parseWithoutException(response));
		}
	}

	//测试段接口调用

	/*
	 * 测试用账号密码
	 * 60024251
	 * Wh123456
	 * http://172.26.11.51:50000
	 */
	@Test
	public void normal_check_tray() throws Exception {
		CheckProcessLotResponse response = CatlWebServiceFunction.miCheckProcessLot_normalTemp(trayId);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

	@Test
	public void normal_check_sfc() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			MiCommonResponse response = CatlWebServiceFunction.miCheckSFCStatusEx_normalTemp(CatlWebServiceConfig.getNormalTempOpertion(), cellList.get(i));
			log.info("{}", JsonUtl.parseWithoutException(response));
		}
	}

	@Test
	public void normal_bind() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			MiCommonResponse response = CatlWebServiceFunction.miBindSFCintoTray_normalTemp(trayId, cellList.get(i), i + 1);
			log.info("{}", JsonUtl.parseWithoutException(response));
		}
	}

	/**
	 * 针对如果ovc异常了那么调用此接口应该能过
	 */
	@Test
	public void normalDatePostEx() throws Exception {
		DataCollectForProcessLotForEachResponse response = MESAppWebservice.normalDataCollectForProcessLotExCore("29", trayId, cellList, 30*60);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

	/**
	 * 针对如果ovc异常了那么调用此接口应该能过
	 */
	@Test
	public void normal2DatePostEx() throws Exception {
		DataCollectForProcessLotForEachResponse response = MESAppWebservice.normalNextDataCollectForProcessLotExCore("29", trayId, cellList, 30*60);
		log.info("{}", JsonUtl.parseWithoutException(response));
	}

//	private DataCollectForProcessLotForEachResponse normal__miDataCollectForProcessLotForEach(String house_no, String operation, String resource, String time, String tray, List<String> cells)
//			throws Exception {
//		List<DataCollectSfcParametricData> sfcArray = new LinkedList<>();
//		for (String cell : cells) {
//			List<com.atlmes.ws.machineintegration.MachineIntegrationParametricData> list1_1 = new LinkedList<>();
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1PSITN", house_no, ParameterDataType.TEXT));
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1TRAYNO", tray, ParameterDataType.TEXT));
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADCAPTM1", time, ParameterDataType.NUMBER));
//			DataCollectSfcParametricData dataCollectSfcParametricData = new DataCollectSfcParametricData(cell, CatlWebServiceConfig.getDcGroup(), CatlWebServiceConfig.getDcGroupVersion(), list1_1,
//					null);
//			sfcArray.add(dataCollectSfcParametricData);
//		}
//		DataCollectForProcessLotForEachResponse response = ICWebServiceFunction.miDataCollectForProcessLotForEach(operation, resource, tray, CatlWebServiceConfig.getNormalTempUser(), sfcArray);
//		return response;
//	}
//
//	private DataCollectForProcessLotForEachResponse normal2__miDataCollectForProcessLotForEach(String house_no, String operation, String resource, String time, String tray, List<String> cells)
//			throws Exception {
//		List<DataCollectSfcParametricData> sfcArray = new LinkedList<>();
//		for (String cell : cells) {
//			List<com.atlmes.ws.machineintegration.MachineIntegrationParametricData> list1_1 = new LinkedList<>();
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1PSITN", house_no, ParameterDataType.TEXT));
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1TRAYNO", tray, ParameterDataType.TEXT));
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADCAPTM1", time, ParameterDataType.NUMBER));
//			DataCollectSfcParametricData dataCollectSfcParametricData = new DataCollectSfcParametricData(cell, CatlWebServiceConfig.getDcGroup(), CatlWebServiceConfig.getDcGroupVersion(), list1_1,
//					null);
//			sfcArray.add(dataCollectSfcParametricData);
//		}
//		DataCollectForProcessLotForEachResponse response = ICWebServiceFunction.miDataCollectForProcessLotForEach(operation, resource, tray, CatlWebServiceConfig.getNormalTempUser(), sfcArray);
//		return response;
//	}

//	/**
//	 * 废弃的接口
//	 */
//	@Test
//	@Deprecated
//	public void normalDatePost() throws Exception {
//		ProcessLotDcResponseEx response = MESAppWebservice.normalDataCollectForProcessLotExCore("29", trayId, 31 * 60);
//		log.info("{}", JsonUtl.parseWithoutException(response));
//	}
//
//	/**
//	 * 废弃的接口
//	 */
//	@Test
//	@Deprecated
//	public void normalNextDatePost() throws Exception {
//		ProcessLotDcResponseEx response = MESAppWebservice.normalNextDataCollectForProcessLotExCore("29", "L29B0302", 31 * 60);
//		log.info("{}", JsonUtl.parseWithoutException(response));
//	}

	@Test
	public void normal_release_cell() throws Exception {
		for (int i = 0, j = cellList.size(); i < j; i++) {
			SfcRemovalResponse response = CatlWebServiceFunction.releaseCell_normal(trayId, cellList.get(i));
			log.info("{}", JsonUtl.parseWithoutException(response));
		}
	}

//	@Test
//	public void temperature1() throws Exception {
//		ProcessLotDcResponseEx response = MESAppWebservice.normalDataCollectForProcessLotExCore("29", trayId, 466 * 60);
//		log.info("{}", JsonUtl.parseWithoutException(response));
//	}

	@Test
	public void ovc1() throws Exception {
		DataCollectForProcessLotForEachResponse response = __miDataCollectForProcessLotForEach("TSOCV1", "OCVT1032", trayId, cellList);
		List<DataCollectResultArrayData> resultArray = response.getResultArray();
		if (resultArray != null) {
			for (DataCollectResultArrayData result : resultArray) {
				log.trace("{}", JsonUtl.parseWithoutException(result));
			}
		}
		log.trace("{}", response.getCode());
		log.trace("{}", response.getMessage());
	}

	private DataCollectForProcessLotForEachResponse __miDataCollectForProcessLotForEach(String operation, String resource, String tray, List<String> cells) throws Exception {
		List<DataCollectSfcParametricData> sfcArray = new LinkedList<>();
		int i=0;
		for (String cell : cells) {
			i++;
			List<com.atlmes.ws.machineintegration.MachineIntegrationParametricData> list1_1 = new LinkedList<>();
			
			if(i==1){
				list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("OCV", "4", ParameterDataType.NUMBER));
			}else{
				list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("OCV", "0.05", ParameterDataType.NUMBER));
			}
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("IMP", "1", ParameterDataType.NUMBER));
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("TEMPERATURE", "26.9", ParameterDataType.NUMBER));
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("POSISURFVOT", "-0.0007", ParameterDataType.NUMBER));
			DataCollectSfcParametricData dataCollectSfcParametricData = new DataCollectSfcParametricData(cell, CatlWebServiceConfig.getDcGroup(), CatlWebServiceConfig.getDcGroupVersion(), list1_1,
					new LinkedList<com.atlmes.ws.machineintegration.NonConfirmCodeArray>());
			sfcArray.add(dataCollectSfcParametricData);
		}
		DataCollectForProcessLotForEachResponse response = ICWebServiceFunction.miDataCollectForProcessLotForEach(operation, resource, tray, CatlWebServiceConfig.getOcvUser(), sfcArray);
		return response;
	}

//	@Test
//	public void temperature2() throws Exception {
//		ProcessLotDcResponseEx response = MESAppWebservice.normalNextDataCollectForProcessLotExCore("29", "L26B0229", 2 * 60);
//		log.info("{}", JsonUtl.parseWithoutException(response));
//	}

	@Test
	public void ovc2() throws Exception {
		DataCollectForProcessLotForEachResponse response = __miDataCollectForProcessLotForEach("TSOCVB", "OCVT1032", trayId, cellList);
//		DataCollectForProcessLotForEachResponse response = __miDataCollectForProcessLotForEach("TSOCVB", "OCVT1010", trayId, cellList);
		List<DataCollectResultArrayData> resultArray = response.getResultArray();
		if (resultArray != null) {
			for (DataCollectResultArrayData result : resultArray) {
				log.trace("{}", JsonUtl.parseWithoutException(result));
			}
		}
		log.trace("{}", response.getCode());
		log.trace("{}", response.getMessage());
	}

	@Test
	public void getOcvResult() throws Exception {
		String trayId = "L29B1109";
//		GetCurrentProcessLotResultResponse response = CatlWebServiceFunction.ctiGetCurrentProcessLotResult(ClWebServiceConfig.getOcvNextNextOperation(), trayId,
//		MESAppWebservice.ctiGetCurrentProcessLotResult("29", trayId);
		GetCurrentProcessLotResultResponse response = MESAppWebservice.ctiGetCurrentProcessLotResult("29", "L29B1019");
		log.trace("{}", response.getCode());
		log.trace("{}", response.getMessage());
		List<CustomSfcArrayData> sfcArray = response.getSfcArray();
		if (sfcArray != null) {
			for (CustomSfcArrayData data : sfcArray) {
				log.trace("{}", JsonUtl.parseWithoutException(data));
			}
		}
	}

//	@Test
//	public void normal_release_tray() throws Exception {
//		ProcessLotReleaseResponse response = CatlWebServiceFunction.releaseTrayId_normal(trayId);
//		log.info("{}", JsonUtl.parseWithoutException(response));
//	}
	
	@Test
	public void NGChangeLocation() throws Exception {
//		MiCommonResponse response = CatlWebServiceFunction.miStepToOtherOperation(CatlWebServiceConfig.getOcvNextNextOperation(),//
//				"OCVTPUBLIC", "15G651500734");
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
		Map<String, String> ocvNextNextResourceMap = new HashMap<String, String>();
		ocvNextNextResourceMap.put("29", "OCVTPUBLIC");
		ocvNextNextResourceMap.put("28", "OCVTPUBLIC");
		ocvNextNextResourceMap.put("27", "OCVTPUBLIC");
		ocvNextNextResourceMap.put("26", "OCVTPUBLIC");
		CatlWebServiceConfig.setOcvNextNextResourceMap(ocvNextNextResourceMap);
		
//		MiCommonResponse response = MESAppWebservice.miStepToOtherOperation("29", "15S7184J3709");
//		log.trace("{}",JsonUtl.parseWithoutException(response));
//		
//		response = MESAppWebservice.miStepToOtherOperation("29", "15S7191J5223");
//		log.trace("{}",JsonUtl.parseWithoutException(response));
//		
//		response = MESAppWebservice.miStepToOtherOperation("29", "15S7191J7690");
//		log.trace("{}",JsonUtl.parseWithoutException(response));
		
	}
	
	

}
