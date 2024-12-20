package com.example.quest_application.repos;

import com.example.quest_application.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByAnswerIdAndUserId(Long answerId, Long userId);
    long countByAnswerId(Long answerId);

    Optional<Like> findByAnswerIdAndUserId(Long answerId, Long userId);
}
