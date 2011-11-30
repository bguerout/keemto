package fr.keemto.scheduling;

import com.google.common.collect.Lists;
import fr.keemto.core.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticApplicationContext;

import java.util.List;

import static org.mockito.Mockito.*;

public class AutoTaskRegistrationTest {

    private AutoTaskRegistration registration;
    private TaskRegistrar taskRegistrar;

    @Before
    public void setUp() throws Exception {
        registration = new AutoTaskRegistration();
        taskRegistrar = mock(TaskRegistrar.class);
        registration.setTaskRegistrar(taskRegistrar);
    }

    @Test
    public void onCoreContextRefreshShouldRegisterAllTasks() throws Exception {

        StaticApplicationContext coreContext = new StaticApplicationContext();
        coreContext.setParent(null);
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(coreContext);
        List<Task> tasks = Lists.newArrayList(mock(Task.class));
        registration.setDiscoveredTasks(tasks);

        registration.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar).registerFetchingTasksForAllUsers();
        verify(taskRegistrar).registerTasks(tasks);

    }

    @Test
    public void shouldIgnoreRefreshOfChildrenContext() throws Exception {

        StaticApplicationContext childrenContext = new StaticApplicationContext();
        childrenContext.setParent(new StaticApplicationContext());
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(childrenContext);

        registration.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar, never()).registerFetchingTasksForAllUsers();

    }
}
