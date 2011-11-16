package fr.keemto.scheduling;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.AccountKey;
import fr.keemto.core.fetching.FetchingTaskFactory;
import fr.keemto.core.fetching.IncrementalFetchingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskUpdater implements AccountInterceptor {
    private final TaskRegistrar registrar;
    private final FetchingTaskFactory taskFactory;

    @Autowired
    public ScheduledTaskUpdater(TaskRegistrar registrar, FetchingTaskFactory taskFactory) {
        this.registrar = registrar;
        this.taskFactory = taskFactory;
    }

    @Override
    public void accountCreated(AccountKey key) {
        IncrementalFetchingTask task = taskFactory.createIncrementalTask(key);
        registrar.registerTask(task);
    }

    @Override
    public void accountDeleted(AccountKey key) {
        registrar.findAndCancelTask(key);
    }
}
