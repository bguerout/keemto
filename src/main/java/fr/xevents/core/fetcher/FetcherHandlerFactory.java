package fr.xevents.core.fetcher;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class FetcherHandlerFactory {

    private final EventRepository eventRepository;

    public FetcherHandlerFactory(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public FetcherHandler createHandler(Fetcher<?> fetcher, User user) {
        return new FetcherHandler(fetcher, user, eventRepository);
    }

}
