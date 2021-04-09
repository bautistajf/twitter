package com.twitter.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.twitter.configuration.TwitterConfiguration;
import com.twitter.service.TwitterService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import twitter4j.Twitter;
import twitter4j.TwitterStream;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TwitterListenerTest {

    @InjectMocks
    private TwitterListener listener;

    @Mock
    private TwitterConfiguration configuration;

    @Mock
    private TwitterService twitterService;

    @Test
    void createListener_should_not_call_listener() {
        when(configuration.isListenerEnabled()).thenReturn(false);

        listener.createListener();

        verify(twitterService, times(0)).save(any());

    }

    @Test
    void createListener_should_call_listener_and_save() {
        when(configuration.isListenerEnabled()).thenReturn(true);
        when(configuration.getTracks()).thenReturn("COVID");
        when(configuration.getLanguages()).thenReturn(List.of("es"));

        TwitterStream twitterStream = Mockito.mock(TwitterStream.class);
        when(configuration.getTwitterStreamFactoryInstance()).thenReturn(twitterStream);
        when(twitterStream.addListener(any())).thenReturn(twitterStream);
        when(twitterStream.filter(anyString())).thenReturn(twitterStream);

        listener.createListener();

        verify(twitterService, times(0)).save(any());

    }
}
