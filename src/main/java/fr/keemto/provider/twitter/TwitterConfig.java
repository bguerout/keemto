package fr.keemto.provider.twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
public class TwitterConfig {

    @Bean
    public TwitterFetcher twitterFetcher() {
        return new TwitterFetcher();
    }

    @Bean
    public TwitterConnectionFactory twitterConnectionFactory(@Value("${twitter.consumerKey}") String consumerKey, @Value("${twitter.consumerSecret}") String consumerSecret) {
        return new TwitterConnectionFactory(consumerKey, consumerSecret);

    }
}
