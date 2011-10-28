package fr.keemto.core.fetcher.scheduling;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.AccountKey;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SchedulingUpdaterTest {

    private AccountInterceptor updater;
    private TaskRegistrar registrar;
    private FetchingTaskFactory taskFactory;


    @Before
    public void setUp() throws Exception {
        registrar = mock(TaskRegistrar.class);
        taskFactory = mock(FetchingTaskFactory.class);
        updater = new SchedulingUpdater(registrar, taskFactory);
    }

    @Test
    public void whenAccountIsCreatedShouldCreateATask() throws Exception {

        AccountKey key = mock(AccountKey.class);

        updater.accountCreated(key);

        verify(taskFactory).createTask(key);
    }

    @Test
    public void whenAccountIsCreatedShouldAddTaskToRegistrar() throws Exception {

        AccountKey key = mock(AccountKey.class);
        FetchingTask task = mock(FetchingTask.class);
        when(taskFactory.createTask(key)).thenReturn(task);

        updater.accountCreated(key);

        verify(registrar).registerTask(task);

    }

    @Test
    public void whenAccountIsDeletedShouldCancelTaskInRegistrar() throws Exception {

        AccountKey key = mock(AccountKey.class);

        updater.accountDeleted(key);

        verify(registrar).findAndCancelTask(key);

    }
}
