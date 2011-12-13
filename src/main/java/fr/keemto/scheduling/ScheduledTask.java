package fr.keemto.scheduling;

import fr.keemto.core.Task;

import java.util.concurrent.ScheduledFuture;

public class ScheduledTask implements Task {
    private static final boolean INTERRUPT_IF_RUNNING = true;
    private final ScheduledFuture<?> future;
    private final Task task;

    ScheduledTask(Task task, ScheduledFuture<?> future) {
        this.task = task;
        this.future = future;
    }

    @Override
    public long getDelay() {
        return task.getDelay();
    }

    @Override
    public void run() {
    }

    void cancel() {
        future.cancel(INTERRUPT_IF_RUNNING);
    }

    @Override
    public String getTaskId() {
        return task.getTaskId();
    }

    @Override
    public String toString() {
        return "ScheduledTask{" +
                "task=" + task +
                '}';
    }
}
