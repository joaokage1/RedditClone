package com.joao.reddit.clone.repository;

import com.joao.reddit.clone.model.Post;
import com.joao.reddit.clone.model.User;
import com.joao.reddit.clone.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends MongoRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}