package com.demo.nutgrow.service;

import com.demo.nutgrow.dto.AnalysisResult;
import com.demo.nutgrow.dto.QuizResult;
import com.demo.nutgrow.model.*;
import com.demo.nutgrow.repository.DocumentRepository;
import com.demo.nutgrow.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public Document saveDocument(
            String fileName,
            String fileUrl,
            AnalysisResult analysis,
            QuizResult quizResult) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).get();

        Document document = new Document();
        document.setName(fileName);
        document.setFileName(fileName);
        document.setFileUrl(fileUrl);
        document.setSummary(analysis.getSummary());

        // Parts
        analysis.getParts().forEach(p -> {
            Part part = new Part();
            part.setTitle(p.getTitle());
            part.setContent(p.getContent());
            part.setDocument(document);
            document.getParts().add(part);
        });

        // Quiz
        Quiz quiz = new Quiz();
        quiz.setTitle("Quiz - " + fileName + " (" + LocalDateTime.now() + ")");
        quiz.setDocument(document);

        quizResult.getQuestions().forEach(q -> {
            Question question = new Question();
            question.setTitle(q.getQuestion());
            question.setOptions(q.getOptions());
            question.setAnswer(q.getAnswer());
            question.setQuiz(quiz);
            quiz.getQuestions().add(question);
        });

        document.getQuizzes().add(quiz);
        document.setUser(user);

        return documentRepository.save(document);
    }
}
