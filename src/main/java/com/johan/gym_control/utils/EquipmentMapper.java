package com.johan.gym_control.utils;

import org.springframework.stereotype.Component;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentRequestDTO;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;

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
    equipment.setEqId(requestDTO.getId());
    equipment.setEqName(requestDTO.getName());
    equipment.setEqDescription(requestDTO.getDescription());
    equipment.setEqStatus(requestDTO.getStatus());
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

    return EquipmentResponseDTO.builder()
        .id(equipment.getEqId())
        .name(equipment.getEqName())
        .description(equipment.getEqDescription())
        .status(equipment.getEqStatus())
        .currentLoans(equipment.getEntryLogs() != null ? equipment.getEntryLogs().size() : 0)
        .loanCount(equipment.getEqLoanCount())
        .build();
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

    if (requestDTO.getName() != null)
      equipment.setEqName(requestDTO.getName());
    if (requestDTO.getDescription() != null)
      equipment.setEqDescription(requestDTO.getDescription());
    if (requestDTO.getStatus() != null)
      equipment.setEqStatus(requestDTO.getStatus());
  }
}