package test.wxzd.efcs.application.mes;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlmes.ws.celltest.CellListRequest;
import com.atlmes.ws.celltest.CellListResponse;
import com.atlmes.ws.celltest.CellTestResultResponse;
import com.atlmes.ws.celltest.ChannelStatus;
import com.atlmes.ws.celltest.ChannelStatusResponse;
import com.atlmes.ws.celltest.FormationCompleteResponse;
import com.atlmes.ws.celltest.IcResultResponse;
import com.atlmes.ws.celltest.ModeProcessFilePath;
import com.atlmes.ws.celltest.ProcessFilePathResponse;
import com.atlmes.ws.celltest.ProcessLotReleaseResponse;
import com.atlmes.ws.celltest.SfcRemovalResponse;
import com.atlmes.ws.celltest.SfcResult;
import com.atlmes.ws.celltest.StandbyDurationResponse;
import com.atlmes.ws.celltestintegration.CheckProcessLotResponse;
import com.atlmes.ws.celltestintegration.CustomSfcArrayData;
import com.atlmes.ws.celltestintegration.GetCurrentProcessLotResultResponse;
import com.atlmes.ws.machine.MachineIntegrationParametricData;
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
import com.wxzd.catl.base.MesSystemServiceBase;

import mepapi.com.sap.me.production.StartFault;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-02-15
 */
