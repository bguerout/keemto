package fr.xevents.core.fetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.xevents.core.Event;
import fr.xevents.core.User;

public abstract class SocialFetcher<T> implements Fetcher<T> {

    private static final Logger log = LoggerFactory.getLogger(SocialFetcher.class);

    private final ApiResolver<T> apiResolver;

    public SocialFetcher(ApiResolver<T> apiResolver) {
        super();
        this.apiResolver = apiResolver;
    }

    @Override
    public final List<Event> fetch(User user, long lastFetchedEventTime) {
        List<Event> events = new ArrayList<Event>();
        List<T> apis = apiResolver.getApis(user);

        if (apis.isEmpty()) {
            log.info("User: " + user + " does not own Api of type: " + apiResolver.getApiClass());
        }

        for (T api : apis) {
            List<Event> apiEvents = fetchApiEvents(api, lastFetchedEventTime);
            events.addAll(apiEvents);

            logFetchResult(user, lastFetchedEventTime, apiEvents.size());
        }
        return events;
    }

    private void logFetchResult(User user, long lastFetchedEventTime, int nbEvents) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String lastEventDate = format.format(new Date(lastFetchedEventTime));
        if (nbEvents == 0) {
            log.info("No event has been fetched because user hasn't update his connection since " + lastEventDate);
        } else {
            log.info(nbEvents + " event(s) have been fetched for " + user);
        }
    }

    @Override
    public boolean canFetch(User user) {
        return !apiResolver.getApis(user).isEmpty();
    }

    protected abstract List<Event> fetchApiEvents(T api, long lastFetchedEventTime);

}
