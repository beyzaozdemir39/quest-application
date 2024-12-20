package com.example.quest_application.repos;

import com.example.quest_application.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // Belirli bir soruya ait tüm cevapları getiren özel sorgu
    Page<Answer> findByQuestionId(Long questionId, Pageable pageable);

    // Belirli bir kullanıcının verdiği tüm cevapları getiren özel sorgu
    List<Answer> findByUserId(Long userId);
}