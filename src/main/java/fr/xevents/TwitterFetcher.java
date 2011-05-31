package fr.xevents;

import java.util.ArrayList;
import java.util.List;

import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;

public class TwitterFetcher extends AbstractFetcher<TimelineOperations> {

    public TwitterFetcher(ApiResolver<TimelineOperations> apiResolver) {
        super(apiResolver);
    }

    @Override
    protected List<Event> getEventsForApi(TimelineOperations api) {
        List<Event> events = new ArrayList<Event>();
        List<Tweet> tweets = api.getUserTimeline();
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
