package com.demo.nutgrow.controller;

import com.demo.nutgrow.dto.AnalysisResult;
import com.demo.nutgrow.dto.QuizResult;
import com.demo.nutgrow.model.Document;
import com.demo.nutgrow.service.FileProcessingService;
import com.demo.nutgrow.service.GitHubUploadService;
import com.demo.nutgrow.service.AIService;
import com.demo.nutgrow.service.DocumentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/api/study")
@RequiredArgsConstructor
@Slf4j
public class QuizAIController {

    private final AIService aiService;
    private final GitHubUploadService gitHubUploadService;
    private final DocumentService documentService;
    private final FileProcessingService fileProcessingService;
    private String text = "";
    private String pathUrl = "";
    private String fileName = "";

    private AnalysisResult analysisResult;
    private QuizResult quizResult;

    private final Map<String, byte[]> tempFileStorage = new HashMap<>();

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading study PDF: {}", file.getOriginalFilename());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "File trống"));
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Chỉ chấp nhận file PDF"));
            }

            String pathOnRepo = gitHubUploadService.generatePath(file.getOriginalFilename());

            String githubUrl = gitHubUploadService.uploadFile(file, pathOnRepo);
            this.pathUrl = githubUrl;
            this.fileName = file.getOriginalFilename();

            String fileId = UUID.randomUUID().toString();
            tempFileStorage.put(fileId, file.getBytes());

            return ResponseEntity.ok(Map.of(
                    "fileId", fileId,
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()));

        } catch (Exception e) {
            log.error("Upload error", e);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Lỗi upload: " + e.getMessage()));
        }
    }

    @PostMapping("/analyze")
    @ResponseBody
    public ResponseEntity<?> analyzeStudyDocument(
            @RequestBody Map<String, String> request) {

        try {
            log.info("========== START STUDY DOCUMENT ANALYSIS ==========");

            String fileId = request.get("fileId");

            if (fileId == null || fileId.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Thiếu fileId"));
            }

            byte[] fileBytes = tempFileStorage.get(fileId);
            if (fileBytes == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "File không tồn tại hoặc đã bị xóa"));
            }

            log.info("Extracting text from PDF...");
            String documentText = fileProcessingService.extractText(fileBytes, "study.pdf");

            this.text = documentText;
            if (documentText.length() < 100) {
                log.warn("Extracted text quá ngắn, có thể PDF scan ảnh");
            }

            log.info("Calling OpenAI AI...");
            AnalysisResult result = aiService.analyze(documentText);
            analysisResult = result;

            log.info("Analysis completed successfully");
            log.info("========== END ANALYSIS ==========");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("========== ANALYSIS FAILED ==========", e);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Lỗi phân tích tài liệu: " + e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    @ResponseBody
    public ResponseEntity<?> clearTempFiles() {
        int count = tempFileStorage.size();
        tempFileStorage.clear();
        log.info("Cleared {} temporary study files", count);
        return ResponseEntity.ok(
                Map.of("message", "Đã xóa " + count + " file tạm"));
    }

    @PostMapping("/quiz")
    @ResponseBody
    public QuizResult generateQuiz() {
        QuizResult quiz = aiService.generateQuiz(this.text);
        this.quizResult = quiz;
        return quiz;
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<?> saveStudyDocument() {

        try {

            if (this.text == null || this.text.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Chưa phân tích tài liệu"));
            }

            log.info("Saving document:");
            log.info("fileName={}", fileName);
            log.info("pathUrl={}", pathUrl);
            log.info("analysisResult={}", analysisResult);
            log.info("quizResult={}", quizResult);

            Document saved = documentService.saveDocument(
                    this.fileName,
                    this.pathUrl,
                    this.analysisResult,
                    this.quizResult);

            return ResponseEntity.ok(Map.of(
                    "documentId", saved.getId(),
                    "message", "Lưu tài liệu thành công"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
