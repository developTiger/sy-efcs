package com.wxzd.efcs.business.webservice;

import java.util.LinkedList;
import java.util.List;

import com.atlmes.ws.celltestintegration.GetCurrentProcessLotResultResponse;
import com.atlmes.ws.machine.MachineIntegrationParametricData;
import com.atlmes.ws.machine.ProcessLotDcResponseEx;
import com.atlmes.ws.machineintegration.DataCollectForProcessLotForEachResponse;
import com.atlmes.ws.machineintegration.DataCollectSfcParametricData;
import com.atlmes.ws.machineintegration.MiCommonResponse;
import com.sap.me.datacollection.ParameterDataType;
import com.wxzd.catl.CatlWebServiceFunction;
import com.wxzd.catl.base.CatlWebServiceConfig;

/**
 * 对mes提供的接口的一个再封装
 * 
 * @version 1
 * @author y
 * @.create 2017-05-02
 */
public class MESAppWebservice {

	/**
	 * 高温静置收数
	 * 
	 * @param house_no
	 * @param tray
	 * @param time
	 *        单位是秒
	 * @throws Exception
	 */
	public static ProcessLotDcResponseEx hightDataCollectForProcessLotExCore(String house_no, String tray, int second) throws Exception {
		int min = second / 60;//转成分钟
		List<MachineIntegrationParametricData> params = new LinkedList<>();
		MachineIntegrationParametricData parametricData = new MachineIntegrationParametricData();
		parametricData.setName("HSTDBYSTATION");//库位号
		parametricData.setValue(house_no);
		parametricData.setDataType(ParameterDataType.TEXT);
		params.add(parametricData);
		parametricData = new MachineIntegrationParametricData();
		parametricData.setName("HSTDBYTRAYID");//托盘号
		parametricData.setValue(tray);
		parametricData.setDataType(ParameterDataType.TEXT);
		params.add(parametricData);
		parametricData = new MachineIntegrationParametricData();
		parametricData.setName("HSTDBYTIME");//静置时间
		parametricData.setValue(String.valueOf(min));
		parametricData.setDataType(ParameterDataType.NUMBER);
		params.add(parametricData);
		return CatlWebServiceFunction.highDataCollectForProcessLotEx(CatlWebServiceConfig.getHighTempOpertion(),//
				tray, CatlWebServiceConfig.getHighTempResourceMap().get(house_no), CatlWebServiceConfig.getHighTempUserMap().get(house_no), CatlWebServiceConfig.getHighTempModeProcessSfc(), params);
	}

	//	/**
	//	 * 常温2静置收数
	//	 * 
	//	 * @param house_no
	//	 * @param tray
	//	 * @param time
	//	 *        单位是秒
	//	 * @throws Exception
	//	 */
	//	public static ProcessLotDcResponseEx normalNextDataCollectForProcessLotExCore(String house_no, String tray, int second) throws Exception {
	//		int min = second / 60;//转成分钟
	//		List<MachineIntegrationParametricData> params = new LinkedList<>();
	//		MachineIntegrationParametricData parametricData = new MachineIntegrationParametricData();
	//		parametricData.setName("STADB2STATION");//库位号
	//		parametricData.setValue(house_no);
	//		parametricData.setDataType(ParameterDataType.TEXT);
	//		params.add(parametricData);
	//		parametricData = new MachineIntegrationParametricData();
	//		parametricData.setName("STADB2TRAYID");//托盘号
	//		parametricData.setValue(tray);
	//		parametricData.setDataType(ParameterDataType.TEXT);
	//		params.add(parametricData);
	//		parametricData = new MachineIntegrationParametricData();
	//		parametricData.setName("STADB2TIME");//静置时间
	//		parametricData.setValue(String.valueOf(min));
	//		parametricData.setDataType(ParameterDataType.NUMBER);
	//		params.add(parametricData);
	//		return CatlWebServiceFunction.normalNextDataCollectForProcessLotEx(CatlWebServiceConfig.getNormalTempNextOpertion(),//
	//				tray, CatlWebServiceConfig.getNormalTempNextResource(), CatlWebServiceConfig.getNormalTempNextUser(), CatlWebServiceConfig.getNormalTempNextModeProcessSfc(), params);
	//	}

