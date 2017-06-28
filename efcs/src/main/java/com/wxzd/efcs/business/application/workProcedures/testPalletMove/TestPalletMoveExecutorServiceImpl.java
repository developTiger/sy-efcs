package com.wxzd.efcs.business.application.workProcedures.testPalletMove;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.SchedulerConfig;
import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.workProcedures.TestProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.dto.DefaultProcedure;
import com.wxzd.efcs.business.application.workProcedures.dto.PalletOperate;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.FcsScheduler;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.business.domain.service.FcsSchedulerService;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.policy.locationAllot.AllotPolicyType;
import com.wxzd.policy.locationAllot.AllotStorageLocation;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.policy.locationAllot.LocationAllotService;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.entities.enums.LockDirection;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("testPalletMoveExecutorService")
public class TestPalletMoveExecutorServiceImpl implements TestPalletMoveExecutorService<DefaultProcedure, ObjectResult<Instruction>> {

	@Autowired
	FmProcedureService fmProcedureService;
	@Autowired
	@Qualifier("testEmptyPalletAllotPolicyService")
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
	FcsSchedulerService fcsSchedulerService;

	private WorkProcedure currentWorkProcedure = WorkProcedure.Test_PalletMove;
	private WorkProcedure nextProcedure = WorkProcedure.Test_Palletize;

