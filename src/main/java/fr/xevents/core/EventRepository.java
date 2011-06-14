package fr.xevents.core;

import java.util.List;

public interface EventRepository {

    List<Event> getAllEvents();

    Event getMostRecentEvent(User user);

    void persist(List<Event> events);

}