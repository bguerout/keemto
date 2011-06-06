package fr.xevents;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterApi;
import org.springframework.stereotype.Service;

@Service
public class TwitterFetcher extends SocialFetcher<TwitterApi> {

    @Inject
    @Named("twitterApiResolver")
    public TwitterFetcher(ApiResolver<TwitterApi> apiResolver) {
        super(apiResolver);
    }

    @Override
    protected List<Event> fetchApiEvents(TwitterApi api) {
        List<Event> events = new ArrayList<Event>();
        List<Tweet> tweets = api.timelineOperations().getUserTimeline();
        for (Tweet tweet : tweets) {
            Event event = convertToEvent(tweet);
            events.add(event);
        }
        return events;
    }

    private Event convertToEvent(Tweet tweet) {
        return new Event(tweet.getCreatedAt().getTime(), tweet.getFromUser(), tweet.getText());
    }
}
