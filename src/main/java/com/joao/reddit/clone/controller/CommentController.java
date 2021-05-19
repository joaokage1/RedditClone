package com.joao.reddit.clone.controller;

import com.joao.reddit.clone.dto.CommentDTO;
import com.joao.reddit.clone.services.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;


@RestController
@RequestMapping("/api/comments/")
@Data
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDTO commentDTO) {
        getCommentService().createComment(commentDTO);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable String postId) {
        return status(OK)
                .body(getCommentService().getCommentByPost(postId));
    }

    @GetMapping("by-user/{userName}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByUser(@PathVariable String userName) {
        return status(OK).body(getCommentService().getCommentsByUser(userName));
    }
}
