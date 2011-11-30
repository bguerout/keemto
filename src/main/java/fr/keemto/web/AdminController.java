package fr.keemto.web;

import fr.keemto.scheduling.ScheduledTask;
import fr.keemto.scheduling.AutoTaskRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final AutoTaskRegistration registration;

    @Autowired
    public AdminController(AutoTaskRegistration registration) {
        this.registration = registration;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ModelAndView getScheduledTasks() {
        Set<ScheduledTask> scheduledTasks = registration.getScheduledTasks();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("tasks");
        mav.addObject("tasks", scheduledTasks);
        return mav;
    }

    @RequestMapping(value = "/tasks/refresh", method = RequestMethod.GET)
    public ModelAndView refresh() {
        registration.registerAllTasks();
        return getScheduledTasks();
    }
}
