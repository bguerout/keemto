package fr.xevents.core.fetcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

@Component
public class FetchingRegistrar implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(FetchingRegistrar.class);

    private final ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
    private final UserResolver userResolver;
    private final FetcherHandlerFactory handlerFactory;
    private final Map<Runnable, Long> fixedDelayTasks = new HashMap<Runnable, Long>();

    @Inject
    public FetchingRegistrar(FetcherHandlerFactory handlerFactory, UserResolver userResolver) {
        super();
        this.userResolver = userResolver;
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<User> users = userResolver.getAllUsers();
        for (User user : users) {
            addFixedDelayTasks(user);
        }
        configureAndInitTaskRegistrar();
    }

    protected Map<Runnable, Long> getFixedDelayTasks() {
        return Collections.unmodifiableMap(fixedDelayTasks);
    }

    private void configureAndInitTaskRegistrar() {
        registrar.setFixedDelayTasks(fixedDelayTasks);
        registrar.afterPropertiesSet();
    }

    private void addFixedDelayTasks(User user) {
        List<FetcherHandler> handlers = handlerFactory.createHandlers(user);
        for (FetcherHandler handler : handlers) {
            fixedDelayTasks.put(handler, handler.getDelay());
            log.info("A new fetching task is going to be registered for user: " + user + ". This task will run every "
                    + handler.getDelay() + "ms");
        }
    }

}
