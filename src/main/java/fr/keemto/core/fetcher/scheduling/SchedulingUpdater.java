package fr.keemto.core.fetcher.scheduling;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.AccountKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulingUpdater implements AccountInterceptor {
    private final TaskRegistrar registrar;
    private final FetchingTaskFactory taskFactory;

    @Autowired
    public SchedulingUpdater(TaskRegistrar registrar, FetchingTaskFactory taskFactory) {
        this.registrar = registrar;
        this.taskFactory = taskFactory;
    }

    @Override
    public void accountCreated(AccountKey key) {
        FetchingTask task = taskFactory.createTask(key);
        registrar.registerTask(task);
    }

    @Override
    public void accountDeleted(AccountKey key) {
        registrar.findAndCancelTask(key);
    }
}
