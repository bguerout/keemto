package fr.xevents.core.fetcher;

import java.util.List;

import fr.xevents.core.Event;
import fr.xevents.core.User;

public interface Fetcher {

    List<Event> fetch(User user, long lastFetchedEventTime);

    String getProviderId();

    long getDelay();

    boolean canFetch(User user);

}
