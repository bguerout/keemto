package fr.xevents.social;

import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterAdapter;

public class HttpTwitterConnectionFactory extends OAuth1ConnectionFactory<Twitter> {

    public HttpTwitterConnectionFactory(String consumerKey, String consumerSecret) {
        super("twitter", new HttpTwitterServiceProvider(consumerKey, consumerSecret), new TwitterAdapter());
    }

}
