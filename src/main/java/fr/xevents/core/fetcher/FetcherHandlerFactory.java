package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class FetcherHandlerFactory {

    private static final Logger log = LoggerFactory.getLogger(FetcherHandlerFactory.class);

    private final EventRepository eventRepository;
    private final List<Fetcher<?>> fetchers;

    public FetcherHandlerFactory(EventRepository eventRepository, List<Fetcher<?>> fetchers) {
        this.eventRepository = eventRepository;
        this.fetchers = fetchers;
    }

    protected FetcherHandler createHandler(Fetcher<?> fetcher, User user) {
        return new FetcherHandler(fetcher, user, eventRepository);
    }

    public List<FetcherHandler> createHandlers(User user) {
        List<FetcherHandler> handlers = new ArrayList<FetcherHandler>();
        for (Fetcher<?> fetcher : fetchers) {
            if (fetcher.canFetch(user)) {
                FetcherHandler handler = createHandler(fetcher, user);
                handlers.add(handler);
            } else {
                log.debug("Fetcher" + fetcher.getProviderId()
                        + " is ignored because fetcher cannot fetch events for user: " + user + ".");
            }
        }
        return handlers;
    }
}
