package test.wxzd.efcs.application.business;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.application.service.ProcedureAppService;
import com.wxzd.efcs.business.application.workProcedures.ProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.TestProcedureRouteMatch;
import com.wxzd.efcs.business.application.workProcedures.factory.ConvertFactory;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.efcs.business.domain.service.FmPalletizeService;
import com.wxzd.efcs.business.domain.service.PalletDispatchService;
import com.wxzd.efcs.business.repositorys.PalletDetailRepository;
import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.equipment.domain.enums.EquipmentType;
import com.wxzd.efcs.equipment.domain.service.EquipmentService;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.json.JsonUtl;
import com.wxzd.policy.locationAllot.LocationAllotQyery;
import com.wxzd.protocol.wcs.battery.BatteryInContainer;
import com.wxzd.protocol.wcs.battery.GrabType;
import com.wxzd.protocol.wcs.battery.feedback.BatteryCheckFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFeedbackEvent;
import com.wxzd.protocol.wcs.battery.feedback.BatteryGrabFinishedFeedbackEvent;
import com.wxzd.wms.core.application.queryService.WarehouseRowQueryService;
import com.wxzd.wms.core.application.querys.WarehouseRowQuery;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.Warehouse;
import com.wxzd.wms.core.domain.entities.WarehouseRow;
import com.wxzd.wms.core.domain.entities.enums.StorageType;
import com.wxzd.wms.core.domain.service.StorageLocationService;
import com.wxzd.wms.core.domain.service.StorageService;
import com.wxzd.wms.core.domain.service.WareHouseService;
import com.wxzd.wms.core.domain.service.WarehouseRowService;
import com.wxzd.wms.core.repositorys.StorageLocationRepository;
import com.wxzd.wms.core.repositorys.WarehouseRowRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.wxzd.efcs.application.mold.SpringDemoBase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/19.
 */
public class inTestDemon extends SpringDemoBase {


    @Autowired
    ProcedureAppService fmProcedureAppService;


    @Autowired
    PalletDispatchService palletDispatchService;

    @Autowired
    EquipmentService equipmentService;


    @Autowired
    StorageService storageService;

    @Autowired
    WareHouseService wareHouseService;


    @Autowired
    PalletDetailRepository palletDetailRepository;

