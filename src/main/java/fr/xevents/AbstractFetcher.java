package fr.xevents;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFetcher<T> {

    private final ApiResolver<T> apiResolver;

    public AbstractFetcher(ApiResolver<T> apiResolver) {
        super();
        this.apiResolver = apiResolver;
    }

    public final List<Event> fetch(List<User> users) {
        List<T> apis = apiResolver.getApis(new User("bguerout"));
        List<Event> fetchedEvents = fetchApis(apis);
        return fetchedEvents;
    }

    private List<Event> fetchApis(List<T> apis) {
        List<Event> fetchedEvents = new ArrayList<Event>();
        for (T api : apis) {
            fetchedEvents.addAll(getEventsForApi(api));
        }
        return fetchedEvents;
    }

    protected abstract List<Event> getEventsForApi(T api);

}
