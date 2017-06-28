package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.PalletMoveDetail;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.business.repositorys.PalletDetailRepository;
import com.wxzd.efcs.business.repositorys.PalletDispatchRepository;
import com.wxzd.efcs.business.repositorys.PalletMoveDetailRepository;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.LocationUseStatus;
import com.wxzd.wms.core.domain.entities.enums.StorageStatus;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.core.repositorys.StorageLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/18.
 */
@Service
public class PalletDispatchServiceImpl implements PalletDispatchService {

	@Autowired
	PalletDispatchRepository palletDispatchRepository;

	@Autowired
	PalletMoveDetailRepository palletMoveDetailRepository;

	@Autowired
	PalletDetailRepository palletDetailRepository;

	@Autowired
	StorageLocationRepository storageLocationRepository;

	@Autowired
	LocationAllotQyery locationAllotQyery;

	/**
	 * 根据id获取 调度信息
	 *
	 * @param id
	 *        主键id
	 * @return PalletDispatch
	 */
	@Override
	public PalletDispatch getById(UUID id) {
		PalletDispatch dispatch = palletDispatchRepository.getById(id);
		return dispatch;
	}

	/**
	 * 根据托盘号获取调度信息
	 *
	 * @param palletNo
	 *        托盘号
	 * @return PalletDispatch
	 */
	@Override
	public PalletDispatch getByPalletNo(String palletNo) {
		PalletDispatch dispatch = palletDispatchRepository.getByNo(palletNo);
		return dispatch;
	}

	/**
	 * 根据托盘号获取调度信息
	 *
	 * @param palletNo
	 *        单据号
	 * @return ObjectResult<lel>
	 */
	@Override
	public PalletDetail getByPalletDetailByBarcode(String palletNo, String barcode) {
		PalletDetail detail = palletDetailRepository.getActivePalletDetailByBarcode(palletNo, barcode);
		return detail;
	}

	/**
	 * 新增调度
	 *
	 * @param palletDispatch
	 *        调度信息
	 * @return GaiaResult
	 */
	@Override
	public UUID newPalletDispatch(PalletDispatch palletDispatch) {
		return palletDispatchRepository.saveById(palletDispatch);

	}

	/**
	 * 新增调度
	 *
	 * @param palletDispatch
	 *        调度信息
	 * @return GaiaResult
	 */
	@Override
	public UUID newPalletDispatchWithMoveDetail(PalletDispatch palletDispatch, Boolean isLogDetail) {

		UUID id = palletDispatchRepository.saveById(palletDispatch);
		if (isLogDetail) {
			addPalletMoveDetail(createMoveDetailFromPalletDispatch(palletDispatch, palletDispatch.getCurrent_form_no()));
		}
		return id;

	}

	/**
	 * 新增调度
	 *
	 * @param palletDispatch
	 *        调度信息
	 * @return GaiaResult
	 */
	@Override
	public GaiaResult saveDispatchWithErrorMoveDetail(PalletDispatch palletDispatch, EfcsErrorCode errorCode, String errorMsg) {

		PalletMoveDetail moveDetail = createMoveDetailFromPalletDispatch(palletDispatch, palletDispatch.getCurrent_form_no());
		moveDetail.setError_desc(errorMsg);
		moveDetail.setError_code(EfcsErrorCode.ErrorFinish);
		palletDispatchRepository.saveById(palletDispatch);
		return addPalletMoveDetail(moveDetail);

	}

