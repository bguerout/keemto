package fr.keemto.web.config;

import fr.keemto.core.Task;
import fr.keemto.core.TaskLocator;
import fr.keemto.scheduling.TaskRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulingSpringBootstrapper implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(TaskLocator.class);

    private final TaskRegistrar taskRegistrar;
    private final TaskLocator taskLocator;

    @Autowired
    public SchedulingSpringBootstrapper(TaskRegistrar taskRegistrar, TaskLocator taskLocator) {
        this.taskRegistrar = taskRegistrar;
        this.taskLocator = taskLocator;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (isAnEventFromWebContext(event)) {
            log.info("Web context has been refreshed, bootstrapper is going to cancel all tasks and register them again.");
            boostrapTasks();
        } else {
            log.debug("Ignoring refresh context event because this is not web context");
        }
    }

    private void boostrapTasks() {
        List<Task> tasks = taskLocator.findTasks();
        taskRegistrar.registerTasks(tasks);
    }

    private boolean isAnEventFromWebContext(ContextRefreshedEvent event) {
        return event.getApplicationContext().getParent() != null;
    }
}
