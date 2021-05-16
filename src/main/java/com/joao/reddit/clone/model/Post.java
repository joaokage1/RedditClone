package com.joao.reddit.clone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.time.Instant;


@Data // Getter and Setters
@Document
@Builder
@AllArgsConstructor //Constructor with fields
@NoArgsConstructor //Empty constructor
public class Post {
    @Id
    private String postId;
    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    private String description;
    private Integer voteCount = 0;
    @DBRef(lazy = true)
    private User user;
    private Instant createdDate;
    @DBRef(lazy = true)
    private Subreddit subreddit;
}
