package fr.keemto.web.config;

import com.google.common.collect.Lists;
import fr.keemto.core.Task;
import fr.keemto.core.User;
import fr.keemto.core.UserRepository;
import fr.keemto.scheduling.TaskRegistrar;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticApplicationContext;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class AutoTaskRegistrationTest {

    private UserRepository userRepository;
    private TaskRegistrar taskRegistrar;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        taskRegistrar = mock(TaskRegistrar.class);
    }

    @Test
    public void onWebContextRefreshShouldRegisterAllTasks() throws Exception {

        StaticApplicationContext webContext = new StaticApplicationContext();
        webContext.setParent(new StaticApplicationContext());
        webContext.registerSingleton("task1", BeanTask.class);
        webContext.registerSingleton("task2", BeanTask.class);

        ArrayList<User> users = Lists.newArrayList(mock(User.class));
        when(userRepository.getAllUsers()).thenReturn(users);

        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(webContext);
        AutoTaskRegistration registration = new AutoTaskRegistration(taskRegistrar, userRepository);
        registration.setApplicationContext(webContext);

        registration.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar).registerFetchingTasksFor(users);
        verify(taskRegistrar).registerTasks(anyListOf(Task.class));

    }

    @Test
    public void shouldIgnoreRefreshOfCoreContext() throws Exception {

        AutoTaskRegistration registration = new AutoTaskRegistration(taskRegistrar, userRepository);
        StaticApplicationContext context = new StaticApplicationContext();
        context.setParent(null);
        registration.setApplicationContext(context);
        ContextRefreshedEvent refreshedEvent = new ContextRefreshedEvent(context);


        registration.onApplicationEvent(refreshedEvent);

        verify(taskRegistrar, never()).registerFetchingTasksFor(anyListOf(User.class));

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
