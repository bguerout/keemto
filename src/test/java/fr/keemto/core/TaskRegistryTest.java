package fr.keemto.core;

import com.google.common.collect.Lists;
import fr.keemto.core.fetching.FetchingTask;
import fr.keemto.core.fetching.FetchingTaskFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskRegistryTest {

    private UserRepository userRepository;
    private FetchingTaskFactory fetchingTaskFactory;
    private List<Task> contextTasks;
    private TaskRegistry taskRegistry;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        fetchingTaskFactory = mock(FetchingTaskFactory.class);
        contextTasks = new ArrayList<Task>();
        taskRegistry = new TaskRegistry(fetchingTaskFactory, userRepository, contextTasks);
    }

    @Test
    public void shouldCreateFetchingTask() throws Exception {

        FetchingTask fetchingTask = mock(FetchingTask.class);
        User user = mock(User.class);
        when(userRepository.getAllUsers()).thenReturn(Lists.newArrayList(user));
        when(fetchingTaskFactory.createTasks(user)).thenReturn(Lists.newArrayList(fetchingTask));

        List<Task> tasks = taskRegistry.findTasks();

        assertThat(tasks, hasItem(fetchingTask));
    }

    @Test
    public void shouldCreateFindInjectedTask() throws Exception {

        Task task = mock(Task.class);
        contextTasks.add(task);

        List<Task> tasks = taskRegistry.findTasks();

        assertThat(tasks, hasItem(task));
    }

}
