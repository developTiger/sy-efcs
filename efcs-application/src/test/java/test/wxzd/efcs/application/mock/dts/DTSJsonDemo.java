package test.wxzd.efcs.application.mock.dts;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.wxzd.dispatcher.dts.feedback.request.TemperatureRequest;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.protocol.dts.TemperatureReport;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-21
 */
public class DTSJsonDemo {

	@Test
	public void name() throws Exception {
		TemperatureRequest request = new TemperatureRequest();
		request.setDeviceId("deviceId");
		request.setTimestamp(new Date());
		List<TemperatureReport> data = request.getData();
		TemperatureReport report = new TemperatureReport();
		report.setTemperature(1);
//		report.setZoneId("zongid");
		data.add(report);
		System.out.println(JsonUtl.parse(request));
	}

	@Test
	public void name2() throws Exception {
		String str = "{\"deviceId\":\"deviceId\",\"data\":[{\"zid\":1,\"zoneId\":\"zongid\",\"temperature\":1.0}],\"timestamp\":\"2017-04-21 10:56:35\"}";
		System.out.println(str);
		TemperatureRequest format = JsonUtl.format(str, TemperatureRequest.class);
		System.out.println(format);
	}
}
