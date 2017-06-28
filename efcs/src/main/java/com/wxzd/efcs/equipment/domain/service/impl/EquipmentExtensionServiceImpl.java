package com.wxzd.efcs.equipment.domain.service.impl;

import com.wxzd.efcs.equipment.application.dtos.EquipmentExtensionDto;
import com.wxzd.efcs.equipment.domain.entities.EquipmentExtension;
import com.wxzd.efcs.equipment.domain.service.EquipmentExtensionService;
import com.wxzd.efcs.equipment.repositorys.EquipmentExtensionRepository;
import com.wxzd.gaia.common.base.bean.BeanUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.GaiaResultFactory;
import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/17
 */
@Service
public class EquipmentExtensionServiceImpl implements EquipmentExtensionService {

    @Resource
    private EquipmentExtensionRepository repository;

    @Override
    public EquipmentExtensionDto queryById(UUID id) {
        EquipmentExtension equipmentExtension = repository.getById(id);
        if (equipmentExtension != null)
            return BeanUtl.copyProperties(equipmentExtension, EquipmentExtensionDto.class);
        return null;
    }
    @Transaction
    @Override
    public GaiaResult addOrUpdateEquipmentExtension(EquipmentExtensionDto dto) {
        EquipmentExtension currentDto = new EquipmentExtension();
        if (dto.getId() != null) {
            currentDto = repository.getById(dto.getId());
            if (currentDto == null) return GaiaResultFactory.getError("信息不存在!");
        }
        BeanUtl.copyPropertiesIgnoreNull(dto, currentDto);
        repository.saveById(currentDto);
        return GaiaResultFactory.getSuccess("操作成功!");
    }

    @Override
    public GaiaResult extensionCheck(UUID equipId, String extKey) {
        if (StringUtl.isEmpty(extKey) || StringUtl.isEmpty(extKey)) return GaiaResultFactory.getError("设备ID/扩展信息键值为空!");
        EquipmentExtension extension = repository.getColumns(extKey);
        if (extension == null || equipId.equals(extension.getEquip_id())) return GaiaResultFactory.getSuccess();
        return GaiaResultFactory.getError("扩展信息键值重复!");
    }

    @Transaction
    @Override
    public GaiaResult deleteEquipmentExtensionById(UUID id) {
        EquipmentExtension equipmentExtension = repository.getById(id);
        if (equipmentExtension!=null) {
            repository.deleteById(id);
            return GaiaResultFactory.getSuccess("删除成功!");
        }
        return GaiaResultFactory.getError("信息不存在!");
    }




}