	@Override
	public ObjectResult<Instruction> doWorkProcedure(DefaultProcedure procedureInfo) {
		//默认一个工序 会经历 3个库位。
		//1、待入
		//2、入库
		//3、出库
		Instruction instruction = new Instruction(procedureInfo.getHouseNo());
		/**
		 * 高温完成 化成开始
		 */
		String route = "";
		SendType sendType = SendType.Direct;
		String stockNo = "";
		InstructionType instructionType = InstructionType.Transport;

		if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_normal_temperature2_up1) || procedureInfo.getCurrentPos().equals(DispatcherConfig.test_normal_temperature2_up2)) {
			//            route = procedureInfo.getPalletDispatch().getProcedure_route();

			//清除遗留库存
			List<Storage> storages = locationAllotQyery.getStorageByPallet(procedureInfo.getPallet_no());
			if(storages.size()>1){
				List<Storage> outList = new ArrayList<>();
				for(Storage st: storages){
					if(st.getSto_type()== StorageType.sku){
						outList.add(st);
					}
				}
				Storage fromContainer = new Storage(storages.get(0).getLocation_id(), "", StorageType.container);
				fromContainer.setHouse_id(procedureInfo.getWarehouse().getId());
				storageService.storageOut(fromContainer, StorageType.sku, outList, WorkProcedure.Formation_Out.toString(), null, null);
			}
			storageService.setPalletIsEmpty(procedureInfo.getPallet_no(), true);
			if (StringUtl.isEmpty(procedureInfo.getPalletDispatch().getStorage_location())) {

				try{
				//分配库位
				ObjectResult<AllotStorageLocation> result = locationAllotService.allotStorageLocationIn(currentWorkProcedure, procedureInfo.getPallet_no(), procedureInfo.getWarehouse().getId(),
						procedureInfo.getHouseNo(), procedureInfo.getCurrentPos(), AllotPolicyType.FromSmallToBig, AllotPolicyType.FromBigToSmall);
				if (result.isSuccess()) {
					StorageLocation storageLocation = result.getObject().getStorageLocation();
					if (storageLocation.getLoc_type() == LocationType.cubic) {
						//立库库位
						route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), storageLocation.getX_pos().toString());
						route = TestProcedureRouteMatch.replaceEndPosition(route, result.getObject().getStorageLocation().getLoc_no());
						instructionType = InstructionType.Stock_In;
						procedureInfo.getPalletDispatch().setStorage_location(result.getObject().getStorageLocation().getLoc_no());
					} else {
						//空托缓存位
						route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_palletize_scaner_no);
						instructionType = InstructionType.Stock_Move;
					}
					procedureInfo.getPalletDispatch().setProcedure_route(route);
				} else {
					if (procedureInfo.getPalletOperate() != PalletOperate.AllotStorageLocation) {
						dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
								PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
						String defaultProcedureJson = JsonUtl.parse(ConvertFactory.convertToCleanProcedure(procedureInfo));
						FcsScheduler scheduler = new FcsScheduler(SchedulerType.ExecutionLoop, TaskType.AllotLocation, new Date(), currentWorkProcedure, defaultProcedureJson,
								SchedulerConfig.allotLocation_interval, SchedulerConfig.allotLocation_interval_unit);
						scheduler.setDevice_no(procedureInfo.getCurrentPos());
						scheduler.setHouse_id(procedureInfo.getWarehouse().getId());
						scheduler.setHouse_no(procedureInfo.getHouseNo());
						fcsSchedulerService.saveScheduler(scheduler);
					}
					return new ObjectResult<>(false, result.getMessage());
				}
				}catch (Exception e) {
					e.printStackTrace();
					route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_palletize_scaner_no);

					// TODO: handle exception
				}

			}else{
				route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(),TestProcedureRouteMatch.getCubicXPos(procedureInfo.getPalletDispatch().getStorage_location()));
				route = TestProcedureRouteMatch.replaceEndPosition(route, procedureInfo.getPalletDispatch().getStorage_location());
				instructionType = InstructionType.Stock_In;
			}

			String nextPos = TestProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
			if (TestProcedureRouteMatch.isStocker(nextPos)) {
				stockNo = nextPos;
				nextPos = TestProcedureRouteMatch.getNextPosition(route, stockNo);
				sendType = SendType.QueuesDirect;
			}
			instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure, procedureInfo.getPallet_no(), instructionType, stockNo,//是否设置为堆垛机
					procedureInfo.getCurrentPos(), nextPos,//入库库位
					InstructionMovePolicy.Static, sendType);

			instruction.setHouse_no(procedureInfo.getHouseNo());
			instructionService.createInstr(instruction);

			//更新托盘状态
			dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
					PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
			return GaiaResultFactory.getObject(instruction);

		} else {
			//入到高温立库了
			if (procedureInfo.getStorageLocation() != null && procedureInfo.getStorageLocation().getLoc_type() == LocationType.cubic) {
				procedureInfo.getPalletDispatch().setStorage_location(procedureInfo.getStorageLocation().getLoc_no());
				dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching,
						PositionType.Storage_Location, procedureInfo.getCurrentPos(), "");
				//库存从 化成位 移动到下架位
				storageService.storageTransfer(procedureInfo.getWarehouse().getId(), procedureInfo.getStorageLocation().getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());
				storageLocationService.setHasGoodAndAndReduceAllotLocationInlock(procedureInfo.getStorageLocation());

				//
				//
				//                    String nextPos = TestProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
				//                    if (TestProcedureRouteMatch.isStocker(nextPos)) {
				//                        stockNo = nextPos;
				//                        nextPos = TestProcedureRouteMatch.getNextPosition(route, stockNo);
				//                        instructionType = InstructionType.Stock_Out;
				//                        sendType = SendType.QueuesDirect;
				//                    }
				//                    //生成出库指令，计划指令，待mes 下发可出库时生效
				//                    instruction.newInstruction(procedureInfo.getHouse_no(), currentWorkProcedure,
				//                            procedureInfo.getPallet_no(),
				//                            instructionType,
				//                            stockNo,//是否设置为堆垛机
				//                            procedureInfo.getCurrentPos(),
				//                            nextPos,//入库库位
				//                            InstructionMovePolicy.Static,
				//                            sendType);
				//
				//                    instruction.setHouse_no(procedureInfo.getHouse_no());
				//                    instructionService.createInstr(instruction);
				return GaiaResultFactory.getObject(instruction);

			}
			if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_palletize_cache_cwzpjxs1) || procedureInfo.getCurrentPos().equals(DispatcherConfig.test_palletize_cache_cwzpjxs2)) {
				List<StorageLocation> locations = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), procedureInfo.getCurrentPos());
				//库存从 上架货位 移动到缓存位
				storageService.storageTransfer(

						procedureInfo.getWarehouse().getId(), locations.get(0).getId(), procedureInfo.getPallet_no(), "", currentWorkProcedure.toString());
				if (!StringUtl.isEmpty(procedureInfo.getPalletDispatch().getStorage_location())) {
					//解锁空托货位
					List<StorageLocation> storageLocation = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), procedureInfo.getPalletDispatch().getStorage_location());
					storageLocationService.setIsHasGood(storageLocation.get(0), false);
					storageLocationService.setInFobiden(storageLocation.get(0), LockDirection.outLock, false);

					procedureInfo.getPalletDispatch().setStorage_location("");
				}
				dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, procedureInfo.getWorkProcedure(), PalletDispatchStatus.Dispatching,
						PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");

				return new ObjectResult<>(true, "");
			}
			if (procedureInfo.getCurrentPos().equals(DispatcherConfig.test_palletize_scaner_no)) {
				// 工序结束

				procedureInfo.getPalletDispatch().setProcedure_route("");
				dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Finished,
						PositionType.Storage_Location, procedureInfo.getCurrentPos(), "");
				//storageLocationService.setInFobiden(procedureInfo.getStorageLocation(), LockDirection.inLock, false);
				return GaiaResultFactory.getObject(instruction);
			} else {
				//都不是上面的情况
				//启动自动寻径
				if (!StringUtl.isEmpty(procedureInfo.getPalletDispatch().getStorage_location())) {
					//解锁空托货位
					List<StorageLocation> storageLocation = locationAllotQyery.getEquipLocation(procedureInfo.getWarehouse().getId(), procedureInfo.getPalletDispatch().getStorage_location());
					storageLocationService.setIsHasGood(storageLocation.get(0), false);
					storageLocationService.setInFobiden(storageLocation.get(0), LockDirection.outLock, false);

					procedureInfo.getPalletDispatch().setStorage_location("");
				}
				route = TestProcedureRouteMatch.getRouteByEquipNo(procedureInfo.getCurrentPos(), DispatcherConfig.test_palletize_scaner_no);
				procedureInfo.getPalletDispatch().setProcedure_route(route);

				String nextPos = TestProcedureRouteMatch.getNextPosition(route, procedureInfo.getCurrentPos());
				if (TestProcedureRouteMatch.isStocker(nextPos)) {
					stockNo = nextPos;
					nextPos = TestProcedureRouteMatch.getNextPosition(route, stockNo);
					instructionType = InstructionType.Stock_Move;
					sendType = SendType.QueuesDirect;
				}
				//创建指令
				if (!DispatcherConfig.noNeedSendNextPosition.contains(procedureInfo.getCurrentPos())) {
					instruction.newInstruction(procedureInfo.getHouseNo(), currentWorkProcedure, procedureInfo.getPallet_no(), instructionType, stockNo, procedureInfo.getCurrentPos(), nextPos,
							InstructionMovePolicy.Static, SendType.Direct);
					instructionService.createInstr(instruction);
				}
				dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, currentWorkProcedure, PalletDispatchStatus.Dispatching,
						PositionType.Transport_Location, procedureInfo.getCurrentPos(), "");
				return GaiaResultFactory.getObject(instruction);

			}

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
	 * 		@return
	 */
	@Override
	public ObjectResult<Instruction> errorFinishProcedure(WorkProcedure workProcedure, String palletNo, String remark) {
		PalletDispatch dispatch = dispatchService.getByPalletNo(palletNo);
		dispatch.setPallet_status(PalletStatus.Error_Finished);
		dispatch.setProcedure_route("");
		dispatchService.saveDispatchWithErrorMoveDetail(dispatch, EfcsErrorCode.ErrorFinish, remark);
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
		if (procedureInfo.getPalletDispatch() == null) {
			procedureInfo.setPalletDispatch(dispatchService.getByPalletNo(procedureInfo.getPallet_no()));
		}
		procedureInfo.getPalletDispatch().setWork_procedure(procedureInfo.getWorkProcedure());
		procedureInfo.getPalletDispatch().setPallet_status(PalletStatus.In_Waiting);
		procedureInfo.getPalletDispatch().setProcedure_route("");
		dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.In_Waiting, workProcedure, PalletDispatchStatus.Dispatching, PositionType.Transport_Location,
				procedureInfo.getCurrentPos(), "");

		//清除遗留库存
		List<Storage> storages = locationAllotQyery.getStorageByPallet(procedureInfo.getPallet_no());
		if(storages.size()>1){
			List<Storage> outList = new ArrayList<>();
			for(Storage st: storages){
				if(st.getSto_type()== StorageType.sku){
					outList.add(st);
				}
			}
			Storage fromContainer = new Storage(storages.get(0).getLocation_id(), "", StorageType.container);
			fromContainer.setHouse_id(procedureInfo.getWarehouse().getId());
			storageService.storageOut(fromContainer, StorageType.sku, outList, WorkProcedure.Formation_Out.toString(), null, null);
		}
		storageService.setPalletIsEmpty(procedureInfo.getPallet_no(), true);

		return GaiaResultFactory.getObject(null);

	}

}
