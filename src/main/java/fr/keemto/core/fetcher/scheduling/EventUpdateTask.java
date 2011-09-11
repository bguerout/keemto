/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.keemto.core.fetcher.scheduling;

import fr.keemto.core.Event;
import fr.keemto.core.EventRepository;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.Fetcher;
import fr.keemto.core.fetcher.FetchingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
