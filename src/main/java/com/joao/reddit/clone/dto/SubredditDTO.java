package com.joao.reddit.clone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDTO {

    @Id
    private String id;
    private String name;
    private String desc;
    private Integer numberOfPosts;
}
