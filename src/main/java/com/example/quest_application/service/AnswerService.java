package com.example.quest_application.service;

import com.example.quest_application.entity.Answer;
import com.example.quest_application.exception.ResourceNotFoundException;
import com.example.quest_application.repos.AnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private static final Logger logger = LoggerFactory.getLogger(AnswerService.class);

    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer addAnswer(Answer answer) {
        logger.info("Adding a new answer for question ID: {}", answer.getQuestion().getId());
        Answer savedAnswer = answerRepository.save(answer);
        logger.info("Answer saved with ID: {}", savedAnswer.getId());
        return savedAnswer;
    }

    public List<Answer> getAnswersByQuestionId(Long questionId) {
        logger.info("Fetching answers for question ID: {}", questionId);
        return answerRepository.findByQuestionId(questionId);
    }

    public Answer getAnswerById(Long id) {
        logger.info("Fetching answer with ID: {}", id);
        return answerRepository.findById(id).orElseThrow(() -> {
            logger.error("Answer not found with ID: {}", id);
            return new ResourceNotFoundException("Answer not found with ID: " + id);
        });
    }

    public Answer updateAnswer(Long id, Answer answer) {
        logger.info("Updating answer with ID: {}", id);
        Answer existingAnswer = getAnswerById(id);
        existingAnswer.setText(answer.getText());
        Answer updatedAnswer = answerRepository.save(existingAnswer);
        logger.info("Answer updated with ID: {}", updatedAnswer.getId());
        return updatedAnswer;
    }

    public boolean deleteAnswer(Long id) {
        logger.info("Deleting answer with ID: {}", id);
        Answer existingAnswer = getAnswerById(id);
        answerRepository.delete(existingAnswer);
        logger.info("Answer deleted with ID: {}", id);
        return true;
    }
}
