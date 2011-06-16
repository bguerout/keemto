package fr.xevents.social;

import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.twitter.api.TwitterApi;
import org.springframework.social.twitter.connect.TwitterApiAdapter;

public class HttpTwitterConnectionFactory extends OAuth1ConnectionFactory<TwitterApi> {

    public HttpTwitterConnectionFactory(String consumerKey, String consumerSecret) {
        super("twitter", new HttpTwitterServiceProvider(consumerKey, consumerSecret), new TwitterApiAdapter());
    }

}
