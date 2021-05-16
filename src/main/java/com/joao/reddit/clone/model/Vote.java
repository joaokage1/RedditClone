package com.joao.reddit.clone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class Vote {
    @Id
    private String voteId;
    private VoteType voteType;
    @NotNull
    @DBRef(lazy = true)
    private Post post;
    @DBRef(lazy = true)
    private User user;
}