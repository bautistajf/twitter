package com.twitter.configuration;

import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
@Getter
public class TwitterConfiguration {

    @Value("${twitter.oauth.consumerKey}")
    private String consumerKey;

    @Value("${twitter.oauth.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.oauth.accessToken}")
    private String accessToken;

    @Value("${twitter.oauth.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${twitter.followers}")
    private Integer followers;

    @Value("${twitter.tracks}")
    private String tracks;

    @Value("#{'${twitter.languages}'.split(',')}")
    private List<String> languages;

    @Value("${twitter.hashTagMoreUsed}")
    private Integer hashTagMoreUsed;

    @Value("${twitter.debug}")
    private boolean debug;

    @Value("${twitter.listenerEnabled}")
    private boolean listenerEnabled;

    private ConfigurationBuilder getTwitterConfiguration() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)
            .setJSONStoreEnabled(debug);
        return configurationBuilder;
    }

    @Bean
    public Twitter getTwitterInstance() {
        TwitterFactory tf = new TwitterFactory(this.getTwitterConfiguration().build());
        return tf.getInstance();
    }

    @Bean
    public TwitterStream getTwitterStreamFactoryInstance() {
        TwitterStreamFactory tf = new TwitterStreamFactory(this.getTwitterConfiguration().build());
        return tf.getInstance();
    }

}
