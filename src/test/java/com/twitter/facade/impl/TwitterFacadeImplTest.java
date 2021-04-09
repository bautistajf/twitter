package com.twitter.facade.impl;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.twitter.TwitterMock;
import com.twitter.entity.TweetEntity;
import com.twitter.exception.ServiceException;
import com.twitter.service.TwitterService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TwitterFacadeImplTest {

    @InjectMocks
    private TwitterFacadeImpl facade;

    @Mock
    private TwitterService service;

    @Test
    void getTweets_should_call_findAll_service() {

        when(service.getTweets()).thenReturn(TwitterMock.getTweets());
        List<TweetEntity> list = facade.getTweets();
        assertEquals(1, list.size());
    }

    @Test
    void getTweetsValidatedByUser_should_call_get_tweets_validated_by_user_service() {

        when(service.getTweetsValidatedByUser (anyString())).thenReturn(TwitterMock.getTweets());
        List<TweetEntity> list = facade.getTweetsValidatedByUser("javier");
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(TwitterMock.TEXT, list.get(0).getText());
        assertEquals(TwitterMock.LOCALIZATION, list.get(0).getLocalization());
        assertEquals(TwitterMock.USER, list.get(0).getUser());
        assertFalse(list.get(0).isValidation());
    }

    @Test
    void getHashTagsMoreUsed_should_call_get_hash_tags_more_used_service() throws ServiceException {

        when(service.getHashTagsMoreUsed()).thenReturn(TwitterMock.getTrendsStr());
        List<String> list = facade.getHashTagsMoreUsed();
        assertEquals(3, list.size());

    }


    @Test
    void validateTweet_should_call_get_validate_tweet_service() throws ServiceException {

        when(service.validateTweet (anyLong())).thenReturn(TwitterMock.getTweet());
        TweetEntity entity = facade.validateTweet(1L);
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(TwitterMock.TEXT, entity.getText());
        assertEquals(TwitterMock.LOCALIZATION, entity.getLocalization());
        assertEquals(TwitterMock.USER, entity.getUser());
        assertFalse(entity.isValidation());
    }

    @Test
    void validateTweet_should_call_validate_tweet_return_exception() throws ServiceException {

        when(service.validateTweet(anyLong())).thenThrow(new ServiceException("Error"));

        Throwable exception = catchThrowable(() -> facade.validateTweet(1L));

        Assertions.assertThat(exception).isExactlyInstanceOf(ServiceException.class);
    }
}
