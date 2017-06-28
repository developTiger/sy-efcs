package com.wxzd.efcs.business.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.gaia.common.base.core.log.FileLogFactory;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.module.Module;
import com.wxzd.gaia.web.web.WebContext;
import com.wxzd.wms.core.ModuleCore;

/**
 * 临时的配置解决方案
 * 
 * @version 1
 * @author y
 * @.create 2017-06-13
 */
@Module(ModuleCore.name)
@Controller
@RequestMapping
public class TempConfigController {

	@RequestMapping
	public void index_view(Model model) {
		model.addAttribute("house_no", DispatcherConfig.currentLine);
		model.addAttribute("house_battery", DispatcherConfig.batteryProfixCheck.get(DispatcherConfig.currentLine));
		model.addAttribute("house_tray", DispatcherConfig.pallet_no_profix);
		model.addAttribute("house_tray_rule", DispatcherConfig.foramtion_pallet_check_no);
		model.addAttribute("house_high_temperature", DispatcherConfig.mes_hightemprature_time.get(DispatcherConfig.currentLine));
		model.addAttribute("house_normal_temperature", DispatcherConfig.mes_normalemprature_1_time.get(DispatcherConfig.currentLine));
		model.addAttribute("house_normal_next_temperature", DispatcherConfig.mes_normalemprature_2_time.get(DispatcherConfig.currentLine));
	}

//	@RequestMapping
//	@ResponseBody
//	public void demo() {
//		System.out.println("demo...");
//		try {
//			Properties properties = new Properties();
//			properties.load(getClass().getClassLoader().getResourceAsStream("tempconfig.properties"));
//			System.out.println(properties.get("xxx"));
//			properties.setProperty("xxx", "mmm");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	
	@RequestMapping
	public String changeHouseNo() {
		String house_no = WebContext.getHttpServletRequest().getParameter("house_no");
		house_no = StringUtl.trim2Null(house_no);
		if (house_no != null) {
			DispatcherConfig.currentLine = house_no;
			log("库位号变更：" + house_no);
		}
		return "redirect:index_view";
	}

	private void log(String str) {
		FileLogFactory.warnCustomer("/message", "", "", str);//
	}

	@RequestMapping
	public String changeBattery() {
		String param = WebContext.getHttpServletRequest().getParameter("house_battery");
		param = StringUtl.trim2Null(param);
		if (param != null) {
			DispatcherConfig.batteryProfixCheck.put(DispatcherConfig.currentLine, param);
			log("电芯前缀变更：" + param);
		}
		return "redirect:index_view";
	}

	@RequestMapping
	public String changeTray() {
		String param = WebContext.getHttpServletRequest().getParameter("house_tray");
		param = StringUtl.trim2Null(param);
		if (param != null) {
			DispatcherConfig.pallet_no_profix = param;
			log("托盘前缀变更：" + param);
		}
		return "redirect:index_view";
	}

	@RequestMapping
	public String changeTrayRule() {
		String param = WebContext.getHttpServletRequest().getParameter("house_tray_rule");
		param = StringUtl.trim2Null(param);
		if (param != null) {
			try {
				param = param.replace(",", "");
				Integer valueOf = Integer.valueOf(param);
				DispatcherConfig.foramtion_pallet_check_no = valueOf;
				log("托盘规则变更：" + param);
			} catch (Exception e) {
				log("托盘规则变更失败：" + param);
			}
		}
		return "redirect:index_view";
	}

	@RequestMapping
	public String changeHigh() {
		String param = WebContext.getHttpServletRequest().getParameter("house_high_temperature");
		param = StringUtl.trim2Null(param);
		if (param != null) {
			try {
				param = param.replace(",", "");
				Integer valueOf = Integer.valueOf(param);
				DispatcherConfig.mes_hightemprature_time.put(DispatcherConfig.currentLine, valueOf);
				log("高温静置时长变更：" + param);
			} catch (Exception e) {
				log("高温静置时长变更失败：" + param);
			}
		}
		return "redirect:index_view";
	}

	@RequestMapping
	public String changeNormal() {
		String param = WebContext.getHttpServletRequest().getParameter("house_normal_temperature");
		param = StringUtl.trim2Null(param);
		if (param != null) {
			try {
				param = param.replace(",", "");
				Integer valueOf = Integer.valueOf(param);
				DispatcherConfig.mes_normalemprature_1_time.put(DispatcherConfig.currentLine, valueOf);
				log("常温静置时长变更：" + param);
			} catch (Exception e) {
				log("常温静置时长变更失败：" + param);
			}
		}
		return "redirect:index_view";
	}

	@RequestMapping
	public String changeNormalNext() {
		String param = WebContext.getHttpServletRequest().getParameter("house_normal_next_temperature");
		param = StringUtl.trim2Null(param);
		if (param != null) {
			try {
				param = param.replace(",", "");
				Integer valueOf = Integer.valueOf(param);
				DispatcherConfig.mes_normalemprature_2_time.put(DispatcherConfig.currentLine, valueOf);
				log("常温2静置时长变更：" + param);
			} catch (Exception e) {
				log("常温2静置时长变更失败：" + param);
			}
		}
		return "redirect:index_view";
	}

}
