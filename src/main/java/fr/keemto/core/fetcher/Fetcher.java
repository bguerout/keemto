package fr.keemto.core.fetcher;

import java.util.List;

import fr.keemto.core.Event;
import fr.keemto.core.User;

public interface Fetcher {

    List<Event> fetch(User user, long lastFetchedEventTime);

    String getProviderId();

    long getDelay();

    boolean canFetch(User user);

}
