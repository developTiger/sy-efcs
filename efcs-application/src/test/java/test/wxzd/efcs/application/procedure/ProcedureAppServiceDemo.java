package test.wxzd.efcs.application.procedure;

import java.util.List;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.configration.catlConfig.ProcedureConfig;
import com.wxzd.efcs.business.domain.entities.form.FmProcedure;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.FmProcedureService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.policy.locationAllot.LocationAllotService;
import com.wxzd.protocol.wcs.battery.GrabType;
import com.wxzd.protocol.wcs.domain.enums.ExecuteStatus;
import com.wxzd.protocol.wcs.domain.enums.PositionType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.protocol.wcs.battery.feedback.BatteryCheckFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFinishedFeedbackEvent;
import com.wxzd.protocol.wcs.transport.feedback.CommandFeedbackEvent;
import com.wxzd.protocol.wcs.transport.feedback.PalletArriveFeedbackEvent;
import com.wxzd.wcs.dispatcher.client.http.EfcsClient;
import com.wxzd.wcs.dispatcher.client.http.EfcsClientConfig;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;

import test.wxzd.efcs.application.mold.SpringDemoBase;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-01
 */
public class ProcedureAppServiceDemo extends SpringDemoBase {

    private static String batteryNoProfix = "17G2017SKU06";

	@Autowired
	private ProcedureAppService procedureAppService;

	@Test
	public void showInstance() {
		System.out.println(procedureAppService);
	}

	@Test
	public void batteryIn() throws Exception {
		BatteryCheckFeedbackEvent event = new BatteryCheckFeedbackEvent();
		event.setSource(this);
		event.setHouse_no("29");
		event.setDevice_no("cwzpjxs");
		event.setCom_no(null);

		//batteries
		List<BatteryInContainer> batteries = event.getBatteries();
		for (int i = 1; i <= 24; i ++) {
            BatteryInContainer battery = new BatteryInContainer();
            battery.setBattery_no(batteryNoProfix + String.format("%04d", i));
            battery.setPos_no("cwzpjxs");
            battery.setBattery_status(DispatcherConfig.battery_default_ok);
            battery.setPos_type(PositionType.Line);
            battery.setPos_channel_no(i % 2);
            battery.setInfo("测试段模拟电池");

            batteries.add(battery);
        }
        event.setBatteries(batteries);

        ApplicationEventPublisher.trigger(event);
//		procedureAppService.batteryIn(event);
	}

	@Test
	public void batteryGrab() throws Exception {
        BatteryGrabFeedbackEvent event = new BatteryGrabFeedbackEvent();

        event.setHouse_no("29");
        event.setDevice_no("cwzpjxs");
        event.setSource(this);

        for (int i = 1; i <= 24; i ++) {
            event.setBattery_no(batteryNoProfix + String.format("%04d", i));
            event.setClamp_no(Integer.toString(i % 2));
            event.setFrom_pallet_no("");
            event.setFrom_pos_no("0");
            event.setFrom_pos_channel_no(i % 2);
            event.setTo_pallet_no("L00P1230");
            event.setTo_pos_no("2");
            event.setTo_pos_channel_no(i);

            ApplicationEventPublisher.trigger(event);
//		procedureAppService.batteryGrab(event);

        }

        ApplicationEventPublisher.trigger(event);
	}

	@Test
	public void batteryGrabFinished() throws Exception {
		BatteryGrabFinishedFeedbackEvent event = new BatteryGrabFinishedFeedbackEvent();
		event.setHouse_no("29");
		event.setDevice_no("2290");
		event.setSource(this);
		event.setCom_no(null);
		event.setGrab_type(GrabType.Split);
		event.setPallet_no("L29B1001");

        ApplicationEventPublisher.trigger(event);
//		procedureAppService.batteryGrabFinished(event);
	}

	@Test
	public void palletArrive() throws Exception {
		EfcsClientConfig.setUrl("http://10.4.37.68/efcs");
		PalletArriveFeedbackEvent event = new PalletArriveFeedbackEvent();
		event.setSource(this);
		event.setHouse_no("29");
		event.setDevice_no("LB030106");
		event.setPallet_no("L26B0322");
		event.setCom_no("632");
		EfcsClient.arrive(event);
//        ApplicationEventPublisher.trigger(event);
//		procedureAppService.palletArrive(event);
	}

	@Test
	public void commandFinished() throws Exception {
		EfcsClientConfig.setUrl("http://localhost:8080");
		CommandFeedbackEvent event = new CommandFeedbackEvent();
		event.setHouse_no("29");
		event.setPallet_no("L29B1001");
		event.setCom_status(ExecuteStatus.Finished);
		event.setCom_no("1396");
		event.setDevice_no("2090");
		event.setLocation("2090");
		EfcsClient.command(event);
//        ApplicationEventPublisher.trigger(event);
//		procedureAppService.commandFinished(event);
	}

	@Test
	public void procedureArrive() throws Exception {
		procedureAppService.procedureArrive("29", "ocv1", "L29B0001");
	}

	@Test
	public void procedureFinished() throws Exception {
		procedureAppService.procedureFinished("29", "ocv1", "L29B0001");
	}

	@Test
	public void redistributeStorageLocation() throws Exception {
		procedureAppService.redistributeStorageLocation(null, null, null);
	}
	
	@Test
	public void changeProcedure() throws Exception {
		procedureAppService.changeProcedure("29", "L26B0326", "3220",WorkProcedure.Test_PalletMove, false, "手动更新");
	}
	
	@Test
	public void changeToErrorProcedure() throws Exception {
		procedureAppService.changeToErrorProcedure(null, null, null, null);
	}
	

    @Autowired
    FmProcedureService fmProcedureService;
    @Autowired
    @Qualifier("detaultLocationAllotService")
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


    private String cuttentProcedureRoute = ProcedureConfig.Formation_Rework;
    private WorkProcedure currentWorkProcedure = WorkProcedure.Formation_Rework;
    private WorkProcedure nextProcedure = WorkProcedure.Formation_Split;
	
	@Test
	public void name() throws Exception {

//		FmProcedure procedure = fmProcedureService.getProcedureByPallet("L26B0326", WorkProcedure.Formation);
//
//		fmProcedureService.saveFmProcedure(procedure);
//		
//		StorageLocation locations = locationAllotQyery.getEquipLocation("29", "")
//		//库存从 上架货位 移动到高温库位
//		storageService.storageTransfer("277d74c6-ef02-4d7f-948e-caaac11c0e13", locations.get(0).getId(), procedureInfo.getPallet_no(), procedure.getForm_no(), currentWorkProcedure.toString());
//		dispatchService.updatePalletStatusAndPos(procedureInfo.getPalletDispatch(), PalletStatus.Out_Finished, currentWorkProcedure, PalletDispatchStatus.Dispatching, PositionType.Storage_Location,
//				procedureInfo.getCurrentPos(), procedure.getForm_no());
//		if (!StringUtl.isEmpty(procedure.getIn_loc_no())) {
//			storageLocationService.setIsHasGood(procedure.getHouse_id(), procedure.getIn_loc_no(), false);
//		}

	}
}
