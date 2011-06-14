package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterApi;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import fr.xevents.core.Event;

@Service
public class TwitterFetcher extends SocialFetcher<TwitterApi> {

    @Inject
    @Named("twitterApiResolver")
    public TwitterFetcher(ApiResolver<TwitterApi> apiResolver) {
        super(apiResolver);
    }

    @Override
    protected List<Event> fetchApiEvents(TwitterApi api, long lastFetchedEventTime) {
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

    private Collection<Tweet> filterTweetsByDate(List<Tweet> tweets, final long lastFetchedEventTime) {
        return Collections2.filter(tweets, new Predicate<Tweet>() {

            @Override
            public boolean apply(Tweet tweet) {
                return tweet.getCreatedAt().after(new Date(lastFetchedEventTime));
            }

        });
    }

    private Event convertToEvent(Tweet tweet) {
        return new Event(tweet.getCreatedAt().getTime(), tweet.getFromUser(), tweet.getText());
    }
}
