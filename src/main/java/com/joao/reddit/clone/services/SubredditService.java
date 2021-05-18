package com.joao.reddit.clone.services;

import com.joao.reddit.clone.dto.SubredditDTO;
import com.joao.reddit.clone.model.Subreddit;
import com.joao.reddit.clone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Data
public class SubredditService {

    private final SubredditRepository repository;

    public SubredditDTO create(SubredditDTO subredditDTO){
        Subreddit subreddit = getRepository().save(mapSubredditDTO(subredditDTO));

        subredditDTO.setId(subreddit.getId());
        return subredditDTO;
    }

    private Subreddit mapSubredditDTO(SubredditDTO subredditDTO) {
        return Subreddit.builder().name(subredditDTO.getName())
                .description(subredditDTO.getDesc()).build();
    }

    public List<SubredditDTO> fetchAll() {
        return getRepository().findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private SubredditDTO mapToDTO(Subreddit subreddit) {
        return SubredditDTO.builder().name(subreddit.getName())
                .desc(subreddit.getDescription())
                .id(subreddit.getId())
                .numberOfPosts(subreddit.getPosts() != null ? subreddit.getPosts().size() : null)
                .build();
    }
}