	/**
	 * 常温静置收数
	 * 
	 * @param house_no
	 * @param tray
	 * @param time
	 *        单位是秒
	 * @throws Exception
	 */
	//	public static ProcessLotDcResponseEx normalDataCollectForProcessLotExCore(String house_no, String tray, int second) throws Exception {
	//		int min = second / 60;//转成分钟
	//		List<MachineIntegrationParametricData> params = new LinkedList<>();
	//		MachineIntegrationParametricData parametricData = new MachineIntegrationParametricData();
	//		parametricData.setName("STADB1STATION");//库位号
	//		parametricData.setValue(house_no);
	//		parametricData.setDataType(ParameterDataType.TEXT);
	//		params.add(parametricData);
	//		parametricData = new MachineIntegrationParametricData();
	//		parametricData.setName("STADB1TRAYID");//托盘号
	//		parametricData.setValue(tray);
	//		parametricData.setDataType(ParameterDataType.TEXT);
	//		params.add(parametricData);
	//		parametricData = new MachineIntegrationParametricData();
	//		parametricData.setName("STADB1TIME");//静置时间
	//		parametricData.setValue(String.valueOf(min));
	//		parametricData.setDataType(ParameterDataType.NUMBER);
	//		params.add(parametricData);
	//		return CatlWebServiceFunction.normalDataCollectForProcessLotEx(CatlWebServiceConfig.getNormalTempOpertion(),//
	//				tray, CatlWebServiceConfig.getNormalTempResource(), CatlWebServiceConfig.getNormalTempUser(), CatlWebServiceConfig.getNormalTempModeProcessSfc(), params);
	//	}

	public static DataCollectForProcessLotForEachResponse normalDataCollectForProcessLotExCore(String house_no, String tray, List<String> batterys, int second) throws Exception {
		int min = second / 60;//转成分钟
		List<DataCollectSfcParametricData> sfcArray = new LinkedList<>();
		for (String cell : batterys) {
			List<com.atlmes.ws.machineintegration.MachineIntegrationParametricData> list1_1 = new LinkedList<>();
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1STATION", house_no, ParameterDataType.TEXT));
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1TRAYID", tray, ParameterDataType.TEXT));
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1TIME", String.valueOf(min), ParameterDataType.NUMBER));
			DataCollectSfcParametricData dataCollectSfcParametricData = new DataCollectSfcParametricData(cell, CatlWebServiceConfig.getDcGroup(), CatlWebServiceConfig.getDcGroupVersion(), list1_1,
					new LinkedList<com.atlmes.ws.machineintegration.NonConfirmCodeArray>());
			sfcArray.add(dataCollectSfcParametricData);
		}
		//user会抽出来，根据不同的拉线号来去用户
		return CatlWebServiceFunction.normal_miDataCollectForProcessLotForEach(CatlWebServiceConfig.getNormalTempOpertion(),//
				CatlWebServiceConfig.getNormalTempResourceMap().get(house_no),//
				tray, CatlWebServiceConfig.getNormalTempUserMap().get(house_no), sfcArray);
	}

	public static DataCollectForProcessLotForEachResponse normalNextDataCollectForProcessLotExCore(String house_no, String tray, List<String> batterys, int second) throws Exception {
		int min = second / 60;//转成分钟
		List<DataCollectSfcParametricData> sfcArray = new LinkedList<>();
		for (String cell : batterys) {
			List<com.atlmes.ws.machineintegration.MachineIntegrationParametricData> list1_1 = new LinkedList<>();
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1STATION", house_no, ParameterDataType.TEXT));
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1TRAYID", tray, ParameterDataType.TEXT));
			list1_1.add(new com.atlmes.ws.machineintegration.MachineIntegrationParametricData("STADB1TIME", String.valueOf(min), ParameterDataType.NUMBER));
			DataCollectSfcParametricData dataCollectSfcParametricData = new DataCollectSfcParametricData(cell, CatlWebServiceConfig.getDcGroup(), CatlWebServiceConfig.getDcGroupVersion(), list1_1,
					new LinkedList<com.atlmes.ws.machineintegration.NonConfirmCodeArray>());
			sfcArray.add(dataCollectSfcParametricData);
		}
		return CatlWebServiceFunction.normal2_miDataCollectForProcessLotForEach(CatlWebServiceConfig.getNormalTempNextOpertion(),//
				CatlWebServiceConfig.getNormalTempResourceMap().get(house_no),//
				tray, CatlWebServiceConfig.getNormalTempUserMap().get(house_no), sfcArray);
	}

	/**
	 * 获取电芯测试结果
	 */
	public static GetCurrentProcessLotResultResponse ctiGetCurrentProcessLotResult(String house_no, String tray) throws Exception {
		return CatlWebServiceFunction.ctiGetCurrentProcessLotResult(CatlWebServiceConfig.getOcvNextNextOperation(), tray, CatlWebServiceConfig.getOcvNextNextUserMap().get(house_no));
	}

	/**
	 * NG品跳转工艺路线
	 */
	public static MiCommonResponse miStepToOtherOperation(String house_no, String battery,String operation) throws Exception {
		return CatlWebServiceFunction.miStepToOtherOperation(operation, CatlWebServiceConfig.getOcvNextNextResourceMap().get(house_no), battery);
	}

}
