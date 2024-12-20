package com.example.quest_application.controller;

import com.example.quest_application.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // Cevabı beğen
    @PostMapping("/answer/{answerId}")
    public ResponseEntity<?> likeAnswer(@PathVariable Long answerId, Authentication authentication) {
        String username = authentication.getName(); // Kullanıcı adını JWT'den al
        boolean isLiked = likeService.likeAnswer(answerId, username);
        if (isLiked) {
            return ResponseEntity.ok("Answer liked successfully.");
        }
        return ResponseEntity.status(400).body("You have already liked this answer.");
    }

    // Belirli bir cevabın beğeni sayısını getir
    @GetMapping("/answer/{answerId}/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long answerId) {
        long likeCount = likeService.getLikeCountByAnswer(answerId);
        return ResponseEntity.ok(likeCount);
    }

    // Kullanıcının cevabı beğenip beğenmediğini kontrol et
    @GetMapping("/answer/{answerId}/isLiked")
    public ResponseEntity<Boolean> isAnswerLikedByUser(@PathVariable Long answerId, Authentication authentication) {
        String username = authentication.getName(); // Kullanıcı adını JWT'den al
        boolean isLiked = likeService.isAnswerLikedByUser(answerId, username);
        return ResponseEntity.ok(isLiked);
    }

    // Beğeniyi kaldır
    @DeleteMapping("/answer/{answerId}")
    public ResponseEntity<?> unlikeAnswer(@PathVariable Long answerId, Authentication authentication) {
        String username = authentication.getName(); // Kullanıcı adını JWT'den al
        boolean isUnliked = likeService.unlikeAnswer(answerId, username);
        if (isUnliked) {
            return ResponseEntity.ok("Answer unliked successfully.");
        }
        return ResponseEntity.status(400).body("Like not found.");
    }
}
