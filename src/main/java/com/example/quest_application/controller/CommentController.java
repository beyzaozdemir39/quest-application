package com.example.quest_application.controller;

import com.example.quest_application.entity.Comment;
import com.example.quest_application.entity.User;
import com.example.quest_application.service.CommentService;
import com.example.quest_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        // Kullanıcıyı JWT token'dan al
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        // Kullanıcıyı yoruma ilişkilendir
        comment.setUser(user);

        // Yorumu kaydet
        return ResponseEntity.ok(commentService.addCommentToQuestion(questionId, comment));
    }

    // Cevaplara yorum ekle
    @PostMapping("/answer/{answerId}")
    public ResponseEntity<Comment> addCommentToAnswer(@PathVariable Long answerId, @RequestBody Comment comment, Authentication authentication) {
        // Kullanıcıyı JWT token'dan al
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        // Kullanıcıyı yoruma ilişkilendir
        comment.setUser(user);

        // Yorumu kaydet
        return ResponseEntity.ok(commentService.addCommentToAnswer(answerId, comment));
    }

    // Belirli bir sorunun yorumlarını getir
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Comment>> getCommentsByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(commentService.getCommentsByQuestion(questionId));
    }

    // Belirli bir cevabın yorumlarını getir
    @GetMapping("/answer/{answerId}")
    public ResponseEntity<List<Comment>> getCommentsByAnswer(@PathVariable Long answerId) {
        return ResponseEntity.ok(commentService.getCommentsByAnswer(answerId));
    }
}
