package com.twitter.listener;

import static java.lang.String.format;

import com.twitter.configuration.TwitterConfiguration;
import com.twitter.entity.TweetEntity;
import com.twitter.service.TwitterService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class TwitterListener implements InitializingBean {

    private static TwitterListener instance = null;

    private final TwitterConfiguration twitterConfiguration;

    private final TwitterService twitterService;

    public static TwitterListener get() {
        return TwitterListener.instance;
    }

    public void createListener() {

        if (twitterConfiguration.isListenerEnabled()) {
            FilterQuery filterQuery = new FilterQuery().track(twitterConfiguration.getTracks());
            filterQuery.language(twitterConfiguration.getLanguages().toArray(String[]::new));

            TwitterStream twitterStream = twitterConfiguration.getTwitterStreamFactoryInstance();
            twitterStream.addListener(getListener());
            twitterStream.filter(filterQuery);
        }
    }

    @Override
    public void afterPropertiesSet() {
        TwitterListener.instance = this;
    }

    private StatusListener getListener() {
        return new StatusListener() {
            @Override
            public void onStatus(Status status) {
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
                if (status.getUser().getFollowersCount() >= twitterConfiguration.getFollowers()) {
                    TweetEntity tweetEntity = TweetEntity.builder()
                        .user(status.getUser().getScreenName())
                        .text(status.getText())
                        .localization(Objects.isNull(status.getGeoLocation()) ? null
                            : format("%s, %s", status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude()))
                        .build();
                    twitterService.save(tweetEntity);
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                log.info("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                log.info("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                log.info("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                log.info("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                log.error(ex);
            }
        };

    }
}
