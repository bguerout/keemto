package fr.xevents.core.fetcher;

import java.util.List;

import fr.xevents.core.Event;
import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

public class FetcherHandler implements Runnable {

    private final Fetcher<?> fetcher;
    private final User user;
    private final EventRepository eventRepository;

    FetcherHandler(Fetcher<?> fetcher, User user, EventRepository eventRepository) {
        this.fetcher = fetcher;
        this.user = user;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run() throws FetchingException {
        Event mostRecentEvent = eventRepository.getMostRecentEvent(user);
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
        } catch (RuntimeException e) {
            handleFetchingException(e);
        }
    }

    protected void handleFetchingException(Exception e) throws FetchingException {
        StringBuilder message = new StringBuilder();
        message.append("An error has occured when trying to update events for user: ");
        message.append(user);
        message.append(" with fetcher: ");
        message.append(fetcher.getProviderId());
        message.append(". Same fetching task will be executed again during next handler invocation. Next estimated fetch in  : "
                + fetcher.getDelay() + "ms");
        throw new FetchingException(message.toString(), e);
    }

}
