package com.wxzd.configration.catlConfig;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.wxzd.catl.base.CatlWebServiceConfig;
import com.wxzd.dispatcher.dts.DTSConfig;
import com.wxzd.dispatcher.wcs.command.http_impl.WCSClientConfig;
import com.wxzd.gaia.common.base.core.log.FileLogConfig;
import com.wxzd.gaia.common.base.spring.core.NonAop;
import com.wxzd.gaia.front.layout.default_.controller.LoginController;
import com.wxzd.wms.catl.fcs.webservice.ICConfig;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-26
 */
@Component
@NonAop
public class ConfigInitial implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		/*
		 * 设置日志根目录
		 */
		FileLogConfig.setDefault_path("D:/log/efcs");
		//设置日志默认目录名
		FileLogConfig.setApp_path("/efcs");
		//设置mes的目录
		FileLogConfig.setMes_log_path("D:/log/mes");
		
		/*
		 * wcs配置
		 */
		WCSClientConfig.getWcsServers().put("29", "http://10.4.37.70:8080/wcs/servlet/server");
//		WCSClientConfig.getWcsServers().put("28", "http://10.4.37.73:8080/wcs/servlet/server");
//		WCSClientConfig.getWcsServers().put("27", "http://10.4.37.74:8080/wcs/servlet/server");
//		WCSClientConfig.getWcsServers().put("26", "http://10.4.37.76:8080/wcs/servlet/server");
//		WCSClientConfig.getWcsServers().put("29", "http://172.20.8.210:8080/wcs/servlet/server");

		//是否只显示命令而不实际发送
		WCSClientConfig.setFakeSend(false);
		//自动回复完成指令
		WCSClientConfig.setFakeReceiveAfterSend(false);

		WCSClientConfig.setManagment("100");

		/*
		 * dts配置
		 */
		DTSConfig.setMq_ip("10.4.37.155");
		DTSConfig.setMq_port(1883);
		//配置的通信订阅主题
		DTSConfig.setMq_client_topic_config("config");
		//接收而不执行功能
		DTSConfig.setIsfakeEvent(false);
		
		/**
		 * webservice配置
		 */
//		CatlWebServiceConfig.setServerPath("http://127.0.0.1:8082");
//		CatlWebServiceConfig.setUsername("60024251");
//		CatlWebServiceConfig.setPassword("Wh123456");
//		CatlWebServiceConfig.setHighTempUser("SUP_L29_HSTDY1");
		CatlWebServiceConfig.setIsCommunicateFake(false);
		
		/**
		 * ic config
		 * 
		 * 化成ocv
		 */
		ICConfig.setIsCommunicateFake(false);
		FormationConfig.pressMaxAllow = 4;
		OCVConfig.fakeReturn = false;
		/**
		 * 登录登出跳转页面
		 */
		LoginController.setIndex("/efcs_dashboard/production/index_view");
		LoginController.setRedirect("redirect:/efcs_dashboard/production/index_view");
		
		
	}

}
