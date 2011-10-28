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

package fr.keemto.provider.social.fetcher;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import fr.keemto.provider.social.EventData;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TwitterFetcher extends ConnectionFetcher<Twitter, Tweet> {

    public TwitterFetcher(long delay) {
        super(delay);
    }

    public TwitterFetcher() {
        this(60000);
    }

    @Override
    protected List<Tweet> fetchApi(Twitter api, long lastFetchedEventTime) {
        List<Tweet> tweets = api.timelineOperations().getUserTimeline();
        Collection<Tweet> filteredTweets = removeAlreadyFetchedTweets(tweets, lastFetchedEventTime);
        return new ArrayList<Tweet>(filteredTweets);
    }

    @Override
    protected EventData convertDataToEvent(Tweet tweet) {
        Date createdAt = tweet.getCreatedAt();
        String tweetText = tweet.getText();
        return new EventData(createdAt.getTime(), tweetText, getProviderId());
    }

    @Override
    public String getProviderId() {
        return "twitter";
    }

    private Collection<Tweet> removeAlreadyFetchedTweets(List<Tweet> tweets, final long lastFetchedEventTime) {
        return Collections2.filter(tweets, new Predicate<Tweet>() {

            @Override
            public boolean apply(Tweet tweet) {
                return tweet.getCreatedAt().after(new Date(lastFetchedEventTime));
            }

        });
    }

}
