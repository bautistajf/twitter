package com.twitter.facade;

import com.twitter.entity.TweetEntity;
import com.twitter.exception.ServiceException;
import java.util.List;

public interface TwitterFacade {

    List<TweetEntity> getTweets();

    TweetEntity validateTweet(Long id) throws ServiceException;

    List<TweetEntity> getTweetsValidatedByUser(String user);

    List<String> getHashTagsMoreUsed() throws ServiceException;
}
