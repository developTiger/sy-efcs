package com.wxzd.efcs.business.domain.service.impl;

import com.wxzd.efcs.business.domain.entities.FcsToolSchedulerLocations;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.service.FcsToolSchedulerLocationService;
import com.wxzd.efcs.business.repositorys.FcsToolSchedulerLocationsRepository;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by zhouzh on 2017/5/29.
 */
@Service
public class FcsToolSchedulerLocationServiceImpl implements FcsToolSchedulerLocationService {


    @Autowired
    FcsToolSchedulerLocationsRepository fcsToolSchedulerLocationsRepository;


//    @Override
//    public GaiaResult addOrUpdateLocationService(FcsToolSchedulerLocations locations) {
//         fcsToolSchedulerLocationsRepository.saveById(locations);
//
//         return GaiaResultFactory.getSuccess();
//    }
//
//    @Override
//    public GaiaResult setLocationStatus(UUID id, AppointmentStatus schedulerStatus) {
//        FcsToolSchedulerLocations locations = fcsToolSchedulerLocationsRepository.getById(id);
//        locations.setAppointment_status(schedulerStatus);
//        fcsToolSchedulerLocationsRepository.saveById(locations);
//
//        return GaiaResultFactory.getSuccess();
//
//    }
//
//    @Override
//    public GaiaResult deleteLocations(UUID uuid) {
//        fcsToolSchedulerLocationsRepository.deleteById(uuid);
//        return GaiaResultFactory.getSuccess();
//    }
}
