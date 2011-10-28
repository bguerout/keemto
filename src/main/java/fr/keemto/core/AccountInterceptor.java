package fr.keemto.core;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.fetcher.scheduling.FetchingTask;
import fr.keemto.core.fetcher.scheduling.FetchingTaskFactory;
import fr.keemto.core.fetcher.scheduling.TaskRegistrar;

public interface AccountInterceptor {

    public void accountCreated(AccountKey key);

    public void accountDeleted(AccountKey key);
}
