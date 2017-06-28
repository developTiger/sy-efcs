package com.wxzd.efcs.business.application.workProcedures.testOut;

import com.wxzd.efcs.business.application.workProcedures.dto.PalletizePrecedureInfo;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.BatteryInfoService;
import com.wxzd.efcs.business.domain.service.FmBatteryOutService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.core.domain.service.StorageService;
import com.wxzd.wms.core.domain.service.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testOutExecutorService")
public class TestOutExecutorServiceImpl implements TestOutExecutorService<PalletizePrecedureInfo, GaiaResult> {

	@Autowired
	FmBatteryOutService fmBatteryOutService;

	@Autowired
	StorageService storageService;

	@Autowired
	LocationAllotQyery locationAllotQyery;

	@Autowired
	BatteryInfoService batteryInfoService;

	@Autowired
	WareHouseService wareHouseService;

	@Autowired
	PalletDispatchService dispatchService;

	@Override
	public GaiaResult doWorkProcedure(PalletizePrecedureInfo procedureInfo) {
		try {
			Storage storage;
			if (!StringUtl.isEmpty(procedureInfo.getBattery_no())) {
				storage = storageService.getStorageByBarcode(procedureInfo.getBattery_no());
			} else {
				PalletDetail detail = dispatchService.getPalletInnerDetail(procedureInfo.getFrom_pallet_no(), procedureInfo.getFrom_pos_channel_no());
				if (detail == null) {
					return GaiaResultFactory.getSuccess();
	//				throw new RuntimeException("条码:" + procedureInfo.getBattery_no() + " 不存在！");
				}
				storage = storageService.getStorageByBarcode(detail.getBattery_barcode());
			}
			if (storage != null) {
	//			throw new RuntimeException("条码:" + procedureInfo.getBattery_no() + " 库存不存在！");
				List<Storage> storages = new ArrayList<>();
				storages.add(storage);
				Storage fromContainer = new Storage(storage.getLocation_id(), "", StorageType.container);
				fromContainer.setHouse_id(procedureInfo.getWarehouse().getId());
				storageService.storageOut(fromContainer, StorageType.sku, storages, WorkProcedure.Test_Out.toString(), null, null);
			}
			//生成入库单
			fmBatteryOutService.save(ConvertFactory.convertToFmBatteryOut(procedureInfo));
			//        PositionType positionType = PositionType.Line;
			//
			//        detail.setToPos(procedureInfo.getCurrentPos(), procedureInfo.getClamp_no(), procedureInfo.getTo_pos_no(), String.valueOf(procedureInfo.getTo_pos_channel_no()), positionType);
			//        dispatchService.savePalletInnerDetail(detail);
	
			return GaiaResultFactory.getSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return GaiaResultFactory.getSuccess();
		}
	}

	/**
	 * 是否需要创建下一道工序
	 *
	 * @param workProcedure
	 * @param palletStatus
	 * @param currentPos
	 * @return
	 */
	@Override
	public Boolean needActiveNextProcedure(WorkProcedure workProcedure, PalletStatus palletStatus, String currentPos) {

		return false;
	}

	/**
	 * 获取下一道工序
	 *
	 * @param workProcedure
	 * @return
	 */
	@Override
	public WorkProcedure getNextProcedure(WorkProcedure workProcedure) {
		return null;
	}

	/**
	 * 异常结束工序
	 *
	 * @param workProcedure
	 * @param palletNo
	 * @param remark
	 * 		@return
	 */
	@Override
	public GaiaResult errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
		return null;
	}

	/**
	 * 非正常的开始一个新工序
	 *
	 * @param workProcedure
	 * @param procedureInfo
	 * @return
	 */
	@Override
	public GaiaResult initProcedureCurrenPosImproper(WorkProcedure workProcedure, PalletizePrecedureInfo procedureInfo) {
		return null;
	}

}
