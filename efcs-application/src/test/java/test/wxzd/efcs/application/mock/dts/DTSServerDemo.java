package test.wxzd.efcs.application.mock.dts;

import java.util.Arrays;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.junit.Test;

import com.wxzd.dispatcher.dts.DTSConfig;
import com.wxzd.dispatcher.dts.command.mqtt_impl.DTSClient;
import com.wxzd.protocol.dts.command.ConfigCommandEvent;

/**
 * 模拟服务，测试发送功能
 * 
 * @version 1
 * @author y
 * @.create 2017-04-20
 */
public class DTSServerDemo {

	//	String ip = "localhost";
	String ip = DTSConfig.getMq_ip();
	//	int port = 61613;
	int port = DTSConfig.getMq_port();

	@Test
	public void server_on() throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		Topic[] topics = { //
				new Topic("config", QoS.AT_LEAST_ONCE),//
		};
		byte[] qoses = connection.subscribe(topics);
		System.out.println(Arrays.toString(qoses));
		Message message = null;
		for (;;) {
			System.out.println("等待一个消息:...");
			message = connection.receive();
			//			System.out.println(message.getTopic());
			byte[] payload = message.getPayload();
			System.out.println(new String(payload));
			message.ack();
		}
	}
	
	@Test
	public void server_on_temperature() throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		Topic[] topics = { //
				new Topic("temperature", QoS.AT_LEAST_ONCE),//
		};
		byte[] qoses = connection.subscribe(topics);
		System.out.println(Arrays.toString(qoses));
		Message message = null;
		for (;;) {
			System.out.println("等待一个消息:...");
			message = connection.receive();
			//			System.out.println(message.getTopic());
			byte[] payload = message.getPayload();
			System.out.println(new String(payload));
			message.ack();
		}
	}

	@Test
	public void client_config() throws Exception {
		ConfigCommandEvent request = new ConfigCommandEvent();
		request.setDevice_no("device_no");
		request.setRealtime_interval(29);
		DTSClient.config_realtime_interval(request);
	}
}
