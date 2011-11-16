package fr.keemto.scheduling;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.AccountKey;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ScheduledTaskUpdaterTest {

    private AccountInterceptor updater;
    private TaskRegistrar registrar;
    private FetchingTaskFactory taskFactory;


    @Before
    public void setUp() throws Exception {
        registrar = mock(TaskRegistrar.class);
        taskFactory = mock(FetchingTaskFactory.class);
        updater = new ScheduledTaskUpdater(registrar, taskFactory);
    }

    @Test
    public void whenAccountIsCreatedShouldCreateATask() throws Exception {

        AccountKey key = mock(AccountKey.class);

        updater.accountCreated(key);

        verify(taskFactory).createIncrementalTask(key);
    }

    @Test
    public void whenAccountIsCreatedShouldAddTaskToRegistrar() throws Exception {

        AccountKey key = mock(AccountKey.class);
        IncrementalFetchingTask task = mock(IncrementalFetchingTask.class);
        when(taskFactory.createIncrementalTask(key)).thenReturn(task);

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
