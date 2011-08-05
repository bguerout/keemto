package fr.keemto.core.fetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import fr.keemto.core.Event;

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
        return 20000;
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
