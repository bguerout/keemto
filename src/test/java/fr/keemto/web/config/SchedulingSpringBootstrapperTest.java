package fr.keemto.web.config;

import fr.keemto.core.Task;
import fr.keemto.core.TaskRegistry;
import fr.keemto.scheduling.TaskRegistrar;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SchedulingSpringBootstrapperTest {

    private TaskRegistry taskRegistry;
    private TaskRegistrar taskRegistrar;
    private SchedulingSpringBootstrapper bootstrapper;

    @Before
    public void setUp() throws Exception {
        taskRegistrar = mock(TaskRegistrar.class);
        taskRegistry = mock(TaskRegistry.class);
        bootstrapper = new SchedulingSpringBootstrapper(taskRegistrar, taskRegistry);
    }

    @Test
    public void onWebContextRefreshShouldCreateFetchingTasks() throws Exception {

        StaticApplicationContext webContext = spy(new StaticApplicationContext());
        webContext.setParent(new StaticApplicationContext());
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(webContext);
        List<Task> tasks = new ArrayList<Task>();
        when(taskRegistry.findTasks()).thenReturn(tasks);

        bootstrapper.onApplicationEvent(refreshedEvent);

        verify(taskRegistry).findTasks();
        verify(taskRegistrar).registerTasks(tasks);

    }

    @Test
    public void shouldIgnoreRefreshOfCoreContext() throws Exception {

        StaticApplicationContext context = new StaticApplicationContext();
        context.setParent(null);
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(context);


        bootstrapper.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar,never()).registerTasks(anyListOf(Task.class));

    }

}
