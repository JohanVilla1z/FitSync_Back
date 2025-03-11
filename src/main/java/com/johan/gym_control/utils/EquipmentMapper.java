package com.johan.gym_control.utils;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentRequestDTO;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

  /**
   * Convierte un DTO de solicitud a una entidad Equipment
   *
   * @param requestDTO DTO con los datos de solicitud
   * @return Entidad Equipment
   */
  public Equipment toEntity(EquipmentRequestDTO requestDTO) {
    if (requestDTO == null) {
      return null;
    }

    Equipment equipment = new Equipment();
    equipment.setEqName(requestDTO.getName());
    equipment.setEqDescription(requestDTO.getDescription());
    equipment.setEqAvailable(requestDTO.getAvailable());
    return equipment;
  }

  /**
   * Convierte una entidad Equipment a un DTO de respuesta
   *
   * @param equipment Entidad Equipment
   * @return DTO de respuesta
   */
  public EquipmentResponseDTO toDto(Equipment equipment) {
    if (equipment == null) {
      return null;
    }

    return EquipmentResponseDTO.builder().id(equipment.getEqId()).name(equipment.getEqName()).description(equipment.getEqDescription()).available(equipment.getEqAvailable()).currentLoans(equipment.getEntryLogs() != null ? equipment.getEntryLogs().size() : 0).build();
  }

  /**
   * Actualiza una entidad existente con los datos del DTO de solicitud
   *
   * @param equipment  Entidad a actualizar
   * @param requestDTO DTO con los nuevos datos
   */
  public void updateEntityFromDto(Equipment equipment, EquipmentRequestDTO requestDTO) {
    if (equipment == null || requestDTO == null) {
      return;
    }

    equipment.setEqName(requestDTO.getName());
    equipment.setEqDescription(requestDTO.getDescription());
    equipment.setEqAvailable(requestDTO.getAvailable());
  }
}