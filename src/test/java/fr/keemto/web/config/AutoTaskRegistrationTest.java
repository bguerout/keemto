package fr.keemto.web.config;

import fr.keemto.core.Task;
import fr.keemto.scheduling.TaskRegistrar;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticApplicationContext;

import static org.mockito.Mockito.*;

public class AutoTaskRegistrationTest {

    private TaskRegistrar taskRegistrar;

    @Before
    public void setUp() throws Exception {
        taskRegistrar = mock(TaskRegistrar.class);
    }

    @Test
    public void onWebContextRefreshShouldRegisterAllTasks() throws Exception {

        StaticApplicationContext webContext = new StaticApplicationContext();
        webContext.setParent(new StaticApplicationContext());
        webContext.registerSingleton("task1", BeanTask.class);
        webContext.registerSingleton("task2", BeanTask.class);

        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(webContext);
        AutoTaskRegistration registration = new AutoTaskRegistration(taskRegistrar);
        registration.setApplicationContext(webContext);

        registration.onApplicationEvent(refreshedEvent);


        verify(taskRegistrar).registerFetchingTasksForAllUsers();
        verify(taskRegistrar).registerTasks(anyListOf(Task.class));

    }

    @Test
    public void shouldIgnoreRefreshOfCoreContext() throws Exception {

        AutoTaskRegistration registration = new AutoTaskRegistration(taskRegistrar);
        StaticApplicationContext context = new StaticApplicationContext();
        context.setParent(null);
        registration.setApplicationContext(context);
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(context);


        registration.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar, never()).registerFetchingTasksForAllUsers();

    }

    private final static class BeanTask implements Task {
        @Override
        public long getDelay() {
            return 0;
        }

        @Override
        public Object getTaskId() {
            return null;
        }

        @Override
        public void run() {
        }
    }
}
