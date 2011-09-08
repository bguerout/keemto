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

package fr.keemto.core.fetcher;

import fr.keemto.core.Event;
import fr.keemto.core.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SocialFetcher<T> implements Fetcher {

    private static final Logger log = LoggerFactory.getLogger(SocialFetcher.class);

    private final ApiResolver<T> apiResolver;
    private long delay;

    public SocialFetcher(ApiResolver<T> apiResolver, long delay) {
        super();
        this.apiResolver = apiResolver;
        this.delay = delay;
    }

    @Override
    public final List<Event> fetch(User user, long lastFetchedEventTime) {
        List<Event> events = new ArrayList<Event>();
        List<T> apis = apiResolver.getApis(user);

        logFetchingBeginning(user, apis);

        for (T api : apis) {
            List<Event> apiEvents = fetchApiEvents(api, lastFetchedEventTime, user);
            events.addAll(apiEvents);
            logFetchResult(user, lastFetchedEventTime, apiEvents.size());
        }
        return events;
    }

    private void logFetchingBeginning(User user, List<T> apis) {
        if (apis.isEmpty()) {
            log.debug("Unable to fetch User: " + user + "because he does not own Api of type: "
                    + apiResolver.getApiClass() + " for provider: " + getProviderId());
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
        return !apiResolver.getApis(user).isEmpty();
    }


    @Override
    public long getDelay() {
        return delay;
    }

    //TODO find another way to call fetchApiEvents without User attribute.
    protected abstract List<Event> fetchApiEvents(T api, long lastFetchedEventTime, User user);

}
