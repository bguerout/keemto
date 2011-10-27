package fr.keemto.web;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.fetcher.scheduling.FetchingTask;
import fr.keemto.core.fetcher.scheduling.FetchingTaskFactory;
import fr.keemto.core.fetcher.scheduling.TaskRegistrar;

public class AccountInterceptor {
    private final TaskRegistrar registrar;
    private final FetchingTaskFactory taskFactory;

    public AccountInterceptor(TaskRegistrar registrar, FetchingTaskFactory taskFactory) {
        this.registrar = registrar;
        this.taskFactory = taskFactory;
    }

    public void accountCreated(Account account) {
        AccountKey key = account.getKey();
        FetchingTask task = taskFactory.createTask(key);
        registrar.registerTask(task);
    }

    public void accountDeleted(AccountKey key) {
        registrar.findAndCancelTask(key);
    }
}
