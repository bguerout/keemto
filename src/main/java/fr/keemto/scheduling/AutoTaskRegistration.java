package fr.keemto.scheduling;

import fr.keemto.core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class AutoTaskRegistration implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(TaskRegistrar.class);

    private TaskRegistrar taskRegistrar;
    private List<Task> discoveredTasks;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (isAnEventFromCoreContext(event)) {
            log.info("Application context has been refreshed, automatic task registrer is going to cancel all tasks and register them again.");
            registerAllTasks();
        } else {
            log.debug("Ignoring refresh context event because this is not core context");
        }
    }

    public void registerAllTasks() {
        taskRegistrar.registerTasks(discoveredTasks);
        taskRegistrar.registerFetchingTasksForAllUsers();
    }

    public Set<ScheduledTask> getScheduledTasks() {
        return taskRegistrar.getScheduledTasks();
    }

    private boolean isAnEventFromCoreContext(ContextRefreshedEvent event) {
        return event.getApplicationContext().getParent() == null;
    }

    @Autowired
    public void setDiscoveredTasks(List<Task> discoveredTasks) {
        this.discoveredTasks = discoveredTasks;
    }

    @Autowired
    public void setTaskRegistrar(TaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }
}
