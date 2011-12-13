package fr.keemto.web;

import com.google.common.collect.Sets;
import fr.keemto.core.Task;
import fr.keemto.core.TaskLocator;
import fr.keemto.scheduling.ScheduledTask;
import fr.keemto.scheduling.TaskRegistrar;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    private AdminController controller;
    private TaskLocator taskLocator;
    private TaskRegistrar taskRegistrar;

    @Before
    public void setUp() throws Exception {
        taskLocator = mock(TaskLocator.class);
        taskRegistrar = mock(TaskRegistrar.class);
        controller = new AdminController(taskRegistrar, taskLocator);
    }

    @Test
    public void shouldReturnTaskList() throws Exception {
        ScheduledTask scheduledTask = mock(ScheduledTask.class);
        Set<ScheduledTask> tasks = Sets.newHashSet(scheduledTask);
        when(taskRegistrar.getScheduledTasks()).thenReturn(tasks);

        ModelAndView scheduledTasks = controller.getScheduledTasks();

        assertThat(scheduledTasks.getViewName(), equalTo("tasks"));
        assertThat(scheduledTasks.getModelMap().get("tasks"), equalTo((Object) tasks));
    }

    @Test
    public void shouldRefreshTask() throws Exception {

        List<Task> tasks = new ArrayList<Task>();
        when(taskLocator.findTasks()).thenReturn(tasks);

        ModelAndView scheduledTasks = controller.refresh();

        verify(taskLocator).findTasks();
        verify(taskRegistrar).registerTasks(tasks);
        assertThat(scheduledTasks.getViewName(), equalTo("tasks"));
    }

    @Test
    public void canCancelATask() throws Exception {
        String taskId = "11";

        ModelAndView mav = controller.cancel(taskId);
    }
}
