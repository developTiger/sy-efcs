package com.wxzd.policy.locationAllot;

import com.wxzd.efcs.business.domain.enums.PositionType;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.wms.core.domain.entities.Storage;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.core.domain.entities.WarehouseRow;
import com.wxzd.wms.core.domain.entities.enums.LocationType;
import com.wxzd.wms.core.domain.entities.enums.StorageStatus;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by zhouzh on 2017/4/17.
 */
@Repository
public class LocationAllotQyery {


    /**
     * 获取设备库位上的空托盘数量
     *
     * @param equitNo 设备号
     * @return 数量
     */
    public Integer getEquipLocationEmptyPalletCount(UUID houseId, String equitNo) {

//        String quipId = DispatcherConfig.formation_palletize_catch_location;
        String sql = "select count(*) from wms_storage s join wms_storage_location l on s.location_id = l.id    where  s.house_id =:houseId and  l.loc_no=:equitNo and  s.sto_type='container' ";
        Map<String, Object> parems = new HashMap<>();
        parems.put("houseId", houseId);

        parems.put("equitNo", equitNo);
        Integer count = DatabaseExecuter.queryNumber(sql, parems).intValue();

        return count;

    }


    /**
     * 获取设备库位库存
     *
     * @param equitNo
     * @return
     */
    public List<Storage> getEquipLocationStorage(UUID houseId, String equitNo) {
        String sql = "select s.* from wms_storage s join wms_storage_location l on s.location_id = l.id   where  l.house_id =:houseId and  l.loc_no=:equitNo ";
        Map<String, Object> parems = new HashMap<>();
        parems.put("equitNo", equitNo);
        parems.put("houseId", houseId);
        List<Storage> locations = DatabaseExecuter.queryBeanList(sql, parems, Storage.class);

        return locations;

    }


    /**
     * 根据托盘获取库存
     *
     * @param houseId
     * @param palletNo
     * @return
     */
    public List<Storage> getStorageByPallet(UUID houseId, String palletNo) {

        GaiaQuery query = new GaiaQuery();
        Map<String, Object> parems = new HashMap<>();
        if (houseId != null) {
            parems.put("house_id", houseId);
        }
        parems.put("pallet_no", palletNo);
        List<Storage> storages = query.from(Storage.class).where(parems).select(Storage.class).toList();
        return storages;

    }


    /**
     * 根据托盘获取库存
     *
     * @param palletNo
     * @return
     */
    public List<Storage> getStorageByPallet(String palletNo) {

        return getStorageByPallet(null, palletNo);

    }

    /**
     * 根据设备号获取库位
     *
     * @param equitNo
     * @return
     */
    public List<StorageLocation> getEquipLocation(UUID houseId, String equitNo) {
        String sql = "select l.* from  wms_storage_location l   where l.house_id =:houseId and  loc_no=:equitNo ";
        Map<String, Object> parems = new HashMap<>();
        parems.put("houseId", houseId);
        parems.put("equitNo", equitNo);
        List<StorageLocation> locations = DatabaseExecuter.queryBeanList(sql, parems, StorageLocation.class);

        if (locations.size() > 1) {
            throw new RuntimeException(I18nContext.getMessage("同一库中出现重复的库位号:") + equitNo);
        }
        return locations;

    }


    /**
     * 根据设备号获取库位
     *
     * @param equitNo
     * @return
     */
    public List<StorageLocation> getEquipLocation(String houseNo, String equitNo) {
        String sql = "select l.* from  wms_storage_location l  join wms_warehouse w on l.house_id = w.id join fcs_equipment e on  l.loc_no  = e.location_no where  w.house_no =:houseNo and  equip_no=:equitNo ";
        Map<String, Object> parems = new HashMap<>();
        parems.put("houseNo", houseNo);
        parems.put("equitNo", equitNo);
        List<StorageLocation> locations = DatabaseExecuter.queryBeanList(sql, parems, StorageLocation.class);
        return locations;

    }


    /**
     * 获取排对应的空库位信息
     *
     * @param rows
     * @return
     */
    public Integer getEmptyLocationCount(UUID houseId, Collection<Integer> rows) {
        String sql = "select count(*) from wms_storage_location where house_id =:houseId and forbid_in=0 and storage_status='empty' and x_pos in (:rows)";
        Map<String, Object> parems = new HashMap<>();
        parems.put("houseId", houseId);
        parems.put("rows", rows);
        Integer count = DatabaseExecuter.queryNumber(sql, parems).intValue();

        return count;
    }

