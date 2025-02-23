package com.johan.gym_control.services.user;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommand;

import java.util.List;

public class GetAllUsersCommand implements ICommand<List<User>> {
    private final UserRepository userRepository;

    public GetAllUsersCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> execute() {
        return userRepository.findAll();
    }
}