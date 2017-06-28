package test.wxzd.efcs.application.wcs;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.protocol.wcs.battery.feedback.BatteryCheckFeedbackEvent;
import com.wxzd.wcs.dispatcher.client.http.EfcsClient;
import com.wxzd.wcs.dispatcher.client.http.EfcsClientConfig;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-01
 */
public class JunitDemo {

	@Before
	public void begin() {
		EfcsClientConfig.setUrl("http://dev.wxzd.com/efcs");
		EfcsClientConfig.setUrl("http://localhost:8080/efcs");
	}
	
	/**
	 * 电芯测试
	 */
	@Test
	public void checkBattery() {
		try {
			BatteryCheckFeedbackEvent request = new BatteryCheckFeedbackEvent();
			List<BatteryInContainer> batteries = request.getBatteries();
			BatteryInContainer battery = new BatteryInContainer();
			battery.setBattery_no("72B650587371");
			battery.setPos_no("0");
			batteries.add(battery);
			request.setHouse_no("29");
			request.setDevice_no(DispatcherConfig.formation_palletize_device_no);
			System.out.println(JsonUtl.parseWithoutException(request));
			BatteryCheckFeedbackEvent response = EfcsClient.check(request);
			System.out.println(JsonUtl.parseWithoutException(response));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 托盘到位
	 */
	@Test
	public void checkTray() {
//		try {
//			PalletArriveFeedbackEvent request = new PalletArriveFeedbackEvent();
//			request.setHouse_no("29");
//			request.setPallet_no("L26B0004");
//			System.out.println(JsonUtl.parseWithoutException(request));
//			PalletArriveFeedbackEvent response = EfcsClient.arrive(request);
//			System.out.println(JsonUtl.parseWithoutException(response));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
