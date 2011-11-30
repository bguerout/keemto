package fr.keemto.scheduling;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.AccountKey;
import fr.keemto.core.fetching.FetchingTask;
import fr.keemto.core.fetching.FetchingTaskFactory;

public class FetchingTaskUpdater implements AccountInterceptor {
    private final TaskScheduler scheduler;
    private final FetchingTaskFactory taskFactory;

    public FetchingTaskUpdater(TaskScheduler scheduler, FetchingTaskFactory taskFactory) {
        this.scheduler = scheduler;
        this.taskFactory = taskFactory;
    }

    @Override
    public void accountCreated(AccountKey key) {
        FetchingTask task = taskFactory.createIncrementalTask(key);
        scheduler.scheduleTask(task);
    }

    @Override
    public void accountDeleted(AccountKey key) {
        scheduler.cancelTask(key);
    }
}
