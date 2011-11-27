package fr.keemto.scheduling;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.AccountKey;
import fr.keemto.core.fetching.FetchingTask;
import fr.keemto.core.fetching.FetchingTaskFactory;

public class FetchingTaskUpdater implements AccountInterceptor {
    private final TaskRegistrar registrar;
    private final FetchingTaskFactory taskFactory;

    public FetchingTaskUpdater(TaskRegistrar registrar, FetchingTaskFactory taskFactory) {
        this.registrar = registrar;
        this.taskFactory = taskFactory;
    }

    @Override
    public void accountCreated(AccountKey key) {
        FetchingTask task = taskFactory.createIncrementalTask(key);
        registrar.registerTask(task);
    }

    @Override
    public void accountDeleted(AccountKey key) {
        registrar.cancelTask(key);
    }
}
