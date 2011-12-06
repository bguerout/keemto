package fr.keemto.provider.yammer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.yammer.api.connect.YammerConnectionFactory;

@Configuration
public class YammerConfig {

    @Bean
    public YammerFetcher yammerFetcher() {
        return new YammerFetcher();
    }

    @Bean
    public YammerConnectionFactory yammerConnectionFactory(@Value("${yammer.consumerKey}") String consumerKey, @Value("${yammer.consumerSecret}") String consumerSecret) {
        return new YammerConnectionFactory(consumerKey, consumerSecret);

    }
}
