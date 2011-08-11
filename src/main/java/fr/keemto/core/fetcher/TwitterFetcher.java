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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import fr.keemto.core.Event;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TwitterFetcher extends SocialFetcher<Twitter> {

    public TwitterFetcher(ApiResolver<Twitter> apiResolver) {
        super(apiResolver);
    }

    @Override
    protected List<Event> fetchApiEvents(Twitter api, long lastFetchedEventTime) {
        List<Event> events = new ArrayList<Event>();
        List<Tweet> tweets = api.timelineOperations().getUserTimeline();

        for (Tweet tweet : filterTweetsByDate(tweets, lastFetchedEventTime)) {
            Event event = convertToEvent(tweet);
            events.add(event);
        }
        return events;
    }

    @Override
    public String getProviderId() {
        return "twitter";
    }

    @Override
    public long getDelay() {
        return 60000;
    }

    private Collection<Tweet> filterTweetsByDate(List<Tweet> tweets, final long lastFetchedEventTime) {
        return Collections2.filter(tweets, new Predicate<Tweet>() {

            @Override
            public boolean apply(Tweet tweet) {
                return tweet.getCreatedAt().after(new Date(lastFetchedEventTime));
            }

        });
    }

    private Event convertToEvent(Tweet tweet) {
        return new Event(tweet.getCreatedAt().getTime(), tweet.getFromUser(), tweet.getText(), getProviderId());
    }
}
