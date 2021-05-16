package com.joao.reddit.clone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "token")
public class VerificationToken {

    @Id
    private String id;
    private String token;
    @DBRef(lazy = true)
    private User user;
    private Instant expiryDate;
}