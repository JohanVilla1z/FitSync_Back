package com.johan.gym_control.controllers.equipment;

import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.equipment.DeleteEquipmentCommand;
import com.johan.gym_control.services.equipment.GetEquipmentByIdCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
public class DeleteEquipmentController {

  private final EquipmentRepository equipmentRepository;

  public DeleteEquipmentController(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
    GetEquipmentByIdCommand getByIdCommand = new GetEquipmentByIdCommand(equipmentRepository);

    if (getByIdCommand.execute(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    DeleteEquipmentCommand deleteCommand = new DeleteEquipmentCommand(equipmentRepository);
    deleteCommand.execute(id);

    return ResponseEntity.noContent().build();
  }
}