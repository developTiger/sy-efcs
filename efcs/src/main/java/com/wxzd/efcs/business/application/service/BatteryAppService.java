package com.wxzd.efcs.business.application.service;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.protocol.wcs.battery.feedback.BatteryInPalletFeedbackEvent;


/**
 * Created by zhouzh on 2017/4/18
 */
public interface BatteryAppService {

     /**
      * 获取托盘电池信息
      * @return BatteryInPalletFeedbackEvent
      */
     BatteryInPalletFeedbackEvent getPalletInnerBatteries(BatteryInPalletFeedbackEvent event) throws Exception;


     /**
      * 电池详情（包括基本信息和移动记录）
      */
     PalletBatteryDto getBatteryMoveDetail(String battery_barcode);


}
