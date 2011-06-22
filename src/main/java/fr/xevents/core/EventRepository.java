package fr.xevents.core;

import java.util.List;

public interface EventRepository {

    List<Event> getAllEvents();

    Event getMostRecentEvent(User user, String providerId);

    void persist(List<Event> events);

}