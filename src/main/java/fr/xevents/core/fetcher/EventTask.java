package fr.xevents.core.fetcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.xevents.core.Event;
import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class EventTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(EventTask.class);

    private final Fetcher fetcher;
    private final User user;
    private final EventRepository eventRepository;

    EventTask(Fetcher fetcher, User user, EventRepository eventRepository) {
        this.fetcher = fetcher;
        this.user = user;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run() throws FetchingException {
        log.debug("Task execution has been triggered for " + user);
        Event mostRecentEvent = eventRepository.getMostRecentEvent(user, fetcher.getProviderId());
        fetchAndPersist(mostRecentEvent.getTimestamp());
    }

    public long getDelay() {
        return fetcher.getDelay();
    }

    public User getUser() {
        return user;
    }

    private void fetchAndPersist(long lastFetchedEventTime) {

        try {
            List<Event> events = fetcher.fetch(user, lastFetchedEventTime);
            eventRepository.persist(events);
            logFetchedEvents(events);
        } catch (RuntimeException e) {
            handleFetchingException(e);
        }
    }

    private void logFetchedEvents(List<Event> events) {
        for (Event event : events) {
            log.debug("A new Event has been fetched " + event);
        }
    }

    protected void handleFetchingException(Exception e) throws FetchingException {
        StringBuilder message = new StringBuilder();
        message.append("An error has occured when trying to update events for user: ");
        message.append(user);
        message.append(" with fetcher: ");
        message.append(fetcher.getProviderId());
        message.append(". This task will be executed again during next scheduled invocation. Next estimated fetch in  : "
                + fetcher.getDelay() + "ms");
        throw new FetchingException(message.toString(), e);
    }

}
