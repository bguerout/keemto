package fr.keemto.web;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.fetcher.scheduling.FetchingTask;
import fr.keemto.core.fetcher.scheduling.FetchingTaskFactory;
import fr.keemto.core.fetcher.scheduling.TaskRegistrar;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class AccountInterceptorTest {

    private AccountInterceptor interceptor;
    private TaskRegistrar registrar;
    private FetchingTaskFactory taskFactory;


    @Before
    public void setUp() throws Exception {
        registrar = mock(TaskRegistrar.class);
        taskFactory = mock(FetchingTaskFactory.class);
        interceptor = new AccountInterceptor(registrar, taskFactory);
    }

    @Test
    public void whenAccountIsCreatedShouldCreateATask() throws Exception {

        Account account = mock(Account.class);
        AccountKey key = mock(AccountKey.class);
        when(account.getKey()).thenReturn(key);
        interceptor.accountCreated(account);

        verify(taskFactory).createTask(key);
    }

    @Test
    public void whenAccountIsCreatedShouldAddTaskToRegistrar() throws Exception {

        Account account = mock(Account.class);
        FetchingTask task = mock(FetchingTask.class);
        when(taskFactory.createTask(any(AccountKey.class))).thenReturn(task);

        interceptor.accountCreated(account);

        verify(registrar).registerTask(task);

    }

    @Test
    public void whenAccountIsDeletedShouldCancelTaskInRegistrar() throws Exception {

        AccountKey key = mock(AccountKey.class);

        interceptor.accountDeleted(key);

        verify(registrar).findAndCancelTask(key);

    }
}
