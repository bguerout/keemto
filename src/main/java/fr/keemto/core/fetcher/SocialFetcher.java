package fr.keemto.core.fetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.keemto.core.Event;
import fr.keemto.core.User;

public abstract class SocialFetcher<T> implements Fetcher {

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

        logFetchingBeginning(user, apis);

        for (T api : apis) {
            List<Event> apiEvents = fetchApiEvents(api, lastFetchedEventTime);
            events.addAll(apiEvents);

            logFetchResult(user, lastFetchedEventTime, apiEvents.size());
        }
        return events;
    }

    private void logFetchingBeginning(User user, List<T> apis) {
        if (apis.isEmpty()) {
            log.debug("Unable to fetch User: " + user + "because he does not own Api of type: "
                    + apiResolver.getApiClass() + " for provider: " + getProviderId());
        } else {
            log.debug("Fetching provider: " + getProviderId() + " for user: " + user);
        }
    }

    private void logFetchResult(User user, long lastFetchedEventTime, int nbEvents) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String lastEventDate = format.format(new Date(lastFetchedEventTime));
        if (nbEvents == 0) {
            log.info("No event has been fetched for provider: " + getProviderId() + " and user: " + user.getUsername()
                    + ". Application is up to date since " + lastEventDate);
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
