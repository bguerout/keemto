package fr.xevents.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SocialFetcher<T> {

    private static final Logger log = LoggerFactory.getLogger(SocialFetcher.class);

    private final ApiResolver<T> apiResolver;

    public SocialFetcher(ApiResolver<T> apiResolver) {
        super();
        this.apiResolver = apiResolver;
    }

    public final List<Event> fetch(List<User> users) {
        List<Event> fetchedEvents = new ArrayList<Event>();
        for (User user : users) {
            List<Event> events = fetchUserEvent(user);
            fetchedEvents.addAll(events);
        }
        return fetchedEvents;
    }

    private List<Event> fetchUserEvent(User user) {
        List<Event> userEvents = new ArrayList<Event>();
        List<T> apis = apiResolver.getApis(user);

        if (apis.isEmpty()) {
            log.info("User: " + user + " does not own Api of type: " + apiResolver.getApiClass());
        }

        for (T api : apis) {
            List<Event> apiEvents = fetchApiEvents(api);
            userEvents.addAll(apiEvents);

            log.info(apiEvents.size() + " event(s) has been fetched from api: " + api + " owned by: " + user);
        }
        return userEvents;
    }

    protected abstract List<Event> fetchApiEvents(T api);

}
