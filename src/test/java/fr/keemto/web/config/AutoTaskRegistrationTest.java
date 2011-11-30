package fr.keemto.web.config;

import com.google.common.collect.Lists;
import fr.keemto.core.Task;
import fr.keemto.scheduling.TaskRegistrar;
import fr.keemto.web.config.AutoTaskRegistration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AutoTaskRegistrationTest {

    private TaskRegistrar taskRegistrar;

    @Before
    public void setUp() throws Exception {
        taskRegistrar = mock(TaskRegistrar.class);
    }

    @Test
    public void onCoreContextRefreshShouldRegisterAllTasks() throws Exception {

        StaticApplicationContext coreContext = new StaticApplicationContext();
        coreContext.setParent(null);
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(coreContext);
        List<Task> tasks = Lists.newArrayList(mock(Task.class));
        AutoTaskRegistration registration = new AutoTaskRegistration(taskRegistrar, tasks);

        registration.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar).registerFetchingTasksForAllUsers();
        verify(taskRegistrar).registerTasks(tasks);

    }

    @Test
    public void shouldIgnoreRefreshOfChildrenContext() throws Exception {

        StaticApplicationContext childrenContext = new StaticApplicationContext();
        childrenContext.setParent(new StaticApplicationContext());
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(childrenContext);
        AutoTaskRegistration registration = new AutoTaskRegistration(taskRegistrar, new ArrayList<Task>());

        registration.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar, never()).registerFetchingTasksForAllUsers();

    }
}
