package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import fr.xevents.core.User;

@Component
public class FetchingRegistrar {

    private static final Logger log = LoggerFactory.getLogger(FetchingRegistrar.class);

    private final TaskScheduler scheduler;
    private final List<TaskMonitor> manageableFutures = new ArrayList<FetchingRegistrar.TaskMonitor>();

    @Autowired
    public FetchingRegistrar(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void registerTasks(List<EventTask> tasks) {
        for (EventTask task : tasks) {
            registerTask(task);
        }
    }

    public void registerTask(EventTask task) {
        TaskMonitor monitor = getOrCreateManager(task.getUser());
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(task, task.getDelay());
        monitor.addFuture(future);
        manageableFutures.add(monitor);
        log.info("A new fetching task has been registered for user: " + task.getUser()
                + ". This task will run every " + task.getDelay() + "ms");
    }

    public void cancelTasks(User user) {
        for (TaskMonitor manager : manageableFutures) {
            if (manager.isOwnedBy(user)) {
                manager.cancellAllFutures();
            }
        }
    }

    private TaskMonitor getOrCreateManager(User user) {
        TaskMonitor manager = new TaskMonitor(user);
        if (manageableFutures.contains(manager)) {
            return manageableFutures.get(manageableFutures.indexOf(manager));
        }
        return manager;
    }

    private final class TaskMonitor {

        private final Set<ScheduledFuture<?>> scheduledFutures = new LinkedHashSet<ScheduledFuture<?>>();
        private final User user;

        public TaskMonitor(User user) {
            this.user = user;
        }

        public void addFuture(ScheduledFuture<?> future) {
            scheduledFutures.add(future);
        }

        public void cancellAllFutures() {
            for (ScheduledFuture<?> future : scheduledFutures) {
                future.cancel(false);
            }
        }

        public boolean isOwnedBy(User owner) {
            return user.equals(owner);
        }
    }
}
