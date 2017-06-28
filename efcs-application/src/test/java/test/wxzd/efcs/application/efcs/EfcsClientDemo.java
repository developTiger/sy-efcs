package test.wxzd.efcs.application.efcs;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import org.junit.Test;

import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFinishedFeedbackEvent;
import com.wxzd.protocol.wcs.domain.enums.ExecuteStatus;
import com.wxzd.protocol.wcs.transport.feedback.CommandFeedbackEvent;
import com.wxzd.wcs.dispatcher.client.http.EfcsClient;
import com.wxzd.wcs.dispatcher.client.http.EfcsClientConfig;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-05-10
 */
public class EfcsClientDemo {

	/**
	 * @see procedureAppServiceDemo
	 */
	@Test
	public void 指令完成() throws Exception {
		EfcsClientConfig.setUrl("http://10.4.37.68:4660/efcs");
		CommandFeedbackEvent event = new CommandFeedbackEvent();
		event.setHouse_no("29");
		event.setPallet_no("L29B0303");
		event.setCom_status(ExecuteStatus.Finished);
		event.setCom_no("1251");
		event.setDevice_no(DispatcherConfig.test_stocker_2);
		event.setLocation("LB033207");
		EfcsClient.command(event);
	}

	/**
	 * @see procedureAppServiceDemo
	 */
	@Test
	public void 指令完成2() throws Exception {
		EfcsClientConfig.setUrl("http://10.4.37.68/efcs");
		CommandFeedbackEvent event = new CommandFeedbackEvent();
		event.setHouse_no("29");
		event.setPallet_no("L26B0322");
		event.setCom_status(ExecuteStatus.Finished);
		event.setCom_no("632");
		event.setDevice_no("LB030106");
		event.setLocation("LB030106");
		EfcsClient.command(event);
	}

	@Test
	public void grab_finish() throws Exception {
		try {
			EfcsClientConfig.setUrl("http://10.4.37.68:4660/efcs");
			BatteryGrabFinishedFeedbackEvent request = new BatteryGrabFinishedFeedbackEvent();
			request.setHouse_no("29");
			request.setDevice_no("cwfjjxs");
			request.setPallet_no("L29B0302");
			EfcsClient.grab_finished(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
