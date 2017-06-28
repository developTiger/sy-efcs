package com.wxzd.efcs.alarm.controller;

import com.wxzd.efcs.alarm.ModuleEfcsAlarm;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常事件
 * Created by jade on 2017/4/21.
 */
@Module(ModuleEfcsAlarm.name)
@Controller
@RequestMapping
public class ErrorEventController {

    @RequestMapping
    public void index_view() {
    }


    @RequestMapping
    @ResponseBody
    public Object grid() {
        Map map = new HashMap<>();
        map.put("xpos_id", "预警事件管理");
        map.put("xpos_no", "测试");
        List list = new ArrayList<>();
        list.add(map);
        return list;
    }

}
