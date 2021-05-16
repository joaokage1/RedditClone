package com.joao.reddit.clone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    private String id;
    private String token;
    private Instant createdDate;
}