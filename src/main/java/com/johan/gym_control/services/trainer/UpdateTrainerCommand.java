package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateTrainerCommand implements ICommandParametrized<Trainer, Trainer> {

    @Autowired
    private TrainerRepository trainerRepository;

    @Override
    public Trainer execute(Trainer trainer) {
        // Verify trainer exists
        Trainer existingTrainer = trainerRepository.findById(trainer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainer.getId()));

        // Update fields
        existingTrainer.setName(trainer.getName());
        existingTrainer.setEmail(trainer.getEmail());
        // Don't update password here unless specifically handling password changes
        existingTrainer.setIsActive(trainer.getIsActive());

        // Save and return updated trainer
        return trainerRepository.save(existingTrainer);
    }
}