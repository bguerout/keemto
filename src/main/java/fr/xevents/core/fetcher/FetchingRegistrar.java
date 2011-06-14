package fr.xevents.core.fetcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class FetchingRegistrar implements InitializingBean {

    private final ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
    private List<Fetcher<?>> fetchers;
    private FetcherHandlerFactory handlerFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<FetcherHandler> handlers = handlerFactory.createHandlers(fetchers);
        Map<Runnable, Long> fixedDelayTasks = new HashMap<Runnable, Long>();
        for (FetcherHandler handler : handlers) {
            fixedDelayTasks.put(handler, handler.getDelay());
        }
        registrar.setFixedDelayTasks(fixedDelayTasks);

    }

    public void setFetchers(List<Fetcher<?>> fetchers) {
        this.fetchers = fetchers;
    }

    public void setHandlerFactory(FetcherHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

}
