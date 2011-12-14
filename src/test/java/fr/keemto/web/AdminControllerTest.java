package fr.keemto.web;

import com.google.common.collect.Sets;
import fr.keemto.core.Task;
import fr.keemto.core.TaskRegistry;
import fr.keemto.scheduling.ScheduledTask;
import fr.keemto.scheduling.TaskRegistrar;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    private AdminController controller;
    private TaskRegistry taskRegistry;
    private TaskRegistrar taskRegistrar;

    @Before
    public void setUp() throws Exception {
        taskRegistry = mock(TaskRegistry.class);
        taskRegistrar = mock(TaskRegistrar.class);
        controller = new AdminController(taskRegistrar, taskRegistry);
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
        when(taskRegistry.findTasks()).thenReturn(tasks);

        View view = controller.refresh();

        verify(taskRegistry).findTasks();
        verify(taskRegistrar).registerTasks(tasks);
        assertThat(view, instanceOf(RedirectView.class));
        assertThat(((RedirectView)view).getUrl(), nullValue());
    }

    @Test
    public void canCancelATask() throws Exception {

        View view = controller.cancel("11");

        verify(taskRegistrar).cancelTask("11");
        assertThat(view, instanceOf(RedirectView.class));
        assertThat(((RedirectView) view).getUrl(), nullValue());
    }
}
