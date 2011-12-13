package fr.keemto.core;

public interface Task extends Runnable {

    long getDelay();

    String getTaskId();
}
