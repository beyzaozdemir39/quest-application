package com.example.quest_application.controller;

import com.example.quest_application.entity.Answer;
import com.example.quest_application.entity.Question;
import com.example.quest_application.entity.User;
import com.example.quest_application.service.AnswerService;
import com.example.quest_application.service.QuestionService;
import com.example.quest_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    // Cevap ekle
    @PostMapping
    public ResponseEntity<Answer> addAnswer(@RequestBody Answer answer, Authentication authentication) {
        // JWT token'dan kullanıcı adını al
        String username = authentication.getName();

        // Kullanıcıyı bul
        User user = userService.getUserByUsername(username);

        // Sorunun var olup olmadığını kontrol et
        Question question = questionService.getQuestionById(answer.getQuestion().getId());
        if (question == null) {
            return ResponseEntity.status(404).body(null); // Sorunun bulunamadığı durumda hata
        }

        // Kullanıcıyı ve soruyu cevapla ilişkilendir
        answer.setUser(user);
        answer.setQuestion(question);

        // Cevabı kaydet
        Answer savedAnswer = answerService.addAnswer(answer);
        return ResponseEntity.ok(savedAnswer);
    }

    // Belirli bir soruya ait cevapları getir
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.getAnswersByQuestionId(questionId));
    }

    // Belirli bir cevabı ID ile getir
    @GetMapping("/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable Long id) {
        Answer answer = answerService.getAnswerById(id);
        return ResponseEntity.ok(answer);
    }

    // Cevap güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable Long id, @RequestBody Answer answer, Authentication authentication) {
        // JWT token'dan kullanıcı adını al
        String username = authentication.getName();

        // Kullanıcıyı bul
        User user = userService.getUserByUsername(username);
        answer.setUser(user); // Güncellenen cevap için kullanıcıyı ayarla

        // Güncellemeyi yap
        Answer updatedAnswer = answerService.updateAnswer(id, answer);
        return ResponseEntity.ok(updatedAnswer);
    }

    // Cevap sil
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long id, Authentication authentication) {
        // JWT token'dan kullanıcı adını al
        String username = authentication.getName();

        // Kullanıcıyı bul
        User user = userService.getUserByUsername(username);

        // Cevabın varlığını ve kullanıcı yetkisini kontrol et
        Answer answer = answerService.getAnswerById(id);
        if (!answer.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You can only delete your own answers.");
        }

        // Cevabı sil
        answerService.deleteAnswer(id);
        return ResponseEntity.ok("Answer deleted successfully.");
    }
}