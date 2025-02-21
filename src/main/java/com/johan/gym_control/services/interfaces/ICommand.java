package com.johan.gym_control.services.interfaces;

public interface ICommand<T> {
    T execute();
}