package com.joao.reddit.clone.services;

import com.joao.reddit.clone.dto.PostRequest;
import com.joao.reddit.clone.dto.PostResponse;
import com.joao.reddit.clone.exceptions.SpringRedditException;
import com.joao.reddit.clone.mapper.PostMapper;
import com.joao.reddit.clone.model.Post;
import com.joao.reddit.clone.model.Subreddit;
import com.joao.reddit.clone.model.User;
import com.joao.reddit.clone.repository.PostRepository;
import com.joao.reddit.clone.repository.SubredditRepository;
import com.joao.reddit.clone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Data
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = getSubredditRepository().findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException(postRequest.getSubredditName()));
        getPostRepository().save(getPostMapper().map(postRequest, subreddit, getAuthService().getCurrentUser()));//authService.getCurrentUser()
    }

    public PostResponse getPost(String id) {
        Post post = getPostRepository().findById(id)
                .orElseThrow(() -> new SpringRedditException(id));
        return getPostMapper().mapToDto(post);
    }

    public List<PostResponse> getAllPosts() {
        return getPostRepository().findAll()
                .stream()
                .map(getPostMapper()::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(String subredditId) {
        Subreddit subreddit = getSubredditRepository().findById(subredditId)
                .orElseThrow(() -> new SpringRedditException(subredditId));
        List<Post> posts = getPostRepository().findAllBySubreddit(subreddit);
        return posts.stream().map(getPostMapper()::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = getUserRepository().findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return getPostRepository().findByUser(user)
                .stream()
                .map(getPostMapper()::mapToDto)
                .collect(Collectors.toList());
    }
}
