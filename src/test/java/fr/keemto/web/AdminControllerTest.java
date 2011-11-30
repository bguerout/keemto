package fr.keemto.web;

import com.google.common.collect.Sets;
import fr.keemto.scheduling.AutoTaskRegistration;
import fr.keemto.scheduling.ScheduledTask;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    private AdminController controller;
    private AutoTaskRegistration registration;

    @Before
    public void setUp() throws Exception {
        registration = mock(AutoTaskRegistration.class);
        controller = new AdminController(registration);
    }

    @Test
    public void shouldReturnTaskList() throws Exception {
        ScheduledTask scheduledTask = mock(ScheduledTask.class);
        Set<ScheduledTask> tasks = Sets.newHashSet(scheduledTask);
        when(registration.getScheduledTasks()).thenReturn(tasks);

        ModelAndView scheduledTasks = controller.getScheduledTasks();

        assertThat(scheduledTasks.getViewName(), equalTo("tasks"));
        assertThat(scheduledTasks.getModelMap().get("tasks"), equalTo((Object) tasks));
    }

    @Test
    public void shouldRefreshTask() throws Exception {

        ModelAndView scheduledTasks = controller.refresh();

        verify(registration).registerAllTasks();
        assertThat(scheduledTasks.getViewName(), equalTo("tasks"));
    }
}
