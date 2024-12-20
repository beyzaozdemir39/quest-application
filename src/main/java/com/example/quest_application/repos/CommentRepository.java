package com.example.quest_application.repos;

import com.example.quest_application.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByQuestionId(Long questionId, Pageable pageable);
    Page<Comment> findByAnswerId(Long answerId, Pageable pageable);
}