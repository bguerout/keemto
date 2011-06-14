package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.List;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

public class FetcherHandlerFactory {

    private final EventRepository eventRepository;
    private final UserResolver userResolver;

    public FetcherHandlerFactory(EventRepository eventRepository, UserResolver userResolver) {
        this.eventRepository = eventRepository;
        this.userResolver = userResolver;
    }

    public FetcherHandler createHandler(Fetcher<?> fetcher, User user) {
        return new FetcherHandler(fetcher, user, eventRepository);
    }

    public List<FetcherHandler> createHandlers(List<Fetcher<?>> fetchers) {
        List<FetcherHandler> handlers = new ArrayList<FetcherHandler>();
        for (User user : userResolver.getAllUsers()) {// TODO we should create handler only for users with accounts.
            for (Fetcher<?> fetcher : fetchers) {
                handlers.add(createHandler(fetcher, user));
            }
        }
        return handlers;
    }
}
