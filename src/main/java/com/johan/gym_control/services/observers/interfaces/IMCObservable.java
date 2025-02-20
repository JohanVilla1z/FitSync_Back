package com.johan.gym_control.services.observers.interfaces;

public interface IMCObservable {
  void addObserver(IMCObserver observer);

  void removeObserver(IMCObserver observer);

  void notifyObservers();
}
