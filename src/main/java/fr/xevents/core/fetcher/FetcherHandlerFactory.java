package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

@Component
public class FetcherHandlerFactory {

    private static final Logger log = LoggerFactory.getLogger(FetcherHandlerFactory.class);

    private final EventRepository eventRepository;

    private final FetcherResolver fetcherResolver;

    @Autowired
    public FetcherHandlerFactory(EventRepository eventRepository, FetcherResolver fetcherResolver) {
        this.eventRepository = eventRepository;
        this.fetcherResolver = fetcherResolver;

    }

    private FetcherHandler createHandler(Fetcher fetcher, User user) {
        return new FetcherHandler(fetcher, user, eventRepository);
    }

    public List<FetcherHandler> createHandlers(User user) {

        List<FetcherHandler> handlers = new ArrayList<FetcherHandler>();
        for (Fetcher fetcher : fetcherResolver.resolve(user)) {
            FetcherHandler handler = createHandler(fetcher, user);
            handlers.add(handler);
        }
        return handlers;
    }

}