@SuppressWarnings("unused")
public class DemoMesSystem extends MesSystemServiceBase {

//	private static final Logger log = LoggerFactory.getLogger(DemoMesSystem.class);
//
//	List<String> cellList = DemoData.getCellList();
//	List<String> trayList = DemoData.getTrayList();
//	List<String> resourceList = DemoData.getResourceList();
//
//	{
//		trayList = new LinkedList<>();
//		trayList.add("P3700001");
//		cellList = new LinkedList<>();
//		cellList.add("14T7063K2523");
//		cellList.add("14T7063K2497");
//		cellList.add("14T7063K2516");
//		cellList.add("14T7063K2517");
//	}
//
//	String trayId = trayList.get(0);
//
//	//	/**
//	//	 * 化成阶段
//	//	 */
//	//	@Test
//	//	public void buildLine() throws Exception {
//	//		boolean res = false;
//	//		String tray1 = trayList.get(0);
//	//		String tray2 = trayList.get(1);
//	//		String cell1 = cellList.get(0);
//	//		String cell2 = cellList.get(1);
//	//		String resource = resourceList.get(0);//化成柜
//	//		List<String> cells = new LinkedList<>();
//	//		cells.add(cell1);
//	//		cells.add(cell2);
//	//		log.debug("开始");
//	//		log.trace("托盘校验：miCheckProcessLot");
//	//
//	//		log.debug("校验托盘:{}", tray1);
//	//		res = isOk(miCheckProcessLot(tray1).getCode());
//	//		Assert.assertTrue("托盘校验失败", res);
//	//
//	//		log.trace("电芯校验：miCheckSFCStatusEx");
//	//
//	//		for (int i = 0, j = cells.size(); i < j; i++) {
//	//			log.debug("校验电芯:{}", cells.get(i));
//	//			res = isOk(miCheckSFCStatusEx(cells.get(i), CatlWebServiceConfig.getOpertionHighTemp()).getCode());
//	//			Assert.assertTrue("电芯校验失败", res);
//	//		}
//	//
//	//		log.trace("电芯绑定托盘：miBindSFCintoTray");
//	//
//	//		for (int i = 0, j = cells.size(); i < j; i++) {
//	//			log.debug("绑定-托盘:{},电芯:{},位置:{}", tray1, cells.get(i), i);
//	//			res = isOk(miBindSFCintoTray(tray1, cells.get(i), i).getCode());
//	//			Assert.assertTrue("电芯绑定托盘失败", res);
//	//		}
//	//
//	//		log.trace("获取高温静置时长：getStandbyDuration");
//	//
//	//		StandbyDurationResponse gaowen = getHighStandbyDuration(tray1);
//	//		res = isOk(gaowen.getCode());
//	//		Assert.assertTrue("获取高温静置时长失败", res);
//	//		double minTime = gaowen.getMinTime();
//	//		log.debug("高温时长是:{}", minTime);
//	//
//	//		log.trace("静置收数&过站：dataCollectForProcessLotEx");
//	//
//	//		res = isOk(hightDataCollectForProcessLotExCore(tray1).getCode());
//	//		Assert.assertTrue("静置收数&过站失败", res);
//	//
//	//		log.trace("获取化成配方：getProcessFilePathByTrayId");
//	//
//	//		ProcessFilePathResponse peifang = getProcessFilePathByTrayId(tray1, resource);
//	//		res = isOk(peifang.getCode());
//	//		Assert.assertTrue("获取化成配方失败", res);
//	//		log.debug("配方是:{}", peifang.getProcessFilePath());
//	//
//	//		log.trace("获取化成通道状态：getChannelStatus");
//	//
//	//		//检查24个通道结果
//	//		List<String> cells2 = new LinkedList<>();
//	//		for (int i = 0; i < 24; i++) {
//	//			res = isOk(getChannelStatus(resource, String.valueOf(i),0));
//	//			cells2.add(cells.get(i));
//	//		}
//	//		Assert.assertTrue("获取化成通道失败", res);
//	//
//	//		log.trace("化成开始：start");
//	//
//	//		//成功的通道进行化成
//	//		List<StartSfcResponse> kaishi = start(resource, cells2);
//	//		for (StartSfcResponse sfc : kaishi) {
//	//			log.debug("化成开始-芯片：{}", sfc.getSfcRef());
//	//		}
//	//
//	//		log.trace("解析文件&化成DC传数：postICResult");
//	//
//	//		for (int i = 0, j = cells.size(); i < j; i++) {
//	//			res = isOk(postICResult(resource, cells.get(i)));
//	//			log.debug("收取化成设备：{}芯片:{}", resource, cells.get(i));
//	//			Assert.assertTrue("化成传数失败", res);
//	//		}
//	//
//	//		log.trace("获取化成结果：getCellTestResultByTrayId");
//	//
//	//		CellTestResultResponse jieguo = getCellTestResultByTrayId(resource, tray1);
//	//		res = isOk(jieguo.getCode());
//	//		Assert.assertTrue("获取化成结果失败", res);
//	//		List<SfcResult> resultArray = jieguo.getResultArray();
//	//		if (resultArray != null) {
//	//			for (SfcResult result : resultArray) {
//	//				log.trace("化成结果-sfc: {} result: {}", result.getSfc(), result.getResult());
//	//			}
//	//		}
//	//
//	//		log.trace("化成结束：completeFormation");
//	//
//	//		res = isOk(completeFormation(resource, tray1));
//	//		Assert.assertTrue("化成结束失败", res);
//	//
//	//		log.trace("解绑整体电芯：releaseTrayId");
//	//
//	//		res = isOk(releaseTrayId(tray1));
//	//		Assert.assertTrue("解绑整体电芯", res);
//	//
//	//		log.debug("结束");
//	//
//	//		log.trace("--------异常库位更换：miChangeResourceAndProcessLot");
//	//		res = isOk(miChangeResourceAndProcessLot(resource, tray1, tray2).getCode());
//	//		Assert.assertTrue("异常库位更换失败", res);
//	//
//	//	}
//	//
//	//	/**
//	//	 * 测试阶段
//	//	 */
//	//	@Test
//	//	public void testLine() throws Exception {
//	//		boolean res = false;
//	//		String tray1 = trayList.get(0);
//	//		String cell1 = cellList.get(0);
//	//		String cell2 = cellList.get(1);
//	//		List<String> cells = new LinkedList<>();
//	//		cells.add(cell1);
//	//		cells.add(cell2);
//	//		log.trace("开始");
//	//		log.trace("托盘校验：miCheckProcessLot");
//	//
//	//		log.debug("校验托盘:{}", tray1);
//	//		res = isOk(miCheckProcessLot(tray1).getCode());
//	//		Assert.assertTrue("托盘校验失败", res);
//	//
//	//		log.trace("电芯校验：miCheckSFCStatusEx");
//	//
//	//		for (int i = 0, j = cells.size(); i < j; i++) {
//	//			log.debug("校验电芯:{}", cells.get(i));
//	//			res = isOk(miCheckSFCStatusEx(cells.get(i), CatlWebServiceConfig.getOpertionHighTemp()).getCode());
//	//			Assert.assertTrue("电芯校验失败", res);
//	//		}
//	//
//	//		log.trace("电芯绑定托盘：miBindSFCintoTray");
//	//
//	//		for (int i = 0, j = cells.size(); i < j; i++) {
//	//			log.debug("绑定-托盘:{},电芯:{},位置:{}", tray1, cells.get(i), i);
//	//			res = isOk(miBindSFCintoTray(tray1, cells.get(i), i).getCode());
//	//			Assert.assertTrue("电芯绑定托盘失败", res);
//	//		}
//	//
//	//		log.trace("获取常温1静置时长：getStandbyDuration");
//	//
//	//		StandbyDurationResponse normal1 = getNormal1StandbyDuration(tray1);
//	//		res = isOk(normal1.getCode());
//	//		Assert.assertTrue("获取常温1静置时长失败", res);
//	//		double minTime = normal1.getMinTime();
//	//		log.debug("常温1时长是:{}", minTime);
//	//
//	//		log.trace("静置收数&过站：dataCollectForProcessLotEx");
//	//
//	//		res = isOk(normal1DataCollectForProcessLotExCore(tray1).getCode());
//	//		Assert.assertTrue("常温1收数&过站失败", res);
//	//
//	//		log.trace("OCV1测试传数：miDataCollectForProcessLotForEach");
//	//
//	//		DataCollectForProcessLotForEachResponse miDataCollectForProcessLotForEach = miDataCollectForProcessLotForEach("OCVT1006", tray1, cells);
//	//		res = isOk(miDataCollectForProcessLotForEach);
//	//		Assert.assertTrue("OCV1测试传数失败", res);
//	//		List<DataCollectResultArrayData> resultArray = miDataCollectForProcessLotForEach.getResultArray();
//	//		if (resultArray != null) {
//	//			for (DataCollectResultArrayData result : resultArray) {
//	//				log.debug("OCV1测试传数 - sfc: {} result: {}", result.getSfc(), result.getResultCode());
//	//			}
//	//		}
//	//
//	//		log.trace("获取常温2静置时长：getStandbyDuration");
//	//
//	//		StandbyDurationResponse normal2 = getNormal2StandbyDuration(tray1);
//	//		res = isOk(normal2.getCode());
//	//		Assert.assertTrue("获取常温2静置时长失败", res);
//	//		double minTime2 = normal1.getMinTime();
//	//		log.debug("常温2时长是:{}", minTime2);
//	//
//	//		log.trace("静置收数&过站：dataCollectForProcessLotEx");
//	//
//	//		res = isOk(normal2DataCollectForProcessLotExCore(tray1).getCode());
//	//		Assert.assertTrue("常温2收数&过站失败", res);
//	//
//	//		log.trace("OCVB测试传数：miDataCollectForProcessLotForEach");
//	//
//	//		DataCollectForProcessLotForEachResponse miDataCollectForProcessLotForEach2 = miDataCollectForProcessLotForEach("OCVT1006", tray1, cells);
//	//		res = isOk(miDataCollectForProcessLotForEach2.getCode());
//	//		Assert.assertTrue("OCVB测试传数失败", res);
//	//		List<DataCollectResultArrayData> resultArray2 = miDataCollectForProcessLotForEach2.getResultArray();
//	//		if (resultArray2 != null) {
//	//			for (DataCollectResultArrayData result : resultArray2) {
//	//				log.debug("OCVB测试传数 - sfc: {} result: {}", result.getSfc(), result.getResultCode());
//	//			}
//	//		}
//	//
//	//		log.trace("获取电芯测试结果：ctiGetCurrentProcessLotResult");
//	//
//	//		GetCurrentProcessLotResultResponse jieguo = ctiGetCurrentProcessLotResult(tray1);
//	//		res = isOk(jieguo.getCode());
//	//		Assert.assertTrue("获取电芯测试结果失败", res);
//	//		List<CustomSfcArrayData> sfcArray = jieguo.getSfcArray();
//	//		if (sfcArray != null) {
//	//			for (CustomSfcArrayData data : sfcArray) {
//	//				log.debug("芯片：{},code:{},operator{}", data.getCode(), data.getSfc(), data.getOperation());
//	//			}
//	//		}
//	//
//	//		log.trace("解绑整体电芯：releaseTrayId");
//	//
//	//		res = isOk(releaseTrayId(tray1));
//	//		Assert.assertTrue("解绑整体电芯", res);
//	//
//	//		log.trace("NG品跳转工艺路线：miStepToOtherOperation");
//	//
//	//		res = isOk(miStepToOtherOperation("OCVTPUBLIC", cell1));
//	//		Assert.assertTrue("NG品跳转工艺路线失败", res);
//	//
//	//		log.trace("结束");
//	//	}
//
//	/**
//	 * 根据托盘获取电芯信息
//	 * 这个未给测试用例
//	 * 测试:无托盘信息则返回空数组
//	 */
//	@Test
//	public void getCellListByTrayId() {
//		CellListRequest request = new CellListRequest();
//		request.setProcessLot(trayId);
//		request.setSite(CatlWebServiceConfig.getSite());
//		request.setMode(com.atlmes.ws.celltest.ModeTrayMatrix.fromValue(CatlWebServiceConfig.getTrayMode()));
//		CellListResponse response = cellTestIntegrationService.getCellListByTrayId(request);
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//		List<String> resultArray = response.getSfcList();
//		if (resultArray != null) {
//			System.out.println(resultArray.size());
//			int i = 0;
//			for (String result : resultArray) {
//				log.trace("{} sfc result: {}", i + 1, result);
//				i++;
//			}
//		}
//	}
//
//	@Test
//	public void demo() {
//		System.out.println("....");
//	}
//
//	/**
//	 * 模块类型测试
//	 * 测试转换工具是否有误
//	 * 论字符串转换为枚举
//	 * 模块设置那边也需要考虑要校验完毕才允许设置
//	 */
//	@Test
//	public void demoModel() throws Exception {
//		try {
//			String model = null;
//			Object type = null;
//
//			//
//			model = CatlWebServiceConfig.getTrayMode();
//			log.trace(model);
//			//		com.atlmes.ws.celltest.ModeTrayMatrix type = TypeUtl.parse(model, com.atlmes.ws.celltest.ModeTrayMatrix.class);
//			type = com.atlmes.ws.celltest.ModeTrayMatrix.fromValue(model);
//			log.trace("{}", type);
//			type = com.atlmes.ws.machine.ModeTrayMatrix.fromValue(model);
//			log.trace("{}", type);
//			type = com.atlmes.ws.machineintegration.ModeTrayMatrix.fromValue(model);
//			log.trace("{}", type);
//
//			//其他model
//			model = CatlWebServiceConfig.getFormationModeProcessSfc();
//			type = com.atlmes.ws.machine.ModeProcessSfc.fromValue(model);
//			log.trace("{}", type);
//			type = com.atlmes.ws.machineintegration.ModeProcessSfc.fromValue(model);
//			log.trace("{}", type);
//		} catch (Exception e) {
//			Assert.fail("类型转换异常");
//		}
//	}
//
//	/**
//	 * 0表示ok
//	 * 1表示fail
//	 */
//	private boolean isOk(Object code) {
//		if (code != null && code.toString().equals("0")) {
//			return true;
//		}
//		return false;
//	}
//	
//	static{
//		CatlWebServiceConfig.setServerPath("http://127.0.0.1:8082");
//		CatlWebServiceConfig.setUsername("60024251");
//		CatlWebServiceConfig.setPassword("Wh123456");
//	}
//	/**
//	 * 托盘校验
//	 */
//	@Test
//	public void miCheckProcessLot() throws Exception {
//		CheckProcessLotResponse response = CatlWebServiceFunction.miCheckProcessLot(trayId);
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//		//		Assert.assertEquals("0", response.getCode());
//	}
//
//	/**
//	 * 对所有的托盘进行校验
//	 */
//	@Test
//	public void miCheckProcessLotAll() throws Exception {
//		for (String tray : trayList) {
//			CheckProcessLotResponse response = CatlWebServiceFunction.miCheckProcessLot(tray);
//			log.trace("{}", response.getCode());
//			log.trace("{}", response.getMessage());
//		}
//	}
//
//	/**
//	 * 电芯校验
//	 * 
//	 */
//	@Test
//	public void miCheckSFCStatusEx() throws Exception {
//		MiCommonResponse response = CatlWebServiceFunction.miCheckSFCStatusEx(cellList.get(0), CatlWebServiceConfig.getHighTempOpertion());
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//		Assert.assertEquals("0", response.getCode());
//	}
//
//	/**
//	 * 对所有的电芯进行校验
//	 */
//	@Test
//	public void miCheckSFCStatusExAll() throws Exception {
//		int i = 0;
//		for (String cell : cellList) {
//			MiCommonResponse response = CatlWebServiceFunction.miCheckSFCStatusEx(CatlWebServiceConfig.getNormalTempOpertion(), cell);
//			log.trace("index:{} - {}", i, response.getCode());
//			log.trace("{}", response.getCode());
//			log.trace("{}", response.getMessage());
//			i++;
//		}
//	}
//
//	/**
//	 * 电芯绑定托盘
//	 * 
//	 * 0~23 超过23会异常
//	 */
//	@Test
//	public void miBindSFCintoTray() throws Exception {
//		MiCommonResponse response = CatlWebServiceFunction.miBindSFCintoTray(trayId, cellList.get(cellList.size() - 1), cellList.size() - 1);
//
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//
//		//		Assert.assertTrue(response.getCode() == 0);
//	}
//
//	/**
//	 * 多个电芯绑定托盘
//	 */
//	@Test
//	public void miBindSFCintoTrayAll() throws Exception {
//		for (int i = 0; i < cellList.size(); i++) {
//			MiCommonResponse response = CatlWebServiceFunction.miBindSFCintoTray(trayId, cellList.get(i), i);
//			log.trace("绑定通道：" + i);
//			log.trace("{}", response.getCode());
//			log.trace("{}", response.getMessage());
//		}
//	}
//
//	/**
//	 * 获取静置时长
//	 */
//	@Test
//	public void getStandbyDuration() throws Exception {
//
//		//		StandbyDurationResponse response = getHighStandbyDuration(trayId);
//		//		StandbyDurationResponse response = getNormal1StandbyDuration(trayId);
//		StandbyDurationResponse response = getNormal2StandbyDuration(trayId);
//
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//		log.trace("getMinTime {}", response.getMinTime());
//		log.trace("getMinTime {}", formatDouble(response.getMinTime()));
//		log.trace("maxTime: {}", response.getMaxTime());//小于max time 都是允许的
//		log.trace("expireTime: {}", response.getExpirTime());//
//		log.trace("scrapTime: {}", response.getScrapTime());
//
//		//		Assert.assertTrue(response.getCode() == 0);
//		//		Assert.assertTrue(response.getMinTime() == 360);
//
//	}
//
//	private static String formatDouble(double d) {
//		DecimalFormat df = new DecimalFormat("0.0000");
//		return df.format(d);
//	}
//
//	/**
//	 * 获取高温静置时长，只需要关心返回值中的 Min Time 即可。
//	 */
//	private StandbyDurationResponse getHighStandbyDuration(String tray) throws Exception {
//		return CatlWebServiceFunction.getStandbyDuration(CatlWebServiceConfig.getHighTempOpertion(), tray, "HTSX1002");
//	}
//
//	/**
//	 * 常温1时长
//	 */
//	private StandbyDurationResponse getNormal1StandbyDuration(String tray) throws Exception {
//		return CatlWebServiceFunction.getStandbyDuration(CatlWebServiceConfig.getNormalTempOpertion(), tray, "RTSX1002");
//	}
//
//	/**
//	 * 常温2时长
//	 */
//	private StandbyDurationResponse getNormal2StandbyDuration(String tray) throws Exception {
//		return CatlWebServiceFunction.getStandbyDuration(CatlWebServiceConfig.getNormalTempNextOpertion(), tray, "RTSX1002");
//	}
//
//	/**
//	 * 静置收数&过站
//	 */
//	@Test
//	public void dataCollectForProcessLotEx() throws Exception {
//
//		ProcessLotDcResponseEx response = hightDataCollectForProcessLotExCore(trayId);
//
//		//		ProcessLotDcResponseEx response = normal1DataCollectForProcessLotExCore(trayId);
////		ProcessLotDcResponseEx response = normal2DataCollectForProcessLotExCore(trayId);
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//
//		//		Assert.assertTrue(response.getCode() == 0);
//	}
//
//	private ProcessLotDcResponseEx hightDataCollectForProcessLotExCore(String tray) throws Exception {
//		return dataCollectForProcessLotExCore(CatlWebServiceConfig.getHighTempOpertion(), tray, "HTSX1002");
//	}
//
//	private ProcessLotDcResponseEx normal1DataCollectForProcessLotExCore(String tray) throws Exception {
//		return dataCollectForProcessLotExCore(CatlWebServiceConfig.getNormalTempOpertion(), tray, "RTSX1002");
//	}
//
//	private ProcessLotDcResponseEx normal2DataCollectForProcessLotExCore(String tray) throws Exception {
//		return dataCollectForProcessLotExCore(CatlWebServiceConfig.getNormalTempNextOpertion(), tray, "RTSX1002");
//	}
//
//	/**
//	 * 静置收数&过站
//	 * 静止时长的计算由MES控制，在这里需要接受到MES的成功信息后才能够取出托盘。
//	 */
//	private ProcessLotDcResponseEx dataCollectForProcessLotExCore(String operation, String tray, String resource) throws Exception {
//		List<MachineIntegrationParametricData> list = new LinkedList<>();
//		//参数具体的时候再确定
//		MachineIntegrationParametricData parametricData = new MachineIntegrationParametricData();
//		parametricData.setName("STADB1PSITN");//库位号
//		parametricData.setValue("1");
//		parametricData.setDataType(ParameterDataType.TEXT);
//		list.add(parametricData);
//		parametricData = new MachineIntegrationParametricData();
//		parametricData.setName("STADB1PSITN");//托盘号
//		parametricData.setValue(trayId);
//		parametricData.setDataType(ParameterDataType.TEXT);
//		list.add(parametricData);
//		parametricData = new MachineIntegrationParametricData();
//		parametricData.setName("STADCAPTIM1");//静置时间
//		parametricData.setValue(String.valueOf(5));
//		parametricData.setDataType(ParameterDataType.NUMBER);
//		list.add(parametricData);
//		parametricData = new MachineIntegrationParametricData();
//		parametricData.setName("STADB1TRAYNO");//静置时间
//		parametricData.setValue(String.valueOf(5));
//		parametricData.setDataType(ParameterDataType.NUMBER);
//		list.add(parametricData);
//		return CatlWebServiceFunction.dataCollectForProcessLotEx(operation, tray, resource,//
//				CatlWebServiceConfig.getFormationUser(), CatlWebServiceConfig.getFormationModeProcessSfc(),//
//				list);
//	}
//
//	/**
//	 * 获取化成配方
//	 */
//	@Test
//	public void __getProcessFilePathByTrayId() throws Exception {
//
//		ProcessFilePathResponse response = ICWebServiceFunction.getProcessFilePathByTrayId(CatlWebServiceConfig.getFormationOpertion(), trayId, resourceList.get(0), ModeProcessFilePath.NORMAL);
//
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//		log.trace("{}", response.getProcessFilePath());
//	}
//
//	/**
//	 * 获取化成通道状态
//	 * 
//	 * 层数是从1开始的
//	 * 
//	 * ok的通道是不会有返回值的
//	 * 返回的通道好也是从1开始的
//	 */
//	@Test
//	public void __getChannelStatus() throws Exception {
//		ChannelStatusResponse response = ICWebServiceFunction.getChannelStatus(resourceList.get(0), "1");
//		log.trace("{}", response.getReturnCode());
//		log.trace("{}", response.getMessage());
//		List<ChannelStatus> list = response.getChannelStatusList();
//		if (list != null) {
//			log.trace("size: {}", list.size());
//			for (ChannelStatus channel : list) {
//				log.trace("channel:{},status:{}", channel.getChannel(), channel.getStatus());
//			}
//		}
//	}
//
//	/**
//	 * 获取所有通道状态
//	 */
//	@Test
//	public void __getChannelStatusAll() throws Exception {
//		for (int i = 0; i < 5; i++) {
//			ChannelStatusResponse response = ICWebServiceFunction.getChannelStatus(resourceList.get(0), String.valueOf(i));
//			log.trace("{}", response.getReturnCode());
//			log.trace("{}", response.getMessage());
//			List<ChannelStatus> list = response.getChannelStatusList();
//			if (list != null) {
//				log.trace("{}", list.size());
//				for (ChannelStatus channel : list) {
//					log.trace("channel:{},status:{}", channel.getChannel(), channel.getStatus());
//				}
//			}
//		}
//	}
//
//	/*
//	 * 化成
//	 * 如果有异常那么化成的整个托盘是不工作的
//	 */
//	@Test
//	public void __start() throws Exception {
//		try {
//			List<String> cell = new LinkedList<>();
//			cell.add(cellList.get(0));
//			cell.add(cellList.get(1));
//			cell.add(cellList.get(2));
//			cell.add(cellList.get(3));
//			cell.add(cellList.get(4));
//			cell.add(cellList.get(5));
//			List<StartSfcResponse> responses = __start(resourceList.get(0), cellList);
//			log.trace("{}", responses.size());
//			for (StartSfcResponse response : responses) {
//				System.out.println(response.getItemRef());
//				System.out.println(response.getSfcRef());
//				System.out.println(response.getRouterRef());
//				System.out.println(response.getStepId());
//			}
//		} catch (Exception e) {
//			if (e instanceof StartFault) {
//				System.out.println("测试失败" + e.getMessage());
//			}
//			System.out.println(e.getClass());
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 化成
//	 */
//	private List<StartSfcResponse> __start(String resource, List<String> cell) throws Exception {
//		return ICWebServiceFunction.start(CatlWebServiceConfig.getFormationOpertion(),resource, cell, "SUP_N2_FORMN3");
//	}
//
//	/**
//	 * 化成
//	 */
//	private StartSfcResponse __startSfc(String resource, String cell) throws Exception {
//		return ICWebServiceFunction.startSfc(CatlWebServiceConfig.getFormationOpertion(),resource, cell, "SUP_N2_FORMN3");
//	}
//
//	/*
//	 * 异常库位更换
//	 * 
//	 * 1.资源号是否变更为新的资源好
//	 * 2.托盘号2个是一样
//	 * 3.目前换过库位后状态不变依然是活动的
//	 * 4.问题:只要托盘中存在状态不是活动状态的，则无法变更库位
//	 */
//	@Test
//	public void __miChangeResourceAndProcessLot() throws Exception {
//		MiCommonResponse response = ICWebServiceFunction.miChangeResourceAndProcessLot(CatlWebServiceConfig.getFormationOpertion(),resourceList.get(0), trayId, trayId);
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}
//
//	/*
//	 * 化成DC传数
//	 * 
//	 */
//	@Test
//	public void postICResult() throws Exception {
//		IcResultResponse response = __postICResult(resourceList.get(0), cellList.get(0));
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}
//
//	/*
//	 * 所有的都化成传数
//	 */
//	@Test
//	public void __postICResultAll() throws Exception {
//		for (int i = 3; i < cellList.size(); i++) {
//			IcResultResponse response = __postICResult(resourceList.get(0), cellList.get(i));
//			log.trace("{}", response.getCode());
//			log.trace("{}", response.getMessage());
//		}
//	}
//
//	/**
//	 * 化成DC传数
//	 * 
//	 * 可以重复传
//	 * setControlSystemID再议
//	 * setChannelID再议
//	 */
//	private IcResultResponse __postICResult(String resource, String cell) throws Exception {
//		//		request.setUser("SUP_N2_FORMN3");//
//		//		request.setControlSystemID("01");//
//		//		request.setChannelID("01");//通道号
//		//		//只能用不足1位补0
//		//		//		request.setControlSystemID(String.valueOf(3));//
//		//		//		request.setChannelID(String.valueOf(1));//通道号
//		List<com.atlmes.ws.celltest.MachineIntegrationParametricData> list = new LinkedList<>();
//		com.atlmes.ws.celltest.MachineIntegrationParametricData data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
//		data.setName("ICLCC");
//		data.setValue("35500");
//		data.setDataType(ParameterDataType.NUMBER);
//		list.add(data);
//		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
//		data.setName("ICC0");
//		data.setValue("35500");
//		data.setDataType(ParameterDataType.NUMBER);
//		list.add(data);
//		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
//		data.setName("BACKUPPATH");
//		data.setValue("localtion");
//		data.setDataType(ParameterDataType.TEXT);
//		list.add(data);
//		data = new com.atlmes.ws.celltest.MachineIntegrationParametricData();
//		data.setName("ICLV");
//		data.setValue("100");
//		data.setDataType(ParameterDataType.NUMBER);
//		list.add(data);
//		IcResultResponse response = ICWebServiceFunction.postICResult(CatlWebServiceConfig.getFormationOpertion(),resource, cell, "SUP_N2_FORMN3", "01", "01", list);
//		return response;
//	}
//
//	/*
//	 * 获取化成结果
//	 */
//	@Test
//	public void getCellTestResultByTrayId() throws Exception {
//		CellTestResultResponse response = CatlWebServiceFunction.getCellTestResultByTrayId(resourceList.get(0), trayId);
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//		List<SfcResult> resultArray = response.getResultArray();
//		if (resultArray != null) {
//			for (SfcResult result : resultArray) {
//				log.trace("sfc: {} result: {}", result.getSfc(), result.getResult());
//			}
//		}
//	}
//
//	/*
//	 * 结束化成
//	 * 
//	 * 
//	 */
//	@Test
//	public void completeFormation() throws Exception {
//		FormationCompleteResponse response = ICWebServiceFunction.completeFormation(CatlWebServiceConfig.getFormationOpertion(), resourceList.get(0), trayId);
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}
//
//	/*
//	 * 解绑整体电芯
//	 */
//	@Test
//	public void releaseTrayId() throws Exception {
//		ProcessLotReleaseResponse response = CatlWebServiceFunction.releaseTrayId(trayId);
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}
//
//	@Test
//	public void releaseCell() throws Exception {
//		SfcRemovalResponse response = CatlWebServiceFunction.releaseCell(trayId, cellList.get(3));
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}
//
//	/*
//	 * 以下测试段独有的
//	 * TODO 9 测试段
//	 */
//
//	/*
//	 * OCV1测试传数
//	 */
//	@Test
//	public void __miDataCollectForProcessLotForEach() throws Exception {
//		List<String> cells = new LinkedList<>();
//		cells.add(cellList.get(0));
//		DataCollectForProcessLotForEachResponse response = __miDataCollectForProcessLotForEach(CatlWebServiceConfig.getOvcOpertion(), "OCVT1006", trayList.get(0), cells);
//		List<DataCollectResultArrayData> resultArray = response.getResultArray();
//		if (resultArray != null) {
//			for (DataCollectResultArrayData result : resultArray) {
//				log.trace("sfc: {} result: {} message: {}", result.getSfc(), result.getResultCode(), result.getErrorMessage());
//			}
//		}
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}
//
//	@Test
//	public void __miDataCollectForProcessLotForEachAll() throws Exception {
//		List<String> cells = new LinkedList<>();
//		//		cells.add(cellList.get(0));
//		cells = cellList;
//		DataCollectForProcessLotForEachResponse response = __miDataCollectForProcessLotForEach(CatlWebServiceConfig.getOvcNextOpertion(), "OCVT1006", trayId, cells);
//		List<DataCollectResultArrayData> resultArray = response.getResultArray();
//		if (resultArray != null) {
//			for (DataCollectResultArrayData result : resultArray) {
//				log.trace("sfc: {} result: {} message: {}", result.getSfc(), result.getResultCode(), result.getErrorMessage());
//			}
//		}
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}
//
//	/**
//	 * OCV1测试传数
//	 */
//	private DataCollectForProcessLotForEachResponse __miDataCollectForProcessLotForEach(String operation, String resource, String tray, List<String> cells) throws Exception {
//
//		//		request.setUser("SUP_N2_FORMN3");
//		//		request.setIsDispositionRequired(true);//固定的
//		//		request.setModeProcessSfc(com.atlmes.ws.machineintegration.ModeProcessSfc.MODE_PASS_SFC_POST_DC);//静置ocv这个，化成不是
//
//		List<DataCollectSfcParametricData> sfcArray = new LinkedList<>();
//
//		int i = 0;
//		for (String cell : cells) {
//			List<com.atlmes.ws.machineintegration.MachineIntegrationParametricData> list1_1 = new LinkedList<>();
//			if (i == 2) {
//				list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("OCV", "3.5", ParameterDataType.NUMBER));
//			} else {
//				list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("OCV", "3.8", ParameterDataType.NUMBER));
//			}
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("OCV", "1", ParameterDataType.NUMBER));
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("IMP", "0.6", ParameterDataType.NUMBER));
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("TEM", "23", ParameterDataType.NUMBER));
//			//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("CHANNELID", "13", ParameterDataType.TEXT));
//			//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("POSISURFVOT", "13", ParameterDataType.TEXT));
//			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("TIMEAFIC", "23", ParameterDataType.NUMBER));
//
//			//			List<NonConfirmCodeArray> list1_2 = new LinkedList<>();
//			//			list1_2.add(new NonConfirmCodeArray("NC450010", false));
//
//			DataCollectSfcParametricData dataCollectSfcParametricData = new DataCollectSfcParametricData(cell, CatlWebServiceConfig.getDcGroup(), CatlWebServiceConfig.getDcGroupVersion(), list1_1,
//					null);
//			sfcArray.add(dataCollectSfcParametricData);
//			i++;
//		}
//		DataCollectForProcessLotForEachResponse response = ICWebServiceFunction.miDataCollectForProcessLotForEach(operation, resource, tray, "SUP_N2_FORMN3", sfcArray);
//		return response;
//	}
//
//	/*
//	 * 获取电芯测试结果
//	 */
//	@Test
//	public void ctiGetCurrentProcessLotResult() throws Exception {
//		GetCurrentProcessLotResultResponse response = CatlWebServiceFunction.ctiGetCurrentProcessLotResult(CatlWebServiceConfig.getCapsuleOpertion(), trayId, "SUP_N2_FORMN3");
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//		List<CustomSfcArrayData> sfcArray = response.getSfcArray();
//		if (sfcArray != null) {
//			for (CustomSfcArrayData data : sfcArray) {
//				log.trace("{}", data.getCode());
//				log.trace("{}", data.getSfc());
//				log.trace("{}", data.getOperation());
//			}
//		}
//	}
//
//	/*
//	 * NG品跳转工艺路线
//	 */
//	@Test
//	public void miStepToOtherOperation() throws Exception {
//		MiCommonResponse response = CatlWebServiceFunction.miStepToOtherOperation(CatlWebServiceConfig.getOvcNextOpertion(),//
//				"OCVT1006", cellList.get(1));
//		log.trace("{}", response.getCode());
//		log.trace("{}", response.getMessage());
//	}

}
