package test.wxzd.efcs.application.mock.dts;

import java.util.Date;
import java.util.List;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.junit.Test;

import com.wxzd.dispatcher.dts.DTSConfig;
import com.wxzd.dispatcher.dts.feedback.request.AlarmsRequest;
import com.wxzd.dispatcher.dts.feedback.request.EventsRequest;
import com.wxzd.dispatcher.dts.feedback.request.RestRequest;
import com.wxzd.dispatcher.dts.feedback.request.TemperatureRequest;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.common.core.constant.CharsetCon;
import com.wxzd.protocol.dts.FireAlarm;
import com.wxzd.protocol.dts.TemperatureReport;
import com.wxzd.protocol.dts.feedback.EventType;

/**
 * 开启app
 * 
 * 模拟发送功能
 * 
 * @version 1
 * @author y
 * @.create 2017-04-20
 */
public class DTSClientDemo {

	//	String ip = "localhost";
	String ip = DTSConfig.getMq_ip();
	//	int port = 61613;
	int port = DTSConfig.getMq_port();

	@Test
	public void alarms() throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		AlarmsRequest request = new AlarmsRequest();
		request.setDeviceId("deviceId");
		request.setTimestamp(new Date());
		List<FireAlarm> alarms = request.getAlarms();
		FireAlarm alarm = new FireAlarm();
//		alarm.setLocation(10000);
		alarm.setTemperature(48.45f);
//		alarm.setZoneId("010101");
		alarms.add(alarm);
		try {
			connection.publish("alarms", JsonUtl.parse(request).getBytes(CharsetCon.utf8), QoS.AT_LEAST_ONCE, false);
		} finally {
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		System.out.println("任务完成");
	}

	@Test
	public void events() throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		EventsRequest request = new EventsRequest();
		request.setDeviceId("deviceId");
		request.setTimestamp(new Date());
		request.setChannelId(2);
		request.setEventType(EventType.fiber_too_long);
		try {
			connection.publish("events", JsonUtl.parse(request).getBytes(CharsetCon.utf8), QoS.AT_LEAST_ONCE, false);
		} finally {
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		System.out.println("任务完成");
	}

	@Test
	public void rest() throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		RestRequest request = new RestRequest();
		request.setDeviceId("deviceId");
		request.setTimestamp(new Date());
		try {
			connection.publish("rest", JsonUtl.parse(request).getBytes(CharsetCon.utf8), QoS.AT_LEAST_ONCE, false);
		} finally {
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		System.out.println("任务完成");
	}

	@Test
	public void temperature() throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		TemperatureRequest request = new TemperatureRequest();
		request.setDeviceId("deviceId");
		request.setTimestamp(new Date());
		List<TemperatureReport> data = request.getData();
		TemperatureReport report = new TemperatureReport();
		report.setTemperature(44.44);
//		report.setZoneId("010101");
		data.add(report);
		report = new TemperatureReport();
		report.setTemperature(44.55);
//		report.setZoneId("010102");
		data.add(report);
		try {
			connection.publish("temperature", JsonUtl.parse(request).getBytes(CharsetCon.utf8), QoS.AT_LEAST_ONCE, false);
		} finally {
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		System.out.println("任务完成");
	}

}
