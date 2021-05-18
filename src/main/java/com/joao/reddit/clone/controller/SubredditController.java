package com.joao.reddit.clone.controller;

import com.joao.reddit.clone.dto.SubredditDTO;
import com.joao.reddit.clone.services.SubredditService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
@Data
public class SubredditController {

    private final SubredditService service;

    @PostMapping
    public ResponseEntity<SubredditDTO> createSubreddit(@RequestBody SubredditDTO subredditDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(getService().create(subredditDTO));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDTO>> fetchAllSubreddits(){
        return ResponseEntity.ok().body(getService().fetchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDTO> fetchSubredditById(@PathVariable String id){
        return ResponseEntity.ok().body(getService().fetchById(id));
    }
}
