package com.wxzd.efcs.equipment.controller;

import com.wxzd.efcs.equipment.ModuleEfcsEquipment;
import com.wxzd.gaia.web.module.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 化成维护
 * Created by jade on 2017/4/21.
 */
@Module(ModuleEfcsEquipment.name)
@Controller
@RequestMapping
public class FormationPlainController {

    @RequestMapping
    public void index_view(){

    }

    @RequestMapping
    @ResponseBody
    public void grid(){

    }

    @RequestMapping
    public void addorupdate_view(){

    }

    @RequestMapping
    public void addorupdate(){

    }
}
