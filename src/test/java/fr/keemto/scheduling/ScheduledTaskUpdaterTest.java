package fr.keemto.scheduling;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.AccountKey;
import fr.keemto.core.fetching.FetchingTaskFactory;
import fr.keemto.core.fetching.IncrementalFetchingTask;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ScheduledTaskUpdaterTest {

    private AccountInterceptor updater;
    private TaskScheduler scheduler;
    private FetchingTaskFactory taskFactory;


    @Before
    public void setUp() throws Exception {
        scheduler = mock(TaskScheduler.class);
        taskFactory = mock(FetchingTaskFactory.class);
        updater = new FetchingTaskUpdater(scheduler, taskFactory);
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

        verify(scheduler).scheduleTask(task);

    }

    @Test
    public void whenAccountIsDeletedShouldCancelTaskInRegistrar() throws Exception {

        AccountKey key = mock(AccountKey.class);

        updater.accountDeleted(key);

        verify(scheduler).cancelTask(key);

    }
}
