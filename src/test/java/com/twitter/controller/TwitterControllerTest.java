package com.twitter.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.twitter.TwitterMock;
import com.twitter.configuration.TwitterConfiguration;
import com.twitter.configuration.WebConfig;
import com.twitter.entity.TweetEntity;
import com.twitter.exception.GlobalExceptionHandler;
import com.twitter.facade.impl.TwitterFacadeImpl;
import com.twitter.mapper.TwitterMapperImpl;
import com.twitter.repository.TwitterRepository;
import com.twitter.service.impl.TwitterServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = {
    TwitterController.class,
    GlobalExceptionHandler.class,
    WebConfig.class,
    TwitterFacadeImpl.class,
    TwitterMapperImpl.class,
    TwitterServiceImpl.class
})
class TwitterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TwitterRepository repository;

    @MockBean
    private TwitterConfiguration configuration;

    @Test
    void getAllTweets_should_return_a_list_of_tweet() throws Exception {
        when(repository.findAll()).thenReturn(TwitterMock.getTweets());

        mockMvc
            .perform(get(TwitterMock.PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(1)))
            .andExpect(jsonPath("$.[0].user", is(TwitterMock.USER)))
            .andExpect(jsonPath("$.[0].localization", is(TwitterMock.LOCALIZATION)))
            .andExpect(jsonPath("$.[0].text", is(TwitterMock.TEXT)))
            .andExpect(jsonPath("$.[0].validation", is(false)));
    }

    @Test
    void getAllTweetsValidatedByUser_should_return_a_list_of_tweet() throws Exception {
        when(repository.findAllByUserAndValidation(anyString(), eq(true))).thenReturn(TwitterMock.getValidatedTweets());

        mockMvc
            .perform(get(TwitterMock.PATH + "/validadosPorUsuario/javier")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(1)))
            .andExpect(jsonPath("$.[0].user", is(TwitterMock.USER)))
            .andExpect(jsonPath("$.[0].localization", is(TwitterMock.LOCALIZATION)))
            .andExpect(jsonPath("$.[0].text", is(TwitterMock.TEXT)))
            .andExpect(jsonPath("$.[0].validation", is(true)));
    }

    @Test
    void hashTagMoreUsed_should_return_a_list_of_hash_tag_string() throws Exception {
        Twitter twitter = Mockito.mock(Twitter.class);
        Trends trends = Mockito.mock(Trends.class);

        when(configuration.getHashTagMoreUsed()).thenReturn(3);
        when(configuration.getTwitterInstance()).thenReturn(twitter);
        when(twitter.getPlaceTrends(1)).thenReturn(trends);
        when(trends.getTrends()).thenReturn(TwitterMock.getTrends().toArray(Trend[]::new));

        mockMvc
            .perform(get(TwitterMock.PATH + "/hashTagMoreUsed")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0]", is("TEST1")))
            .andExpect(jsonPath("$.[1]", is("TEST2")))
            .andExpect(jsonPath("$.[2]", is("TEST3")));
    }

    @Test
    void validar_should_return_a_tweet_validated() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.of(TwitterMock.getTweet()));
        when(repository.save(any(TweetEntity.class))).thenReturn(TwitterMock.getValidatedTweet()) ;

        mockMvc
            .perform(put(TwitterMock.PATH + "/validar/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.user", is(TwitterMock.USER)))
            .andExpect(jsonPath("$.localization", is(TwitterMock.LOCALIZATION)))
            .andExpect(jsonPath("$.text", is(TwitterMock.TEXT)))
            .andExpect(jsonPath("$.validation", is(true)));
    }

    @Test
    void getAllTweetsValidatedByUsers_should_return_a_list_of_tweet_grouped() throws Exception {
        when(repository.findAllByValidation (true)).thenReturn(TwitterMock.getValidatedTweets());

        mockMvc
            .perform(get(TwitterMock.PATH + "/allTweetsValidatedByUsers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.['javier'].[0].user", is(TwitterMock.USER)))
            .andExpect(jsonPath("$.['javier'].[0].id", is(1)))
            .andExpect(jsonPath("$.['javier'].[0].user", is(TwitterMock.USER)))
            .andExpect(jsonPath("$.['javier'].[0].localization", is(TwitterMock.LOCALIZATION)))
            .andExpect(jsonPath("$.['javier'].[0].text", is(TwitterMock.TEXT)))
            .andExpect(jsonPath("$.['javier'].[0].validation", is(true)));

    }
}
