package com.twitter.facade.impl;

import com.twitter.entity.TweetEntity;
import com.twitter.exception.ServiceException;
import com.twitter.facade.TwitterFacade;
import com.twitter.service.TwitterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitterFacadeImpl implements TwitterFacade {

    private final TwitterService service;

    @Override
    public List<TweetEntity> getTweets() {
        return service.getTweets();
    }

    @Override
    public TweetEntity validateTweet(Long id) throws ServiceException {
        return service.validateTweet(id) ;
    }

    @Override
    public List<TweetEntity> getTweetsValidatedByUser(String user) {
        return service.getTweetsValidatedByUser(user);
    }

    @Override
    public List<String> getHashTagsMoreUsed() throws ServiceException {
        return service.getHashTagsMoreUsed();
    }
}
