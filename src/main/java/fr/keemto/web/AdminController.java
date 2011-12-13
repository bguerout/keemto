package fr.keemto.web;

import fr.keemto.core.Task;
import fr.keemto.core.TaskLocator;
import fr.keemto.scheduling.ScheduledTask;
import fr.keemto.scheduling.TaskRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final TaskRegistrar taskRegistrar;
    private final TaskLocator taskLocator;

    @Autowired
    public AdminController(TaskRegistrar taskRegistrar, TaskLocator taskLocator) {
        this.taskRegistrar = taskRegistrar;
        this.taskLocator = taskLocator;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ModelAndView getScheduledTasks() {
        Set<ScheduledTask> scheduledTasks = taskRegistrar.getScheduledTasks();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("tasks");
        mav.addObject("tasks", scheduledTasks);
        return mav;
    }

    @RequestMapping(value = "/tasks/refresh", method = RequestMethod.GET)
    public ModelAndView refresh() {
        List<Task> tasks = taskLocator.findTasks();
        taskRegistrar.registerTasks(tasks);
        return getScheduledTasks();
    }


    public ModelAndView cancel(String taskId) {
        return null;
    }
}
