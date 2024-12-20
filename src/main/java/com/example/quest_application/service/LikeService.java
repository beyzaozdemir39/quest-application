package com.example.quest_application.service;

import com.example.quest_application.entity.Answer;
import com.example.quest_application.entity.Like;
import com.example.quest_application.entity.User;
import com.example.quest_application.exception.ResourceNotFoundException;
import com.example.quest_application.repos.AnswerRepository;
import com.example.quest_application.repos.LikeRepository;
import com.example.quest_application.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    private final LikeRepository likeRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public boolean likeAnswer(Long answerId, String username) {
        logger.info("User '{}' is liking answer ID: {}", username, answerId);
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User '{}' not found", username);
            return new IllegalArgumentException("User not found");
        });

        if (likeRepository.existsByAnswerIdAndUserId(answerId, user.getId())) {
            logger.warn("User '{}' already liked answer ID: {}", username, answerId);
            return false;
        }

        Like like = new Like();
        like.setAnswer(answerRepository.findById(answerId).orElseThrow(() -> {
            logger.error("Answer not found with ID: {}", answerId);
            return new IllegalArgumentException("Answer not found");
        }));
        like.setUser(user);
        likeRepository.save(like);
        logger.info("Answer ID: {} liked by user '{}'", answerId, username);
        return true;
    }

    public long getLikeCountByAnswer(Long answerId) {
        logger.info("Fetching like count for answer ID: {}", answerId);

        // Cevabın mevcut olup olmadığını kontrol et
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> {
            logger.error("Answer not found with ID: {}", answerId);
            return new ResourceNotFoundException("Answer not found with id: " + answerId);
        });

        // Beğeni sayısını al
        long likeCount = likeRepository.countByAnswerId(answerId);
        logger.info("Like count for answer ID: {} is {}", answerId, likeCount);

        return likeCount;
    }

    public boolean isAnswerLikedByUser(Long answerId, String username) {
        logger.info("Checking if user '{}' liked answer ID: {}", username, answerId);

        // Kullanıcıyı bulun
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User '{}' not found", username);
            return new ResourceNotFoundException("User not found with username: " + username);
        });

        // Beğeni var mı kontrol et
        boolean isLiked = likeRepository.existsByAnswerIdAndUserId(answerId, user.getId());
        logger.info("User '{}' {} liked answer ID: {}", username, isLiked ? "has" : "has not", answerId);

        return isLiked;
    }

    public boolean unlikeAnswer(Long answerId, String username) {
        logger.info("Attempting to unlike answer ID: {} by user '{}'", answerId, username);

        // Kullanıcıyı bulun
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User '{}' not found", username);
            return new ResourceNotFoundException("User not found with username: " + username);
        });

        // Beğeni var mı kontrol et ve kaldır
        Like like = likeRepository.findByAnswerIdAndUserId(answerId, user.getId()).orElseThrow(() -> {
            logger.error("Like not found for answer ID: {} and user ID: {}", answerId, user.getId());
            return new ResourceNotFoundException("Like not found");
        });

        likeRepository.delete(like);
        logger.info("User '{}' successfully unliked answer ID: {}", username, answerId);

        return true;
    }
}
