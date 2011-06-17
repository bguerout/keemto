package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import fr.xevents.core.User;

@Component
public class FetchingRegistrar {

    private static final Logger log = LoggerFactory.getLogger(FetchingRegistrar.class);

    private final TaskScheduler scheduler;
    private final List<ScheduledHandlerManager> manageableFutures = new ArrayList<FetchingRegistrar.ScheduledHandlerManager>();

    @Inject
    public FetchingRegistrar(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void registerHandlers(List<FetcherHandler> handlers) {
        for (FetcherHandler handler : handlers) {
            registerHandler(handler);
        }
    }

    public void registerHandler(FetcherHandler handler) {
        ScheduledHandlerManager manager = getOrCreateManager(handler.getUser());
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(handler, handler.getDelay());
        manager.addFuture(future);
        manageableFutures.add(manager);
        log.info("A new fetching task has been registered for user: " + handler.getUser()
                + ". This task will run every " + handler.getDelay() + "ms");
    }

    public void cancelHandlers(User user) {
        for (ScheduledHandlerManager manager : manageableFutures) {
            if (manager.isOwnedBy(user)) {
                manager.cancellAllFutures();
            }
        }
    }

    private ScheduledHandlerManager getOrCreateManager(User user) {
        ScheduledHandlerManager manager = new ScheduledHandlerManager(user);
        if (manageableFutures.contains(manager)) {
            return manageableFutures.get(manageableFutures.indexOf(manager));
        }
        return manager;
    }

    private final class ScheduledHandlerManager {

        private final Set<ScheduledFuture<?>> scheduledFutures = new LinkedHashSet<ScheduledFuture<?>>();
        private final User user;

        public ScheduledHandlerManager(User user) {
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
