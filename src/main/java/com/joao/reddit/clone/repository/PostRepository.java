package com.joao.reddit.clone.repository;

import com.joao.reddit.clone.model.Post;
import com.joao.reddit.clone.model.Subreddit;
import com.joao.reddit.clone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
