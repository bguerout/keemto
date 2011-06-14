package fr.xevents.core;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class EventSynchronizer {

    private static final Logger log = LoggerFactory.getLogger(JdbcEventRepository.class);

    private final List<Fetcher<?>> fetchers;
    private final EventRepository eventRepository;

    public EventSynchronizer(List<Fetcher<?>> fetchers, EventRepository eventRepository) {
        this.fetchers = fetchers;
        this.eventRepository = eventRepository;
    }

    public void updateEvents(List<User> users, String providerId) {
        for (User user : users) {
            long lastFetchedEventTime = getLastFetchedEventTime(user);
            for (Fetcher<?> fetcher : getFetchersForProvider(providerId)) {
                executeFetcher(fetcher, lastFetchedEventTime, user);
            }
        }
    }

    private long getLastFetchedEventTime(User user) {
        Event mostRecentEvent = eventRepository.getMostRecentEvent(user);
        long lastFetchedEventTime = mostRecentEvent.getTimestamp();
        return lastFetchedEventTime;
    }

    private Collection<Fetcher<?>> getFetchersForProvider(final String providerId) {
        return Collections2.filter(fetchers, new ProviderPredicate(providerId));
    }

    private void executeFetcher(Fetcher<?> fetcher, long lastFetchedEventTime, User user) {
        try {
            List<Event> events = fetcher.fetch(user, lastFetchedEventTime);
            eventRepository.persist(events);
        } catch (RuntimeException e) {
            logFetchingException(fetcher, user, e);
        }
    }

    private void logFetchingException(Fetcher<?> fetcher, User user, Exception e) {
        StringBuilder message = new StringBuilder();
        message.append("An error has occured when trying to update events for user: ");
        message.append(user);
        message.append(" with fetcher: ");
        message.append(fetcher.getProviderId());
        message.append(". This fetching task will be executed again during next update.");
        log.error(message.toString(), e);
    }

    private final class ProviderPredicate implements Predicate<Fetcher<?>> {
        private final String providerId;

        private ProviderPredicate(String providerId) {
            this.providerId = providerId;
        }

        @Override
        public boolean apply(Fetcher<?> fetcher) {
            return fetcher.getProviderId().equals(providerId);
        }
    }

}
