package fr.xevents.core;

import java.util.List;

public interface Fetcher<T> {

    List<Event> fetch(User user, long lastFetchedEventTime);

    String getProviderId();

}
