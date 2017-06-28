package com.wxzd.efcs.business.application.workProcedures.formationErrorExport;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.ProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.policy.locationAllot.LocationAllotService;
import com.wxzd.wms.core.domain.entities.Container;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;
import com.wxzd.wms.core.domain.service.ContainerService;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("formationErrorExportExecutorService")
public class FormationErrorExportExecutorServiceImpl implements FormationErrorExportExecutorService<DefaultProcedure, ObjectResult<Instruction>> {

	@Autowired
	FmProcedureService fmProcedureService;
	@Autowired
	@Qualifier("emptyPalletAllotPolicyService")
	LocationAllotService locationAllotService;
	@Autowired
	StorageService storageService;
	@Autowired
	MemoryInstructionAppService instructionService;

	@Autowired
	PalletDispatchService dispatchService;

	@Autowired
	StorageLocationService storageLocationService;

	@Autowired
	LocationAllotQyery locationAllotQyery;

	@Autowired
	ContainerService containerService;

	private WorkProcedure currentWorkProcedure = WorkProcedure.FORMATION_ERROR_EXPORT;
	private WorkProcedure nextProcedure = WorkProcedure.Formation_Split;

	@Override
	public ObjectResult<Instruction> doWorkProcedure(DefaultProcedure procedureInfo) {
		//默认一个工序 会经历 3个库位。
		//1、待入
		//2、入库
		//3、出库
		Instruction instruction = new Instruction(procedureInfo.getHouseNo());
		String stockNo = "";
		SendType sendType = SendType.Direct;
		InstructionType instructionType = InstructionType.Transport;
		/**
		 * 高温完成 化成开始
		 */
		if (!procedureInfo.getCurrentPos().equals(DispatcherConfig.Formation_enter_exit)) {
			PalletDispatch dispatch = dispatchService.getByPalletNo(procedureInfo.getPallet_no());
			if (dispatch == null) {
				Container container = containerService.getContainerByBarcode(procedureInfo.getPallet_no());

				if (container != null) {
					dispatch = new PalletDispatch();

					dispatch.setHouse_id(procedureInfo.getWarehouse().getId());
					dispatch.setPallet_status(PalletStatus.In_Waiting);
					dispatch.setDispatch_status(PalletDispatchStatus.Dispatching);
					dispatch.setWork_procedure(currentWorkProcedure);
					dispatch.setContainer_id(container.getId().toString());
					dispatch.setContainer_no(procedureInfo.getPallet_no());
					dispatch.setIs_empty(true);

					procedureInfo.setPalletDispatch(dispatch);
				} else {
					throw new RuntimeException("托盘不存在！");
				}

			}
			//临时修改，修改下架点
            if (procedureInfo.getCurrentPos().equals(DispatcherConfig.high_temperature_waitin)) {

                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        InstructionType.Stock_Move,
                        DispatcherConfig.formation_stocker_1,
                        procedureInfo.getCurrentPos(),
                        DispatcherConfig.formation_pallet_to_hczpjxs,
                        InstructionMovePolicy.Static,
                        SendType.Direct);
                instructionService.createInstr(instruction);
                return GaiaResultFactory.getObject(instruction);
            }
			String route = dispatch.getProcedure_route();//异常排除 上一道路线需要清空路线信息
			if (StringUtl.isEmpty(route)) {  //异常排出 第一次进入工序方法
				//都不是上面的情况
				//启动自动寻径
				if (procedureInfo.getStorageLocation() != null && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic) {
					route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getStorageLocation().getX_pos().toString(), DispatcherConfig.Formation_enter_exit);
					route = ProcedureRouteMatch.replaceStartPosition(route, procedureInfo.getCurrentPos());
					String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
					if (ProcedureRouteMatch.isStocker(nextPos)) {
						stockNo = nextPos;
						nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);
						instructionType = InstructionType.Stock_Out;
						sendType = SendType.QueuesDirect;
					}
					procedureInfo.getPalletDispatch().setProcedure_route(route);
					dispatch.setWork_procedure(currentWorkProcedure);

					List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), DispatcherConfig.formation_error_exit_storage_location);
					//库存从 上架货位 移动到缓存位
					storageService.storageTransfer(procedureInfo.getWarehouse().getId(), locations.get(0).getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());

					instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure, procedureInfo.getPallet_no(), instructionType, stockNo,//是否设置为堆垛机
							procedureInfo.getCurrentPos(), nextPos,//入库库位
							InstructionMovePolicy.Static, sendType);
					instructionService.createInstr(instruction);
					procedureInfo.getPalletDispatch().setStorage_location(procedureInfo.getStorageLocation().getLoc_no());

					//更新托盘状态
					dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
							PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
					return GaiaResultFactory.getObject(instruction);
				} else {
					route = ProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.Formation_enter_exit);
				}

				
			}

            //临时修改，修改下架点
            if (procedureInfo.getPalletDispatch().getCurrent_pos().equals(DispatcherConfig.formation_pallet_split_location_right)) {

                instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure,
                        procedureInfo.getPallet_no(),
                        InstructionType.Transport,
                        "",
                        procedureInfo.getPalletDispatch().getCurrent_pos(),
                        DispatcherConfig.formation_rework_pallet_catch_location,
                        InstructionMovePolicy.Static,
                        SendType.Direct);
                instructionService.createInstr(instruction);
                return GaiaResultFactory.getObject(instruction);
            }

			if (procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_pallet_to_hczpjxs) || procedureInfo.getCurrentPos().equals(DispatcherConfig.formation_palletize_catch_location)) {
				List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), DispatcherConfig.formation_palletize_catch_location);
				if (locations.size() == 0) {
					throw new RuntimeException(I18nContext.getMessage("空托缓存设备库位未设置"));
				}
				if (!StringUtl.isEmpty(procedureInfo.getPalletDispatch().getStorage_location())) {
					//解锁空托货位
					List<StorageLocation> storageLocation = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), procedureInfo.getPalletDispatch().getStorage_location());
					storageLocationService.setIsHasGood(storageLocation.get(0), false);
					procedureInfo.getPalletDispatch().setStorage_location("");
				}

				//库存从 上架货位 移动到缓存位
				storageService.storageTransfer(procedureInfo.getWarehouse().getId(), locations.get(0).getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());
				procedureInfo.getPalletDispatch().setProcedure_route("");
				dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching,
						PositionType.Storage_Location, procedureInfo.getCurrentPos(), "");
				//                instruction.newInstruction(procedureInfo.getHouse_no(), currentWorkProcedure,
				//                        procedureInfo.getPallet_no(),
				//                        instructionType,
				//                        stockNo,
				//                        procedureInfo.getCurrentPos(),
				//
				//                        DispatcherConfig.formation_palletize_scaner_no,
				//                        InstructionMovePolicy.Static,
				//                        SendType.Direct);
				//                instructionService.createInstr(instruction);
				//                return GaiaResultFactory.getObject(instruction);
				return GaiaResultFactory.getObject(null);
				//创建指令

			}
			String nextPos = ProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
			if (!DispatcherConfig.noNeedSendNextPosition.contains(procedureInfo.getCurrentPos())) {
				if (ProcedureRouteMatch.isStocker(nextPos)) {
					stockNo = nextPos;
					nextPos = ProcedureRouteMatch.getNextPosition(route, stockNo);

					instructionType = InstructionType.Stock_Move;
					sendType = SendType.QueuesDirect;
				}
				if(procedureInfo.getStorageLocation() != null && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic){
					instructionType = InstructionType.Stock_Out
					;

				}
				procedureInfo.getPalletDispatch().setProcedure_route(route);

				instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure, procedureInfo.getPallet_no(), instructionType, stockNo,//是否设置为堆垛机
						procedureInfo.getCurrentPos(), nextPos,//入库库位
						InstructionMovePolicy.Static, sendType);
				instructionService.createInstr(instruction);
			}
			//更新托盘状态
			dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
					PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
			return GaiaResultFactory.getObject(instruction);

		} else {
			List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), DispatcherConfig.formation_error_exit_storage_location);
			//库存从 上架货位 移动到缓存位
			storageService.storageTransfer(procedureInfo.getWarehouse().getId(), locations.get(0).getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());
			//排除完成
			procedureInfo.getPalletDispatch().setProcedure_route("");
			dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching,
					PositionType.Storage_Location, procedureInfo.getCurrentPos(), "");
			return GaiaResultFactory.getObject(instruction);
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
		if (palletStatus == PalletStatus.Out_Finished)
			return true;
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

		return nextProcedure;
	}

	/**
	 * 异常结束工序
	 *
	 * @param workProcedure
	 * @param palletNo
	 * @param remark
	 * @return
	 */
	@Override
	public ObjectResult<Instruction> errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {

		PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
		if (dispatch == null) {
			dispatch.setPallet_status(PalletStatus.Error_Finished);
			dispatch.setProcedure_route("");
			dispatchService.saveDispatchWithErrorMoveDetail(dispatch, EfcsErrorCode.ErrorFinish, remark);
		}
		return GaiaResultFactory.getObject(null);
	}

	/**
	 * 非正常的开始一个新工序
	 *
	 * @param workProcedure
	 * @param procedureInfo
	 * @return
	 */
	@Override
	public ObjectResult<Instruction> initProcedureCurrenPosImproper(WorkProcedure workProcedure, DefaultProcedure procedureInfo) {
		PalletDispatch dispatch = dispatchService.getByPalletNo(procedureInfo.getPallet_no());
		procedureInfo.setPalletDispatch(dispatch);

		procedureInfo.getPalletDispatch().setWork_procedure(procedureInfo.getWorkProcedure());
		procedureInfo.getPalletDispatch().setPallet_status(PalletStatus.Out_Waiting);
		procedureInfo.getPalletDispatch().setProcedure_route("");
		dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Waiting, workProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location,
				procedureInfo.getCurrentPos(), "");
		return GaiaResultFactory.getObject(null);
	}

}
