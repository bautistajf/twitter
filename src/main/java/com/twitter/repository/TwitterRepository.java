package com.twitter.repository;

import com.twitter.entity.TweetEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterRepository extends JpaRepository<TweetEntity, Long> {

    List<TweetEntity> findAllByUserAndValidation(String name, boolean validation);
}
