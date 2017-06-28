package com.wxzd.efcs.business.application.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.efcs.business.application.queryService.BatteryInfoExQueryService;
import com.wxzd.efcs.business.application.queryService.PalletMoveDetailExQuery;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlmes.ws.celltestintegration.CustomSfcArrayData;
import com.atlmes.ws.celltestintegration.GetCurrentProcessLotResultResponse;
import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.application.service.BatteryAppService;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.business.webservice.MESAppWebservice;
import com.wxzd.gaia.common.base.core.type.TypeUtl;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.protocol.wcs.battery.feedback.BatteryInPalletFeedbackEvent;
import com.wxzd.protocol.wcs.domain.enums.PalletChannelPolicy;
import com.wxzd.protocol.wcs.domain.enums.PalletSplitPolicy;
import com.wxzd.protocol.wcs.domain.enums.PositionType;

/**
 * Created by zhouzh on 2017/4/18.
 */
@Service
public class BatteryAppServiceImpl implements BatteryAppService {

    @Autowired
    PalletDispatchService palletDispatchService;


    @Autowired
    BatteryInfoService batteryInfoService;


    @Autowired
    private BatteryInfoExQueryService batteryInfoExQueryService;

    @Autowired
    private PalletMoveDetailExQuery palletMoveDetailExQuery;

    /**
     * 电池详情（包括基本信息和移动记录）
     *
     * @param battery_barcode
     */
    @Override
    public PalletBatteryDto getBatteryMoveDetail(String battery_barcode) {
        if (!StringUtl.isEmpty(battery_barcode)) {
            PalletBatteryExQuery exQuery = new PalletBatteryExQuery();
            exQuery.setBattery_barcode(battery_barcode);
            exQuery.setDispatch_status(PalletDispatchStatus.Dispatching);
            List<PalletBatteryDto> list = batteryInfoExQueryService.getPalletBatteryById(exQuery);
            PalletBatteryDto batteryDto = new PalletBatteryDto();
            if (list != null && list.size() > 0) batteryDto = list.get(0);
            batteryDto.setBatteryMoveDetail(palletMoveDetailExQuery.getPalletMoveDetailById(null, battery_barcode));
            return batteryDto;
        }
        return null;
    }

    /**
     * 获取托盘电池信息
     *
     * @return BatteryInPalletFeedbackEvent
     */
    @Override
    public BatteryInPalletFeedbackEvent getPalletInnerBatteries(BatteryInPalletFeedbackEvent event) throws Exception {

        // 测试拆盘之前需要调用MES的获取结果接口拿到电池的数据
        // TODO 这里是传机械手的设备号还是输送线的设备号？
        if (event.getDevice_no().equals(DispatcherConfig.test_split_device_no)) {
            // 获取电池信息
            GetCurrentProcessLotResultResponse response = MESAppWebservice.ctiGetCurrentProcessLotResult(event.getHouse_no(), event.getPallet_no());
            if (response.getCode() == 0) {
                for (CustomSfcArrayData item : response.getSfcArray()) {
//                    String status = Integer.parseInt() == 0 ? DispatcherConfig.battery_default_ok : DispatcherConfig.battery_default_ng;
//                	batteryInfoService.updateBatteryStatus(item.getSfc(), item.getCode(), "MES 测试结果反馈");
                    if ("0".equals(item.getCode())) {
                        batteryInfoService.updateBatteryStatus(item.getSfc(), "0", "MES 测试结果反馈");
                    } else {
                        batteryInfoService.updateBatteryStatus(item.getSfc(), "3", "MES 测试结果反馈");

                    }
                }
            } else {
                // TODO MES返回异常，如何处理？？
            }
        }

        String palletNo = event.getPallet_no();
        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletNo);
        if (palletDispatch == null) {
            throw new Exception("活动托盘信息不存在");
            // TODO 是否需要将当前托盘转变为异常托盘
        } else if (palletDispatch.getWork_procedure() == WorkProcedure.FORMATION_ERROR_EXPORT ||
                palletDispatch.getWork_procedure() == WorkProcedure.TEST_ERROR_EXPORT) {
            throw new Exception("托盘需要异常排出");
        } else {
            // TODO 需要检查拆盘的分配策略是否合适
            event.setChannel_policy(TypeUtl.parse(palletDispatch.getChannel_policy().name(), PalletChannelPolicy.class));
            event.setSplit_policy(PalletSplitPolicy.valueOf(palletDispatch.getSplit_policy().name()));
            // 获取电池信息，组装到事件中
            List<PalletDetail> details = palletDispatchService.getActivePalletDetailByPalletWithStatus(palletNo);
            event.setBatteries(new ArrayList<BatteryInContainer>());
            for (PalletDetail d : details) {
                BatteryInContainer b = new BatteryInContainer();
                b.setBattery_no(d.getBattery_barcode());
                // 需要将数据库中保存的电池状态转换为机械手认识的电池状态
                b.setBattery_status(DispatcherConfig.getRobotBatteryStatus(d.getBattery_status()));
                b.setPos_type(PositionType.Pallet);
                b.setPos_no(d.getPallet_no());
                b.setPos_channel_no(Integer.parseInt(d.getChannel_no()));

                event.getBatteries().add(b);
            }
        }

        return event;
    }

}
