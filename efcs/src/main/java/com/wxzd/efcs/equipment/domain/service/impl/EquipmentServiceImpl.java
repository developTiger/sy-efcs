package com.wxzd.efcs.equipment.domain.service.impl;

import com.wxzd.efcs.equipment.application.dtos.EquipmentDto;
import com.wxzd.efcs.equipment.domain.entities.Equipment;
import com.wxzd.efcs.equipment.domain.service.EquipmentService;
import com.wxzd.efcs.equipment.repositorys.EquipmentRepository;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/16
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Resource
    private EquipmentRepository repository;

    @Override
    public EquipmentDto getById(UUID id) {
        Equipment equipment = repository.getById(id);
        if (equipment != null)
            return BeanUtl.copyProperties(equipment, EquipmentDto.class);
        return null;
    }
    @Transaction
    @Override
    public GaiaResult addOrUpdateEquipment(EquipmentDto equipmentDto) {
        Equipment currentDto = new Equipment();
        if (equipmentDto.getId() != null) {
            currentDto = repository.getById(equipmentDto.getId());
            if (currentDto == null) return GaiaResultFactory.getError("设备不存在!");
        }
        BeanUtl.copyPropertiesIgnoreNull(equipmentDto, currentDto);
        repository.saveById(currentDto);
        return GaiaResultFactory.getSuccess("操作成功");
    }

    @Override
    public GaiaResult equipmentCheck(UUID id, String equip_name, String equip_no) {
        if (StringUtl.isEmpty(equip_name) || StringUtl.isEmpty(equip_no)) return GaiaResultFactory.getError("名称编号不为空");
        List<Equipment> list = repository.queryByColumn(id, equip_name, equip_no);
        if (id == null && list.size() == 0) return GaiaResultFactory.getSuccess();
        for (Equipment equipment : list) {
            if (id != null && id.equals(equipment.getId())) {
                continue;
            } else {
                if (equip_name.equals(equipment.getEquip_name())) {
                    return GaiaResultFactory.getError("设备名称重复!");
                }
                if (equip_no.equals(equipment.getEquip_no())) {
                    return GaiaResultFactory.getError("设备编号重复!");
                }
            }
        }
        return GaiaResultFactory.getSuccess();
    }
}
