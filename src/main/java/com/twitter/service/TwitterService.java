package com.twitter.service;

import com.twitter.entity.TweetEntity;
import com.twitter.exception.ServiceException;
import java.util.List;
import java.util.Map;

public interface TwitterService {

    List<TweetEntity> getTweets();

    TweetEntity validateTweet(Long id) throws ServiceException;

    List<TweetEntity> getTweetsValidatedByUser(String user);

    List<String> getHashTagsMoreUsed() throws ServiceException;

    void save(TweetEntity twitter);

    Map<String, List<TweetEntity>> getAllTweetsValidatedByUsers();
}
