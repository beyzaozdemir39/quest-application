package com.example.quest_application.controller;

import com.example.quest_application.entity.Comment;
import com.example.quest_application.entity.User;
import com.example.quest_application.service.CommentService;
import com.example.quest_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    // Sorulara yorum ekle
    @PostMapping("/question/{questionId}")
    public ResponseEntity<Comment> addCommentToQuestion(@PathVariable Long questionId, @RequestBody Comment comment, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        comment.setUser(user);
        return ResponseEntity.ok(commentService.addCommentToQuestion(questionId, comment));
    }

    // Cevaplara yorum ekle
    @PostMapping("/answer/{answerId}")
    public ResponseEntity<Comment> addCommentToAnswer(@PathVariable Long answerId, @RequestBody Comment comment, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        comment.setUser(user);
        return ResponseEntity.ok(commentService.addCommentToAnswer(answerId, comment));
    }

    // Belirli bir sorunun yorumlarını getir (pagination destekli)
    @GetMapping("/question/{questionId}")
    public ResponseEntity<Page<Comment>> getCommentsByQuestion(@PathVariable Long questionId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByQuestion(questionId, pageable));
    }

    // Belirli bir cevabın yorumlarını getir (pagination destekli)
    @GetMapping("/answer/{answerId}")
    public ResponseEntity<Page<Comment>> getCommentsByAnswer(@PathVariable Long answerId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByAnswer(answerId, pageable));
    }
}
