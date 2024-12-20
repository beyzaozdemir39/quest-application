package com.example.quest_application.controller;

import com.example.quest_application.entity.Question;
import com.example.quest_application.entity.User;
import com.example.quest_application.service.QuestionService;
import com.example.quest_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Question> addQuestion(@RequestBody Question question, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        question.setUser(user);
        return ResponseEntity.ok(questionService.addQuestion(question));
    }

    @GetMapping
    public ResponseEntity<Page<Question>> getAllQuestions(Pageable pageable) {
        return ResponseEntity.ok(questionService.getQuestions(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Question>> getQuestionsByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(questionService.findQuestionsByUserId(userId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @RequestBody Question updatedQuestion,
            Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        updatedQuestion.setUser(user);
        return ResponseEntity.ok(questionService.updateQuestion(id, updatedQuestion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Question question = questionService.getQuestionById(id);
        if (!question.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You can only delete your own questions.");
        }
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Question deleted successfully.");
    }
}
