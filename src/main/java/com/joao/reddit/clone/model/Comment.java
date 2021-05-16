package com.joao.reddit.clone.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Comment {

    @Id
    private String id;
    @NotEmpty
    private String text;
    @DBRef(lazy = true)
    private Post post;
    private Instant createdDate;
    @DBRef(lazy = true)
    private User user;
}
