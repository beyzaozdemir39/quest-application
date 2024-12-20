package com.example.quest_application.repos;

import com.example.quest_application.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Belirli bir kullanıcının tüm sorularını getiren özel sorgu
    List<Question> findByUserId(Long userId);
}