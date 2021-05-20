package com.joao.reddit.clone.services;

import com.joao.reddit.clone.dto.VoteDTO;
import com.joao.reddit.clone.exceptions.SpringRedditException;
import com.joao.reddit.clone.model.Post;
import com.joao.reddit.clone.model.Vote;
import com.joao.reddit.clone.repository.PostRepository;
import com.joao.reddit.clone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.joao.reddit.clone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
@Data
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void vote(VoteDTO voteDTO) {
        Post post = getPostRepository().findById(voteDTO.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post Not Found with ID - " + voteDTO.getPostId()));
        Optional<Vote> voteByPostAndUser = getVoteRepository().findTopByPostAndUserOrderByVoteIdDesc(post, getAuthService().getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDTO.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDTO.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDTO.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        getVoteRepository().save(mapToVote(voteDTO, post));
        getPostRepository().save(post);
    }

    private Vote mapToVote(VoteDTO voteDTO, Post post) {
        return Vote.builder()
                .voteType(voteDTO.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
