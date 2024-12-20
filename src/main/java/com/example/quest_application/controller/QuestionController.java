package com.example.quest_application.controller;

import com.example.quest_application.entity.Question;
import com.example.quest_application.entity.User;
import com.example.quest_application.service.QuestionService;
import com.example.quest_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    // Soru ekle
    @PostMapping
    public ResponseEntity<Question> addQuestion(@RequestBody Question question, Authentication authentication) {
        // JWT token'dan kullanıcı adını al
        String username = authentication.getName();

        // Kullanıcıyı bul ve soruya ekle
        User user = userService.getUserByUsername(username);
        question.setUser(user);

        // Soruyu kaydet ve yanıtla dön
        Question savedQuestion = questionService.addQuestion(question);
        return ResponseEntity.ok(savedQuestion);
    }

    // Tüm soruları getir
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    // ID ile soru getir
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    // Soru güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion, Authentication authentication) {
        // JWT token'dan kullanıcı adını al
        String username = authentication.getName();

        // Kullanıcıyı bul ve soruya ekle
        User user = userService.getUserByUsername(username);
        updatedQuestion.setUser(user);

        // Güncellemeyi yap
        Question updated = questionService.updateQuestion(id, updatedQuestion);
        return ResponseEntity.ok(updated);
    }

    // Soru sil
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id, Authentication authentication) {
        // JWT token'dan kullanıcı adını al
        String username = authentication.getName();

        // Kullanıcıyı bul
        User user = userService.getUserByUsername(username);

        // Sorunun varlığını ve kullanıcının sahibi olup olmadığını kontrol et
        Question question = questionService.getQuestionById(id);
        if (!question.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You can only delete your own questions.");
        }

        // Soruyu sil
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Question deleted successfully.");
    }
}
