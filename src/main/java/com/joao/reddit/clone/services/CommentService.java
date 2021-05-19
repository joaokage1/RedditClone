package com.joao.reddit.clone.services;

import com.joao.reddit.clone.dto.CommentDTO;
import com.joao.reddit.clone.exceptions.SpringRedditException;
import com.joao.reddit.clone.mapper.CommentMapper;
import com.joao.reddit.clone.model.Comment;
import com.joao.reddit.clone.model.NotificationEmail;
import com.joao.reddit.clone.model.Post;
import com.joao.reddit.clone.model.User;
import com.joao.reddit.clone.repository.CommentRepository;
import com.joao.reddit.clone.repository.PostRepository;
import com.joao.reddit.clone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    //TODO: Construct POST URL
    private static final String POST_URL = "";

    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentDTO commentDTO) {
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new SpringRedditException(commentDTO.getPostId().toString()));
        Comment comment = commentMapper.map(commentDTO, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentDTO> getCommentByPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException(postId));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public List<CommentDTO> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }
}
