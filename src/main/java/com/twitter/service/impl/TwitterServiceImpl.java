package com.twitter.service.impl;

import static com.twitter.exception.ErrorMessageCode.ERROR_DB_001;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.twitter.configuration.TwitterConfiguration;
import com.twitter.entity.TweetEntity;
import com.twitter.exception.ServiceException;
import com.twitter.repository.TwitterRepository;
import com.twitter.service.TwitterService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Service
@RequiredArgsConstructor
@Log4j2
public class TwitterServiceImpl implements TwitterService {

    private final TwitterRepository repository;

    private final TwitterConfiguration twitterConfiguration;

    @Override
    public List<TweetEntity> getTweets() {
        return repository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TweetEntity validateTweet(Long id) throws ServiceException {
        TweetEntity entity = repository.findById(id).orElseThrow(() -> new ServiceException(BAD_REQUEST,
            ERROR_DB_001.getName(), ERROR_DB_001));
        entity.setValidation(true);
        save(entity);
        return entity;
    }

    @Override
    public List<TweetEntity> getTweetsValidatedByUser(String user) {
        return repository.findAllByUserAndValidation(user, true) ;
    }

    @Override
    public Map<String, List<TweetEntity>> getAllTweetsValidatedByUsers() {
        return repository.findAllByValidation(true).stream().collect(
            Collectors.groupingBy(TweetEntity::getUser));
    }

    @Override
    public List<String> getHashTagsMoreUsed() throws ServiceException {
        Twitter twitter = twitterConfiguration.getTwitterInstance();
        List<String> hashTagMoreUsed = new ArrayList<>();
        try {
            Trends trends = twitter.getPlaceTrends(1);
            int count = 0;
            for (Trend trend : trends.getTrends()) {
                if (count < twitterConfiguration.getHashTagMoreUsed()) {
                    hashTagMoreUsed.add(trend.getName());
                    count++;
                }
            }
            return hashTagMoreUsed;
        } catch (TwitterException ex) {
            log.error(ex);
            throw new ServiceException(ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(TweetEntity twitter) {
        repository.save(twitter);
    }
}
