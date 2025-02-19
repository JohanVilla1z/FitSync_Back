package com.johan.gym_control.services.interfaces;

public interface ICommandParametrized<T, R> {
    T execute(R parameter);
}