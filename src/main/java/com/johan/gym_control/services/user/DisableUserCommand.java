package com.johan.gym_control.services.user;

import java.util.Optional;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class DisableUserCommand implements ICommandParametrized<Void, Long> {
    private final UserRepository userRepository;

    public DisableUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Void execute(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(false);
            userRepository.save(user);
        }
        return null;
    }
}
