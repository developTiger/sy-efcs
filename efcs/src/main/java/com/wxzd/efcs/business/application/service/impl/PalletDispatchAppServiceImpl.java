package com.wxzd.efcs.business.application.service.impl;

import com.wxzd.efcs.business.application.dtos.PalletBatteryDto;
import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import com.wxzd.efcs.business.application.dtos.PalletDispatchDto;
import com.wxzd.efcs.business.application.queryService.BatteryInfoExQueryService;
import com.wxzd.efcs.business.application.queryService.PalletDetailExQueryService;
import com.wxzd.efcs.business.application.querys.PalletBatteryExQuery;
import com.wxzd.efcs.business.application.service.PalletDispatchAppService;
import com.wxzd.efcs.business.application.workProcedures.WorkProcedureService;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.FmCreateMode;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.wms.core.domain.entities.Warehouse;
import com.wxzd.wms.core.domain.service.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zhouzh on 2017/4/20
 */
@Service
public class PalletDispatchAppServiceImpl implements PalletDispatchAppService {
    @Autowired
    WareHouseService wareHouseService;

    @Autowired
    PalletDispatchService palletDispatchService;


    @Autowired
    private PalletDetailExQueryService detailExQueryService;


    @Autowired
    private BatteryInfoExQueryService batteryInfoExQueryService;


    @Autowired
    private WorkProcedureService procedureService;


    @Override
    public WorkProcedure getCurrentWorkProcedure(String houseNo, String palletNo) {
        PalletDispatch palletDispatch = palletDispatchService.getByPalletNo(palletNo);
        return palletDispatch.getWork_procedure();
    }

    /**
     * 根据ID跟新调度工序
     *
     * @param id
     * @param procedure 工序
     * @param desc      更改原因
     * @return
     */
    @Transaction
    @Override
    public GaiaResult updatePalletDispatch(UUID id, WorkProcedure procedure, String desc) {
        if (id != null) {
            PalletDispatch dispatch = palletDispatchService.getById(id);
            if (dispatch == null) return GaiaResultFactory.getError("托盘调度未找到!");
            if (procedure.equals(WorkProcedure.Formation_PalletMove)) {
                dispatch.setWork_procedure(procedure);
                palletDispatchService.newPalletDispatch(dispatch);
                return GaiaResultFactory.getSuccess();
            } else {
                GaiaResult result = procedureService.errorFinishProcedure(dispatch.getWork_procedure(), dispatch.getContainer_no(), desc);
                if (result.isSuccess()) {
                    Map<String, Object> map = detailExQueryService.getHouseNobyId(dispatch.getHouse_id());
                    if (map != null) {
                        DefaultProcedure defaultProcedure = new DefaultProcedure(map.get("house_no").toString(), null, FmCreateMode.Manual);
                        procedureService.initProcedureCurrenPosImproper(procedure, defaultProcedure);
                        return GaiaResultFactory.getSuccess();
                    }
                }
            }
        }
        return GaiaResultFactory.getError("id为空!");
    }

    /**
     * 获取当期托盘调度信息
     *
     * @param palletNo
     * @return
     */
    @Override
    public PalletDispatchDto getCurrentPalletDiapatch(String palletNo) {
        PalletDispatchDto palletDispatchDto = detailExQueryService.getPalletDispatchByNo(palletNo);
        if (palletDispatchDto != null) {
            List<PalletDetailDto> details = detailExQueryService.getInnerDetailById(palletDispatchDto.getId());
            Map<String, PalletDetailDto> map = new HashMap<>();
            for (PalletDetailDto detail : details) {
                map.put(detail.getChannel_no(), detail);
            }
            palletDispatchDto.setPalletDetailMap(map);
        }
        return palletDispatchDto;
    }

    /**
     * TODO 组盘  @Leon Regulus
     *
     * @param batterylists
     * @return
     */
    @Override
    public GaiaResult palletGroup(PalletDispatchDto dispatchDto, Map<String, Object> batterylists) {
        //TODO 是否新增调度状态
        List<PalletDetail> list = new ArrayList<>();
        Set<String> keySet = batterylists.keySet();
        for (String channelNo : keySet) {
            String barcode = batterylists.get(channelNo).toString();
            PalletBatteryExQuery exQuery = new PalletBatteryExQuery();
            exQuery.setBattery_barcode(barcode);
            exQuery.setContainer_no(dispatchDto.getContainer_no());
            PalletBatteryDto batteryDto = batteryInfoExQueryService.getPalletBatteryById(exQuery).get(0);
            if (batteryDto != null) {
                PalletDetail palletDetail = BeanUtl.copyProperties(batteryDto, PalletDetail.class);
                palletDetail.setPallet_no(dispatchDto.getContainer_no());
                palletDetail.setChannel_no(channelNo);
                list.add(palletDetail);
            }
        }
        return palletDispatchService.palletGroup(list);
    }

    /**
     * TODO 拆盘  @Leon Regulus
     *
     * @param batterylists
     * @return
     */
    @Override
    public GaiaResult palletSplit(PalletDispatchDto dispatchDto, Map<String, Object> batterylists) {
        return null;
    }


    /**
     * 获取电池信息
     *
     * @param battery_barcode 电池条码
     * @return
     */
    @Override
    public ObjectResult<PalletBatteryDto> getPalletDetail(String battery_barcode) {
        PalletBatteryExQuery exQuery = new PalletBatteryExQuery();
        exQuery.setBattery_barcode(battery_barcode);
        List<PalletBatteryDto> list = batteryInfoExQueryService.getPalletBatteryById(exQuery);
        PalletBatteryDto batteryDto = new PalletBatteryDto();
        if (list != null && list.size() > 0) batteryDto = list.get(0);
        return GaiaResultFactory.getObject(batteryDto);
    }
}