    private UUID houseId = UUID.fromString("277d74c6-ef02-4d7f-948e-caaac11c0e13");
//
//    @Test
//    public void addEquipment() {
//        EquipmentDto dto = new EquipmentDto();
//        dto.setHouse_id(houseId);
//        dto.setEquip_name("入库口设备");
//        dto.setEquip_no("hczpjxs");
//        dto.setEquip_type(EquipmentType.Line);
////        dto.setHuose_no("29");
//        dto.setLocation_no("hczpjxs");
//        GaiaResult result = equipmentService.equipmentCheck(null, dto.getEquip_name(), dto.getEquip_no());
//        if (result.isSuccess()) {
//            result = equipmentService.addOrUpdateEquipment(dto);
//        }
//        System.out.printf(JsonUtl.parse(result));
//
//    }
//
//
//    @Test
//    public void testIn() {
//
//        List<BatteryInContainer> batteryInContainers = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            BatteryInContainer batteryInContainer = new BatteryInContainer();
//            batteryInContainer.setBattery_no("LA29_" + i);
//            batteryInContainer.setBattery_status("2");
//            batteryInContainer.setPos_channel_no(1);
//            batteryInContainer.setInfo("坏了");
//            batteryInContainers.add(batteryInContainer);
//        }
//
//        BatteryCheckFeedbackEvent e = new BatteryCheckFeedbackEvent();
//        e.setDevice_no(DispatcherConfig.formation_palletize_device_no);
//        e.setHouse_no("29");
//        e.setBatteries(batteryInContainers);
//
//        try {
//            fmProcedureAppService.batteryIn(e);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//
//
////        System.out.printf(JsonUtl.parse(result));
//
//
//    }
//
//    @Test
//    public void Shott() {
//
//        String route = ProcedureRouteMatch.getRouteByEquipNo("row3", "row4");
//        String route2 = ProcedureRouteMatch.getRouteByEquipNo("1230", "1170");
//        System.out.println(route);
//        System.out.println(route2);
//        //String no= SerialNoGenerator.getSerialNo("INS");
//        System.out.printf("no");
//
//    }
//
//    @Test
//    public void newDispatch() {
//        PalletDispatch dispatch = new PalletDispatch();
//        dispatch.setHouse_id(houseId);
//        dispatch.setPallet_status(PalletStatus.In_Waiting);
//        dispatch.setWork_procedure(WorkProcedure.Formation_PalletMove);
//        dispatch.setContainer_id("d2dca66d-b165-456d-8300-367be237acf6");
//        dispatch.setContainer_no("T1001");
//        dispatch.setDispatch_status(PalletDispatchStatus.Dispatching);
//        dispatch.setIs_empty(true);
//
//
//        palletDispatchService.newPalletDispatch(dispatch);
//
//        //  (UUID houseId,UUID locationId, String palletNo,String businessType, String businessNo, String processId)
//        storageService.palletInStorage(houseId, UUID.fromString("1867c3f7-0237-40ad-ad8e-8f5b34e8e541"),
//                "T1001", WorkProcedure.Formation_PalletMove.toString(), "", "");
//
//    }
//
//    @Test
//    public void palletmove() throws Exception {
//        fmProcedureAppService.procedureArrive("29", "2060", "T1001");
//    }
//
//    @Test
//    public void palletizeComplete() throws Exception {
//        BatteryGrabFinishedFeedbackEvent event = new BatteryGrabFinishedFeedbackEvent();
//        event.setHouse_no("29");
//        event.setPallet_no("T1001");
//        event.setGrab_type(GrabType.Palletize);
//        event.setDevice_no("LA030301");
//
//
//        fmProcedureAppService.batteryGrabFinished(event);
//
//
//    }
//
//
//    @Test
//    public void finish() throws Exception {
//        fmProcedureAppService.procedureFinished("29", "LA030301", "T1001");
//    }
//
//    @Test
//    public void palletize() throws Exception {
//
//        BatteryGrabFeedbackEvent event = new BatteryGrabFeedbackEvent();
//        event.setHouse_no("29");
//        event.setDevice_no("hczpjxs");
//        event.setBattery_no("LA29_1");
//        event.setClamp_no("1");
//        event.setTo_pos_no("1");
//        event.setTo_pallet_no("T1001");
//        event.setTo_pos_channel_no(2);
//        event.setFrom_pos_no("1");
//        event.setFrom_pos_channel_no(1);
//        fmProcedureAppService.batteryGrab(event);
//
//
//    }
//
//    @Test
//    public void route() throws Exception {
//
//
//        String route = ProcedureRouteMatch.getRouteByEquipNo("2260", "1230");
//        System.out.printf(route);
//    }
//
//    @Test
//    public void route1() throws Exception {
//
//        palletDetailRepository.getActivePalletDetailByPalletWithStatus("123456");
//    }
//
//    @Autowired
//    LocationAllotQyery locationAllotQyery;
//
//    @Autowired
//    StorageLocationRepository storageLocationRepository;
//
//    @Autowired
//    WarehouseRowRepository warehouseRowRepository;
//
//    @Test
//    public void initRow() throws Exception {
//        Warehouse warehouse1 = wareHouseService.getByNo("29");
//        Warehouse warehouse = wareHouseService.getByNo("28");
//        List<WarehouseRow> warehouseRows = locationAllotQyery.getAllWarehouseRowsByHouseId(warehouse1.getId());
//        for (WarehouseRow row : warehouseRows) {
//            row.setId(null);
//            row.setHouse_id(warehouse.getId());
//        }
//        warehouseRowRepository.saveById(warehouseRows);
//
//    }
//
//    @Test
//    public void initHouseLocation() throws Exception {
//        Warehouse warehouse1 = wareHouseService.getByNo("29");
//        Warehouse warehouse = wareHouseService.getByNo("28");
//
//        List<StorageLocation> locations = locationAllotQyery.getAllStorageLocationByHouseId(warehouse1.getId());
//
//        for (StorageLocation l : locations) {
//            l.setId(null);
//            l.setHouse_id(warehouse.getId());
//            if (l.getX_pos().equals(3) || l.getX_pos().equals(4)) {
//                l.setForbid_in(0);
//                l.setForbid_out(0);
//                l.setBusiness_in_lock(0);
//                l.setBusiness_out_lock(0);
//            }
//        }
//        storageLocationRepository.saveById(locations);
//
//    }
//
//    @Autowired
//    FmPalletizeService fmPalletizeService;
//    @Test
//    public void testProcedure(){
//
//        String palletNo= "L29B0315";
//        PalletDispatch dispatch = palletDispatchService.getByPalletNo(palletNo);
//        FmPalletize procedure = fmPalletizeService.getProcedureByPallet(palletNo);
//
//        if (dispatch==null)
//            return;
//        if(procedure == null) return;
//
//        PalletDispatch dis = ConvertFactory.convertOldDispatchToNewOne(dispatch);
//        dis.setDispatch_status(PalletDispatchStatus.Dispatching);
//        dis.setWork_procedure(WorkProcedure.Formation_Rework_Palletize);
//        palletDispatchService.updatePalletStatusAndPos(dispatch, PalletStatus.In_Finished, WorkProcedure.Formation_Split, PalletDispatchStatus.Finished, PositionType.Transport_Location, "人工调整", "");
//
//        UUID id = palletDispatchService.newPalletDispatch(dis);
//        procedure.setPallet_cargo_id(id);
//        fmPalletizeService.saveFmPalletize(procedure);
//
//    }
//    @Autowired
//    StorageLocationService storageLocationService ;
//    
//    @Test
//    public void  changeStorage(){
//    	
//    	Warehouse warehouse =wareHouseService.getByNo("29");
//    	String locno = "LA011002";
//    	String palletNo="L29B0119";
//    	StorageLocation storageLocation = storageLocationService.getLocationByLocationNo("29", locno);
//    	 //库存从 移动到库里
//        storageService.storageTransfer(
//        		warehouse.getId(),
//        		storageLocation.getId(),
//        		palletNo,
//                "",
//                "异常调整"
//        );
//    }
//    


    @Autowired
    LocationAllotQyery locationAllotQyery;
    
    
    @Test
   public void  setStorageToMannel(){

        Warehouse warehouse = wareHouseService.getByNo("29");
        List<Storage> storages = locationAllotQyery.getEquipLocationStorage(warehouse.getId(),"1010");
        System.out.println(storages.size());
        List<StorageLocation> location= locationAllotQyery.getEquipLocation(warehouse.getId(), DispatcherConfig.manual_temp_location);
        for(Storage storage:storages){
            if(storage.getSto_type()== StorageType.container){
                System.out.println(storage.getPallet_no());
                storageService.storageTransfer(warehouse.getId(), location.get(0).getId(), storage.getPallet_no(), "", "");
            }
        }
    }
    
    
    
    
    

}
