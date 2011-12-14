package fr.keemto.web;

import fr.keemto.core.Task;
import fr.keemto.core.TaskRegistry;
import fr.keemto.scheduling.ScheduledTask;
import fr.keemto.scheduling.TaskRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final TaskRegistrar taskRegistrar;
    private final TaskRegistry taskRegistry;

    @Autowired
    public AdminController(TaskRegistrar taskRegistrar, TaskRegistry taskRegistry) {
        this.taskRegistrar = taskRegistrar;
        this.taskRegistry = taskRegistry;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ModelAndView getScheduledTasks() {
        Set<ScheduledTask> scheduledTasks = taskRegistrar.getScheduledTasks();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("tasks");
        mav.addObject("tasks", scheduledTasks);
        return mav;
    }

    @RequestMapping(value = "/tasks/reset", method = RequestMethod.POST)
    public View refresh() {
        List<Task> tasks = taskRegistry.findTasks();
        taskRegistrar.registerTasks(tasks);
        return new RedirectView();
    }

    @RequestMapping(value = "/tasks/{taskId}", method = RequestMethod.DELETE)
    public View cancel(@PathVariable String taskId) {
        taskRegistrar.cancelTask(taskId);
        return new RedirectView();
    }
}
