package com.twitter.service.impl;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.twitter.TwitterMock;
import com.twitter.configuration.TwitterConfiguration;
import com.twitter.entity.TweetEntity;
import com.twitter.exception.ServiceException;
import com.twitter.repository.TwitterRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TwitterServiceImplTest {

    @InjectMocks
    private TwitterServiceImpl service;

    @Mock
    private TwitterRepository repository;

    @Mock
    private TwitterConfiguration configuration;

    @Test
    void getTweets_should_call_findAll_repository() {

        when(repository.findAll()).thenReturn(TwitterMock.getTweets());
        List<TweetEntity> list = service.getTweets();
        assertEquals(1, list.size());
    }

    @Test
    void getTweetsValidatedByUser_should_call_get_tweets_validated_by_user_repository() {

        when(repository.findAllByUserAndValidation (anyString(), eq(true))).thenReturn(TwitterMock.getTweets());
        List<TweetEntity> list = service.getTweetsValidatedByUser("javier");
        assertEquals(1, list.size() );
        assertEquals(1L, list.get(0).getId());
        assertEquals(TwitterMock.TEXT, list.get(0).getText());
        assertEquals(TwitterMock.LOCALIZATION, list.get(0).getLocalization());
        assertEquals(TwitterMock.USER, list.get(0).getUser());
        assertFalse(list.get(0).isValidation());
    }

    @Test
    void validateTweet_should_call_save_repository() throws ServiceException {

        when(repository.findById(anyLong())).thenReturn(Optional.of(TwitterMock.getTweet()));
        when(repository.save(any(TweetEntity.class))).thenReturn(TwitterMock.getValidatedTweet()) ;

        TweetEntity tweet = service.validateTweet(1L);
        assertEquals(1L, tweet.getId());
        assertEquals(TwitterMock.TEXT, tweet.getText());
        assertEquals(TwitterMock.LOCALIZATION, tweet.getLocalization());
        assertEquals(TwitterMock.USER, tweet.getUser());
        assertTrue(tweet.isValidation());
    }

    @Test
    void validateTweet_should_call_find_by_id_return_exception() {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> service.validateTweet(1L));

        Assertions.assertThat(exception).isExactlyInstanceOf(ServiceException.class);
    }

    @Test
    void getHashTagsMoreUsed_should_call_twitter_instance_and_trend() throws ServiceException, TwitterException {

        Twitter twitter = Mockito.mock(Twitter.class);
        Trends trends = Mockito.mock(Trends.class);

        when(configuration.getHashTagMoreUsed()).thenReturn(3);
        when(configuration.getTwitterInstance()).thenReturn(twitter);
        when(twitter.getPlaceTrends(1)).thenReturn(trends);
        when(trends.getTrends()).thenReturn(TwitterMock.getTrends().toArray(Trend[]::new));

        List<String> list = service.getHashTagsMoreUsed();
        assertEquals(3L, list.size());
    }

    @Test
    void getHashTagsMoreUsed_should_call_twitter_instance_and_trend_2() throws ServiceException, TwitterException {

        Twitter twitter = Mockito.mock(Twitter.class);
        Trends trends = Mockito.mock(Trends.class);

        when(configuration.getHashTagMoreUsed()).thenReturn(2);
        when(configuration.getTwitterInstance()).thenReturn(twitter);
        when(twitter.getPlaceTrends(1)).thenReturn(trends);
        when(trends.getTrends()).thenReturn(TwitterMock.getTrends().toArray(Trend[]::new));

        List<String> list = service.getHashTagsMoreUsed();
        assertEquals(2L, list.size());
    }

    @Test
    void getHashTagsMoreUsed_should_call_twitter_instance_and_trend_return_exception() throws ServiceException, TwitterException {

        Twitter twitter = Mockito.mock(Twitter.class);

        when(configuration.getTwitterInstance()).thenReturn(twitter);
        when(twitter.getPlaceTrends(1)).thenThrow (new TwitterException("Forced Error"));

        Throwable exception = catchThrowable(() -> service.getHashTagsMoreUsed());

        Assertions.assertThat(exception).isExactlyInstanceOf(ServiceException.class);

        verify(configuration).getTwitterInstance();
        verify(twitter).getPlaceTrends(1);

    }

}