    /**
     * 根据规则获取库位
     *
     * @param rows               可分配排
     * @param rowAllotPolicyType 排策略
     * @param colAllotPolicyType 列策略
     * @return ObjectResult<StorageLocation>
     */
    public ObjectResult<StorageLocation> getStorageLocation(WorkProcedure workProcedure, UUID houseId, Collection<Integer> rows, AllotPolicyType rowAllotPolicyType, AllotPolicyType colAllotPolicyType) {
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> parems = new HashMap<>();
        parems.put("house_id", houseId);
        parems.put("x_pos", rows);
        parems.put("forbid_in", 0);
        parems.put("storage_status", StorageStatus.empty);
        parems.put("loc_is_error", false);
        parems.put("loc_type", LocationType.cubic);
        try {
            query.from(StorageLocation.class).where(parems);


            if (workProcedure == WorkProcedure.Formation || workProcedure == WorkProcedure.Formation_Rework) {
                query.orderBy("z_pos", GaiaQuery.OrderType.ASC);
                if (rowAllotPolicyType == AllotPolicyType.FromBigToSmall) {
                    query.orderBy("x_pos", GaiaQuery.OrderType.DESC);
                } else {
                    query.orderBy("x_pos", GaiaQuery.OrderType.ASC);
                }

                if (colAllotPolicyType == AllotPolicyType.FromBigToSmall) {
                    query.orderBy("y_pos", GaiaQuery.OrderType.DESC);
                } else {
                    query.orderBy("y_pos", GaiaQuery.OrderType.ASC);
                }


            } else {
                if (rowAllotPolicyType == AllotPolicyType.FromBigToSmall) {
                    query.orderBy("x_pos", GaiaQuery.OrderType.DESC);
                } else {
                    query.orderBy("x_pos", GaiaQuery.OrderType.ASC);
                }

                if (colAllotPolicyType == AllotPolicyType.FromBigToSmall) {
                    query.orderBy("y_pos", GaiaQuery.OrderType.DESC);
                } else {
                    query.orderBy("y_pos", GaiaQuery.OrderType.ASC);
                }
                query.orderBy("z_pos", GaiaQuery.OrderType.ASC);
            }

            List<StorageLocation> locations = query.select(StorageLocation.class).toList();
            if (locations.size() == 0) {
                return new ObjectResult<>(false,"无可分配库位");
            }
            for(StorageLocation location:locations){
                //防呆检测，库位是否有库存存在。有的话跳过
                List<Storage> st = this.getEquipLocationStorage(location.getHouse_id(), location.getLoc_no());
                if(st.size()==0){
                    //无库存 返回该库位
                    return GaiaResultFactory.getObject(location);
                }

            }
            return new ObjectResult<>(false,"无可分配库位");
        } catch (Exception ex) {
            throw new RuntimeException("库位分配异常！");
        }

    }

    /**
     * 生成选择的库位对象AllotStorageLocation
     *
     * @param location     库位
     * @param equipNo      设备号
     * @param positionType 库位类型
     * @return
     */
    public AllotStorageLocation createAllotStorageLocation(String houseNo, StorageLocation location, String equipNo, PositionType positionType) {
        AllotStorageLocation allotLocation = new AllotStorageLocation();
        allotLocation.setHouseNo(houseNo);
        if (positionType == PositionType.Transport_Location) {
            allotLocation.setLocationCode(equipNo);
        } else {
            allotLocation.setLocationCode(location.getLoc_no());
        }
        allotLocation.setPositionType(positionType);
        allotLocation.setStorageLocation(location);
        return allotLocation;
    }

    public List<AllotStorageLocation> createAllotStorageLocation(String houseNo, List<StorageLocation> locations, String equipNo, PositionType positionType) {
        List<AllotStorageLocation> allotLocationList = new ArrayList<>();
        for (StorageLocation storageLocation : locations) {
            AllotStorageLocation location = createAllotStorageLocation(houseNo, storageLocation, equipNo, positionType);
            allotLocationList.add(location);
        }
        return allotLocationList;
    }


    public List<StorageLocation> getAllStorageLocationByHouseId(UUID houseId){

        GaiaQuery query = new GaiaQuery();

        Map<String, Object> parems = new HashMap<>();
        parems.put("house_id", houseId);


        List<StorageLocation> locations = query.from(StorageLocation.class).where(parems).select(StorageLocation.class).toList();

        return locations;

    }




    public List<WarehouseRow> getAllWarehouseRowsByHouseId(UUID houseId){
        GaiaQuery query = new GaiaQuery();
        Map<String, Object> parems = new HashMap<>();
        parems.put("house_id", houseId);
        List<WarehouseRow> rows = query.from(WarehouseRow.class).where(parems).select(WarehouseRow.class).toList();
        return rows;

    }


    /**
     * 获取排对应的空库位信息
     *
     * @param rows
     * @return
     */
    public List<StorageLocation> getAllLocationsByRows(UUID houseId, List<Integer> rows) {
//        String sql = "select count(*) from wms_storage_location where house_id =:houseId and forbid_in=0 and storage_status='empty' and x_pos in (:rows)";
//        Map<String, Object> parems = new HashMap<>();
//        parems.put("houseId", houseId);
//        parems.put("rows", rows);
//        Integer count = DatabaseExecuter.queryNumber(sql, parems).intValue();

        return null;
    }
    public List<StorageLocation> getLocationByLocationIds(List<UUID> locationIds) {
        String sql = "select * from wms_storage_location where id in(";
        StringBuilder sb =new StringBuilder();
        for(UUID id : locationIds)
        {
            sb.append("'"+id +"',");
        }
        sb.delete(sb.lastIndexOf(","),sb.length());
        sql+=sb.toString()+") and is_active=1";

        List<StorageLocation> locations = DatabaseExecuter.queryBeanList(sql,StorageLocation.class);

        return locations;
    }

}
