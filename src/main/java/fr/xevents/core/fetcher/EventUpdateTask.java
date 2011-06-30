package fr.xevents.core.fetcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.xevents.core.Event;
import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class EventUpdateTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(EventUpdateTask.class);

    private final Fetcher fetcher;
    private final User user;
    private final EventRepository eventRepository;

    EventUpdateTask(Fetcher fetcher, User user, EventRepository eventRepository) {
        this.fetcher = fetcher;
        this.user = user;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run() throws FetchingException {
        log.debug("Task execution has been triggered for " + user);
        Event mostRecentEvent = eventRepository.getMostRecentEvent(user, fetcher.getProviderId());
        updateEvents(mostRecentEvent);
    }

    private void updateEvents(Event mostRecentEvent) {
        try {
            List<Event> events = fetch(mostRecentEvent.getTimestamp());
            persist(events);
            logFetchedEvents(events);
        } catch (RuntimeException e) {
            handleExceptionDuringUpdate(e);
        }
    }

    protected List<Event> fetch(long lastFetchedEventTime) {
        return fetcher.fetch(user, lastFetchedEventTime);
    }

    protected void persist(List<Event> events) {
        eventRepository.persist(events);
    }

    protected void handleExceptionDuringUpdate(Exception e) throws FetchingException {
        StringBuilder message = new StringBuilder();
        message.append("An error has occured when trying to update events for user: ");
        message.append(user);
        message.append(" with fetcher: ");
        message.append(fetcher.getProviderId());
        message.append(". This task will be executed again during next scheduled invocation. Next estimated fetch in  : "
                + fetcher.getDelay() + "ms");
        throw new FetchingException(message.toString(), e);
    }

    private void logFetchedEvents(List<Event> events) {
        for (Event event : events) {
            log.debug("A new Event has been fetched: " + event);
        }
    }

    public long getDelay() {
        return fetcher.getDelay();
    }

    public User getUser() {
        return user;
    }
}
