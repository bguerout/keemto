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

    public final List<Event> fetch(User user) {
        List<Event> events = new ArrayList<Event>();
        List<T> apis = apiResolver.getApis(user);

        if (apis.isEmpty()) {
            log.info("User: " + user + " does not own Api of type: " + apiResolver.getApiClass());
        }

        for (T api : apis) {
            List<Event> apiEvents = fetchApiEvents(api);
            events.addAll(apiEvents);

            log.info(apiEvents.size() + " event(s) has been fetched from api: " + api + " owned by: " + user);
        }
        return events;
    }

    protected abstract List<Event> fetchApiEvents(T api);

}
