package com.example.quest_application.service;

import com.example.quest_application.entity.Answer;
import com.example.quest_application.entity.Comment;
import com.example.quest_application.entity.Question;
import com.example.quest_application.exception.ResourceNotFoundException;
import com.example.quest_application.repos.AnswerRepository;
import com.example.quest_application.repos.CommentRepository;
import com.example.quest_application.repos.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public CommentService(CommentRepository commentRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public Comment addCommentToQuestion(Long questionId, Comment comment) {
        logger.info("Adding comment to question ID: {}", questionId);
        Question question = questionRepository.findById(questionId).orElseThrow(() -> {
            logger.error("Question not found with ID: {}", questionId);
            return new IllegalArgumentException("Invalid question ID");
        });
        comment.setQuestion(question);
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment saved with ID: {}", savedComment.getId());
        return savedComment;
    }

    public Page<Comment> getCommentsByQuestion(Long questionId, Pageable pageable) {
        logger.info("Fetching comments for question ID: {} with pagination", questionId);
        return (Page<Comment>) commentRepository.findByQuestionId(questionId, pageable);
    }

    public Page<Comment> getCommentsByAnswer(Long answerId, Pageable pageable) {
        logger.info("Fetching comments for answer ID: {} with pagination", answerId);
        return (Page<Comment>) commentRepository.findByAnswerId(answerId, pageable);
    }

    public Comment addCommentToAnswer(Long answerId, Comment comment) {
        logger.info("Adding comment to answer ID: {}", answerId);

        // Cevabın mevcut olup olmadığını kontrol et
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> {
            logger.error("Answer not found with ID: {}", answerId);
            return new ResourceNotFoundException("Answer not found with id: " + answerId);
        });

        // Yorumu cevaba ilişkilendir
        comment.setAnswer(answer);

        // Yorumu kaydet
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment successfully added to answer ID: {} with comment ID: {}", answerId, savedComment.getId());

        return savedComment;
    }
}