	/**
	 * 更新托盘位置
	 *
	 * @param disId
	 *        调度id
	 * @param posType
	 *        位置类型
	 * @param currentPos
	 *        当前位置
	 * @return GaiaResult
	 */
	@Override
	public GaiaResult updatePalletStatusAndPos(UUID disId, PalletStatus palletStatus, PositionType posType, String currentPos, String formNo) {
		setPalletStatus(disId, palletStatus, null, posType, currentPos, formNo);
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 更新托盘位置
	 *
	 * @param dispatch
	 *        调度id
	 * @param palletStatus
	 * @param workProcedure
	 * @param palletDispatchStatus
	 * @param posType
	 *        位置类型
	 * @param currentPos
	 *        当前位置
	 * @param formNo
	 * 		@return GaiaResult
	 */
	@Override
	public GaiaResult 	updatePalletStatusAndPos(PalletDispatch dispatch, PalletStatus palletStatus, WorkProcedure workProcedure, PalletDispatchStatus palletDispatchStatus, PositionType posType,
			String currentPos, String formNo) {
		dispatch.setPallet_status(palletStatus);
		dispatch.setWork_procedure(workProcedure);
		dispatch.setCurrent_form_no(formNo);
		if (palletStatus == PalletStatus.Out_Finished) {
			dispatch.setProcedure_route("");
		}
		if (palletDispatchStatus != null)
			dispatch.setDispatch_status(palletDispatchStatus);
		if (workProcedure != null)
			dispatch.setWork_procedure(workProcedure);
		if (posType != null)
			dispatch.setPos_type(posType);
		if (!StringUtl.isEmpty(currentPos)) {

			dispatch.setCurrent_pos(currentPos);
			addPalletMoveDetail(createMoveDetailFromPalletDispatch(dispatch, formNo));
		}
		palletDispatchRepository.saveById(dispatch);
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 设置托盘状态
	 *
	 * @param disId
	 *        调度id
	 * @param palletStatus
	 *        托盘状态
	 * @return
	 */
	@Override
	public GaiaResult setPalletStatus(UUID disId, PalletStatus palletStatus) {
		PalletDispatch dispatch = palletDispatchRepository.getDomainById(disId);
		setPalletStatus(dispatch, palletStatus);
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 设置托盘状态
	 *
	 * @param dispatch
	 *        调度号
	 * @param palletStatus
	 *        托盘状态
	 * @return
	 */
	@Override
	public GaiaResult setPalletStatus(PalletDispatch dispatch, PalletStatus palletStatus) {
		dispatch.setPallet_status(palletStatus);
		palletDispatchRepository.saveById(dispatch);
		return GaiaResultFactory.getSuccess();
	}

	public GaiaResult setPalletStatus(UUID disId, PalletStatus palletStatus, PalletDispatchStatus palletDispatchStatus, PositionType posType, String currentPos, String formNo) {
		PalletDispatch dispatch = palletDispatchRepository.getDomainById(disId);
		return updatePalletStatusAndPos(dispatch, palletStatus, null, palletDispatchStatus, posType, currentPos, formNo);

	}

	/**
	 * 异常设置
	 *
	 * @param disId
	 *        调度id
	 * @param errorCode
	 *        错误代码
	 * @param errorMsg
	 *        错误描述
	 * @return
	 */
	@Override
	public GaiaResult setError(UUID disId, EfcsErrorCode errorCode, String errorMsg) {
		PalletDispatch dispatch = palletDispatchRepository.getDomainById(disId);
		dispatch.setError_code(errorCode);
		dispatch.setError_desc(errorMsg);
		palletDispatchRepository.saveById(dispatch);
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 新增托盘货物明细
	 *
	 * @param palletDetail
	 * @return
	 */
	@Override
	public GaiaResult savePalletInnerDetail(PalletDetail palletDetail) {
		palletDetailRepository.saveById(palletDetail);
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 新增托盘货物明细
	 *
	 * @param palletDetail
	 * @return是
	 */
	@Override
	public GaiaResult savePalletInnerDetail(List<PalletDetail> palletDetail) {
		palletDetailRepository.saveById(palletDetail);
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 查询palletDetail
	 *
	 * @param containerNo
	 * @param batteryBarcode
	 * @return
	 */
	@Override
	public PalletDetail getPalletInnerDetail(String containerNo, String batteryBarcode) {
		PalletDetail detail = palletDetailRepository.getActivePalletDetailByBarcode(containerNo, batteryBarcode);
		return detail;
	}

	/**
	 * 查询palletDetail
	 *
	 * @param containerNo
	 * @return
	 */
	@Override
	public List<PalletDetail> getPalletInnerDetail(String containerNo) {
		return palletDetailRepository.getActivePalletDetailByPallet(containerNo);
	}

	@Override
	public List<PalletDetail> getActivePalletDetailByPalletWithStatus(String containerNo) {
		return palletDetailRepository.getActivePalletDetailByPalletWithStatus(containerNo);
	}

	/**
	 * 查询palletDetail
	 *
	 * @param dispatchId
	 * @return
	 */
	@Override
	public List<PalletDetail> getPalletInnerDetailResort(UUID dispatchId) {
		return palletDetailRepository.getPalletInnerDetailResort(dispatchId);
	}

	/**
	 * 新增移动记录
	 *
	 * @param palletMoveDetail
	 * @return
	 */
	@Override
	public GaiaResult addPalletMoveDetail(PalletMoveDetail palletMoveDetail) {
		palletMoveDetailRepository.saveById(palletMoveDetail);
		return GaiaResultFactory.getSuccess();
	}

	@Override
	public PalletMoveDetail createMoveDetailFromPalletDispatch(PalletDispatch dispatch, String formNo) {
		PalletMoveDetail moveDetail = new PalletMoveDetail();
		moveDetail.setPos_type(dispatch.getPos_type());
		moveDetail.setArrive_pos(dispatch.getCurrent_pos());
		moveDetail.setForm_no(formNo);
		moveDetail.setPallet_dispatch_id(dispatch.getId());

		moveDetail.setPallet_status(dispatch.getPallet_status());
		//TODO 是否定义中文名称
		if (dispatch.getWork_procedure() != null) {
			moveDetail.setProcedure_name(dispatch.getWork_procedure().toString());
		}
		moveDetail.setWork_procedure(dispatch.getWork_procedure());
		return moveDetail;
	}

	/**
	 * 组盘
	 *
	 * @param batterylists
	 * @return
	 */
	@Transaction
	@Override
	public GaiaResult palletGroup(List<PalletDetail> batterylists) {
		palletDetailRepository.saveById(batterylists);
		return GaiaResultFactory.getSuccess();
	}

	/**
	 * 拆盘
	 *
	 * @param palletNo
	 * @param batterylists
	 * @return
	 */
	@Transaction
	@Override
	public GaiaResult palletSplit(String palletNo, List<Map<String, Object>> batterylists) {
		return null;
	}

	@Override
	public PalletDetail getPalletInnerDetail(String pallet_no, int from_pos_channel_no) {
		PalletDetail detail = palletDetailRepository.getActivePalletDetailByChannel(pallet_no, from_pos_channel_no);
		return detail;
	}

	@Override
	public void deleteDetailHard(UUID id) {
		palletDetailRepository.hardDelete(id);
	}

	@Override
	public PalletDetail getActivePalletDetailByPalletWithStatus(String palletNo, String batteryNo) {
		return palletDetailRepository.getActivePalletDetailByPalletWithStatus( palletNo, batteryNo);
	}

	/**
	 * 获取托盘电池数量
	 *
	 * @param disId
	 * @return
	 */
	@Override
	public Integer getInnerDetailCountByPalletDispatchId(UUID disId) {
		return palletDetailRepository.getInnerDetailCountByPalletDispatchId(disId);
	}

	@Override
	public GaiaResult clearLocationStatus(UUID locationId) {

		StorageLocation location = storageLocationRepository.getById(locationId);

		List<Storage> storages =locationAllotQyery.getEquipLocationStorage(location.getHouse_id(),location.getLoc_no());

		if(storages.size()>0){
			for(Storage storage:storages){
				if(storage.getSto_type()== StorageType.container){
					return GaiaResultFactory.getError("清空失败，库位中尚存在库存，请先处理库存数据。");
				}
			}
		}
		if(location!=null)
		{
			location.setForbid_in(0);
			location.setForbid_out(0);
			location.setStorage_status(StorageStatus.empty);
			location.setBusiness_in_lock(0);
			location.setBusiness_out_lock(0);
			location.setLoc_use_status(LocationUseStatus.available);
			location.setArtificial_in_lock(0);
			location.setArtificial_out_lock(0);
			location.setLoc_is_error(false);
			location.setLoc_error_reason("");
			storageLocationRepository.saveById(location);
			return GaiaResultFactory.getSuccess();
		}

		return GaiaResultFactory.getError("清理失败，库位信息异常!");
	}
}
