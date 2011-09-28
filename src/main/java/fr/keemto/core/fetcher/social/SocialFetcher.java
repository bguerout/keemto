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

package fr.keemto.core.fetcher.social;

import fr.keemto.core.Event;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.Fetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SocialFetcher<T, D> implements Fetcher {

    private static final Logger log = LoggerFactory.getLogger(SocialFetcher.class);

    private final ConnectionResolver<T> connectionResolver;
    private long delay;

    public SocialFetcher(ConnectionResolver<T> connectionResolver, long delay) {
        super();
        this.connectionResolver = connectionResolver;
        this.delay = delay;
    }

    @Override
    public final List<Event> fetch(User user, long lastFetchedEventTime) {
        List<Event> events = new ArrayList<Event>();
        List<Connection<T>> connections = connectionResolver.getConnectionsFor(user);
        logFetchingBeginning(user, connections);
        for (Connection<T> connection : connections) {
            EventBuilder eventBuilder = createBuilder(user, connection);
            List<Event> eventsFromConnection = fetchConnection(connection, lastFetchedEventTime, eventBuilder);
            events.addAll(eventsFromConnection);
            logFetchResult(user, lastFetchedEventTime, eventsFromConnection.size());
        }
        return events;
    }

    private List<Event> fetchConnection(Connection<T> connection, long lastFetchedEventTime, EventBuilder eventBuilder) {
        List<Event> events = new ArrayList<Event>();
        T api = connection.getApi();
        List<D> fetchedDatas = fetchApi(api, lastFetchedEventTime);
        for (D data : fetchedDatas) {
            Event event = convertDataToEvent(data, eventBuilder);
            events.add(event);
        }
        return events;
    }

    private EventBuilder createBuilder(User user, Connection<T> connection) {
        SocialProviderConnection providerConnection = new SocialProviderConnection(connection);
        return new EventBuilder(user, providerConnection);
    }

    private void logFetchingBeginning(User user, List<Connection<T>> connections) {
        if (connections.isEmpty()) {
            log.debug("Unable to fetch User: " + user + "because he does not own connection for provider: " + getProviderId());
        } else {
            log.debug("Fetching provider: " + getProviderId() + " for user: " + user);
        }
    }

    private void logFetchResult(User user, long lastFetchedEventTime, int nbEvents) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String lastEventDate = format.format(new Date(lastFetchedEventTime));
        if (nbEvents == 0) {
            log.info("No event has been fetched for provider: " + getProviderId() + " and user: " + user.getUsername()
                    + ". Application is up to date since " + lastEventDate);
        } else {
            log.info(nbEvents + " event(s) have been fetched for " + user);
        }
    }

    @Override
    public boolean canFetch(User user) {
        return !connectionResolver.getConnectionsFor(user).isEmpty();
    }


    @Override
    public long getDelay() {
        return delay;
    }

    protected abstract List<D> fetchApi(T api, long lastFetchedEventTime);

    protected abstract Event convertDataToEvent(D data, EventBuilder builder);

}
