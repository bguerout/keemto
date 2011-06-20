package fr.xevents.core.fetcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ScheduledFuture;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.TaskScheduler;

import com.google.common.collect.Lists;

import fr.xevents.core.User;

public class FetchingRegistrarTest {

    private FetchingRegistrar registrar;
    private TaskScheduler scheduler;
    private User user;

    @Before
    public void initBeforeTest() throws Exception {
        user = new User("bguerout");
        scheduler = mock(TaskScheduler.class);
        registrar = new FetchingRegistrar(scheduler);

    }

    @Test
    public void shouldAutomaticallyRegisterTaskToScheduler() throws Exception {
        EventTask task = mock(EventTask.class);
        when(task.getUser()).thenReturn(user);

        registrar.registerTask(task);

        verify(scheduler).scheduleWithFixedDelay(task, task.getDelay());
    }

    @Test
    public void shouldRegisterAllTasks() throws Exception {
        EventTask task = mock(EventTask.class);
        when(task.getUser()).thenReturn(user);
        EventTask anotherTask = mock(EventTask.class);
        when(anotherTask.getUser()).thenReturn(user);

        registrar.registerTasks(Lists.newArrayList(task, anotherTask));

        verify(scheduler, timeout(2)).scheduleWithFixedDelay(task, task.getDelay());
    }

    @Test
    public void shouldCancelTAsksForUser() throws Exception {
        EventTask task = mock(EventTask.class);
        when(task.getUser()).thenReturn(user);
        ScheduledFuture<?> future = mock(ScheduledFuture.class);
        when(scheduler.scheduleWithFixedDelay(task, task.getDelay())).thenReturn(future);

        registrar.registerTask(task);
        registrar.cancelTasks(user);

        verify(future).cancel(false);

    }

}
