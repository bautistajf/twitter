package com.twitter;

import com.twitter.entity.TweetEntity;
import java.util.List;
import java.util.Map;
import twitter4j.Trend;

public interface TwitterMock {

    String PATH = "/1.0/tweets";

    String TEXT = "TWEET1";
    String USER = "javier";
    String LOCALIZATION = "LOCALIZACION";

    static List<TweetEntity> getTweets() {
        return List.of(getTweet());
    }

    static Map<String, List<TweetEntity>> getTweetsMap() {
        return Map.of(USER, List.of(getTweet()));
    }

    static List<TweetEntity> getValidatedTweets() {
        return List.of(getValidatedTweet());
    }

    static TweetEntity getTweet() {
        return TweetEntity.builder()
            .id(1L)
            .text(TEXT)
            .user(USER)
            .localization(LOCALIZATION)
            .validation(false)
            .build();
    }

    static TweetEntity getValidatedTweet() {
        return TweetEntity.builder()
            .id(1L)
            .text(TEXT)
            .user(USER)
            .localization(LOCALIZATION)
            .validation(true)
            .build();
    }

    static List<Trend> getTrends() {
        return List.of(
            new TrendImpl("TEST1"),
            new TrendImpl("TEST2"),
            new TrendImpl("TEST3"));

    }

    static List<String> getTrendsStr() {
        return List.of("TEST1", "TEST2", "TEST3");
    }

    class TrendImpl implements Trend {

        private String name;

        public TrendImpl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getURL() {
            return null;
        }

        @Override
        public String getQuery() {
            return null;
        }

        @Override
        public int getTweetVolume() {
            return 0;
        }
    }
}
