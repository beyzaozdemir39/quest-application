package com.example.quest_application.service;

import com.example.quest_application.entity.Question;
import com.example.quest_application.exception.ResourceNotFoundException;
import com.example.quest_application.repos.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question addQuestion(Question question) {
        logger.info("Adding a new question with title: {}", question.getTitle());
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        logger.info("Fetching all questions");
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        logger.info("Fetching question with ID: {}", id);
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + id));
    }

    public Question updateQuestion(Long id, Question question) {
        logger.info("Updating question with ID: {}", id);
        Question existingQuestion = getQuestionById(id);
        existingQuestion.setTitle(question.getTitle());
        existingQuestion.setDescription(question.getDescription());
        return questionRepository.save(existingQuestion);
    }

    public boolean deleteQuestion(Long id) {
        logger.info("Deleting question with ID: {}", id);
        Question existingQuestion = getQuestionById(id);
        questionRepository.delete(existingQuestion);
        return true;
    }

    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    public Page<Question> findQuestionsByUserId(Long userId, Pageable pageable) {
        return questionRepository.findByUserId(userId, pageable);
    }
}
