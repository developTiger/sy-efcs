package com.wxzd.efcs.equipment.controller;

import com.wxzd.efcs.equipment.ModuleEfcsEquipment;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ocv校准
 * Created by jade on 2017/4/21.
 */
@Module(ModuleEfcsEquipment.name)
@Controller
@RequestMapping
public class OcvCalibrateController {

    @RequestMapping
    public void index_view(){

    }

    @RequestMapping
    @ResponseBody
    public void grid(){

    }


}
