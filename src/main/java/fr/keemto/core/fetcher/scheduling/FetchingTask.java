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

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;
import fr.keemto.core.EventRepository;
import fr.keemto.core.fetcher.FetchingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FetchingTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FetchingTask.class);

    private final Account account;
    private final EventRepository eventRepository;


    public FetchingTask(Account account, EventRepository eventRepository) {
        this.account = account;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run() throws FetchingException {
        AccountKey key = account.getKey();
        Event mostRecentEvent = eventRepository.getMostRecentEvent(account);
        log.debug("Task execution has been triggered for {} and last event {}", key, mostRecentEvent.getTimestamp());
        updateEvents(mostRecentEvent);
    }

    private void updateEvents(Event mostRecentEvent) {
        try {
            long newerThan = mostRecentEvent.getTimestamp();
            List<Event> events = account.fetch(newerThan);
            persist(events);
            logFetchedEvents(events);
        } catch (RuntimeException e) {
            handleExceptionDuringUpdate(e);
        }
    }

    protected void persist(List<Event> events) {
        eventRepository.persist(events);
    }

    protected void handleExceptionDuringUpdate(Exception e) throws FetchingException {
        StringBuilder message = new StringBuilder();
        message.append("An error has occured when trying to update events for account: ");
        message.append(account.getKey());
        message.append(". This task will be executed again during next scheduled invocation. Next estimated fetch in :" + getDelay() + "ms");
        throw new FetchingException(message.toString(), e);
    }

    private void logFetchedEvents(List<Event> events) {
        for (Event event : events) {
            log.debug("A new Event has been fetched: {}", event);
        }
    }

    public long getDelay() {
        return 60000; //TODO link delay to fetcher
    }

    public AccountKey getFetchedAccountKey() {
        return account.getKey();
    }

    @Override
    public String toString() {
        return "FetchingTask{" +
                "account=" + account.getKey() +
                ", delay=" + getDelay() +
                '}';
    }
}
