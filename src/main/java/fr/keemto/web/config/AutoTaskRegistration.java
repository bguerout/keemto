package fr.keemto.web.config;

import fr.keemto.core.Task;
import fr.keemto.core.User;
import fr.keemto.core.UserRepository;
import fr.keemto.scheduling.ScheduledTask;
import fr.keemto.scheduling.TaskRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class AutoTaskRegistration implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(AutoTaskRegistration.class);

    private final TaskRegistrar taskRegistrar;
    private final UserRepository userRepository;
    private ApplicationContext applicationContext;

    @Autowired
    public AutoTaskRegistration(TaskRegistrar taskRegistrar, UserRepository userRepository) {
        this.taskRegistrar = taskRegistrar;
        this.userRepository = userRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (isAnEventFromWebContext(event)) {
            log.info("Web context has been refreshed, automatic task registrer is going to cancel all tasks and register them again.");
            registerAllTasks();
        } else {
            log.debug("Ignoring refresh context event because this is not web context");
        }
    }

    private List<Task> getTasksFromContext() {
        Map<String, Task> factoryMap = applicationContext.getBeansOfType(Task.class);
        return new ArrayList<Task>(factoryMap.values());
    }

    public void registerAllTasks() {
        taskRegistrar.registerTasks(getTasksFromContext());
        List<User> users = userRepository.getAllUsers();
        taskRegistrar.registerFetchingTasksFor(users);
    }

    public Set<ScheduledTask> getScheduledTasks() {
        return taskRegistrar.getScheduledTasks();
    }

    private boolean isAnEventFromWebContext(ContextRefreshedEvent event) {
        return event.getApplicationContext().getParent() != null;
    }
}
